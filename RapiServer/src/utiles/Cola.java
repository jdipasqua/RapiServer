package utiles;

import java.util.List;

public class Cola <T>
{
	private List<T> cola;
	
	public Cola (List<T> cola)
	{
		this.cola = cola;
	}
	
	public T pop ()
	{
		if (cola == null || cola.isEmpty())
			return null;
		
		return cola.remove(0);
	}
	
	public boolean push (T o)
	{
		if (cola == null)
			return false;
		
		return cola.add(o);
	}
	
	public List<T> getCola ()
	{
		return cola;
	}
}