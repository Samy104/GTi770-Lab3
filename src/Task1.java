import java.util.Arrays;
import java.util.Collections;

import Jama.Matrix;

public class Task1 {

	Matrix class1 = null;
	Matrix class2 = null;
	
	public Matrix xBar = null;
	
	public Matrix matriceDeCovariance1 = null;
	public Matrix matriceDeCovariance2 = null;	
	
	Matrix principauxVecteurs = null;
	Matrix Z = null;
	
	public Task1(Matrix class1, Matrix class2){
		this.class1 = class1;
		this.class2 = class2;
	}
	
	public void ExecuteTask1()
	{
		sousTache1();
		//sousTache2();
		
	}
	
	public void sousTache1(){
		//Matrice de covariance
		
		this.matriceDeCovariance1 = reduireDimension(class1);
		
		System.out.print("Matrice de Covariance:" );
		matriceDeCovariance1.print(5,7);
		
		System.out.print("Vecteur Propre:" );
		vecteurPropre(matriceDeCovariance1).print(5, 3);
		
		System.out.print("Diagonal:" );
		diagonal(matriceDeCovariance1).print(5, 8);
		getSwappedDIAGMatrix(diagonal(matriceDeCovariance1)).print(5, 8);
		
		System.out.print("Vecteur Propre Transpose:" );
		vecPTranspose(matriceDeCovariance1).print(5, 3);
		
		
		Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance1));
		
		calculatePrincipauxVec(swapped);
		
		// Do the shit for task 1-5: Projeter dans le sous espace de K Z= Xmoyenne*Vk
		
		System.out.println("Matrice Z Projetée ");
		
		Z = getXbar().times(principauxVecteurs);
		Z.print(Z.getColumnDimension(), 8);
	}
	
	public void sousTache2(){
		//Matrice de covariance
		
			this.matriceDeCovariance2 = reduireDimension(class2);
			System.out.print("Matrice de Covariance:" );
			matriceDeCovariance2.print(5,7);
			
			System.out.print("Vecteur Propre:" );
			vecteurPropre(matriceDeCovariance2).print(5, 3);
			
			System.out.print("Diagonal:" );
			diagonal(matriceDeCovariance2).print(5, 8);
			getSwappedDIAGMatrix(diagonal(matriceDeCovariance2)).print(5, 8);
			
			System.out.print("Vecteur Propre Transpose:" );
			vecPTranspose(matriceDeCovariance2).print(5, 3);
			
			
			Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance2));
			
			calculatePrincipauxVec(swapped);
			
			// Do the shit for task 1-5: Projeter dans le sous espace de K Z= Xmoyenne*Vk
			
			System.out.println("Matrice Z Projetée ");
			
			Z = getXbar().times(principauxVecteurs);
			Z.print(Z.getColumnDimension(), 8);
	}
	
	public void calculatePrincipauxVec(Matrix swap){
		int i = 0;
		
		while(i < swap.getRowDimension() && !getKPrincipauxVecteurs(i, swap))	{
			i++;			
		}
		i++;
		
		principauxVecteurs = vecteurPropre(matriceDeCovariance1).getMatrix(0, vecteurPropre(matriceDeCovariance1).getRowDimension()-1,
				vecteurPropre(matriceDeCovariance1).getColumnDimension()-i, vecteurPropre(matriceDeCovariance1).getColumnDimension()-1);
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
	
	public Matrix getSwappedDIAGMatrix(Matrix start)
	{
		Matrix exit = new Matrix(start.getRowDimension(),start.getColumnDimension());
		
		Double[] sort = new Double[start.getRowDimension()];
		
		for(int i = 0; i < start.getRowDimension(); i++)
		{
			sort[i] = start.get(i,i);
		}
		Arrays.sort(sort, Collections.reverseOrder());
		
		for(int i = 0; i < start.getRowDimension(); i++)
		{
			exit.set(i, i, sort[i]);
		}
		
		return exit;
	}
	
	public boolean getKPrincipauxVecteurs(int k, Matrix from)
	{
		boolean decision = false;
		
		double num = 0;
		double denum = 0;
		
		for(int i = 0; i < from.getRowDimension(); i++)
		{
			if(i <= k)
			{
				num += from.get(i, i);
			}
			denum += from.get(i, i);
		}
		decision = (num/denum >= 0.9) ? true : false;
		
		return decision;
	}
	
}
