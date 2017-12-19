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

		String [][] parking_aux = new String [n_filas][n_columnas];

		Car [][] parking_init = new Car [n_filas][n_columnas];
		for (int fil = 0; fil < n_filas; fil++)
		{
			str = buffer1.readLine();
			parking_aux[fil] = str.split(" ");
			for (int col = 0; col < n_columnas; col++)
			{
				parking_init[fil][col] = new Car(parking_aux[fil][col]);
			}
		}
		buffer1.close();
		
		FileReader file2 = new FileReader(args[1]);
		BufferedReader buffer2 = new BufferedReader(file2);
		Car [][] parking_goal = new Car [n_filas][n_columnas];
		for (int fil = 0; fil < n_filas; fil++)
		{
			str = buffer2.readLine();
			parking_aux[fil] = str.split(" ");
			for (int col = 0; col < n_columnas; col++)
			{
				parking_goal[fil][col] = new Car(parking_aux[fil][col]);
			}
		}
		buffer2.close();
		
		System.out.println(Arrays.deepToString(parking_init));
		System.out.println(Arrays.deepToString(parking_goal));

		Node initNode = new Node(parking_init);
		initNode.setG(0);
		Node goalNode = new Node(parking_goal);
		goalNode.setH(0);

		ArrayList <Node> openSet = new ArrayList <Node>();
		openSet.add(initNode);
		ArrayList <Node> closedSet = new ArrayList <Node>();
		boolean success = false;
		while (!openSet.isEmpty() && !success)
		{
			Node current = openSet.remove(0);	// Remove first node from OPEN
			closedSet.add(current);				// Put it inside CLOSED
			if (current.sameParking(goalNode))		// If aux == goal, success, exit loop
			{
				success = true;
			}
			else	// If aux != goal
			{
				ArrayList <Node> expanded = new ArrayList <Node>();		// Expand node and get list of children
				
				expanded = expand(current);
				/* EXPANDIR Y SUMAR LASTCOST */
				
				for (int i = 0; i < expanded.size(); i++)	// For each children of current
				{
					if (containsNode(closedSet, expanded.get(i)) != -1)	// If child in closedSet
					{
						continue;	// Ignore the child
					}
					if (containsNode(openSet, expanded.get(i)) == -1)	// If child not in openSet
					{
						openSet.add(expanded.get(i));	// Add it to openSet
					}
					int tentative_g = current.getG() + expanded.get(i).getLastCost();	// Possibly better g value
					if (tentative_g >= expanded.get(i).getG())	// If not better, continue
					{
						continue;
					}
					expanded.get(i).setFather(current);		// If it's better, set the father to the new best path
					expanded.get(i).setG(tentative_g);		// Update G (f updates too)
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

	private static ArrayList<Node> expand(Node current)
	{
		Car [][] currentParking = current.getParking();
		ArrayList <Node> expanded = new ArrayList <Node>();
		ArrayList <Car> movableCars = new ArrayList <Car>();
		for (int fil = 0; fil < currentParking.length; fil++)
		{
			for (int col = 0; col < currentParking[fil].length; col++)
			{
				if (!currentParking[fil][col].getId().equals("__"))
				{
					if (currentParking[fil][col].isFirst())
					{
						for (int fil_ite = 0; fil_ite < currentParking.length; fil_ite++)
						{
							for (int col_ite = currentParking[fil].length - 1; currentParking[fil_ite][col_ite].getId().equals("__"); col_ite--)
							{
								Car [][] expParking = currentParking;
								expParking[fil_ite][col_ite].setId(expParking[fil][col].getId());
								expParking[fil][col].setId("__");
								Node exp = new Node (expParking, current);
								exp.updateCars();
								int tmp = expanded.containsNode(exp);
								if (tmp == -1)
								{
									expanded.add(current);
								}
								else
								{
									
									expanded.add(index, element);
								}
							}
						}
					}
				}
			}
		}
	}

	private static int containsNode(ArrayList <Node> set, Node node)
	{
		for (int pos = 0; pos < set.size(); pos++)
		{
			if (node.getParking().equals(set.get(pos).getParking()))
			{
				return pos;
			}
		}
		return -1;
	}
}