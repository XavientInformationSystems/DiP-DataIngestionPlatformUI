package com.xavient.dataingest.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class KafkaWrapperServlet
 */
@WebServlet("/KafkaWrapperServlet")
public class KafkaWrapperServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KafkaWrapperServlet() {
		super();
		}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.sendRedirect("UI.jsp");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String payload = request.getParameter("text1");
		System.out.println("!!!!!!!" + payload);
		if(payload.length()<1)
		{
			request.getSession().setAttribute("getAlert", "Please enter a valid message");
			
			response.sendRedirect("UI.jsp");
			//doGet(request, response);
		
		}
		else{
		
		String[] individualMessage  = payload.split("\n");
		
		for ( String msg  : individualMessage){
		
		KafkaPublisher.sendToKafka(msg);
		}
		request.getSession().setAttribute("getAlert", "Message publised to the Kafka Topic");
		doGet(request, response);
	}
	}
}
