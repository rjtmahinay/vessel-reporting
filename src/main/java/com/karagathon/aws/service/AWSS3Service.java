package com.karagathon.aws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.DateFormatter;
import com.karagathon.helper.FileCollisionAvoidanceHelper;

@Service
public class AWSS3Service {
	
	@Autowired
    private AmazonS3 amazonS3;
 
    // @Async annotation ensures that the method is executed in a different background thread 
    // but not consume the main thread.
    @Async
    public String uploadFile(final MultipartFile multipartFile, final BucketBeanHelper bucketBean) {
    	
    	final StringBuffer filename = new StringBuffer();
    	
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            filename.append( uploadFileToS3Bucket(bucketBean.getBucketName(), file, bucketBean.getObjectFilePath()) ) ;
            file.delete();  // To remove the file locally created in the project folder.
        } catch (final AmazonServiceException ex) {
        	ex.printStackTrace();
        }
        
        return filename.toString();
    }
 
    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
           ex.printStackTrace();
        }
        return file;
    }
 
    private String uploadFileToS3Bucket(final String bucketName, final File file, final String filePath) {
    	final StringBuffer renamedFileName = new StringBuffer()
    			.append( FileCollisionAvoidanceHelper.getRenamedString( DateFormatter.format( LocalDateTime.now() ) ) )
				.append( FilenameUtils.EXTENSION_SEPARATOR )
				.append( FilenameUtils.getExtension( file.getName() ) );
    	
    	// to update
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,  filePath + renamedFileName.toString(), file);
        amazonS3.putObject(putObjectRequest);
        
        return renamedFileName.toString();
    }
    
    @Async
    public byte[] downloadFile(final String keyName, BucketBeanHelper bucketBean) throws AmazonS3Exception {
        byte[] content = null;
        final S3Object s3Object = amazonS3.getObject(bucketBean.getBucketName(), bucketBean.getObjectFilePath()+keyName);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            s3Object.close();
        } catch(final IOException ex) {
        	ex.printStackTrace();
        }
        return content;
    }
}
