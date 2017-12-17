package heuristica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

public class AstarParking
{
	public static void main(String[] args) throws IOException
	{
		String str;

		int n_filas, n_columnas;
		String [] str_filas_columnas = new String [2];
		FileReader file1 = new FileReader(args[0]);
		BufferedReader buffer1 = new BufferedReader(file1);
		if ((str = buffer1.readLine()) != null)
		{
			str_filas_columnas = str.split(" ");
		}
		n_filas = Integer.parseInt(str_filas_columnas[0]);
		n_columnas = Integer.parseInt(str_filas_columnas[1]);

		String [][] parking_init = new String [n_filas][n_columnas];
		for (int fil = 0; fil < n_filas; fil++)
		{
			str = buffer1.readLine();
			parking_init[fil] = str.split(" ");
		}
		buffer1.close();
		
		FileReader file2 = new FileReader(args[1]);
		BufferedReader buffer2 = new BufferedReader(file2);
		String [][] parking_goal = new String [n_filas][n_columnas];
		for (int fil = 0; fil < n_filas; fil++)
		{
			str = buffer2.readLine();
			parking_goal[fil] = str.split(" ");
		}
		buffer2.close();

		System.out.println(Arrays.deepToString(parking_init));
		System.out.println(Arrays.deepToString(parking_goal));
		
		Node initNode = new Node(parking_init);
		Node goalNode = new Node(parking_goal);

		ArrayList <Node> openSet = new ArrayList <Node>();
		openSet.add(initNode);
		ArrayList <Node> closedSet = new ArrayList <Node>();
		boolean success = false;
		while (!openSet.isEmpty() && !success)
		{
			Node current = openSet.remove(0);	// Remove first node from OPEN
			closedSet.add(current);				// Put it inside CLOSED
			if (current.getParking().equals(goalNode.getParking()))		// If aux == goal, exit loop
			{
				success = true;
			}
			else
			{
				ArrayList <Node> expanded = new ArrayList <Node>();
				for (int i = 0; i < expanded.size(); i++)
				{
					if (contains(closedSet, expanded.get(i)))
					{
						
					}
				}
			}
		}
		
		/* Open = {I}, Closed = {}, Success = false
		
		While Open != {} or !Success:
		{
			Open.first (N) -> Closed
			if N = goal then Success=true
			else expand(N), 
		*/
	}

	private static boolean contains(ArrayList <Node> set, Node node)
	{
		for (int pos = 0; pos < set.size(); pos++)
		{
			if (node.getParking().equals(set.get(pos).getParking()))
			{
				return true;
			}
		}
		return false;
	}
}