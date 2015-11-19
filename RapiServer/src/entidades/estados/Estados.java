package entidades.estados;

public class Estados
{
	public enum EstadoChofer
	{
		DISPONIBLE, OCUPADO, FUERA_SERVICIO;
		
		@Override
		public String toString ()
		{
			switch (this)
			{
			case DISPONIBLE:
				return "Disponible";
			case OCUPADO:
				return "Ocupado";
			case FUERA_SERVICIO:
				return "Fuera servicio";
			default:
				return "";
			}
		}
	}
	
	public enum EstadoViaje
	{
		SIN_CHOFER, ASIGNADO, INICIADO, FINALIZADO, CANCELADO;
		
		@Override
		public String toString ()
		{
			switch (this)
			{
			case SIN_CHOFER:
				return "Sin chofer";
			case ASIGNADO:
				return "Asignado";
			case INICIADO:
				return "Iniciado";
			case FINALIZADO:
				return "Finalizado";
			case CANCELADO:
				return "Cancelado";
			default:
				return "";
			}
		}
	}
}
