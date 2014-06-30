import Jama.Matrix;


public class Task2 {
	
	Matrix zed;
	Task1 tsk = null;
	int j = 0;
	Matrix class1 = null;
	Matrix class2 = null;
	
	public Task2(Matrix class1, Matrix class2)
	{
		this.class1 = class1;
		this.class2 = class2;
		tsk = new Task1(class1, class2);
	}
	
	public void ExecuteTask2()
	{
		// Get matrix z from task one then train the model
		tsk.sousTache1();
		
		System.out.println("D�but de la tache 2");
		this.zed = tsk.Z;
		double ent = EntrainerModele(zed, 1);
		
		System.out.println("Le model entrain� nous retourne");
		System.out.println(ent);
	}
	
	public double EntrainerModele(Matrix z, int j)
	{
		
		//return AddToMatrix(GetLogTen((ProbZC(z,j))) , Math.log10(ProbC(z,j)));
		return Math.log10(ProbZC(z,j)) + Math.log10(ProbC(z,j));
	}
	
	public int ClassifierExemple(Matrix z)
	{
		return 0;
	}
	
	// Calcules la probabilité de x étant donné Cj
	public double ProbZC(Matrix z, int j)
	{
		Matrix coVar = tsk.matriceDeCovariance1;
		Matrix x = this.class1;
		Matrix mu = tsk.getXbar();
		double total = 0;
		
		for(int row = 0; row < coVar.getRowDimension(); row++)
		{
			for(int col = 0; col < coVar.getColumnDimension(); col++)
			{
				total += (1/(Math.sqrt(2*Math.PI)) * Math.pow(Math.E, (-Math.pow((x.get(row,col)-mu.get(row,col)),2)/(2*Math.pow(coVar.get(row,col),2)))));
			}
		}
		
		//z.getMatrix(arg0, arg1, arg2, arg3)
		// return (1/(Math.sqrt(2*Math.PI)) * Math.pow(Math.E, (-Math.pow((x-mu),2)/(2*Math.pow(coVar,2)))));
		/*return (coVar.times((Math.sqrt(2*Math.PI))).inverse()
				.times(GetEPowered(( GetMatrixPowered(coVar,2).times(2) ).inverse()
						.times((-Math.pow((x-mu),2))
				))));*/
		return total;
	}
	
	// Calcules la probabilité de Cj
	public double ProbC(Matrix z, int j)
	{
		return 0;
	}
	
	/*
	 * Functions for Matrices
	 * Will have to create a Extended Matrix class or add it to the Main or create a functions class.
	 */
	
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
}
