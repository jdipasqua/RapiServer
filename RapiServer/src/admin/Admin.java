/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin;

import comparadores.ComparadorDistanciaChofer;
import controladores.ControladorChoferes;
import controladores.ControladorGenerico;
import controladores.ControladorTurnos;
import controladores.ControladorViajes;
import entidades.Caracteristica;
import entidades.Movil;
import entidades.PuntoGeografico;
import entidades.Viaje;
import entidades.estados.Estados.EstadoChofer;
import entidades.nopersistence.DistanciaChofer;
import entidades.nopersistence.Solicitud;
import entidades.nopersistence.SolicitudPasajero;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utiles.GCMHelper;
import utiles.GoogleGeoCodeHelper;

/**
 * 
 * @author fcarou
 */
public class Admin
{
	public static final String KEY_PROYECTO = "AIzaSyCYPbaQK8rXN_QmViamOHFpPLdkfhBcUk0";

	public static final String SERVICIO_GOOGLE = "https://android.googleapis.com/gcm/send";

	private static Admin admin;

	private Logger logger = Logger.getLogger(Admin.class);

	private GCMHelper helper;
	
	private MiServlet servlet;

	public synchronized static Admin getAdmin()
	{
		if (admin == null)
			admin = new Admin();

		return admin;
	}

	private Admin()
	{
		configurar();

		ControladorGenerico con = new ControladorGenerico();

		@SuppressWarnings("unchecked")
		List<Chofer> choferes = con.buscarTodos(Chofer.class);

		if (choferes == null || choferes.isEmpty())
			inventar();

		helper = new GCMHelper();
		
		servlet = MiServlet.getServlet();
	}

	public synchronized Viaje aceptarViaje(long idChofer, long idViaje)
	{
		if (!servlet.aceptar(idChofer, idViaje))
			return null;
		
		Viaje viaje = new ControladorViajes().aceptarViaje(idChofer, idViaje);

		if (viaje != null)
			helper.enviarChoferAsignado(viaje.getPasajero(), viaje.getTurno()
					.getChofer());

		return viaje;
	}
	
	public synchronized void rechazarViaje (long idChofer, long idViaje)
	{
		servlet.rechazar(idChofer, idViaje);
	}

	public boolean finalizarViajeChofer(long idViaje)
	{
		Viaje viaje = new ControladorViajes().finalizarViaje(idViaje);

		if (viaje != null)
			helper.enviarFinalizacion(viaje.getPasajero().getGcm());

		return viaje != null;
	}

	public boolean finalizarViajePasajero(long idViaje)
	{
		Viaje viaje = new ControladorViajes().finalizarViaje(idViaje);

		if (viaje != null && viaje.getTurno() != null)
			helper.enviarFinalizacion(viaje.getTurno().getChofer().getGcm());

		return viaje != null;
	}

	public boolean iniciarViaje(long idViaje)
	{
		Viaje res = new ControladorViajes().iniciarViaje(idViaje);

		if (res != null)
			helper.enviarInicio(res.getPasajero());

		return res != null;
	}

	public synchronized Viaje solicitar(SolicitudPasajero solicitud)
	{
		GoogleGeoCodeHelper geoHelper = new GoogleGeoCodeHelper();
		
		for (int i = 0; i < solicitud.getPuntos().length; i++)
			geoHelper.completarPunto(solicitud.getPuntos()[i]);
		
		List<Chofer> choferes = new ControladorChoferes().choferesMovilCaracteristica(solicitud.getCaracteristicas());
		
		if (choferes.isEmpty())
			return null;
		
		Viaje viaje = new ControladorViajes().solicitar(solicitud.getIdPasajero(), solicitud.getComentario(), solicitud.getPuntos());
		
		if (viaje == null)
			return null;
		
		AdminDistancias admin = new AdminDistancias();
		choferes = admin.ordenarChoferes(choferes, solicitud.getPuntos()[0]);
		
		servlet.iniciar(choferes, viaje);
		
		return viaje;
//		logger.info("Solicitar, ID: " + idPasajero);
//		
//		GoogleGeoCodeHelper geoHelper = new GoogleGeoCodeHelper();
//
//		for (int i = 0; i < puntos.length; i++)
//			geoHelper.completarPunto(puntos[i]);
//
//		Viaje viaje = new ControladorViajes().solicitar(idPasajero, puntos);
//
//		if (viaje != null)
//			enviar(viaje);
//
//		return viaje;
	}

	public boolean cancelarViajeChofer(long idViaje)
	{
		Viaje viaje = (Viaje) new ControladorGenerico().buscarPorID(
				Viaje.class, idViaje);

		if (new ControladorViajes().cancelarViaje(idViaje))
		{
			Pasajero pasajero = viaje.getPasajero();
			helper.enviarCancelacion(pasajero.getGcm());

			return true;
		}

		return false;
	}

