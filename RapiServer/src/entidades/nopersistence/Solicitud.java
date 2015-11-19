package entidades.nopersistence;

import entidades.Viaje;

public class Solicitud
{
	private Viaje viaje;
	private int[] calificaciones;
	
	public Solicitud(Viaje viaje, int[] calificaciones)
	{
		super();
		this.viaje = viaje;
		this.calificaciones = calificaciones;
	}
	public Viaje getViaje()
	{
		return viaje;
	}
	public void setViaje(Viaje viaje)
	{
		this.viaje = viaje;
	}
	public int[] getCalificaciones()
	{
		return calificaciones;
	}
	public void setCalificaciones(int[] calificaciones)
	{
		this.calificaciones = calificaciones;
	}
}
