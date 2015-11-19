package entidades.nopersistence;

import utiles.GoogleGeoCodeHelper;
import entidades.PuntoGeografico;
import entidades.usuarios.Chofer;

public class DistanciaChofer
{
	private Chofer chofer;
	private int distancia;
	
	public DistanciaChofer (Chofer chofer)
	{
		this.chofer = chofer;
	}
	
	public void calcularDistanciaLocalmente (PuntoGeografico punto)
	{
		distancia = distance (punto, chofer.getUbicacion());
	}
	
	public void calcularDistancia (PuntoGeografico punto)
	{
		distancia = new GoogleGeoCodeHelper().getDistancia(chofer.getUbicacion(), punto);
	}
	
	private int distance(PuntoGeografico punto1, PuntoGeografico punto2)
	{
		double lat1 = punto1.getLatitud(), lat2 = punto2.getLatitud();
		double lng1 = punto1.getLongitud(), lng2 = punto2.getLongitud();
		
		double theta = lng1 - lng2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;

		return (int) (dist * 1000);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad)
	{
		return (rad * 180 / Math.PI);
	}

	public Chofer getChofer()
	{
		return chofer;
	}

	public void setChofer(Chofer chofer)
	{
		this.chofer = chofer;
	}

	public int getDistancia()
	{
		return distancia;
	}

	public void setDistancia(int distancia)
	{
		this.distancia = distancia;
	}
}
