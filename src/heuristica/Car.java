package heuristica;

public class Car
{
	String id = null;
	boolean first, last, canMoveFront, canMoveBack;
	public Car(String id)
	{
		super();
		this.id = id;
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
	public boolean canMoveFront()
	{
		return canMoveFront;
	}
	public void setCanMoveFront(boolean canMoveFront)
	{
		this.canMoveFront = canMoveFront;
	}
	public boolean canMoveBack()
	{
		return canMoveBack;
	}
	public void setCanMoveBack(boolean canMoveBack)
	{
		this.canMoveBack = canMoveBack;
	}
}
