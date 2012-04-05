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

import com.TheJobCoach.userdata.UserDocumentManager;
import com.TheJobCoach.webapp.userpage.shared.CassandraException;
import com.TheJobCoach.webapp.userpage.shared.UserDocument;

public class DownloadFileServlet extends HttpServlet {

	private static final long serialVersionUID = -8067428735370164388L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,	IOException 
	{
		UserDocumentManager cm = new UserDocumentManager();
		String docId = request.getParameter("docid");
		UserDocument userDoc;
		ServletOutputStream out = response.getOutputStream();
		try {
			userDoc = cm.getUserDocument(null, docId);
		} catch (CassandraException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			out.flush();
			out.close();
			return;
		}
		// Send zero length, because this File.size() is a long, while
		// HttpServletResponse.setContentLength() takes an int.
		ServletUtils.sendHeaders(userDoc.fileName, 0, request, response);
		response.setBufferSize( 8 * 1024 );
		int bufSize = response.getBufferSize();
		byte [] buffer = new byte[bufSize];

		byte[] doc = null;
		try 
		{
			doc = cm.getUserDocumentContent(null, docId);
			System.out.println("Document has length :" + doc.length);
		} 
		catch (CassandraException e) 
		{
			System.out.println("Document not found:");
			e.printStackTrace();
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