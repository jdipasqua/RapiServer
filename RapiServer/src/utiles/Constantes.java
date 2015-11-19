package utiles;

public class Constantes
{
	public static final String SOLICITUD_VIAJE = "solicitud viaje";
	public static final String CHOFER_ASIGNADO = "chofer asignado";
	public static final String VIAJE_INCIADO = "viaje iniciado";
	public static final String VIAJE_FINALIZADO = "viaje finalizado";
	public static final String VIAJE_CANCELADO = "viaje cancelado";
	public static final String SIN_CHOFER = "no hay choferes";
	
	public static class Calificaciones
	{
		public static final int NEGATIVA = -1;
		public static final int NEUTRAL = 0;
		public static final int POSITIVA = 1;
	}
	
	public static class Registro
	{
		public static final int OK = 0;
		public static final int FALLO_PERSISTENCIA = 1;
		public static final int EMAIL_EXISTENTE = 2;
		public static final int FALTAN_DATOS = 3;		
		public static final int NULL = 4;
	}
}
