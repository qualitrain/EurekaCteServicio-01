package org.qtx.web.cteRest;

import java.util.List;

import javax.ws.rs.client.InvocationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallBackArticulos implements InvocationCallback<String> {
	private List<String> respuestas;
	private int posRespuesta;
	
	private static Logger bitacora = LoggerFactory.getLogger(CallBackArticulos.class);
	
	public CallBackArticulos() {
		super();
	}

	public CallBackArticulos(List<String> respuestas, int folio) {
		this.respuestas = respuestas;
		this.posRespuesta = folio;
	}

	public String getRespuesta() {
		return respuestas.get(posRespuesta);
	}

	public void setRespuesta(String respuesta) {
		this.respuestas.set(posRespuesta, respuesta);
	}

	@Override
	public void completed(String respuesta) {
		bitacora.info("*** Llamado asíncrono terminado ***");
		this.setRespuesta(respuesta);
	}
 
	@Override
	public void failed(Throwable ex) {
		bitacora.info("*** Llamado asíncrono ha fallado ***");
		this.setRespuesta("Falla:" + ex.getClass().getName() + ", mensaje:" + ex.getMessage());
	}

}
