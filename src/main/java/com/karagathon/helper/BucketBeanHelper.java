package com.karagathon.helper;

import org.springframework.beans.factory.annotation.Value;

public class BucketBeanHelper {
	
	private String bucketName;
	private String objectFilePath;
	
	public BucketBeanHelper(String bucketName, String objectFilePath) {
		this.bucketName = bucketName;
		this.objectFilePath = objectFilePath;
	}
	
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getObjectFilePath() {
		return objectFilePath;
	}
	public void setObjectFilePath(String objectFilePath) {
		this.objectFilePath = objectFilePath;
	}
	
	
	
	
}
