package org.qtx.config;
import org.qtx.web.cteRest.CteRestArticuloJaxRs2;
import org.qtx.web.cteRest.InfoServicios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.netflix.discovery.EurekaClient;

@Configuration
public class Configuracion {
	
	private static Logger bitacora = LoggerFactory.getLogger(Configuracion.class);
	
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
		bitacora.info(" Configuracion() intanciado");
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
	@Bean
	@Primary
	public InfoServicios getInfoServicios(EurekaClient cteEureka, LoadBalancerClient balanceador) {
		InfoServicios infoServicios = new InfoServicios(cteEureka, balanceador, appName, uriDefault);
		bitacora.info("InfoServicios instanciado (balanceador = " + balanceador);
		infoServicios.setContexPath(contexPath);
		infoServicios.setRestApplicationPath(restApplicationPath);
		infoServicios.setRecursoArticulo(recursoArticulo);
		infoServicios.setSufijoTodos(sufijoTodos);
		return infoServicios;
	}

}
