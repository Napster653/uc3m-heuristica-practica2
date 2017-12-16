package heuristica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.jacop.core.BooleanVar;
import org.jacop.core.Store;
import org.jacop.jasat.utils.structures.IntVec;
import org.jacop.satwrapper.SatWrapper;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;
public class parte1
{
	public static void main(String[] args) throws IOException
	{
		int n_filas, n_columnas;

		// Se lee el archivo con los datos del parking
		FileReader file = new FileReader(args[0]);
		BufferedReader buffer = new BufferedReader(file);

		String cadena;
		String []filas_columnas = new String[2];
		if ((cadena = buffer.readLine()) != null)
		{
			filas_columnas = cadena.split(" ");
		}

		n_filas = Integer.parseInt(filas_columnas[0]);
		n_columnas = Integer.parseInt(filas_columnas[1]);
		System.out.println("Filas: " + n_filas);
		System.out.println("Columnas: " + n_columnas);
		// Se rellena un array con los datos leídos
		String[][] parking = new String[n_filas][n_columnas];
		for (int i = 0; i < n_filas; i++)
		{
			cadena = buffer.readLine();
			parking[i] = cadena.split(" ");
		}

		// Se imprime el array generado para comprobar que los datos son correctos
		System.out.println(Arrays.deepToString(parking));
		buffer.close();

		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper();
		store.impose(satWrapper);

		// Se declaran las variables
		BooleanVar saleDelante [][] = new BooleanVar[n_filas][n_columnas];
		BooleanVar saleDetras [][] = new BooleanVar[n_filas][n_columnas];
		BooleanVar letraInferiorDelante [][] = new BooleanVar[n_filas][n_columnas];
		BooleanVar numerInferiorDelante [][] = new BooleanVar[n_filas][n_columnas];
		BooleanVar letraInferiorDetras [][] = new BooleanVar[n_filas][n_columnas];
		BooleanVar numerInferiorDetras [][] = new BooleanVar[n_filas][n_columnas];

		// Se inicializan las variables necesarias
		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 1; col < n_columnas - 1; col++)
			{
				if (!parking[fil][col].equals("__") && !parking[fil][col-1].equals("__") && !parking[fil][col+1].equals("__"))
				{
					saleDelante[fil][col] = new BooleanVar(store, "\nsaleDelante");
					saleDetras[fil][col] = new BooleanVar(store, "\nsaleDetras");
					letraInferiorDelante[fil][col] = new BooleanVar(store, "\nletraInferiorDelante");
					numerInferiorDelante[fil][col] = new BooleanVar(store, "\nnumerInferiorDelante");
					letraInferiorDetras[fil][col] = new BooleanVar(store, "\nletraInferiorDetras");
					numerInferiorDetras[fil][col] = new BooleanVar(store, "\nnumerInferiorDetras");
				}
			}
		}

		int totalVariables = 0;

		// Se registran todas las variables y se cuentan para saber cuántas tienen valores
		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 1; col < n_columnas - 1; col++)
			{
				if (!parking[fil][col].equals("__") && !parking[fil][col-1].equals("__") && !parking[fil][col+1].equals("__"))
				{
					satWrapper.register(saleDelante[fil][col]);
					satWrapper.register(saleDetras[fil][col]);
					satWrapper.register(letraInferiorDelante[fil][col]);
					satWrapper.register(numerInferiorDelante[fil][col]);
					satWrapper.register(letraInferiorDetras[fil][col]);
					satWrapper.register(numerInferiorDetras[fil][col]);
					totalVariables=totalVariables+6;
				}
			}
		}

		// Se crea un array en el que se almacenarán las variables sin dejar huecos
		BooleanVar allVariables[] = new BooleanVar[totalVariables];

		int allVarIndex = 0;

		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 1; col < n_columnas - 1; col++)
			{
				if (!parking[fil][col].equals("__") && !parking[fil][col-1].equals("__") && !parking[fil][col+1].equals("__"))
				{
					allVariables[allVarIndex] = saleDelante[fil][col];
					allVariables[allVarIndex + 1] = saleDetras[fil][col];
					allVariables[allVarIndex + 2] = letraInferiorDelante[fil][col];
					allVariables[allVarIndex + 3] = letraInferiorDetras[fil][col];
					allVariables[allVarIndex + 4] = numerInferiorDelante[fil][col];
					allVariables[allVarIndex + 5] = numerInferiorDetras[fil][col];
					allVarIndex += 6;
				}
			}
		}

		// Se crean los arrays de literales
		int saleDelanteLiteral[][] = new int[n_filas][n_columnas];
		int saleDetrasLiteral[][] = new int[n_filas][n_columnas];
		int letraInferiorDelanteLiteral[][] = new int[n_filas][n_columnas];
		int letraInferiorDetrasLiteral[][] = new int[n_filas][n_columnas];
		int numerInferiorDelanteLiteral[][] = new int[n_filas][n_columnas];
		int numerInferiorDetrasLiteral[][] = new int[n_filas][n_columnas];

		// Se inicializan los literales
		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 1; col < n_columnas - 1; col++)
			{
				if (!parking[fil][col].equals("__") && !parking[fil][col-1].equals("__") && !parking[fil][col+1].equals("__"))
				{
					letraInferiorDelanteLiteral[fil][col] = satWrapper.cpVarToBoolVar(letraInferiorDelante[fil][col], 1, true);
					letraInferiorDetrasLiteral[fil][col] = satWrapper.cpVarToBoolVar(letraInferiorDetras[fil][col], 1, true);
					numerInferiorDelanteLiteral[fil][col] = satWrapper.cpVarToBoolVar(numerInferiorDelante[fil][col], 1, true);
					numerInferiorDetrasLiteral[fil][col] = satWrapper.cpVarToBoolVar(numerInferiorDetras[fil][col], 1, true);
					saleDelanteLiteral[fil][col] = satWrapper.cpVarToBoolVar(saleDelante[fil][col], 1, true);
					saleDetrasLiteral[fil][col] = satWrapper.cpVarToBoolVar(saleDetras[fil][col], 1, true);
				}
			}
		}

		// Se añaden las cláusulas
		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 1; col < n_columnas - 1; col++)
			{
				if (!parking[fil][col].equals("__") && !parking[fil][col-1].equals("__") && !parking[fil][col+1].equals("__"))
				{
					// saleDel ->letraInfDel OR numerInfDel pasado a FNC, lo mismo para Detrás
					addClause(satWrapper, -saleDelanteLiteral[fil][col], letraInferiorDelanteLiteral[fil][col], numerInferiorDelanteLiteral[fil][col]);		/* (-saleDel v letraInfDel v numerInfDel) */
					addClause(satWrapper, -saleDetrasLiteral[fil][col], letraInferiorDetrasLiteral[fil][col], numerInferiorDetrasLiteral[fil][col]);		/* (-saleDet v letraInfDet v numerInfDet) */
					// Los coches bloqueados deben poder salir por delante OR por detrás
					addClause(satWrapper, saleDelanteLiteral[fil][col], saleDetrasLiteral[fil][col]);														/* (saleDel v saleDet) */

					//letraInferiorDelanteLiteral
					if(parking[fil][col+1].charAt(0) < parking[fil][col].charAt(0)) // Si el coche de delante es de letra menor que el actual, letraInferiorDelante es true
						addClause(satWrapper, letraInferiorDelanteLiteral[fil][col]);
					else
						addClause(satWrapper, -letraInferiorDelanteLiteral[fil][col]);
					
					//letraInferiorDetrasLiteral
					if(parking[fil][col-1].charAt(0) < parking[fil][col].charAt(0))
						addClause(satWrapper, letraInferiorDetrasLiteral[fil][col]);
					else
						addClause(satWrapper, -letraInferiorDetrasLiteral[fil][col]);
					
					//numerInferiorDelanteLiteral
					if((parking[fil][col+1].charAt(0) == parking[fil][col].charAt(0)) && (parking[fil][col+1].charAt(1) < parking[fil][col].charAt(1))) // Si el coche de delante es de la misma letra y número menor que el actual, numerInfDelante es true
						addClause(satWrapper, numerInferiorDelanteLiteral[fil][col]);
					else
						addClause(satWrapper, -numerInferiorDelanteLiteral[fil][col]);
					
					//numerInferiorDetrasLiteral
					if((parking[fil][col-1].charAt(0) == parking[fil][col].charAt(0)) && (parking[fil][col-1].charAt(1) < parking[fil][col].charAt(1)))
						addClause(satWrapper, numerInferiorDetrasLiteral[fil][col]);
					else
						addClause(satWrapper, -numerInferiorDetrasLiteral[fil][col]);
				}
			}
		}

		// Solver
		Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
		SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(allVariables, new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		Boolean result = search.labeling(store, select);

		if(result)
			System.out.println("SAT");
		else
			System.out.println("UNSAT");

		char[][] resultado = new char[n_filas][n_columnas];

		for (int fil = 0; fil < n_filas; fil++)
		{
			for (int col = 0; col < n_columnas; col++)
			{
				if (parking[fil][col].equals("__"))
				{
					resultado[fil][col] = '-';
				}
				else if (col == 0)
				{
					resultado[fil][col] = '<';
				}
				else if (col == n_columnas - 1)
				{
					resultado[fil][col] = '>';
				}
				else if (saleDelante[fil][col] == null)
				{
					if (parking[fil][col-1].equals("__"))
					{
						resultado[fil][col] = '<';
					}
					else
					{
						resultado[fil][col] = '>';
					}
				}
				else if (saleDelante[fil][col].value() == 1)
				{
					resultado[fil][col] = '>';
				}
				else if (saleDetras[fil][col].value() == 1)
				{
					resultado[fil][col] = '<';
				}
				else
				{
					resultado[fil][col] = 'X';
				}
			}
		}

		System.out.println(Arrays.deepToString(resultado));

	}

	public static void addClause(SatWrapper satWrapper, int literal1)
	{
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		satWrapper.addModelClause(clause.toArray());
	}

	public static void addClause(SatWrapper satWrapper, int literal1, int literal2)
	{
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}

	public static void addClause(SatWrapper satWrapper, int literal1, int literal2, int literal3)
	{
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		clause.add(literal3);
		satWrapper.addModelClause(clause.toArray());
	}
}
