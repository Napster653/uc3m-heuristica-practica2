package heuristica;

public class Car
{
	String id = null;
	boolean first, last;

	public Car ()
	{
		this.id = "__";
	}
	public Car(String id)
	{
		this.id = id;
	}
	public Car(Car c)
	{
		this.id = c.getId();
		this.first = c.isFirst();
		this.last = c.isLast();
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public boolean isFirst()
	{
		return first;
	}
	public void setFirst(boolean isFirst)
	{
		this.first = isFirst;
	}
	public boolean isLast()
	{
		return last;
	}
	public void setLast(boolean isLast)
	{
		this.last = isLast;
	}
}
