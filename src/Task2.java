import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import Jama.Matrix;


public class Task2 {
	
	Task1 tsk = null;
	int j = 0;
	Matrix main = null;
	Matrix mu1 = null;
	Matrix coV1 = null;
	Matrix mu2 = null;
	Matrix coV2 = null;
	

	public Task2(Matrix x)
	{
		this.main = x;
		tsk = new Task1(main);
	}
	
	public void ExecuteTask2()
	{
		// Get matrix z from task one then train the model
		tsk.ExecuteTask1();
		
		System.out.println("D�but de la tache 2");
		
		
		Validation(tsk.Z);
	}
	
	public void EntrainerModele(Matrix z, int j)
	{
		if(j == 1)
		{
			mu1 = GenerateMu(tsk.Z, j);
			coV1 = GenerateCoVar(tsk.Z, j); 
		}
		else
		{
			mu2 = GenerateMu(tsk.Z, j);
			coV2 = GenerateCoVar(tsk.Z, j); 
		}
			
	}

		
		public void Validation(Matrix z)
		{
			Matrix Zed = /*returnRandomizedMatrix(*/z/*)*/;
			
			double total = 0;
			
			ArrayList<Matrix> arrayMatrix = new ArrayList<Matrix>(10);
			double moyenneErreur = 0;
			double totaux[] = new double[10];
			
			
			// Populate array list
			for(int i=0; i<10; i++)
			{
				Matrix zedTot = new Matrix(25,2);
				zedTot.setMatrix(0, 14, 0, 1, Zed.getMatrix(15*i, (15*i+14), 0, 1));
				zedTot.setMatrix(15, 24, 0, 1, Zed.getMatrix((10*i+150), (10*i+159), 0, 1));
				arrayMatrix.add(zedTot);
			}
			
			for(int k = 0; k < 10; k++)
			{
				System.out.println("Itération # "+k);
				EntrainerModele(aggregateExceptOne(arrayMatrix,k), 1);
				EntrainerModele(aggregateExceptOne(arrayMatrix,k), 2);
				
				int nbErreurs = 0;
				for(int row = 0; row < arrayMatrix.get(k).getRowDimension(); row++)
				{
							
					nbErreurs += (ClassifierExemple(arrayMatrix.get(k)) == 
								((row < 15) ? 1 : 2)) 
								? 0 : 1;

				}
				
				totaux[k] = nbErreurs;
				moyenneErreur += nbErreurs;
				System.out.println("Erreurs " + nbErreurs);
			}
			moyenneErreur = moyenneErreur/10;
			System.out.println("Moyenne d'erreurs " + moyenneErreur);
			
			double sumVar = 0;
			for(int t = 0; t < 10; t++)
			{
				sumVar += Math.pow(totaux[t]/25-(moyenneErreur/25),2);
			}
			sumVar = sumVar/10;
			
			System.out.println("\nL'erreur moyen " + moyenneErreur);
			System.out.println("Taux d'erreur moyen " + moyenneErreur/25);
			System.out.println("Variance de taux d'erreur moyen " + sumVar);
			
		}

		


	public double ClassifierExemple(Matrix z)
	{
		//return classified;
		return (ProbZC(z,1) + Math.log(ProbC(z,1)) > ProbZC(z,2) + Math.log(ProbC(z,2))) ? 1 : 2;
	}
	
	// Calcules la probabilité de x étant donné Cj
	public double ProbZC(Matrix z, int j)
	{
		double probability = 0;
		if(j==1)
		{
			probability += Math.log(coV1.det())/(-2)-RemoveToMatrix(z, mu1.get(0, j-1))
					.times(coV1.inverse().times(RemoveToMatrix(z, mu1.get(0, j-1))
							.transpose()))
							.get(0, 0)/(2);
		}
		else
		{
			probability += Math.log(coV2.det())/(-2)-RemoveToMatrix(z, mu2.get(0, j-1))
					.times(coV2.inverse().times(RemoveToMatrix(z, mu2.get(0, j-1))
							.transpose()))
							.get(0, 0)/(2);
		}
		

		return probability;
	}

	// Calcules la probabilité de Cj
	public double ProbC(Matrix z, int j)
	{
		double probability = 1;
		double total = this.main.getRowDimension();
		if(j == 1)
		{
			probability = GetAmount(this.main, 1) / total;
		}
		else
		{
			probability = GetAmount(this.main, 2) / total;
		}
		
		return probability;
	}
	
	/*
	 * Functions for Matrices
	 * Will have to create a Extended Matrix class or add it to the Main or create a functions class.
	 */
	
	private int GetAmount(Matrix matrix, int i) {
		int count = 0;
		for(int row = 0; row < matrix.getRowDimension(); row++)
		{
			count += (matrix.get(row, 0) == i) ? 1 : 0;
		}
		
		return count;
	}

	public Matrix GetMatrixPowered(Matrix m, int power)
	{
		for(int i = 0; i <= power; i++)
		{
			m.times(m);
		}
		
		return m;
	}
	
