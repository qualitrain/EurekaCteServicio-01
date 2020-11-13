package org.qtx.web.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilWeb {
	public static void invocarJsp(String uri, String prefijo, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		RequestDispatcher reqJsp;
		reqJsp = request.getServletContext()
				        .getRequestDispatcher(prefijo + uri);
		reqJsp.forward(request, response);
	}
	public static void invocarVistaError(Exception ex, HttpServletRequest request, HttpServletResponse response, String prefijo) 
			throws ServletException, IOException {
		String error =  ex.getClass().getName() 
				+ ":" + ex.getMessage();
		if(ex.getCause() != null) {
			error += ", causado por " + ex.getCause().getClass().getName()
					 + ":" + ex.getCause().getMessage();
		}
		request.setAttribute("error", error);
		invocarJsp("/error550.jsp", prefijo, request, response);		
	}

}
