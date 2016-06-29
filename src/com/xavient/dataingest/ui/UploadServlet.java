package com.xavient.dataingest.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 8154525737516844828L;

	public void init() {
	}

	public UploadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("UI.jsp");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// Create path components to save the file
		final Part filePart = request.getPart("file");

		InputStream filecontent = null;
		InputStreamReader inR = null;
		BufferedReader buf = null;
		final PrintWriter writer = response.getWriter();

		try {
			filecontent = filePart.getInputStream();
			inR = new InputStreamReader(filecontent);
			buf = new BufferedReader(inR);
			String line;
			while ((line = buf.readLine()) != null) {
				KafkaPublisher.sendToKafka(line);
			}
			request.getSession().setAttribute("getAlert", "Message sent to Kafka topic");
			response.sendRedirect("UI.jsp");
		} catch (FileNotFoundException fne) {
			writer.println("Error: " + fne.getMessage());
		} finally {
			if (filecontent != null) {
				filecontent.close();
			}
			if (inR != null) {
				inR.close();
			}
			if (buf != null) {
				buf.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

}
