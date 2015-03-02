package com.TheJobCoach.webapp.userpage.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.webapp.util.server.ServletSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class DownloadImage extends HttpServlet {

	private static final long serialVersionUID = -8067428735370164388L;
	
	private static AccountManager account = new AccountManager();
	
	Logger logger = LoggerFactory.getLogger(DownloadImage.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,	IOException 
	{
		String userId = request.getParameter("userid");
		String fromUserId = request.getParameter("fromuserid");
		String token = request.getParameter("token");
		String type = request.getParameter("type");
		String format = request.getParameter("format");
		logger.info("Requesting image: " + type + " of format: " + format + " of user: " + userId + " from user: " + fromUserId + " with token: " + token);
		try
		{
			ServletSecurityCheck.check(request, new UserId(fromUserId, token, UserId.UserType.USER_TYPE_SEEKER));
		}
		catch (CoachSecurityException e)
		{
			return;
		}
		
		ServletOutputStream out = response.getOutputStream();
		UserId user = new UserId(userId, token, UserId.UserType.USER_TYPE_SEEKER);
		byte[] userImg = null;
		userImg = account.getUserImage(user, format);
		// Send zero length, because this File.size() is a long, while
		// HttpServletResponse.setContentLength() takes an int.
		if (userImg == null)
		{
			// No such document
			response.sendError(404);
			ServletUtils.sendHeaders("ERROR", 0, request, response);
			out.flush();
			out.close();
			return;
		}
		response.setContentType("image/jpg");
		ServletUtils.sendHeaders(userId + "-" + type + "-" + format + ".jpg", userImg.length, request, response);
		response.setBufferSize( 8 * 1024 );
		int bufSize = response.getBufferSize();
		byte [] buffer = new byte[bufSize];

		InputStream is = new ByteArrayInputStream(userImg);
		BufferedInputStream bis = new BufferedInputStream(is, bufSize);
		int count;
		while ( ( count = bis.read( buffer, 0, bufSize) ) != -1 ) 
		{
			out.write( buffer, 0, count );
		}
		bis.close();
		out.flush();
		out.close();
	}
}