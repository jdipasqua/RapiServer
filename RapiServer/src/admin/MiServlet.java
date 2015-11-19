package admin;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import controladores.ControladorViajes;
import entidades.Viaje;
import entidades.nopersistence.Solicitud;
import entidades.usuarios.Chofer;

public class MiServlet implements ServletContextListener
{
	private static MiServlet instancia;

	private List<Sender> hilos;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		for (Sender t : hilos)
		{
			System.out.println("Matando hilo.");
			t.frenar();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		System.out.println("Inicia...");
		
		instancia = this;
		
		hilos = new ArrayList<>();
	}
	
	public boolean existe (long idChofer)
	{
		for (Sender s : hilos)
		{
			State state = s.getState();
			
			if (!state.equals(Thread.State.TERMINATED))
				if (s.enCola(idChofer))
					return true;
		}
		
		return false;
	}
	
	public static MiServlet getServlet ()
	{
		return instancia;
	}
	
	/**
	 * Permite que el hilo notificador al que pertenece un chofer
	 * se despierte y continue su ejecucion. Es decir, cuando se envia una
	 * solicitud a un chofer, el hilo duerme determinado tiempo. Si el chofer
	 * que recibio la notificacion rechaza, se informa a ese hilo que debe
	 * continuar ejecucion en vez de esperar a despertarse por si mismo.
	 * @param idChofer la id del chofer que rechazo.
	 * @return true si la operacion se pudo completar.
	 */
	public boolean rechazar (long idChofer, long idViaje)
	{
		for (Sender sender : hilos)
		{
			if (!sender.getState().equals(Thread.State.TERMINATED))
				if (sender.rechazar(idChofer, idViaje))
					return true;
		}
		
		return false;
	}
	
	public boolean aceptar (long idChofer, long idViaje)
	{
		Sender aceptado = null;
		
		for (Sender sender : hilos)
		{
			if (!sender.getState().equals(Thread.State.TERMINATED))
			{
				if (sender.aceptar(idChofer, idViaje))
					aceptado = sender;
				else
					sender.remover(idChofer);
			}
		}
		
		if (aceptado != null)
		{
			hilos.remove(aceptado);
			return true;
		}
		
		return false;
	}
	
	public void iniciar (List<Chofer> choferes, Viaje viaje)
	{
		Solicitud solicitud = new Solicitud(viaje, new ControladorViajes().calificaciones(viaje.getPasajero().getId()));
		Sender sender = new Sender(solicitud, choferes);
		hilos.add(sender);
		sender.start();
	}
}
