
/**
 * @author Christopher Lariviere
 * 		   Samy Lemcelli
 */

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

	
	/**
	 * Initialise la matrice xMatrix avec la matrice passée en paramètre
	 * 
	 * @param matriceOriginal
	 */
	
	public Task1 (Matrix main)
	{
		this.xMatrix = main;
	}
	
	/**
	 * Objectif: Exécuter la tâche 1, cette tâche suis les étapes de la présentation diapositive
	 * @return void
	 */
	
	public void ExecuteTask1()
	{		
		generateR();
		
		this.matriceDeCovariance = reduireDimension(xMatrix);
		
		System.out.print("Matrice de Covariance:" );
		matriceDeCovariance.print(5,7);
		
		System.out.print("Vecteur Propre:" );
		vecteurPropre(matriceDeCovariance).print(5, 3);
		
		System.out.print("Diagonal (Ordonnée):" );
		getSwappedDIAGMatrix(diagonal(matriceDeCovariance)).print(5, 8);
		
		System.out.print("Vecteur Propre Transpose:" );
		vecPTranspose(matriceDeCovariance).print(5, 3);
		
		
		
		Matrix swapped = getSwappedDIAGMatrix(diagonal(matriceDeCovariance));
		
		calculatePrincipauxVec(swapped,matriceDeCovariance);
		
		System.out.println("Matrice Z Projete");
		
		Z = getXbar().times(principauxVecteurs);
		
		//Ce code a été commenter, la chargée de laboratoire n'a pas besoin d'imprimer le Z projetee dans un fichier
		
		/*PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream("Project_Z/ZProjetee.csv"));
			Z.print(pw, Z.getColumnDimension(), 8);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		Z = getSwapMatrix(Z);
		Z.print(Z.getColumnDimension(), 8);
		
	}
	
	/**
	 * Cette fonction calcule les principaux a l'aide de la fonction getKPrincipauxVecteurs()
	 * 
	 * @param la matrice diagonale ordonnée en ordre décroissant
	 * @param la matrice de covariance
	 * 
	 * @return la matrice des K principaux vecteurs
	 */
	
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
	
	/**
	 * Calcule la moyenne de la colonne du matrix passée en paramètre
	 * 
	 * @param Matrice x
	 * @param le numéro de la colonne que nous voulons la moyenne
	 * @return la moyenne de la colonne
	 */
	
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
	 * Réduit la dimension de la matrice passée en paramètre
	 * Calcule X barre centrée, X barre, et x barre transposée
	 * 
	 * @return Matrix Matrice de dimension réduite
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
	
	/**
	 * Retourne la matrice de vecteur propre à l'aide des fonctions de JAMA Matrix project
	 * @param Matrix matriceX
	 * @return Matrix Matrice de vecteur propre
	 */
	
	public Matrix vecteurPropre(Matrix matriceDeCovariance){		
		return matriceDeCovariance.eig().getV();	
	}
	
	/**
	 * Retourne la matrice diagonale de la matrice passée en paramètre
	 * @param Matrix matriceX
	 * @return Matrix matrice diagonale
	 */
	
	public Matrix diagonal(Matrix matriceDeCovariance){
		return matriceDeCovariance.eig().getD();
	}
	
	/**
	 * Retourne la matrice des vecteurs propres transposées
	 * @param Matrix matriceX
	 * @return Matrix matrice de vecteurs propres transposées
	 */
	
	public Matrix vecPTranspose(Matrix matriceDeCovariance){
		return vecteurPropre(matriceDeCovariance).transpose();
	}
	
	/**
	 * Retourne la matrice X Barre
	 * @return Matrix X Barre
	 */
	
	public Matrix getXbar(){
		return this.xBar;
	}
	
	/**
	 * Retourne la matrice X Barre transposée
	 * @return matrice X Barre transposée
	 */
	
	public Matrix getXbarTranspose(){
		return this.xBar.transpose();
	}
	
	/**
	 * Génère la matrice R, qui contient les classes originales de la matrice originale
	 */
	
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
	
	/**
	 * Ordonne la matrice passée en paramètre en ordre décroissant
	 * @param Matrix matrice à ordonnée
	 * @return Matrix matrice ordonoée en ordre décroissant
	 */
	
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
	
	/**
	 * Évalue si les vecteurs propres représentent un minimum de alpha >= 90%
	 * Cela signifie que les K colonnes des vecteurs propres représentent plus que 90% des données
	 * @param k
	 * @param Matrix 
	 * @return vrai ou faux, si la sum des k colonnes représentent plus que 90% des données
	 */
	
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
	
	/**
	 * Retourne la matrice échanger en colonnes.
	 * Ex: colonne 5 serait à la position 1, colonne 4 serait à colonne 2... ainsi de suite.
	 * 
	 * @param Matrix matrice a échanger ces colonnes de positions
	 * @return Matrix échanger
	 */
	
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
