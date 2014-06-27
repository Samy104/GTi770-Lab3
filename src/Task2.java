import Jama.Matrix;


public class Task2 {
	
	Matrix z;
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
	
	public Matrix entrainerModele(Matrix z, int j)
	{
		
		return addToMatrix(getLogTen((probZC(z,j))) , Math.log10(probC(z,j)));
	}
	
	public int classifierExemple(Matrix z)
	{
		return 0;
	}
	
	// Calcules la probabilité de x étant donné Cj
	public Matrix probZC(Matrix z, int j)
	{
		Matrix coVar = tsk.reduireDimension(this.class1);
		double x=0;
		double mu = 0;
		//z.getMatrix(arg0, arg1, arg2, arg3)
		// return (1/(Math.sqrt(2*Math.PI)) * Math.pow(Math.E, (-Math.pow((x-mu),2)/(2*Math.pow(coVar,2)))));
		return (coVar.times((Math.sqrt(2*Math.PI))).inverse()
				.times(getEPowered(( getMatrixPowered(coVar,2).times(2) ).inverse()
						.times((-Math.pow((x-mu),2))
				))));
	}
	
	// Calcules la probabilité de Cj
	public double probC(Matrix z, int j)
	{
		return 0;
	}
	
	/*
	 * Functions for Matrices
	 * Will have to create a Extended Matrix class or add it to the Main or create a functions class.
	 */
	
	public Matrix getMatrixPowered(Matrix m, int power)
	{
		for(int i = 0; i <= power; i++)
		{
			m.times(m);
		}
		
		return m;
	}
	
	public Matrix getEPowered(Matrix m)
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
	public Matrix getLogTen(Matrix m)
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
	
	public Matrix addToMatrix(Matrix m, double d)
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
	
	public Matrix addToMatrix(Matrix m, int x)
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
