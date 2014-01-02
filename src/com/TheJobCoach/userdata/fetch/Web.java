package com.TheJobCoach.userdata.fetch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web
{
	static final int MAX_TMP = 1024*1024;
	static Logger logger = LoggerFactory.getLogger(Web.class);
	
	public static byte[] get(String urlStr) throws IOException
	{
		URL url;
		InputStream is = null;
		byte[] result = new byte[0];
		
		try {
			byte[] block = new byte[MAX_TMP];
			url = new URL(urlStr);
			is = url.openStream();
			int call_result;
			while ((call_result = is.read(block)) != -1)
			{
				int srcPos = result.length;
				result = Arrays.copyOf(result, result.length + call_result);
				System.arraycopy(block, 0, result, srcPos, call_result);
			}
		}
		finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				logger.error("Error closing stream " + ioe.getMessage());
				return result;
			}
		}
		return result;
	}
}
