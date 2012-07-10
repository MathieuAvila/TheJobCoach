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

import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.webapp.mainpage.shared.UserId;

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -2947803123194402987L;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Upload FILE : " + request.toString());
	
		//if (!ServletFileUpload.isMultipartContent(request)) 
		//	return; 

		UserDocumentManager cm = UserDocumentManager.getInstance();
		String docId = request.getParameter("docid");
		String user = request.getParameter("userid");
		String token = request.getParameter("token");
		UserId userId = new UserId(user, token, UserId.UserType.USER_TYPE_SEEKER);
		if (docId == null)
		{
			System.out.println("No DOC ID provided");
			return;
		}
		System.out.println("Upload for DOC ID:" + docId + " for user: " + user + " with token: " + token);
		
		FileItemFactory factory = new DiskFileItemFactory(); 
		ServletFileUpload upload = new ServletFileUpload(factory); 

		List<?> items = null;
		try {
			items = upload.parseRequest(request); 
		}
		catch (FileUploadException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Upload FILE items: " + items.size());
		
		for (Iterator<?> i = items.iterator(); i.hasNext();) {
			
			
			FileItem item = (FileItem) i.next();
			System.out.println("New item");
			System.out.println("Upload FILE item: " + item.getContentType());
			System.out.println("Upload FILE item: " + item.getName());
			System.out.println("Upload FILE item: " + item.getSize());
			
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
				//	File uploadedFile = new File(DB.PATH_UPLOAD+ fileName);
				//	item.write(uploadedFile);
				System.out.println("Upload FILE : "+ fileName + " size " + item.getSize());
				cm.setUserDocumentContent(userId, docId, fileName, item.get());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} // end for

	} // end service()
}