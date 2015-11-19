package comparadores;

import java.util.Comparator;

import entidades.nopersistence.DistanciaChofer;

public class ComparadorDistanciaChofer implements Comparator<DistanciaChofer>
{	
	@Override
	public int compare(DistanciaChofer distancia1, DistanciaChofer distancia2)
	{
		try
		{
			return distancia1.getDistancia() - distancia2.getDistancia();
		}
		catch (Exception e)
		{
			return 0;
		}
	}
}
