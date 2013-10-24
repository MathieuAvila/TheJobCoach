package com.TheJobCoach.webapp.userpage.server;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

	/**
	 * Sends headers to allow a file to be download to the workstation vs.
	 * opened in the browser.
	 * @param fileName the name of the file. This will be encoded as UTF-8.
	 * @param contentLen the length of the file, zero (0) if unknown.
	 * @param request the {@code HttpServletRequest}.
	 * @param response the {@code HttpSerletResponse}.
	 * @throws UnsupportedEncodingException
	 */
	public static void sendHeaders(String fileName, int contentLen,
			HttpServletRequest request,
			HttpServletResponse response)
					throws UnsupportedEncodingException {
		String ua = request.getHeader("User-Agent").toLowerCase();
		boolean isIE = ((ua.indexOf("msie 6.0") != -1) ||
				(ua.indexOf("msie 7.0") != -1)) ? true : false;

		String encName = URLEncoder.encode(fileName, "UTF-8");

		// Derived from Squirrel Mail and from
		// http://www.jspwiki.org/wiki/BugSSLAndIENoCacheBug
		if (request.isSecure()) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Expires", "-1");
			response.addHeader("Cache-Control", "no-cache");
		}
		else {
			response.addHeader("Cache-Control", "private");
			response.addHeader("Pragma", "public");
		}

		if (isIE) {
			response.addHeader("Content-Disposition", "attachment; filename=\""
							+
							encName + "\"" );
			response.addHeader("Connection", "close");
			response.setContentType("application/force-download; name=\"" +
							encName + "\"" );
		}
		else {
			response.addHeader("Content-Disposition", "attachment; filename=\""
							+
							encName + "\"" );
			response.setContentType("application/octet-stream; name=\"" +
							encName + "\"" );
			if (contentLen > 0)
				response.setContentLength(contentLen);
		}

	}
}
