package heuristica;

public class Node
{
	Node father = null;
	Car [][] parking = null;
	int g;
	int h = 1;
	int f;
	int lastCost;
	
	// Constructors
	public Node (Car [][] parking)
	{
		this.parking = parking;
		this.father = null;
		updateCars();
	}
	public Node (Car [][] parking, Node node)
	{
		this.parking = parking;
		this.father = node;
		updateCars();
	}
	
	// Father
	public Node getFather ()
	{
		return this.father;
	}
	public void setFather (Node node)
	{
		this.father = node;
	}
	
	// Parking
	public Car [][] getParking ()
	{
		return this.parking;
	}
	public void setParking (Car [][] parking)
	{
		this.parking = parking;
	}
	
	// g
	public int getG ()
	{
		return this.g;
	}
	public void setG (int g)
	{
		this.g = g;
		this.f = this.g + this.h;
	}

	// h
	public int getH ()
	{
		return this.h;
	}
	public void setH (int h)
	{
		this.h = h;
		this.f = this.g + this.h;
	}

	// f
	public int getF ()
	{
		return this.f;
	}
	
	// lastCost
	public int getLastCost ()
	{
		return this.lastCost;
	}
	public void setLastCost (int lastCost)
	{
		this.lastCost = lastCost;
	}
	
	// sameParking
	public boolean sameParking (Node node)
	{
		for (int fil = 0; fil < this.parking.length; fil++)
		{
			for (int col = 0; col < this.parking[fil].length; col++)
			{
				if (!this.parking[fil][col].getId().equals(node.parking[fil][col].getId()))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	// updateCars
	public void updateCars ()
	{
		for (int fil = 0; fil < this.parking.length; fil++)
		{
			for (int col = 0; col < this.parking[fil].length; col++)
			{
				if (!this.parking[fil][col].getId().equals("__"))
				{
					this.parking[fil][col].setLast(true);
					break;
				}
			}
			for (int col = this.parking[fil].length - 1; col >= 0; col--)
			{
				if (!this.parking[fil][col].getId().equals("__"))
				{
					this.parking[fil][col].setFirst(true);
					break;
				}
			}
		}
	}
}
