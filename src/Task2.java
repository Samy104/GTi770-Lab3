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
		
		System.out.println("DÈbut de la tache 2");
		double ent = EntrainerModele(tsk.Z, 1);
		
		System.out.println("Le model entrainÈ 1 nous retourne");
		System.out.println(ent);
		
		System.out.println(ProbC(tsk.Z,1));
		/*
		double ent2 = EntrainerModele(tsk.Z2, 2);
		
		System.out.println("Le model entrainÈ 2 nous retourne");
		System.out.println(ent2);
		
		System.out.println(ProbC(tsk.Z2,2));*/
	}
	
	public double EntrainerModele(Matrix z, int j)
	{
		coV = DivideToMatrix(z.transpose().times(z), z.transpose().times(z).getRowDimension());
		mu = GenerateMu(z);
		coV.print(5, 8);
		mu.print(5, 8);
		return Math.log(ProbZC(z,j)) + Math.log(ProbC(z,j));
		//return ProbZC(z,j);
	}
	
	public int ClassifierExemple(Matrix z, int class1, int class2)
	{
		return (EntrainerModele(z,class1) >= EntrainerModele(z,class2)) ? class1 : class2;
	}
	
	// Calcules la probabilit√© de x √©tant donn√© Cj
	public double ProbZC(Matrix z, int j)
	{
		
		double total = 0;
		int count = z.getColumnDimension() * z.getRowDimension();
		
		for(int col = 0; col < z.getColumnDimension(); col++)
		{
			for(int row = 0; row < z.getRowDimension(); row++)
			{		
				/*total += Math.log(coVar.det())/(-2);
				total -= RemoveToMatrix(z, mu).times(coVar.inverse().times(RemoveToMatrix(z, mu).transpose())).get(row, col)/(2);*/
				
				total += 1/(Math.sqrt(coV.det())*2*Math.PI)
						* Math.pow(Math.E, 
								RemoveToMatrix(z, mu.get(0, col))
								.times(coV.inverse()
								.times(RemoveToMatrix(z, mu.get(0, col)).transpose()))
								.get(row, col)/(-2));
				
			}
		}
		
		return total/z.getRowDimension();
	}
	
	// Calcules la probabilit√© de Cj
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
	
	private Matrix GenerateMu(Matrix z) {
		Matrix moyenne = new Matrix(1,z.getColumnDimension());
		double dimension = z.getRowDimension();
		
		for(int col = 0; col < z.getColumnDimension(); col++)
		{
			double total = 0;
			
			for(int row = 0; row < z.getRowDimension(); row++)
			{
				total += z.get(row, col);
			}
			moyenne.set(0, col, total/dimension);
			System.out.println(total + "  " + col);
		}
		
		return moyenne;
	}
}
