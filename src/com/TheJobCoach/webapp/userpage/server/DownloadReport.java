package com.TheJobCoach.webapp.userpage.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TheJobCoach.userdata.ReportActionHtml;
import com.TheJobCoach.webapp.mainpage.shared.UserId;
import com.TheJobCoach.webapp.mainpage.shared.UserId.UserType;
import com.TheJobCoach.webapp.util.shared.CassandraException;
import com.TheJobCoach.webapp.util.shared.FormatUtil;

public class DownloadReport extends HttpServlet {

	private static final long serialVersionUID = -8067428735370164389L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,	IOException 
	{
		String type = request.getParameter("reporttype");
		String userId = request.getParameter("userid");
		String token = request.getParameter("token");
		String lang = request.getParameter("lang");
		String format = request.getParameter("format");
		
		String start = request.getParameter("start");
		Date startDate = FormatUtil.getStringDate(start);
		String end = request.getParameter("end");
		Date endDate = FormatUtil.getStringDate(end);
		System.out.println("Start is: " + start + " converted to:" + startDate);
		System.out.println("End is: " + end + " converted to:" + endDate);
		String detailOpp = request.getParameter("detailopp");
		boolean includeOppDetail = FormatUtil.trueString.equals(detailOpp);
		String detailLog = request.getParameter("detaillog");
		boolean includeLogDetail = FormatUtil.trueString.equals(detailLog);
		String logPeriod = request.getParameter("logperiod");
		boolean onlyLogPeriod = FormatUtil.trueString.equals(logPeriod);
		
		System.out.println(
				"Requesting report: " + type 
				+ " user:" + userId 
				+ " format:" + format 
				+ " token:" + token 
				+ " start:" + start
				+ " end:" + end
				+ " lang:" + lang
				+ " detailOpp:" + detailOpp
				+ " detailLog:" + detailLog
				+ " logPeriod:" + logPeriod
				);
		ServletOutputStream out = response.getOutputStream();
		UserId user =new UserId(userId, token, UserType.USER_TYPE_SEEKER);
		
		ServletUtils.sendHeaders("Report.html", 0, request, response);
		response.setBufferSize( 8 * 1024 );
		int bufSize = response.getBufferSize();
		byte [] buffer = new byte[bufSize];
		byte[] doc = null;
		
		try 
		{
			if (type.equals("reportaction"))				
			{
				ReportActionHtml report = new ReportActionHtml(user, lang);
				doc = report.getReport(startDate, endDate, includeOppDetail, includeLogDetail, onlyLogPeriod);
			}
			System.out.println("Document has length :" + doc.length);
		} 
		catch (CassandraException e) 
		{
			System.out.println("Document type:");
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