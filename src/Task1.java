import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

import Jama.Matrix;

public class Task1 {

	Matrix xMatrix = null;
	
	public Matrix xBar = null;
	public Matrix R = null;
	
	public Matrix matriceDeCovariance = null;
	
	Matrix principauxVecteurs = null;
	Matrix Z = null;

	

	public Task1 (Matrix main)
	{
		this.xMatrix = main;
	}
	
	public void ExecuteTask1()
	{
		//sousTache1();
		//sousTache2();
		
		generateR();
		
		this.matriceDeCovariance = reduireDimension(xMatrix);
		
		System.out.print("Matrice de Covariance:" );
		matriceDeCovariance.print(5,7);
		
		System.out.print("Vecteur Propre:" );
		vecteurPropre(matriceDeCovariance).print(5, 3);
		
		System.out.print("Diagonal:" );
		diagonal(matriceDeCovariance).print(5, 8);
		getSwappedDIAGMatrix(diagonal(matriceDeCovariance)).print(5, 8);
		
		System.out.print("Vecteur Propre Transpose:" );
		vecPTranspose(matriceDeCovariance).print(5, 3);
		
		
		Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance));
		
		calculatePrincipauxVec(swapped,matriceDeCovariance);
		
		// Do the shit for task 1-5: Projeter dans le sous espace de K Z= Xmoyenne*Vk
		
		System.out.println("Matrice Z Projete");
		
		Z = getXbar().times(principauxVecteurs);
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream("Project_Z/ZProjetee.csv"));
			Z.print(pw, Z.getColumnDimension(), 8);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Z = getSwapMatrix(Z);
		System.out.println("TESTING");
		Z.print(Z.getColumnDimension(), 8);
		
	}
	
	public void calculatePrincipauxVec(Matrix swap,Matrix matriceDeCov){
		int i = 0;
		
		while(i < swap.getRowDimension() && !getKPrincipauxVecteurs(i, swap))	{
			i++;			
		}
		i++;
		
		principauxVecteurs = vecteurPropre(matriceDeCov).getMatrix(0, vecteurPropre(matriceDeCov).getRowDimension()-1,
				vecteurPropre(matriceDeCov).getColumnDimension()-i, vecteurPropre(matriceDeCov).getColumnDimension()-1);
	
	System.out.println("Vecteurs propres choisi: ");
	principauxVecteurs.print(i, 8);
		
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
				double meanTest = (x.get(i, j+1) - average);
				this.xBar.set(i, j,meanTest);
			}
		}
		
		return (getXbarTranspose().times(getXbar())).times(1.d/x.getRowDimension());
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
	
	public void generateR()
	{
		R = new Matrix(xMatrix.getRowDimension(), 2);
		for(int row = 0; row < xMatrix.getRowDimension(); row++)
		{
			if(xMatrix.get(row, 0) == 1)
			{
				R.set(row, 0, 1);
				R.set(row, 1, 0);
			}
			else
			{
				R.set(row, 0, 0);
				R.set(row, 1, 1);
			}
		}
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
		System.out.println("Pourcentage de alpha: " + (num/denum));
		decision = (num/denum >= 0.9) ? true : false;
		
		return decision;
	}
	
	public Matrix getSwapMatrix(Matrix swapping){
		Matrix t = new Matrix(swapping.getRowDimension(),swapping.getColumnDimension());
		
		int indexJ = 0;
		
		for (int j = swapping.getColumnDimension()-1; j >= 0;j--){	
			for(int i = 0; i < swapping.getRowDimension();i++){
				t.set(i, indexJ,
						swapping.get(i,j));
			}
			indexJ++;
		}
		return t;
	}
	
}
