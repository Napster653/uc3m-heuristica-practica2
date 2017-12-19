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

		Node initNode = new Node(parking_init);
		initNode.setG(0);
		initNode.updateCars();
		Node goalNode = new Node(parking_goal);
		goalNode.setH(0);
		goalNode.updateCars();

		ArrayList <Node> openSet = new ArrayList <Node>();
		openSet.add(initNode);
		ArrayList <Node> closedSet = new ArrayList <Node>();
		boolean success = false;
		
		int steps = 0;
		while (!openSet.isEmpty() && !success)
		{
			steps++;
			System.out.println("Step: " + steps);
			int lowest_f = openSet.get(0).getF();
			int lowest_f_position = 0;
			for (int index = 0; index < openSet.size(); index++)
			{
				if (openSet.get(index).getF() < lowest_f)
				{
					lowest_f_position = index;
				}
			}
			Node current = openSet.remove(lowest_f_position);	// Remove first node from OPEN
			closedSet.add(current);				// Put it inside CLOSED
			if (current.sameParking(goalNode))		// If aux == goal, success, exit loop
			{
				success = true;
				reconstructPath(current);
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
						System.out.println("Child in closedSet, "+containsNode(closedSet,expanded.get(i)));
						continue;	// Ignore the child
					}
					if (containsNode(openSet, expanded.get(i)) == -1)	// If child not in openSet
					{
						System.out.println("Child not in openSet");
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
		if (success)
		{
			System.out.println("Success");
			System.out.println(steps + " Steps");
		}
		else
		{
			System.out.println("Failure");
		}
		
		/* Open = {I}, Closed = {}, Success = false
		
		While Open != {} or !Success:
		{
			Open.first (N) -> Closed
			if N = goal then Success=true
			else expand(N), 
		*/
	}

	private static void reconstructPath(Node current)
	{
		while (current.getFather() != null)
		{
			for (int fil = 0; fil < current.getParking().length; fil++)
			{
				for (int col = 0; col < current.getParking()[fil].length; col++)
				{
					System.out.print(current.getParking()[fil][col].getId() + ((col == current.getParking()[fil].length - 1) ? "" : " "));
				}
				System.out.println();
			}
			System.out.println();
			current = current.getFather();
		}
		for (int fil = 0; fil < current.getParking().length; fil++)
		{
			for (int col = 0; col < current.getParking()[fil].length; col++)
			{
				System.out.print(current.getParking()[fil][col].getId() + ((col == current.getParking()[fil].length - 1) ? "" : " "));
			}
			System.out.println();
		}
		
	}

	private static ArrayList<Node> expand(Node current)
	{
		Car [][] currentParking = current.getParking();
		int n_filas = current.getParking().length;
		int n_columnas = current.getParking()[0].length;
		ArrayList <Node> expanded = new ArrayList <Node>();
		for (int fil = 0; fil < currentParking.length; fil++)
		{
			for (int col = 0; col < currentParking[fil].length; col++)
			{
				if (!currentParking[fil][col].getId().equals("__") && currentParking[fil][col].isLast()) // Para cada coche que sea el último (puede salir por detrás)
				{
					for (int fil_ite = 0; fil_ite < currentParking.length; fil_ite++)
					{
						if (currentParking[fil_ite][currentParking[fil_ite].length - 1].getId().equals("__")) // Filas en las que se puede entrar marcha atrás
						{
							int ind = getIndexFromFront(currentParking[fil_ite]);
							
							Car [][] expParking = new Car [n_filas][n_columnas];
							for (int i = 0; i < n_filas; i++)
							{
								for (int j = 0; j < n_columnas; j++)
								{
									Car c = currentParking[i][j];
									if (c != null)
									{
										expParking[i][j] = new Car (c);
									}
								}
							}
							String newValue = expParking[fil][col].getId();
							expParking[fil_ite][ind].setId(newValue);
							expParking[fil][col].setId("__");
							
							Node exp = new Node (expParking, current);
							exp.updateCars();
							exp.setLastCost(6); // 2 + 4
							int tmp = containsNode(expanded, exp);
							if (tmp == -1)
							{
								expanded.add(exp);
							}
							else
							{
								expanded.add(tmp, exp);
							}
						}
						if (currentParking[fil_ite][0].getId().equals("__")) // Filas en las que se puede entrar marcha alante
						{
							int ind = getIndexFromBack(currentParking[fil_ite]);
							
							Car [][] expParking = new Car [n_filas][n_columnas];
							for (int i = 0; i < n_filas; i++)
							{
								for (int j = 0; j < n_columnas; j++)
								{
									Car c = currentParking[i][j];
									if (c != null)
									{
										expParking[i][j] = new Car (c);
									}
								}
							}
							expParking[fil_ite][ind].setId(expParking[fil][col].getId());
							expParking[fil][col].setId("__");
							
							Node exp = new Node (expParking, current);
							exp.updateCars();
							exp.setLastCost(5); // 2 + 3
							int tmp = containsNode(expanded, exp);
							if (tmp == -1)
							{
								expanded.add(exp);
							}
							else
							{
								expanded.add(tmp, exp);
							}
						}
					}
				}
				if (!currentParking[fil][col].getId().equals("__") && currentParking[fil][col].isFirst()) // Para cada coche que sea el primero (puede salir por delante)
				{
					for (int fil_ite = 0; fil_ite < currentParking.length; fil_ite++)
					{
						if (currentParking[fil_ite][currentParking[fil_ite].length - 1].getId().equals("__")) // Filas en las que puede entrar marcha atrás
						{
							int ind = getIndexFromFront(currentParking[fil_ite]);
							
							Car [][] expParking = new Car [n_filas][n_columnas];
							for (int i = 0; i < n_filas; i++)
							{
								for (int j = 0; j < n_columnas; j++)
								{
									Car c = currentParking[i][j];
									if (c != null)
									{
										expParking[i][j] = new Car (c);
									}
								}
							}
							expParking[fil_ite][ind].setId(expParking[fil][col].getId());
							expParking[fil][col].setId("__");
							
							Node exp = new Node (expParking, current);
							exp.updateCars();
							exp.setLastCost(5); // 1 + 4
							int tmp = containsNode(expanded, exp);
							if (tmp == -1)
							{
								expanded.add(exp);
							}
							else
							{
								expanded.add(tmp, exp);
							}
						}
						if (currentParking[fil_ite][0].getId().equals("__")) // Filas en las que puede entrar marcha alante
						{
							int ind = getIndexFromBack(currentParking[fil_ite]);
							
							Car [][] expParking = new Car [n_filas][n_columnas];
							for (int i = 0; i < n_filas; i++)
							{
								for (int j = 0; j < n_columnas; j++)
								{
									Car c = currentParking[i][j];
									if (c != null)
									{
										expParking[i][j] = new Car (c);
									}
								}
							}
							expParking[fil_ite][ind].setId(expParking[fil][col].getId());
							expParking[fil][col].setId("__");
							
							Node exp = new Node (expParking, current);
							exp.updateCars();
							exp.setLastCost(4); // 1 + 3
							int tmp = containsNode(expanded, exp);
							if (tmp == -1)
							{
								expanded.add(exp);
							}
							else
							{
								expanded.add(tmp, exp);
							}
						}
					}
				}
			}
		}
		return expanded;
	}

	private static int getIndexFromFront(Car[] currentRow)
	{
		for (int ind = currentRow.length - 1; ind >= 0; ind--)
		{
			if (!currentRow[ind].getId().equals("__"))
			{
				return (ind+1);
			}
		}
		return -1;
	}

	private static int getIndexFromBack(Car[] currentRow)
	{
		for (int ind = 0; ind < currentRow.length; ind++)
		{
			if (!currentRow[ind].getId().equals("__"))
			{
				return (ind-1);
			}
		}
		return -1;
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