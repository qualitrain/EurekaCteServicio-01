package org.qtx.web.cte;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.qtx.dominio.Articulo;

public interface ICteRestArticulo {
	String postRemoto_ArticuloJson(Articulo artI);
	String postRemoto_ArticuloFormUrlEncoded(HttpServletRequest request);
	Articulo getRemoto_ArticuloJsonXml(String cveArt);
	String getRemoto_ArticuloText(String cveArt);
	Map<String, String> getRemoto_ArticulosJsonXml();
	int getRemotoAsincrono_Articulos(List<String> respuestas);
}
