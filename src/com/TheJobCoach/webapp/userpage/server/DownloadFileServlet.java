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

import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;
import com.TheJobCoach.webapp.util.shared.CassandraException;

public class DownloadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -8067428735370164388L;
	
	Logger logger = LoggerFactory.getLogger(DownloadFileServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,	IOException 
	{
		UserDocumentManager cm = UserDocumentManager.getInstance();
		String docId = request.getParameter("docid");
		String userId = request.getParameter("userid");
		String token = request.getParameter("token");
		logger.info("Requesting doc: " + docId + " for user: " + userId + " with token: " + token);
		UserDocument userDoc;
		ServletOutputStream out = response.getOutputStream();
		UserId user =new UserId(userId, token, UserType.USER_TYPE_SEEKER);
		try {
			userDoc = cm.getUserDocument(user, docId);
		} catch (CassandraException e1) {
			logger.error("getUserDocument " + e1.getMessage());
			out.flush();
			out.close();
			return;
		}
		// Send zero length, because this File.size() is a long, while
		// HttpServletResponse.setContentLength() takes an int.
		if (userDoc == null)
		{
			// No such document
			response.sendError(404);
			ServletUtils.sendHeaders("ERROR", 0, request, response);
			out.flush();
			out.close();
			return;
		}
		ServletUtils.sendHeaders(userDoc.fileName, 0, request, response);
		response.setBufferSize( 8 * 1024 );
		int bufSize = response.getBufferSize();
		byte [] buffer = new byte[bufSize];
		byte[] doc = null;
		try 
		{
			doc = cm.getUserDocumentContent(user, docId);
			logger.info("Document has length :" + doc.length);
		} 
		catch (CassandraException e) 
		{
			logger.info("Unable to build report " + e.getMessage());
			out.flush();
			out.close();
			return;
		}
		InputStream is = new ByteArrayInputStream(doc);
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