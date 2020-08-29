package com.karagathon.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSService {

	@Value("${sms.url}")
	private String SMS_URL;

	@Value("${sms.api_key}")
	private String SMS_API_KEY;

	public void sendSMS(String message) {

		HttpPost post = new HttpPost(SMS_URL);

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("apikey", SMS_API_KEY));
		urlParameters.add(new BasicNameValuePair("message", message));
		// Set the SMS receiver to my phone
		urlParameters.add(new BasicNameValuePair("number", "09272697150,09328560207"));
		urlParameters.add(new BasicNameValuePair("sendername", "OBYTES"));

		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			CloseableHttpClient httpClient = HttpClients.createDefault();
			httpClient.execute(post);

		} catch (UnsupportedEncodingException ue) {
		} catch (IOException ie) {
		}
	}

	public static String singleReportMessage() {
		return "An incident has been reported.";
	}

	public static String moreThanOneReportMessage() {
		return "More than 1 incident has been reported.";
	}
}
