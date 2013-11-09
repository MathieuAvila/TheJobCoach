package com.TheJobCoach.webapp.userpage.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.webapp.util.server.ServletSecurityCheck;
import com.TheJobCoach.webapp.util.shared.CoachSecurityException;
import com.TheJobCoach.webapp.util.shared.UserId;


public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -2947803123194402987L;
	private static Logger logger = LoggerFactory.getLogger(UploadFileServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		logger.info("Upload FILE : " + request.toString());
	
		//if (!ServletFileUpload.isMultipartContent(request)) 
		//	return; 

		UserDocumentManager cm = UserDocumentManager.getInstance();
		String docId = request.getParameter("docid");
		String user = request.getParameter("userid");
		String token = request.getParameter("token");
		UserId userId = new UserId(user, token, UserId.UserType.USER_TYPE_SEEKER);
		if (docId == null)
		{
			logger.error("No DOC ID provided");
			return;
		}
		logger.info("Upload for DOC ID:" + docId + " for user: " + user + " with token: " + token);
		
		try
		{
			ServletSecurityCheck.check(request, new UserId(user, token, UserId.UserType.USER_TYPE_SEEKER));
		}
		catch (CoachSecurityException e2)
		{
			return;
		}

		FileItemFactory factory = new DiskFileItemFactory(); 
		ServletFileUpload upload = new ServletFileUpload(factory); 

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
			
			if (item.isFormField()) 
				continue;

			String fileName = item.getName(); 
			int slash = fileName.lastIndexOf("/"); 
			if (slash == -1) { 
				slash = fileName.lastIndexOf("\\"); 
			}
			if (slash != -1) { 
				fileName = fileName.substring(slash + 1);
			}
			try {
				logger.info("Upload FILE : "+ fileName + " size " + item.getSize());
				cm.setUserDocumentContent(userId, docId, fileName, item.get());
			}
			catch (Exception e) {
				logger.error("setUserDocumentContent " + e.getMessage());
			}
		}

	}
}