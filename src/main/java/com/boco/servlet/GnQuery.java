package com.boco.servlet;

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

import com.boco.hbase.client.QueryGn;

/**
 * Servlet implementation class GnQuery
 */
@WebServlet("/GnQuery")
public class GnQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(GnQuery.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GnQuery() {
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
		String msisdn = request.getParameter("MSISDN");
		String startTime = request.getParameter("START_TIME");
		String endTime = request.getParameter("END_TIME");
		String random = request.getParameter("RANDOM");
		
		String ret = "";
		
		HttpSession session = request.getSession();
		if (null != msisdn && null != startTime && null != endTime) {
			try {
				long l = System.currentTimeMillis();
				ret = QueryGn.getJsonData(msisdn, startTime, endTime);
				long l2 = System.currentTimeMillis();
				session.setAttribute(random, (l2 -l));
			} catch (TException e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			}
		}
		ret = !"".equals(ret) ? ret : "{ total: 0, rows: [] }";
		out.write(ret);
		out.close();
	}

}
