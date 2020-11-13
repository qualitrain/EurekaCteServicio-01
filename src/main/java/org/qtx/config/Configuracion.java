package org.qtx.config;
import org.qtx.web.cteRest.InfoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.EurekaClient;

@Configuration
public class Configuracion {
	
	@Value("${qtx.servicioArticulo}")
	private String appName;
	
	@Value("${qtx.servicioArticulo.uriDefault}")
	private String uriDefault;
	@Value("${qtx.servicioArticulo.context-path}")
	private String contexPath;
	@Value("${qtx.servicioArticulo.rest-application-path}")
	private String restApplicationPath;
	@Value("${qtx.servicioArticulo.recurso-articulo}")
	private String recursoArticulo;
	@Value("${qtx.servicioArticulo.sufijoTodos}")
	private String sufijoTodos;
	
	public Configuracion() {
		System.out.println("********* Configuracion() *********");
	}
	@Bean
	public InfoServicios getInfoServicios(EurekaClient cteEureka) {
		InfoServicios infoServicios = new InfoServicios(cteEureka, appName, uriDefault);
		infoServicios.setContexPath(contexPath);
		infoServicios.setRestApplicationPath(restApplicationPath);
		infoServicios.setRecursoArticulo(recursoArticulo);
		infoServicios.setSufijoTodos(sufijoTodos);
		return infoServicios;
	}

}
