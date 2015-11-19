package entidades.nopersistence;

import java.util.List;

import entidades.Caracteristica;
import entidades.PuntoGeografico;

public class SolicitudPasajero
{
	private PuntoGeografico [] puntos;
	
	private long idPasajero;
	
	private String comentario;
	
	private List<Caracteristica> caracteristicas;

	public SolicitudPasajero ()
	{
		
	}
	
	public SolicitudPasajero(PuntoGeografico [] puntos,
			long idPasajero, String comentario, List<Caracteristica> caracteristicas)
	{
		super();
		this.puntos = puntos;
		this.idPasajero = idPasajero;
		this.caracteristicas = caracteristicas;
	}

	public PuntoGeografico[] getPuntos()
	{
		return puntos;
	}

	public void setPuntos(PuntoGeografico[] puntos)
	{
		this.puntos = puntos;
	}

	public long getIdPasajero()
	{
		return idPasajero;
	}

	public void setIdPasajero(long idPasajero)
	{
		this.idPasajero = idPasajero;
	}

	public String getComentario()
	{
		return comentario;
	}

	public void setComentario(String comentario)
	{
		this.comentario = comentario;
	}

	public List<Caracteristica> getCaracteristicas()
	{
		return caracteristicas;
	}

	public void setCaracteristicas(List<Caracteristica> caracteristicas)
	{
		this.caracteristicas = caracteristicas;
	}
}
