import Jama.Matrix;

public class Task1 {

	Matrix class1 = null;
	Matrix class2 = null;
	
	Matrix xBar = null;
	
	Matrix V = null;
	Matrix D = null;
	Matrix VTranspose = null;
	
	public Task1(Matrix class1, Matrix class2){
		this.class1 = class1;
		this.class2 = class2;
	}
	
	public void ExecuteTask1()
	{
		reduireDimension();
		
	}
	
	private double mean(Matrix x, int column)
	{
		double data = 0;
		for(int i = 0; i < x.getRowDimension(); i++)
		{
			data += x.get(column, i);
		}
		
		return data/x.getRowDimension();
	}
	
	/**
	 * Matrice de covariance
	 * @return
	 */
	
	private Matrix reduireDimension(){
		Matrix task1Mat = new Matrix(5, 1);
		task1Mat.set(0, 0, mean(class1,1));
		task1Mat.set(1, 0, mean(class1,2));
		task1Mat.set(2, 0, mean(class1,3));
		task1Mat.set(3, 0, mean(class1,4));
		task1Mat.set(4, 0, mean(class1,5));
		
		xBar = class1.minus(task1Mat);
		
		return (getXbarTranspose().times(getXbar())).times(1/class1.getRowDimension());
	}
	
	public void vecteurPropre(){		
		this.V = reduireDimension().eig().getV();
		this.D = reduireDimension().eig().getD();
		this.VTranspose = V.transpose();
	}
	
	public Matrix getXbar(){
		return xBar;
	}
	public Matrix getXbarTranspose(){
		return xBar.transpose();
	}
	
}
