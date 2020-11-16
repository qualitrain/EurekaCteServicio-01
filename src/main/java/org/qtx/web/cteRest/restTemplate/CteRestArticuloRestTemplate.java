package org.qtx.web.cteRest.restTemplate;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

import org.qtx.config.InfoServicios;
import org.qtx.dominio.Articulo;
import org.qtx.web.cte.CteException;
import org.qtx.web.cte.ICteRestArticulo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Primary
@Component
public class CteRestArticuloRestTemplate implements ICteRestArticulo {
	private static Logger bitacora = LoggerFactory.getLogger(CteRestArticuloRestTemplate.class);
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	@Qualifier("SinEurekaSinRibbon")
	private InfoServicios infoServicios;
	
	public CteRestArticuloRestTemplate() {
		bitacora.info("***** CteRestArticuloRestTemplate instanciado *****");
	}

	private String getUriArticulo() {
		return "http://" + infoServicios.getAppName() + "/" 
                + infoServicios.getContexPath() + "/" 
                + infoServicios.getRestApplicationPath() + "/" 
                + infoServicios.getRecursoArticulo();		
	}

	@Override
	public String postRemoto_ArticuloJson(Articulo artI) {
		String uriArticulo = this.getUriArticulo();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
			HttpEntity<Articulo> httpArt = new HttpEntity<Articulo>(artI,headers);
			ResponseEntity<String> resultadoInsert = restTemplate.postForEntity(uriArticulo, httpArt, String.class);
			
			bitacora.info(uriArticulo);
			bitacora.info("postRemoto_ArticuloJson(" + artI
					+ ") " + "Valor recuperado:" + resultadoInsert.getBody());
			return resultadoInsert.getBody();
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

	@Override
	public String postRemoto_ArticuloFormUrlEncoded(HttpServletRequest request) {
		String uriArticulo = this.getUriArticulo();
		try {
			MultiValueMap<String,String> bodyAltaArticulo = getBodyAltaArticulo(request);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
			HttpEntity<MultiValueMap<String,String>> httpArt = new HttpEntity<>(bodyAltaArticulo, headers);
			
			ResponseEntity<String> resultadoInsert = restTemplate.postForEntity(uriArticulo, httpArt, String.class);			
			
			bitacora.info(uriArticulo);
			bitacora.info("postRemoto_ArticuloFormUrlEncoded() " + "Valor recuperado:" + resultadoInsert.getBody());
			return resultadoInsert.getBody();
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
	private MultiValueMap<String,String> getBodyAltaArticulo(HttpServletRequest request) {
		Map<String,String> body = new HashMap<>();
		body.put("clave", request.getParameter("clave"));
		body.put("nombre", request.getParameter("nombre"));
		body.put("descripcion", request.getParameter("descripcion"));
		body.put("costo", request.getParameter("costo"));
		body.put("precio", request.getParameter("precio"));
		body.put("existencia", request.getParameter("existencia"));
		MultiValueMap<String,String> mvmBody = new LinkedMultiValueMap<>();
		mvmBody.setAll(body);
		return mvmBody;
	}
	
	@Override
	@HystrixCommand(fallbackMethod = "fbackGetRemoto_ArticuloJsonXml")
	public Articulo getRemoto_ArticuloJsonXml(String cveArt) {
		String uriArticulo = this.getUriArticulo() + "/" + cveArt;
		try {
			if(Math.random() < 0.5)
				throw new RuntimeException("Error simulado");
			Articulo articulo = restTemplate.getForObject(uriArticulo, Articulo.class);
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
	
	public Articulo fbackGetRemoto_ArticuloJsonXml(String cveArt) {
		bitacora.warn("***** fbackGetRemoto_ArticuloJsonXml(" + cveArt +") *****");
		Articulo art = new Articulo("999999","Servicio abajo","Servicio desde fallback", new BigDecimal("0.99"), new BigDecimal("9.99"), 0);
		return art;
	}

	@Override
	public String getRemoto_ArticuloText(String cveArt) {
		String uriArticulo = this.getUriArticulo() + "/" + cveArt;
		try {
			RequestEntity<Void> request =  
			         RequestEntity.get(new URI(uriArticulo))
			              .accept(MediaType.TEXT_PLAIN).build();
			ResponseEntity<String> resp = restTemplate.exchange(request, String.class);
			String cadArticulo = resp.getBody();
			
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

	@Override
	public Map<String, String> getRemoto_ArticulosJsonXml() {
		String uriArticulos = this.getUriArticulo();
		try {
			RequestEntity<Void> request =  
			         RequestEntity.get(new URI(uriArticulos))
			              .accept(MediaType.APPLICATION_JSON).build();
			ResponseEntity<String> resp = restTemplate.exchange(request, String.class);
			
			String cadArticulosJson = resp.getBody();
			
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

	@Override
	public int getRemotoAsincrono_Articulos(List<String> respuestas) {
		bitacora.error("getRemotoAsincrono_Articulos no implementado");
		return 0;
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

}