	public Matrix GetEPowered(Matrix m)
	{
		Matrix e = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < e.getRowDimension();i++)
		{
			for(int j = 0; j < e.getColumnDimension(); j++)
			{
				e.set(i, j, Math.pow(Math.E,m.get(i, j)));
			}
		}
		
		return e;
	}
	public Matrix GetLogTen(Matrix m)
	{
		Matrix l = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < l.getRowDimension();i++)
		{
			for(int j = 0; j < l.getColumnDimension(); j++)
			{
				l.set(i, j, Math.log10(m.get(i, j)));
			}
		}
		
		return l;
	}
	
	public Matrix AddToMatrix(Matrix m, double d)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) + d));
			}
		}
		
		return a;
	}
	
	public Matrix AddToMatrix(Matrix m, int x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) + x));
			}
		}
		
		return a;
	}
	
	public Matrix RemoveToMatrix(Matrix m, int x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) - x));
			}
		}
		
		return a;
	}
	
	public Matrix RemoveToMatrix(Matrix m, double x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) - x));
			}
		}
		
		return a;
	}
	
	public Matrix DivideToMatrix(Matrix m, int x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) / x));
			}
		}
		
		return a;
	}
	
	public Matrix DivideToMatrix(Matrix m, double x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) / x));
			}
		}
		
		return a;
	}
	
	private Matrix GenerateMu(Matrix z, int j) {
		Matrix moyenne = new Matrix(1,z.getColumnDimension());
		double dimension = 0;
		
		for(int col = 0; col < z.getColumnDimension(); col++)
		{
			double total = 0;
			
			for(int row = 0; row < z.getRowDimension(); row++)
			{
				if(GetClass(row,j))
				{
					total += z.get(row, col);
					dimension++;
				}
			}
			moyenne.set(0, col, total/dimension);
		}
		
		return moyenne;
	}
	
	private Matrix GenerateCoVar(Matrix z, int j) {
		
		ArrayList<Matrix> coVarList = new ArrayList<Matrix>();
		for(int i = 0; i < z.getRowDimension(); i++)
		{
			if(GetClass(i,j))
			{
				Matrix mat = new Matrix(1,2);
				mat.set(0,0,z.get(i, 0));
				mat.set(0,1,z.get(i, 1));
				coVarList.add(mat);
			}
			
		}
		
		Matrix zClass = new Matrix(coVarList.size(),2);
		int[] col = {0,1};
		for(int i = 0; i < zClass.getRowDimension(); i++)
		{
			zClass.setMatrix(i,i,col,coVarList.get(i));
		}	
		return DivideToMatrix(zClass.transpose().times(zClass), zClass.getRowDimension());
	}
	
	private boolean GetClass(int row, int j) {
		
		if(tsk.R.get(row, j-1) == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private Matrix ClassedMatrix(Matrix main, int chosen, int row)
	{
		Matrix chosenM = new Matrix(main.getRowDimension(), main.getColumnDimension());
		
			if(tsk.R.get(row, chosen-1) == 1)
			{
				chosenM.set(row, 0, main.get(row, 0));
				chosenM.set(row, 1, main.get(row, 1));
			}
			else
			{
				chosenM.set(row, 0, 0);
				chosenM.set(row, 1, 0);
			}
		
		return chosenM;
	}
	/**
	 * Retourne une matrice aléatoirement choisi
	 * @author Samy Lemcelli
	 * 
	 * @param Matrice m
	 * @return Matrice aléatoire
	 */
	
	public Matrix returnRandomizedMatrix(Matrix m) {
		
		Matrix randMatrix = new Matrix(m.getRowDimension(), m.getColumnDimension());
		ArrayList<Integer> remainingElements = new ArrayList<Integer>(m.getRowDimension());
		
		for(int i = 0; i < m.getRowDimension(); i++)
		{
			remainingElements.add(i);
		}
		int finalPosition = 0;
		while(!remainingElements.isEmpty())
		{
			int position = new Random().nextInt(remainingElements.size());
			int random = remainingElements.get(position);
			randMatrix.set(finalPosition,0,m.get(random, 0));
			randMatrix.set(finalPosition,1,m.get(random, 1));
			remainingElements.remove(position);
			finalPosition++;
		}
		return randMatrix;
	}
	
	/**
	 * La fonction prends le ArrayList de Matrices et choisis les 225 rangées qui n'appartiennent pas à l'indice.
	 * 
	 * @param ArrayList<Matrix> agg
	 * @param int thene
	 * @return Matrice aggregation
	 */
	public Matrix aggregateExceptOne(ArrayList<Matrix> agg, int theone)
	{
		Matrix aggregation = new Matrix(225,2);
		int currentPos = 0;
		int[] col = {0,1};
		
		for(int i = 1; i < 10; i++)
		{
			if(theone != i)
			{
				aggregation.setMatrix(currentPos,(currentPos+24), col, agg.get(i));
				currentPos += 25;
			}
			
		}
		
		return aggregation;
	}
	
	public void printFile(String input, String poly){
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("data"+poly+".txt"), "utf-8"));
		    
		    writer.append(input);
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
}
