package heuristica;

public class Node
{
	Node father = null;
	Node son = null;
	String [][] parking = null;
	
	public Node(String [][] parking)
	{
		this.parking = parking;
		this.father = null;
	}
	public Node(String [][] parking, Node node)
	{
		this.parking = parking;
		this.father = node;
	}
	public String [][] getParking()
	{
		return this.parking;
	}
	public void setParking(String [][] parking)
	{
		this.parking = parking;
	}
	public Node getFather()
	{
		return this.father;
	}
	public void setFather(Node node)
	{
		this.father = node;
	}
	public Node getSon()
	{
		return this.son;
	}
	public void setSon(Node node)
	{
		this.son = node;
	}
}
