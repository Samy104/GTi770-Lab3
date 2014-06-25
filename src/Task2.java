import Jama.Matrix;


public class Task2 {
	
	Matrix z;
	int j = 0;
	
	public double entrainerModele(Matrix z, int j)
	{
		
		return Math.log10(probZC(z,j)) + Math.log10(probC(z,j));
	}
	
	public int classifierExemple(Matrix z)
	{
		return 0;
	}
	
	// Calcules la probabilité de x étant donné Cj
	public double probZC(Matrix z, int j)
	{
		double coVar = 0;
		double x=0;
		double mu = 0;
		//z.getMatrix(arg0, arg1, arg2, arg3)
		return (1/(Math.sqrt(2*Math.PI)*coVar) * Math.pow(Math.E, (-Math.pow((x-mu),2)/(2*Math.pow(coVar,2)))));
	}
	
	// Calcules la probabilité de Cj
	public double probC(Matrix z, int j)
	{
		return 0;
	}
}
