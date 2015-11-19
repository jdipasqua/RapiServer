package admin;

import java.util.Iterator;
import java.util.List;

import controladores.ControladorViajes;
import utiles.Cola;
import utiles.GCMHelper;
import entidades.nopersistence.Solicitud;
import entidades.usuarios.Chofer;

/**
 * Clase que permite enviar solicitudes a choferes en orden, de a uno por vez.
 * @author fcarou
 *
 */
public class Sender extends Thread
{
	public static final int SEGUNDOS_BLOQUEO = 60;
	
	private Solicitud solicitud;
	private Cola<Chofer> cola;
	private GCMHelper helper;
	
	private Chofer actual;
	
	private boolean seguir;
	
	/**
	 * Crea un sender con una solicitud a enviar y una lista de choferes.
	 * @param solicitud la solicitud a enviar a cada chofer.
	 * @param choferes la lista de choferes, en el orden que hay que enviar las notificaciones.
	 */
	public Sender (Solicitud solicitud, List<Chofer> choferes)
	{
		this.solicitud = solicitud;
		this.cola = new Cola<Chofer>(choferes);
		this.helper = new GCMHelper();
	}
	
	public boolean enCola (long idChofer)
	{
		for (Chofer chofer : cola.getCola())
			if (chofer.getId() == idChofer)
				return true;
		
		return false;
	}
	
	public boolean remover (long idChofer)
	{
		synchronized (cola)
		{
			Iterator<Chofer> iterador = cola.getCola().iterator();
			
			while (iterador.hasNext())
			{
				if (idChofer == iterador.next().getId())
				{
					iterador.remove();
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Aborta la notificacion a los choferes.
	 */
	public void frenar ()
	{
		// Se pone la bandera seguir en false, para indicar que no hay que seguir enviando notificaciones.
		seguir = false;
		
		// Se interrumpe el hilo, en caso que este estuviera durmiendo.
		this.interrupt();
	}
	
	/**
	 * Indica que el ultimo chofer al que se le envio una notificacion rechazo la solicitud,
	 * y el hilo deberia continuar con el siguiente. En caso que el hilo ya se haya despertado
	 * por tiempo, este metodo no tiene efecto alguno.
	 * @param idChofer la id del chofer que rechazo.
	 * @return true si el ultimo chofer al que se le envio la notificacion es el mismo de la id.
	 */
	public boolean rechazar (long idChofer, long idViaje)
	{
		if (idChofer == actual.getId() && solicitud.getViaje().getId() == idViaje)
		{
			this.interrupt();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Indica que el ultimo chofer al que se le envio una solicitud acepto. Si es asi,
	 * se fuerza al hilo a detenerse.
	 * @param idChofer la id del chofer que acepto.
	 * @return true si el ultimo chofer al que fue enviada la solicitud es el mismo que el que acepto, false en caso que 'haya llegado tarde'.
	 */
	public boolean aceptar (long idChofer, long idViaje)
	{
		if (idChofer == actual.getId() && solicitud.getViaje().getId() == idViaje)
		{
			this.frenar();
			return true;
		}
		
		return false;
	}

	@Override
	public void run()
	{
		seguir = true;
		
		// Saco el primer chofer de la cola.
		actual = cola.pop();
		
		// Mientras haya choferes en la cola, envio notificiaciones.
		while (actual != null && seguir)
		{
			// Envio la solicitud al chofer.
			helper.enviarSolicitud(actual, solicitud);
			
			try
			{
				// Duermo la cantidad de segundos indicados por la constante.
				Thread.sleep(SEGUNDOS_BLOQUEO * 1000);
			}
			catch (InterruptedException ex)
			{
				// Interrumpen el hilo, ya que un chofer acepto el viaje o el actual
				// cancelo y hay que enviar al proximo.
			}
			
			// Saco otro chofer.
			synchronized (cola)
			{
				actual = cola.pop();
			}
		}

		// Ya que ningun chofer acepto, se cancela el viaje y se
		// notifica al pasajero.
		if (seguir)
		{
			// Se cancela el viaje.
			if (new ControladorViajes().cancelarViaje(solicitud.getViaje().getId()))
				// Notificacion al pasajero.
				helper.enviarSinChoferes(solicitud.getViaje().getPasajero());
		}
	}
}
