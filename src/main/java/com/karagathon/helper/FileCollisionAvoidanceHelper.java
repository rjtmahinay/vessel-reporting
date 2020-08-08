package com.karagathon.helper;

import java.util.UUID;

public class FileCollisionAvoidanceHelper {
	
	private static final String DELIMITER = "_";
	
	public static final String getRenamedString(final String ... names) {
		final StringBuilder sb = new StringBuilder(UUID.randomUUID().toString());
		sb.append(DELIMITER)
			.append(String.join(DELIMITER, names));
		return sb.toString();
	}
}
