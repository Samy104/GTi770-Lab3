import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
		sousTache2();
		
	}
	
	public void sousTache1(){
		//Matrice de covariance
		
		this.matriceDeCovariance1 = reduireDimension(class1);
		
		System.out.print("Matrice de Covariance Classe 1:" );
		matriceDeCovariance1.print(5,7);
		
		System.out.print("Vecteur Propre Classe 1:" );
		vecteurPropre(matriceDeCovariance1).print(5, 3);
		
		System.out.print("Diagonal Classe 1:" );
		diagonal(matriceDeCovariance1).print(5, 8);
		getSwappedDIAGMatrix(diagonal(matriceDeCovariance1)).print(5, 8);
		
		System.out.print("Vecteur Propre Transpose Classe 1:" );
		vecPTranspose(matriceDeCovariance1).print(5, 3);
		
		
		Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance1));
		
		calculatePrincipauxVec(swapped,matriceDeCovariance1);
		
		// Do the shit for task 1-5: Projeter dans le sous espace de K Z= Xmoyenne*Vk
		
		System.out.println("Matrice Z Projetée Classe 1");
		
		Z = getXbar().times(principauxVecteurs);
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream("Project_Z/ZProjetee_Classe1.csv"));
			Z.print(pw, Z.getColumnDimension(), 8);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Z.print(Z.getColumnDimension(), 8);
	}
	
	public void sousTache2(){
		//Matrice de covariance
		
			matriceDeCovariance2 = reduireDimension(class2);
			
			System.out.print("Matrice de Covariance Classe 2:" );
			matriceDeCovariance2.print(5,7);
			
			System.out.print("Vecteur Propre Classe 2:" );
			vecteurPropre(matriceDeCovariance2).print(5, 3);
			
			System.out.print("Diagonal Classe 2:" );
			diagonal(matriceDeCovariance2).print(5, 8);
			getSwappedDIAGMatrix(diagonal(matriceDeCovariance2)).print(5, 8);
			
			System.out.print("Vecteur Propre Transpose Classe 2:" );
			vecPTranspose(matriceDeCovariance2).print(5, 3);
			
			
			Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance2));
			
			calculatePrincipauxVec(swapped,matriceDeCovariance2);
			
			// Do the shit for task 1-5: Projeter dans le sous espace de K Z= Xmoyenne*Vk
			
			System.out.println("Matrice Z Projetée ");
			
			Z = getXbar().times(principauxVecteurs);
			
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream("Project_Z/ZProjetee_Classe2.csv"));
				Z.print(pw, Z.getColumnDimension(), 8);
				pw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
				double meanTest = (x.get(i, j+1) - average)/x.getRowDimension();
				this.xBar.set(i, j,meanTest);
			}
		}
		
		return getXbarTranspose().times(getXbar());
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
		System.out.println("Pourcentage de alpha: " + (num/denum));
		decision = (num/denum >= 0.9) ? true : false;
		
		return decision;
	}
	
}
