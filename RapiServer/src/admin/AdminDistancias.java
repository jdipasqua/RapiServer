package admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparadores.ComparadorDistanciaChofer;

import controladores.ControladorTurnos;
import entidades.PuntoGeografico;
import entidades.estados.Estados.EstadoChofer;
import entidades.nopersistence.DistanciaChofer;
import entidades.usuarios.Chofer;

public class AdminDistancias
{
	// Cantidad de choferes que quedaran luego de hacer el calculo
	// de distancia local.
	public static final int PRIMER_CORTE = 20;
	// Cantidad de choferes que quedaran luego de hacer el calculo
	// de distancia utilizando los servicios de google.
	public static final int SEGUNDO_CORTE = 5;
	
	// Maximo radio de distancia entre el solicitante
	// y el chofer, en metros.
	public static final int MAXIMO_RADIO = 5000;
	
	public AdminDistancias ()
	{
		
	}
	
	/**
	 * Obtiene una lista de choferes ordenada con respecto a
	 * la distancia entre el chofer y el punto dado. La lista de choferes
	 * original se obtiene de los turnos disponibles.
	 * @param punto el punto del cual calcular la distancia.
	 * @return la lista de choferes.
	 */
	public List<Chofer> ordenarChoferes (PuntoGeografico punto)
	{
		return ordenarChoferes(getChoferesDisponibles(), punto);
	}
	
	/**
	 * Obtiene una lista de choferes ordenada con respecto a
	 * la distancia entre el chofer y el punto dado.
	 * @param choferes la lista de choferes sobre la cual calcular las distancias.
	 * @param punto el punto del cual calcular la distancia.
	 * @return la lista de choferes.
	 */
	public List<Chofer> ordenarChoferes (List<Chofer> choferes, PuntoGeografico punto)
	{
		// Genero la lista de distancias en base a la lista de choferes.
		List<DistanciaChofer> distancias = calcularDistancias(choferes, punto);
		
		// Calculo localmente las distancias de cada chofer.
		calcularDistanciasLocalmente(distancias, punto);
		
		// Saco los choferes que estan a mayor distancia
		// que la constante MAXIMO_RADIO.
		removerLejanos(distancias);
		
		// Ordeno los choferes por distancia al punto.
		Collections.sort(distancias, new ComparadorDistanciaChofer());
		
		// Si la cantidad de choferes es mayor a PRIMER_CORTE,
		// genero una sublista.
		if (distancias.size() > PRIMER_CORTE)
			distancias = distancias.subList(0, PRIMER_CORTE - 1);
		
		// Calculo las distancias utilizando los servicios de google.
		calcularDistanciasGoogle(distancias, punto);
		
		// Ordeno los choferes por distancia al punto.
		Collections.sort(distancias, new ComparadorDistanciaChofer());
		
		// Si la cantidad de choferes es mayor a SEGUNDO_CORTE,
		// genero una sublista.
		if (distancias.size() > SEGUNDO_CORTE)
			distancias = distancias.subList(0, SEGUNDO_CORTE - 1);
		
		return getChoferes(distancias);
	}
	
	private List<DistanciaChofer> calcularDistancias (List<Chofer> choferes, PuntoGeografico punto)
	{
		List<DistanciaChofer> distancias = new ArrayList<>();

		for (Chofer chofer : choferes)
		{
			DistanciaChofer distancia = new DistanciaChofer(chofer);
			distancias.add(distancia);
		}

		return distancias;
	}
	
	private void calcularDistanciasLocalmente (List<DistanciaChofer> distancias, PuntoGeografico punto)
	{
		for (DistanciaChofer distancia : distancias)
			distancia.calcularDistanciaLocalmente(punto);
	}
	
	private void calcularDistanciasGoogle (List<DistanciaChofer> distancias, PuntoGeografico punto)
	{
		for (DistanciaChofer distancia : distancias)
			distancia.calcularDistancia(punto);
	}
	
	private List<Chofer> getChoferes (List<DistanciaChofer> distancias)
	{
		List<Chofer> choferes = new ArrayList<>();

		for (DistanciaChofer distancia : distancias)
			choferes.add(distancia.getChofer());

		return choferes;
	}
	
	private void removerLejanos (List<DistanciaChofer> distancias)
	{
		for (int i = 0; i < distancias.size(); i++)
			if (distancias.get(i).getDistancia() > MAXIMO_RADIO)
				distancias.remove(i);
	}
	
	private List<Chofer> getChoferesDisponibles ()
	{
		return new ControladorTurnos().getChoferesTurnoAbierto(EstadoChofer.DISPONIBLE);
	}
}
