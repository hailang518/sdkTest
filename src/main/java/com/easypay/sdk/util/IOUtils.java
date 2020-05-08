package com.easypay.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	private static final int DEFAULT_BUFFER_SIZE = 4096;

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while (-1 != (len = input.read(buffer))) {
			output.write(buffer, 0, len);
		}
		return output.toByteArray();
	}
}
