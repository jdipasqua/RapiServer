package utiles;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;

import entidades.nopersistence.Contenido;
import entidades.nopersistence.Data;
import entidades.nopersistence.Solicitud;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

public class GCMHelper
{
	Logger logger = Logger.getLogger(GCMHelper.class);
	
	public static final String KEY_PROYECTO = "AIzaSyCYPbaQK8rXN_QmViamOHFpPLdkfhBcUk0";
	public static final String SERVICIO_GOOGLE = "https://android.googleapis.com/gcm/send";
	
	public GCMHelper ()
	{
		
	}
	
	public boolean enviarSinChoferes (Pasajero pasajero)
	{
		return enviarNotificacion(Constantes.SIN_CHOFER, pasajero.getGcm());
	}
	
	public boolean enviarInicio (Pasajero pasajero)
	{
		return enviarNotificacion(Constantes.VIAJE_INCIADO, pasajero.getGcm());
	}
	
	public boolean enviarFinalizacion (String gcm)
	{
		return enviarNotificacion(Constantes.VIAJE_FINALIZADO, gcm);
	}
	
	public boolean enviarChoferAsignado (Pasajero pasajero, Chofer chofer)
	{
		return enviarNotificacion(Constantes.CHOFER_ASIGNADO, chofer, pasajero.getGcm());
	}
	
	public boolean enviarCancelacion (String gcm)
	{
		return enviarNotificacion(Constantes.VIAJE_CANCELADO, gcm);
	}
	
	public boolean enviarSolicitud (Chofer chofer, Solicitud solicitud)
	{
		return enviarNotificacion(Constantes.SOLICITUD_VIAJE, solicitud, chofer.getGcm());
	}
	
	public boolean enviarNotificacion (String tipo, String id)
	{
		return enviarNotificacion(tipo, null, id);
	}
	
	public boolean enviarNotificacion (String tipo, Object object, String gcm)
    {
		if (gcm == null || gcm.equals(""))
			return false;
		
		Contenido contenido = this.generarContenido(tipo, object, gcm);
		
    	HttpHelper helper = new HttpHelper(SERVICIO_GOOGLE);
    	
    	String contenidoJson = new Gson().toJson(contenido);
    	System.out.println (contenidoJson);
    	Header header = new BasicHeader("Authorization", "key=" + KEY_PROYECTO);
    	
    	JSONObject res = new JSONObject(helper.doPost(contenidoJson, header));
    	
    	return res.getInt("success") == 1;
    }
	
	private Contenido generarContenido (String tipo, Object object, String gcm)
    {   		
		Data data = new Data(tipo, object);
		Contenido contenido = new Contenido(new String[]{gcm}, data);
    	
    	return contenido;
    }
}
