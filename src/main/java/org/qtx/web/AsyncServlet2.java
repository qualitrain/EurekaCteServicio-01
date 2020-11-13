package org.qtx.web;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qtx.web.cte.ICteRestArticulo;
import org.qtx.web.util.UtilWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet("/TestAsync")
public class AsyncServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Vector<String> respuestas = new Vector<>();
	private static final String PREFIJO_JSP = "/WEB-INF/jsp";
	@Autowired
	private ICteRestArticulo cteRest;
	
	private static Logger bitacora = LoggerFactory.getLogger(AsyncServlet2.class);
       
    public AsyncServlet2() {
        super();
    }
    @Override
    public void init() throws ServletException {
    	super.init();
		bitacora.info("AsyncServlet.init()");
     }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String vista = request.getParameter("vista");
		String operacion = request.getParameter("operacion");
		bitacora.info("AsyncServlet.doGet(vista:" + vista +", operacion:" + operacion + ")");
		switch(vista) {
		case "menu.jsp":
			switch (operacion){
				case "Async":
					int pos = this.cteRest.getRemotoAsincrono_Articulos(respuestas);
					request.setAttribute("folio", pos);
					request.setAttribute("mensaje", "Cheque el resultado con el folio " + pos );
					request.setAttribute("boton", "submit");
					this.invocar("/espera.jsp", request, response);
					return;
				default:
					return;
			}
		case "espera.jsp":
			switch (operacion){
				case "leerResultado":
					String strfolio = request.getParameter("folio");
					int folio = Integer.parseInt(strfolio);
					String respuesta = this.respuestas.get(folio);
					if(respuesta.isEmpty()) {
						request.setAttribute("folio", folio);
						request.setAttribute("mensaje", "Pruebe nuevamente en un instante, con el folio " + folio);
						request.setAttribute("respuesta", "...en espera..");
						request.setAttribute("boton", "submit");
						this.invocar("/espera.jsp", request, response);
					}
					else {
						request.setAttribute("mensaje", "Petici&oacute;n as&iacute;ncrona resuelta");
						request.setAttribute("respuesta", respuesta);
						request.setAttribute("boton", "hidden");
						this.invocar("/espera.jsp", request, response);
					}
					return;
				case "regresar":
					this.invocar("/inicio.jsp",request,response);
					return;
			}
		
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	private void invocar(String uri, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		UtilWeb.invocarJsp(uri, PREFIJO_JSP, request, response);
	}

}
