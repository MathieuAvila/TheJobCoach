package com.TheJobCoach.webapp.userpage.server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.AccountManager;
import com.TheJobCoach.webapp.util.server.ServletSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class UploadImage extends HttpServlet {

	private static final long serialVersionUID = -2947803123194402987L;
	private static Logger logger = LoggerFactory.getLogger(UploadFileServlet.class);

	static byte[] OK;
	static byte[] ERROR;
	
	static {
		OK = "OK".getBytes(Charset.forName("UTF-8"));
		ERROR = "ERROR".getBytes(Charset.forName("UTF-8"));
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		logger.info("Upload image : " + request.toString());
	
		//if (!ServletFileUpload.isMultipartContent(request)) 
		//	return; 

		AccountManager cm = new AccountManager();
		String type = request.getParameter("type");
		String user = request.getParameter("userid");
		String token = request.getParameter("token");
		UserId userId = new UserId(user, token, UserId.UserType.USER_TYPE_SEEKER);
		if (type == null)
		{
			logger.error("No type provided");
			return;
		}
		logger.info("Upload for image type:" + type + " for user: " + user + " with token: " + token);

		try
		{
			ServletSecurityCheck.check(request, userId);
		}
		catch (CoachSecurityException e2)
		{
			return;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory(); 
		ServletFileUpload upload = new ServletFileUpload(factory); 
		factory.setFileCleaningTracker(new FileCleaningTracker());
		List<?> items = null;
		try {
			items = upload.parseRequest(request); 
		}
		catch (FileUploadException e) {
			logger.error("parseRequest : " + e.getMessage());
			return;
		}
		logger.info("Upload FILE items: " + items.size());
		
		for (Iterator<?> i = items.iterator(); i.hasNext();) {
	
			FileItem item = (FileItem) i.next();
			logger.info("New item");
			logger.info("Upload FILE item: " + item.getContentType());
			logger.info("Upload FILE item: " + item.getName());
			logger.info("Upload FILE item: " + item.getSize());

			if (item.getSize() > AccountManager.MAX_SIZE_IMAGE)
			{
				logger.error("Size too big : " + item.getSize() + " max is: " + AccountManager.MAX_SIZE_IMAGE);
				response.setStatus(200);
				response.getOutputStream().write(ERROR);
				return;
			}
			try {
				logger.info("Upload image size " + item.getSize());
				cm.setUserImage(userId, item.get());
			}
			catch (Exception e) {
				logger.error("setUserImage " + e.getMessage());
			}
		}
		
		response.getOutputStream().write(OK);
		response.setStatus(200);
	}
}