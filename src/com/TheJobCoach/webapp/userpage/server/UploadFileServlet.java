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

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -2947803123194402987L;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (!ServletFileUpload.isMultipartContent(request)) 
			return; 

		UserDocumentManager cm = new UserDocumentManager();
		String docId = request.getParameter("docid");
		if (docId == null)
		{
			System.out.println("No DOC ID provided");
			return;
		}
		//ServletOutputStream out = response.getOutputStream();
		
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

		for (Iterator<?> i = items.iterator(); i.hasNext();) { 
			FileItem item = (FileItem) i.next();

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
				System.out.println("Upload FILE : "+ fileName + " size " + item.getSize() + item.get());
				cm.setUserDocumentContent(null, docId, fileName, item.get());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} // end for

	} // end service()
}