package org.qtx.config;

import java.util.List;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

public class InfoServicios {
	private EurekaClient cteEureka;
	private LoadBalancerClient balanceadorCarga;
	
	private String appName;
	private String uriDefault;
	private String contexPath;
	private String restApplicationPath;
	private String recursoArticulo;
	private String sufijoTodos;
	private static long nInvocacion = 0;

	public InfoServicios() {
		super();
	}
	public InfoServicios(String appName, String uriDefault) {
		super();
		this.appName = appName;
		this.uriDefault = uriDefault;
	}
	public InfoServicios(EurekaClient cteEureka, String appName, String uriDefault) {
		super();
		this.cteEureka = cteEureka;
		this.appName = appName;
		this.uriDefault = uriDefault;
	}
	public InfoServicios(EurekaClient cteEureka, LoadBalancerClient balancer, String appName, String uriDefault) {
		super();
		this.cteEureka = cteEureka;
		this.balanceadorCarga = balancer;
		this.appName = appName;
		this.uriDefault = uriDefault;
	}
	public EurekaClient getCteEureka() {
		return cteEureka;
	}
	public void setCteEureka(EurekaClient cteEureka) {
		this.cteEureka = cteEureka;
	}
	public LoadBalancerClient getBalanceadorCarga() {
		return balanceadorCarga;
	}
	public void setBalanceadorCarga(LoadBalancerClient balanceadorCarga) {
		this.balanceadorCarga = balanceadorCarga;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppName() {
		return this.appName;
	}
	public String getUriDefault() {
		return uriDefault;
	}
	public void setUriDefault(String uriDefault) {
		this.uriDefault = uriDefault;
	}
	public String getContexPath() {
		return contexPath;
	}
	public void setContexPath(String contexPath) {
		this.contexPath = contexPath;
	}
	public String getRestApplicationPath() {
		return restApplicationPath;
	}
	public void setRestApplicationPath(String restApplicationPath) {
		this.restApplicationPath = restApplicationPath;
	}
	public String getRecursoArticulo() {
		return recursoArticulo;
	}
	public void setRecursoArticulo(String recursoArticulo) {
		this.recursoArticulo = recursoArticulo;
	}
	public String getSufijoTodos() {
		return sufijoTodos;
	}
	public void setSufijoTodos(String sufijoTodos) {
		this.sufijoTodos = sufijoTodos;
	}
	public int getNinstancias() {
		Application appServicio = cteEureka.getApplication(appName);
		if(appServicio == null)
			return 0;
		List<InstanceInfo> instanciasServicio = appServicio.getInstances();
		return instanciasServicio.size();
	}
	public String getInstanciasString() {
		Application appServicio = cteEureka.getApplication(appName);
		if(appServicio == null)
			return "[NO HAY INSTANCIAS]";
		List<InstanceInfo> instanciasServicio = appServicio.getInstances();
		return instanciasServicio.stream()
				                 .map( i-> "[" + i.toString() + "]")
				                 .reduce("", (a,i) -> a + i + ", ");
	}
	public String getUriServicio() {
		Application appServicio = cteEureka.getApplication(appName);
		if(appServicio == null)
			return this.uriDefault;
		List<InstanceInfo> instanciasServicio = appServicio.getInstances();
		if(instanciasServicio.size() == 0)
			return this.uriDefault;
		InstanceInfo instancia = instanciasServicio.get( (int) (nInvocacion % instanciasServicio.size()));
		nInvocacion++;
		return "http://" + instancia.getHostName() + ":" + instancia.getPort() + "/";
	}
	public String getUriServicioBalanceado() {
		ServiceInstance instancia = this.balanceadorCarga.choose(appName);
		nInvocacion++;
		return "http://" + instancia.getHost() + ":" + instancia.getPort() + "/";
	}
	public String getUriRecursoArticulo() {
		return this.getUriServicio() + this.getContexPath() + "/" + this.getRestApplicationPath() + "/" + this.getRecursoArticulo();
	}
	public String getUriRecursoArticuloBalanceado() {
		return this.getUriServicioBalanceado() + this.getContexPath() + "/" + this.getRestApplicationPath() + "/" + this.getRecursoArticulo();
	}
	public String getUriRecursoArticuloTodos() {
		return this.getUriRecursoArticulo() 
//				+ "/" + this.getSufijoTodos()
				;
	}
	public void mostrarDatosServiciosDescubiertos(Logger bitacora) {
		bitacora.info("App name:" + this.getAppName());
		bitacora.info("Cant instancias:" + this.getNinstancias());
		bitacora.info("Instancias: " + this.getInstanciasString());
		bitacora.info("uri: " + this.getUriServicio());
		bitacora.info("uri recurso articulo: " + this.getUriRecursoArticulo());
		bitacora.info("uri recurso articulo/todos: " + this.getUriRecursoArticuloTodos());
	}
	@PreDestroy
	public void desregistrarClienteEureka() {
		this.cteEureka.shutdown();
	}
}
