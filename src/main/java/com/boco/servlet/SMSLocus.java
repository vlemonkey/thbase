package com.boco.servlet;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.hbase.client.QuerySMS;

/**
 * Servlet implementation class SMSLocus
 */
@WebServlet("/SMSLocus")
public class SMSLocus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger LOG = LoggerFactory.getLogger(SMSLocus.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMSLocus() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		String smId = request.getParameter("SM_ID");
		String startTime = request.getParameter("START_TIME");
		String endTime = request.getParameter("END_TIME");
		String isHWZX = request.getParameter("ISHWZX");
		String random = request.getParameter("RANDOM");
		
//		LOG.info(smId);
//		LOG.info(startTime);
//		LOG.info(endTime);
//		LOG.info(random);
		
		String ret = "";
		
		HttpSession session = request.getSession();
		if (isNotBlank(smId) && isNotBlank(startTime) && isNotBlank(endTime)) {
			try {
				long l = System.currentTimeMillis();
				ret = QuerySMS.getLocusJsonData(smId, startTime, endTime, isHWZX);
				long l2 = System.currentTimeMillis();
				session.setAttribute(random, (l2 -l));
			} catch (TException e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			}
		}
		ret = !"".equals(ret) ? ret : "{}";
		out.write(ret);
		out.close();
	}

}