	/**
	 * Cancela el viaje a traves del pasajero e informa al chofer.
	 * 
	 * @param idViaje
	 *            el id del viaje.
	 * @return true si se pudo realizar la operacion.
	 */
	public boolean cancelarViajePasajero(long idViaje)
	{
		Viaje viaje = (Viaje) new ControladorGenerico().buscarPorID(
				Viaje.class, idViaje);

		if (new ControladorViajes().cancelarViaje(idViaje))
		{
			// Si el viaje esta asignado, informar al chofer.
			if (viaje.getTurno() != null)
				helper.enviarCancelacion(viaje.getTurno().getChofer().getGcm());

			return true;
		}

		return false;
	}

	public void enviar(Viaje viaje)
	{
		List<Chofer> choferes = getChoferesPorDistancia(viaje.getPuntos().get(0));

		if (choferes.isEmpty())
			return;

		Chofer chofer = choferes.get(0);
		
		Solicitud solicitud = new Solicitud(viaje, new ControladorViajes().calificaciones(viaje.getPasajero().getId()));
		System.out.print(helper.enviarSolicitud(chofer, solicitud));
	}

	private List<Chofer> getChoferesPorDistancia(PuntoGeografico punto)
	{
		List<DistanciaChofer> distancias = calcularDistancias(
				getChoferesDisponibles(), punto);
		Collections.sort(distancias, new ComparadorDistanciaChofer());

		if (distancias.isEmpty())
			return new ArrayList<>();

		if (distancias.size() > 5)
			distancias = distancias.subList(0, 4);

		return getChoferes(distancias);
	}

	private List<DistanciaChofer> calcularDistancias(List<Chofer> choferes,
			PuntoGeografico punto)
	{
		List<DistanciaChofer> distancias = new ArrayList<>();

		for (Chofer chofer : choferes)
		{
			DistanciaChofer distancia = new DistanciaChofer(chofer);
			distancia.calcularDistancia(punto);

			distancias.add(distancia);
		}

		return distancias;
	}

	private List<Chofer> getChoferesDisponibles()
	{
		return new ControladorTurnos()
				.getChoferesTurnoAbierto(EstadoChofer.DISPONIBLE);
	}

	public List<Chofer> getChoferes(List<DistanciaChofer> distancias)
	{
		List<Chofer> choferes = new ArrayList<>();

		for (DistanciaChofer distancia : distancias)
			choferes.add(distancia.getChofer());

		return choferes;
	}

	private void inventar()
	{
		logger.info("Poblando DB.");

		Chofer chofer2 = new Chofer("David", "Gilmour", "15448693",
				"el_david@hotmail.com", null, "cacaaaa", null);
		Chofer chofer = new Chofer("Felipe", "Carou", "154564544",
				"pipecarou@gmail.com", null, "pepito", null);
		chofer.setMoviles(new ArrayList<Movil>());
		chofer2.setMoviles(new ArrayList<Movil>());
		chofer.setUbicacion(new PuntoGeografico(-42.7848922, -65.0173401));
		chofer2.setUbicacion(new PuntoGeografico(-42.7667324, -65.0333368));
		Pasajero pasajero = new Pasajero("Jorge", "Di Pasqua", "15446458",
				"jdipasqua@hotmail.com", "carlito", null, null);
		Caracteristica caracteristica = new Caracteristica("Aclimatado",
				"El movil posee aire acondicionado / calefaccion.");

		List<Caracteristica> caracteristicas = new ArrayList<>();
		caracteristicas.add(caracteristica);
		Movil movil = new Movil("AAA-332", "Lamborghini", "Gallardo",
				caracteristicas);
		Movil movil2 = new Movil("BDF-562", "Fiat", "600", caracteristicas);

		chofer.getMoviles().add(movil);
		chofer2.getMoviles().add(movil2);

		ControladorGenerico con = new ControladorGenerico();
		con.create(caracteristica);
		con.create(movil);
		con.create(movil2);
		con.create(chofer);
		con.create(chofer2);
		con.create(pasajero);

		logger.info("Fin poblado DB.");
	}

	private void configurar()
	{
		BasicConfigurator.configure();

		/*
		 * try { Calendar c = Calendar.getInstance(); File f = new File
		 * ("Ejecucion");
		 * 
		 * if (!f.exists()) f.mkdir();
		 * 
		 * FileAppender fileAppender = new FileAppender (new HTMLLayout(),
		 * "Ejecucion" + File.separator + c.get(Calendar.DAY_OF_MONTH) + "-" +
		 * c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR) + ".html", true);
		 * BasicConfigurator.configure(fileAppender); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
	}
}
