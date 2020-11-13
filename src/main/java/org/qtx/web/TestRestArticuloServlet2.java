package org.qtx.web;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue.ValueType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qtx.dominio.Articulo;
import org.qtx.web.cte.CteException;
import org.qtx.web.cte.ICteRestArticulo;
import org.qtx.web.cteRest.InfoServicios;
import org.qtx.web.util.UtilWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet("/TestRest")
public class TestRestArticuloServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PREFIJO_JSP = "/WEB-INF/jsp";
	
	@Autowired
	private ICteRestArticulo cteRest;
//	@Autowired
//	private InfoServicios infoServicios;
	
	private static Logger bitacora = LoggerFactory.getLogger(TestRestArticuloServlet2.class);
    
    public TestRestArticuloServlet2() {
        super();
    }
    @Override
    public void init() throws ServletException {
    	super.init();
		bitacora.info("TestRestArticuloServlet.init()");
     }
    @Override
    public void destroy() {
    	super.destroy();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
												throws ServletException, IOException {
		String vista = request.getParameter("vista");
		String operacion = request.getParameter("operacion");
		
		bitacora.info("TestRestArticuloServlet.doGet(vista:" + vista +", operacion:" + operacion);
//		this.infoServicios.mostrarDatosServicio(bitacora);
		
		if(vista==null) {
			vista = "inicio.jsp";
		}
		switch(vista) {
			case "inicio.jsp":
				try {
					Map<String,String> cvesArticulos = cteRest.getRemoto_ArticulosJsonXml();
					request.setAttribute("mapaArticulos", cvesArticulos);
					this.invocar("/menu.jsp", request, response);
					return;
				}
				catch(CteException crex){
					UtilWeb.invocarVistaError(crex, request, response, PREFIJO_JSP);
					return;					
				}
			case "menu.jsp":
				switch (operacion){
					case "consulta":
						String cveArt = request.getParameter("cveArt");
						try {
							Articulo articulo = cteRest.getRemoto_ArticuloJsonXml(cveArt);
							if(articulo != null)
								request.setAttribute("articulo", articulo);
							else
								request.setAttribute("articulo", new Articulo("Inexistente","Inexistente","Inexistente",new BigDecimal(-1)));
							this.invocar("/salida.jsp",request, response);
							return;
						}
						catch(CteException crex) {
							UtilWeb.invocarVistaError(crex, request, response, PREFIJO_JSP);
							return;
						}
					case "alta":
						this.invocar("/alta.jsp",request,response);
						return;
					case "carga":
						this.invocar("/cargaJson.jsp",request,response);
						return;
					default: return;
				}
			case "alta.jsp":
				switch (operacion){
					case "insercion":
						String resultadoInsert = "";
						try {
							resultadoInsert = cteRest.postRemoto_ArticuloFormUrlEncoded(request);
						}
						catch(CteException crex) {
							UtilWeb.invocarVistaError(crex, request, response, PREFIJO_JSP);
							return;
						}
						request.setAttribute("resultadoInsert", resultadoInsert);
						this.invocar("/alta.jsp",request,response);
						return;
					case "regresar":
						this.invocar("/inicio.jsp",request,response);
						return;
				}
			case "cargaJson.jsp":
				switch (operacion){
					case "cargaJson":
						String cadJson = request.getParameter("articulos");
						Map<String,String> respuestasCarga = null;
						if(cadJson == null || cadJson.trim().isEmpty()) {
							respuestasCarga = new TreeMap<>();
							respuestasCarga.put("General", "Json Vac&iacute;o");
						}
						else {
						    List<Articulo> articulos = extraerArticulos(cadJson);
							respuestasCarga = insertarArticulos(articulos);
						}
						request.setAttribute("respuestasCarga", respuestasCarga);
						this.invocar("/cargaJson.jsp",request,response);
						return;
					case "regresar":
						this.invocar("/inicio.jsp",request,response);
						return;
				}
			case "salida.jsp":
				switch (operacion){
					case "regresar":
						this.invocar("/inicio.jsp",request,response);
						return;
				}
		}
	}
	private List<Articulo> extraerArticulos(String cadJson) {
		JsonReader reader = Json.createReader(new StringReader(cadJson));
		JsonStructure estructuraJson = reader.read();
		ValueType tipoValor = estructuraJson.getValueType();
		List<Articulo> articulos = null;
		if(tipoValor == ValueType.OBJECT) {
			articulos = new ArrayList<Articulo>();
			articulos.add(parsearArticulo(estructuraJson.asJsonObject()));
		}
		else 
		if(tipoValor == ValueType.ARRAY){
			articulos = parsearArticulos(estructuraJson.asJsonArray());
		}
		return articulos;
	}

	private Map<String, String> insertarArticulos(List<Articulo> articulos) {
		Map<String,String> mapRespuestasCarga = new TreeMap<>();
		for(Articulo artI: articulos) {
			try {
				String resultadoI = this.cteRest.postRemoto_ArticuloJson(artI);
				mapRespuestasCarga.put(artI.getClave(), resultadoI);
			}
			catch(CteException crex) {
				mapRespuestasCarga.put(artI.getClave(), crex.getMessage());
			}
		}
		return mapRespuestasCarga;
	}
	private List<Articulo> parsearArticulos(JsonArray artJsonArray) {
		ArrayList<Articulo> articulos = new ArrayList<>();
		for(int i=0; i < artJsonArray.size(); i++) {
			articulos.add(this.parsearArticulo(artJsonArray.getJsonObject(i)));
		}
		return articulos;
	}
	private Articulo parsearArticulo(JsonObject artJson) {
		JsonString claveJson = artJson.getJsonString("clave");
		JsonString nombreJson = artJson.getJsonString("nombre");
		JsonString descripcionJson = artJson.getJsonString("descripcion");
		BigDecimal bdCosto = artJson.getJsonNumber("costo").bigDecimalValue();
		BigDecimal bdPrecio = artJson.getJsonNumber("precio").bigDecimalValue();
		int existencia = artJson.getJsonNumber("existencia").intValue();
		return new Articulo(claveJson.getString(), 
							nombreJson.getString(), 
							descripcionJson.getString(), 
							bdCosto, bdPrecio, existencia);
	}
	private void invocar(String uri, HttpServletRequest request, HttpServletResponse response) 
					throws ServletException, IOException {
		UtilWeb.invocarJsp(uri, PREFIJO_JSP, request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
