import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;

import Jama.Matrix;


public class Task2 {
	
	Task1 tsk = null;
	int j = 0;
	Matrix main = null;
	Matrix mu = null;
	Matrix coV = null;
	

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
		Matrix classed = ClassifierExemple(tsk.Z,1,2);
		classed.print(2, 8);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream("Project_Z/Classed.csv"));
			classed.print(pw, classed.getColumnDimension(), 8);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Validation vd = new Validation(tsk.Z);
	}
	
	public double EntrainerModele(Matrix z, int j, int row)
	{
		mu = GenerateMu(tsk.Z, j);
		return ProbZC(z,j,row) + Math.log(ProbC(z,j));
	}
	
	public Matrix ClassifierExemple(Matrix z, int class1, int class2)
	{
		coV = DivideToMatrix(z.transpose().times(z), z.transpose().times(z).getRowDimension());
		Matrix classified = new Matrix(z.getRowDimension(), z.getColumnDimension()+1);
		for(int row = 0; row < z.getRowDimension(); row++)
		{
			classified.set(row, 0, (EntrainerModele(z.getMatrix(row, row, 0, 1),class1, row) >= EntrainerModele(z.getMatrix(row, row, 0, 1),class2, row)) ? class1 : class2);
			classified.set(row, 1, z.get(row,0));
			classified.set(row, 2, z.get(row,1));
		}
		return classified;
	}
	
	// Calcules la probabilité de x étant donné Cj
	public double ProbZC(Matrix z, int j, int row)
	{
		double probability = 0;

		probability += Math.log(coV.det())/(-2);
		probability -= RemoveToMatrix(z, mu.get(0, j-1))
				.times(coV.inverse().times(RemoveToMatrix(z, mu.get(0, j-1))
						.transpose()))
						.get(0, 0)/(2);
		
		/*probability += 1/(Math.sqrt(coV.det())*2*Math.PI)
				* Math.pow(Math.E, 
						RemoveToMatrix(z, mu.get(0, j-1))
						.times(coV.inverse()
						.times(RemoveToMatrix(z, mu.get(0, j-1)).transpose()))
						.get(row, j-1)/(-2));
		*/

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
			probability = GetAmount(this.main, 1) / total;
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
			count += (matrix.get(row, 0) == 1) ? 1 : 0;
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
				a.set(i, j, (m.get(i, j) * x));
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
				a.set(i, j, (m.get(i, j) * x));
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
}
