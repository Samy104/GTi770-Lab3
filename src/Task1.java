import Jama.Matrix;

public class Task1 {

	Matrix class1 = null;
	Matrix class2 = null;
	
	public Matrix xBar = null;
	
	Matrix V = null;
	Matrix D = null;
	Matrix VTranspose = null;
	
	public Task1(Matrix class1, Matrix class2){
		this.class1 = class1;
		this.class2 = class2;
	}
	
	public void ExecuteTask1()
	{
		Matrix matriceDeCovariance = reduireDimension(class1);
		
		//Matrice de covariance
		System.out.print("Matrice de Covariance:" );
		matriceDeCovariance.print(5,7);
		
		System.out.print("Vecteur Propre:" );
		vecteurPropre(matriceDeCovariance).print(5, 3);
		
		System.out.print("Diagonal:" );
		diagonal(matriceDeCovariance).print(5, 3);
		
		System.out.print("Vecteur Propre Transpose:" );
		vecPTranspose(matriceDeCovariance).print(5, 3);
		
		
		vecteurPropre(matriceDeCovariance).times(diagonal(matriceDeCovariance)).times(vecPTranspose(matriceDeCovariance)).print(5, 7);
	}
	
	public double mean(Matrix x, int column)
	{
		double data = 0;
		for(int i = 0; i < x.getRowDimension(); i++)
		{
			data += x.get(i, column);
		}
		
		return data/x.getRowDimension();
	}
	
	/**
	 * Matrice de covariance
	 * @return
	 */
	
	public Matrix reduireDimension(Matrix x){	
		
		this.xBar = new Matrix(x.getRowDimension(), x.getColumnDimension()-1);
		double average;
		for(int j = 0; j < 5;j++){
			average = mean(x,j+1);
			for(int i = 0; i < x.getRowDimension();i++){
				double meanTest = (x.get(i, j+1) - average)/x.getRowDimension();
				this.xBar.set(i, j,meanTest);
			}
		}
		
		return getXbarTranspose().times(getXbar());
	}
	
	public void calculerMatriceCovariance(){
		
	}
	
	public Matrix vecteurPropre(Matrix matriceDeCovariance){		
		return matriceDeCovariance.eig().getV();	
	}
	
	public Matrix diagonal(Matrix matriceDeCovariance){
		return matriceDeCovariance.eig().getD();
	}
	
	public Matrix vecPTranspose(Matrix matriceDeCovariance){
		return vecteurPropre(matriceDeCovariance).transpose();
	}
	
	public Matrix getXbar(){
		return this.xBar;
	}
	
	public Matrix getXbarTranspose(){
		return this.xBar.transpose();
	}
	
}
