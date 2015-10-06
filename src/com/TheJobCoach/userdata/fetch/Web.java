package com.TheJobCoach.userdata.fetch;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web
{
	static final int MAX_TMP = 1024*1024;
	static Logger logger = LoggerFactory.getLogger(Web.class);

	public static byte[] get(String urlStr) throws IOException
	{
		URL url = null;
		InputStream is = null;
		byte[] result = new byte[0];

		try {
			byte[] block = new byte[MAX_TMP];

			boolean redirect = true;
			boolean error = false;
			boolean zipped = false;
			while (redirect && !error)
			{
				url = new URL(urlStr);			
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int status = connection.getResponseCode();
				logger.info("Return code is: " + status + " type: " + connection.getContentEncoding());
				zipped = "gzip".equals(connection.getContentEncoding());
				if (status != HttpURLConnection.HTTP_OK) 
				{
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
							|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					{
						redirect = true;
						urlStr = connection.getHeaderField("Location");
						logger.info("Redirect to: " + urlStr);
					}
					else
					{
						logger.error("Error on URL. Abort ");
						error = true;
					}
				}
				else redirect = false;
			}
			is = url.openStream();
			if (zipped)
			{
				logger.info("Opening as gzip stream");
				is = new GZIPInputStream(url.openStream());
			}
			int call_result;
			while ((call_result = is.read(block)) != -1)
			{
				int srcPos = result.length;
				result = Arrays.copyOf(result, result.length + call_result);
				System.arraycopy(block, 0, result, srcPos, call_result);
			}
		}
		catch (Exception e)
		{
			logger.error("Exception on web stream " + e.getMessage());
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
