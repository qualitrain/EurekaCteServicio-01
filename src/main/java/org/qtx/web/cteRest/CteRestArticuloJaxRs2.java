package org.qtx.web.cteRest;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PreDestroy;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.qtx.dominio.Articulo;
import org.qtx.web.cte.CteException;
import org.qtx.web.cte.ICteRestArticulo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CteRestArticuloJaxRs2 implements ICteRestArticulo{
	@Autowired
	private InfoServicios infoServicios;
	
	private static Logger bitacora = LoggerFactory.getLogger(CteRestArticuloJaxRs2.class);
	
	private Client cteJaxRs;
	
	public CteRestArticuloJaxRs2() {
		super();
		this.cteJaxRs = ClientBuilder.newClient();
		bitacora.info("Abriendo conexion Cte Jax-Rs");
	}
	@PreDestroy
	public void cerrarCliente() {
		this.cteJaxRs.close();
		bitacora.info("Cerrando conexion Cte Jax-Rs");
	}
	
	public Articulo getRemoto_ArticuloJsonXml(String cveArt) {
		String uriArticulo = "";
		try {
			uriArticulo = infoServicios.getUriRecursoArticulo();
			Articulo articulo = this.cteJaxRs
					  				.target(uriArticulo)
					  				.path(cveArt)	
								    .request(MediaType.APPLICATION_JSON)
//								    .request(MediaType.APPLICATION_XML)
								    .get(Articulo.class);
			bitacora.info(uriArticulo);
			bitacora.info("getRemoto_ArticuloJsonXml(" + cveArt 
					+ "), " + "Valor recuperado:" + articulo);
			return articulo;
		}
		catch(Exception ex) {
			String msjError = "getRemoto_ArticuloJsonXml("
					+ cveArt + ")"
					+ ": GET " + uriArticulo + "?clave=" + cveArt + ", en Xml/JSon. / "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
	}
	public String getRemoto_ArticuloText(String cveArt) {
		String uriArticulo = "";
		try {
			uriArticulo = infoServicios.getUriRecursoArticulo();
			String cadArticulo = this.cteJaxRs
					  				 .target(uriArticulo)
					  				 .path(cveArt)
								     .request(MediaType.TEXT_PLAIN)
								     .get(String.class);
			
			bitacora.info(uriArticulo);
			bitacora.info("getRemoto_ArticuloText(" + cveArt +"): Valor recuperado:" + cadArticulo);
			return cadArticulo;
		}
		catch(Exception ex) {
			String msjError = "getRemoto_ArticuloText("
					+ cveArt + ")"
					+ ": GET " + uriArticulo + "?clave=" + cveArt + ", en Texto. / "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
	}

	public Map<String, String> getRemoto_ArticulosJsonXml() {
		String uriArticulos = "";
		try {
			uriArticulos = infoServicios.getUriRecursoArticuloTodos();
			String cadArticulosJson = this.cteJaxRs
					                      .target(uriArticulos)
//			                              .request(MediaType.APPLICATION_XML)
			                              .request(MediaType.APPLICATION_JSON)
			                              .get(String.class);
			bitacora.info(uriArticulos);
			bitacora.info("getRemoto_ArticulosJsonXml(): " + "Valor recuperado:" + cadArticulosJson);
			Map<String,String> cvesArticulos = this.ArticulosJsonToMap(cadArticulosJson);
			return cvesArticulos;
		}
		catch(Exception ex) {
			String msjError = "getRemoto_ArticulosJsonXml()"
					+ ": GET " + uriArticulos + ", en Json/Xml. / "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
	}
	public Map<String, String> getRemoto_ArticulosJsonXml2() {
		String uriArticulos = "";
		try {
			uriArticulos = infoServicios.getUriRecursoArticuloTodos();
			List<Articulo> articulos = this.cteJaxRs
					                       .target(uriArticulos)
//			                         	   .request(MediaType.APPLICATION_XML)
			                         	   .request(MediaType.APPLICATION_JSON)
			                         	   .get( new GenericType< List<Articulo> >() {} );
			bitacora.info(uriArticulos);
			bitacora.info("getRemoto_ArticulosJsonXml(): " + "Valor recuperado:" + articulos);
			Map<String,String> cvesArticulos = new TreeMap<>();
			for(Articulo artI : articulos) {
				cvesArticulos.put(artI.getNombre().toLowerCase() + ":" + artI.getClave(), artI.getClave());
			}
			return cvesArticulos;
		}
		catch(Exception ex) {
			String msjError = "getRemoto_ArticulosJsonXml()"
					+ ": GET " + uriArticulos + ", en Json/Xml. / "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
	}
	private Map<String, String> ArticulosJsonToMap(String cadArticulosJson) {
		Map<String,String> cvesArticulos = new TreeMap<>();
		JsonReader jsonReader = Json.createReader(new StringReader(cadArticulosJson));
		JsonArray jsonArticulos = jsonReader.readArray(); 
		for(int i=0; i<jsonArticulos.size(); i++) {
			JsonObject artIjson = jsonArticulos.getJsonObject(i);
			String claveI = artIjson.getString("clave");
			String nombreI = artIjson.getString("nombre");
			cvesArticulos.put(nombreI.toLowerCase() + ":" + claveI , claveI);
		}
		return cvesArticulos;
	}
	
	public String postRemoto_ArticuloJson(Articulo artI) {
		String uriArticulo = infoServicios.getUriRecursoArticulo();
		try {
			Entity<Articulo> bodyPeticion = Entity.entity(artI, MediaType.APPLICATION_JSON);
			String resultadoInsert = this.cteJaxRs
										 .target(uriArticulo)
					                     .request(MediaType.TEXT_PLAIN)
					                     .post(bodyPeticion, String.class);
			bitacora.info(uriArticulo);
			bitacora.info("postRemoto_ArticuloJson(" + artI
					+ ") " + "Valor recuperado:" + resultadoInsert);
			return resultadoInsert;
		}
		catch(Exception ex) {
			String msjError = "postRemoto_ArticuloJson("
					+ artI + ")"
					+ ": POST " + uriArticulo + ", Body en Json"
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
	}

	public String postRemoto_ArticuloFormUrlEncoded(HttpServletRequest request) {
		String uriArticulo = infoServicios.getUriRecursoArticulo();
		try {
			Entity<Form> bodyAltaArticulo = getBodyAltaArticulo(request);
			String resultadoInsert = this.cteJaxRs
						                 .target(uriArticulo)
						                 .request(MediaType.TEXT_PLAIN)
						                 .post(bodyAltaArticulo, String.class);
			bitacora.info(uriArticulo);
			bitacora.info("postRemoto_ArticuloFormUrlEncoded() " + "Valor recuperado:" + resultadoInsert);
			return resultadoInsert;
		}
		catch(Exception ex) {
			String msjError = "postRemoto_ArticuloFormUrlEncoded("
					+ request.getRequestURI() + "): "
					+ "POST " + uriArticulo + ", Body en FormUrlEncoded, "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
		}
		
	}
	private Entity<Form> getBodyAltaArticulo(HttpServletRequest request) {
		Form unaForma = new Form();
		unaForma.param("clave", request.getParameter("clave"));
		unaForma.param("nombre", request.getParameter("nombre"));
		unaForma.param("descripcion", request.getParameter("descripcion"));
		unaForma.param("costo", request.getParameter("costo"));
		unaForma.param("precio", request.getParameter("precio"));
		unaForma.param("existencia", request.getParameter("existencia"));

	    Entity<Form> bodyPeticion = 
	    		 Entity.entity(unaForma, MediaType.APPLICATION_FORM_URLENCODED);
		return bodyPeticion;
	}
	public int getRemotoAsincrono_Articulos(List<String> respuestas) {
		int folio = 0;
		final Object LOCK = new Object();
		CallBackArticulos callBack = null;
		synchronized (LOCK) {
			 respuestas.add("");
			 folio = respuestas.size() - 1;
			 callBack = new  CallBackArticulos(respuestas, folio);
		}
		String uriArticulos = null;
		try {
			uriArticulos = infoServicios.getUriRecursoArticulo();
	
			this.cteJaxRs
                .target(uriArticulos).path("todos_lento")
                .request(MediaType.APPLICATION_XML)
                .async()
                .get(callBack);
			bitacora.info(uriArticulos);
			bitacora.info("getRemotoAsincrono_Articulos( " + respuestas + ")");
			return folio;
		}
		catch(Exception ex) {
			String msjError = "getRemotoAsincrono_Articulos("
					+ respuestas + "): "
					+ "GET " + uriArticulos + ", en Xml, "
					+ ex.getClass().getName() + ":" + ex.getMessage();
			bitacora.error(msjError);
			throw new CteException(msjError,ex);
			
		}
	}
}
