/**
 * @author Samy Lemcelli,
 * 		   Christopher Lariviere
 */

import java.util.ArrayList;
import java.util.Random;
import Jama.Matrix;


public class Task2 {
	
	Task1 tsk = null;
	int j = 0;
	Matrix main = null;
	Matrix mu1 = null;
	Matrix coV1 = null;
	Matrix mu2 = null;
	Matrix coV2 = null;
	
	/**
	 * Prend la matrice originale et l'insère dans tache 1 et 2
	 * @param Matrix x, matrice à insérer dans la tâche 1 et 2
	 */
	public Task2(Matrix x)
	{
		this.main = x;
		tsk = new Task1(main);
	}
	
	public void ExecuteTask2()
	{
		tsk.ExecuteTask1();
		
		System.out.println("Début de la tâche 2 ");
		
		
		Validation(tsk.Z);
	}
	
	/**
	 * Cette fonction utilise la matrice Z de la tâche 1 et entrain le mu et la covariance pour chaque classe (1 et 2)
	 * @param Matrix z, Matrx ZProjetee
	 * @param int le numéro de la classe à entraîner.
	 */
	
	public void EntrainerModele(Matrix z, int j)
	{
		if(j == 1)
		{
			mu1 = GenerateMu(tsk.Z, j);
			coV1 = GenerateCoVar(tsk.Z, j); 
		}
		else
		{
			mu2 = GenerateMu(tsk.Z, j);
			coV2 = GenerateCoVar(tsk.Z, j); 
		}
			
	}

	/**
	 * Effectue la validation-croisée K-fold sur la matrice Z passée en paramètre
	 * et les imprimes dans la console java.
	 * @param Matrix z, matrice z projetée
	 */
		
		public void Validation(Matrix z)
		{
			Matrix Zed = z;
						
			ArrayList<Matrix> arrayMatrix = new ArrayList<Matrix>(10);
			double moyenneErreur = 0;
			double totaux[] = new double[10];
			
		
			for(int i=0; i<10; i++)
			{
				Matrix zedTot = new Matrix(25,2);
				zedTot.setMatrix(0, 14, 0, 1, Zed.getMatrix(15*i, (15*i+14), 0, 1));
				zedTot.setMatrix(15, 24, 0, 1, Zed.getMatrix((10*i+150), (10*i+159), 0, 1));
				arrayMatrix.add(zedTot);
			}
			
			for(int k = 0; k < 10; k++)
			{
				System.out.println("Itération # "+(k+1));
				EntrainerModele(aggregateExceptOne(arrayMatrix,k), 1);
				EntrainerModele(aggregateExceptOne(arrayMatrix,k), 2);
				
				int nbErreurs = 0;
				for(int row = 0; row < arrayMatrix.get(k).getRowDimension(); row++)
				{
							
					nbErreurs += (ClassifierExemple(arrayMatrix.get(k)) == 
								((row < 15) ? 1 : 2)) 
								? 0 : 1;

				}
				
				totaux[k] = nbErreurs;
				moyenneErreur += nbErreurs;
				System.out.println("Erreurs " + nbErreurs);
			}
			moyenneErreur = moyenneErreur/10;
			System.out.println("Moyenne d'erreurs " + moyenneErreur);
			
			double sumVar = 0;
			for(int t = 0; t < 10; t++)
			{
				sumVar += Math.pow(totaux[t]/25-(moyenneErreur/25),2);
			}
			sumVar = sumVar/10;
			
			System.out.println("\nL'erreur moyen " + moyenneErreur);
			System.out.println("Taux d'erreur moyen " + moyenneErreur/25);
			System.out.println("Variance de taux d'erreur moyen " + sumVar);
			
		}

		
	/**
	 * Classifie le point de la matrice Z projetée dans la classe 1 ou la classe 2.
	 * @param Matrix z, Matrice z projetée
	 * @return soit le numéro de la classe 1 ou la classe 2 ( 1 ou 2 )
	 */

	public double ClassifierExemple(Matrix z)
	{
		return (ProbZC(z,1) + Math.log(ProbC(z,1)) > ProbZC(z,2) + Math.log(ProbC(z,2))) ? 1 : 2;
	}
	 
	/**
	 * Calcules la probabilité de x étant donné Cj
	 * @param Matrix Matrice, ZProjetee
	 * @param int classe à vérifier la probabilité
	 * @return le logarithme de la probabilité de Z étant donnée la classe. P(z|Cj) Probabilité de la vraisemblance
	 */
	
	public double ProbZC(Matrix z, int j)
	{
		double probability = 0;
		if(j==1)
		{
			probability += Math.log(coV1.det())/(-2)-SubtractToMatrix(z, mu1.get(0, j-1))
					.times(coV1.inverse().times(SubtractToMatrix(z, mu1.get(0, j-1))
							.transpose()))
							.get(0, 0)/(2);
		}
		else
		{
			probability += Math.log(coV2.det())/(-2)-SubtractToMatrix(z, mu2.get(0, j-1))
					.times(coV2.inverse().times(SubtractToMatrix(z, mu2.get(0, j-1))
							.transpose()))
							.get(0, 0)/(2);
		}
		

		return probability;
	}
	
	/**
	 * Calcules la probabilité de Cj
	 * @param Matrix z, Matrice zProjetée
	 * @param int numéro de la classe 
	 * @return la probabilité apriori P(Cj)
	 */
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
			probability = GetAmount(this.main, 2) / total;
		}
		
		return probability;
	}
	
	/**
	 * Génère mu pour la classe j
	 * @param Matrix z, matrice Z à générer le mu
	 * @param int j, la classe pour calculer mu
	 * @return Matrix 1*n de valeurs mu par colonnes.
	 */
	private Matrix GenerateMu(Matrix z, int j) {
		Matrix moyenne = new Matrix(1,z.getColumnDimension());
		double dimension = 0;
		
		for(int col = 0; col < z.getColumnDimension(); col++)
		{
			double total = 0;
			
			for(int row = 0; row < z.getRowDimension(); row++)
			{
				if(GetClass(row,j))
				{
					total += z.get(row, col);
					dimension++;
				}
			}
			moyenne.set(0, col, total/dimension);
		}
		
		return moyenne;
	}
	
	/**
	 * Génère la matrice de covariance pour la classe j
	 * @param Matrix z, matrice Z à générer la covariance
	 * @param int j, la classe pour calculer la covariance 
	 * @return Matrix de covariance pour la classe j
	 */
	private Matrix GenerateCoVar(Matrix z, int j) {
		
		ArrayList<Matrix> coVarList = new ArrayList<Matrix>();
		for(int i = 0; i < z.getRowDimension(); i++)
		{
			if(GetClass(i,j))
			{
				Matrix mat = new Matrix(1,2);
				mat.set(0,0,z.get(i, 0));
				mat.set(0,1,z.get(i, 1));
				coVarList.add(mat);
			}
			
		}
		
		Matrix zClass = new Matrix(coVarList.size(),2);
		int[] col = {0,1};
		for(int i = 0; i < zClass.getRowDimension(); i++)
		{
			zClass.setMatrix(i,i,col,coVarList.get(i));
		}	
		return DivideToMatrix(zClass.transpose().times(zClass), zClass.getRowDimension());
	}
	
	
	
	/**
	 * CETTE PARTIE CONTIENT SEULEMENT DES FONCTIONS QUE NOUS UTILISONS SUR LES MATRICES. 
	 * IL SE PEUT QU'IL NE SOIT PAS TOUS UTILISÉS
	 */
	
	/**
	 * Compte le nombre d'éléments se trouvent dans la classe 1 et la classe 2
	 * @param Matrix matrix, Matrice originale 
	 * @param le numéro de la classe à compter
	 * @return le nombre d'éléments de la classe
	 */
	
	private int GetAmount(Matrix matrix, int i) {
		int count = 0;
		for(int row = 0; row < matrix.getRowDimension(); row++)
		{
			count += (matrix.get(row, 0) == i) ? 1 : 0;
		}
		
		return count;
	}

	/**
	 * Élève la matrice à une puissance X
	 * @param Matrix m, Matrice à subir l'opération
	 * @param le chiffre à élever la matrice à puissance X
	 * @return la matrice élever à la puissance X
	 */
	
	public Matrix GetMatrixPowered(Matrix m, int power)
	{
		for(int i = 0; i <= power; i++)
		{
			m.times(m);
		}
		
		return m;
	}
	
	/**
	 * Élève la matrice à la puissance e
	 * @param Matrix m, Matrix à élèver à la puissance e
	 * @return Matrix matrice élèver à la puissance e
	 */
	
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
	
	/**
	 * Applique le logarithmique base 10 à la matrice X
	 * @param Matrix x, matrice à subir l'opération base 10
	 * @return matrice appliquée avec l'opération logarithmique à la base 10.
	 */
	
	public Matrix GetLogTen(Matrix x)
	{
		Matrix l = new Matrix(x.getRowDimension(),x.getColumnDimension());
		
		for(int i = 0; i < l.getRowDimension();i++)
		{
			for(int j = 0; j < l.getColumnDimension(); j++)
			{
				l.set(i, j, Math.log10(x.get(i, j)));
			}
		}
		
		return l;
	}
	
	/**
	 * Ajout d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération ajout
	 * @param double d, le nombre à ajouter à chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments ajouter avec d.
	 */
	
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
	
	/**
	 * Ajout d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération ajout
	 * @param int d, le nombre à ajouter à chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments ajouter avec d.
	 */
	
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
	
	/**
	 * Soustraction d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération soustraction
	 * @param int d, le nombre à soustraire à chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments soustrait avec d.
	 */
	
	public Matrix SubtractToMatrix(Matrix m, int x)
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
	
	/**
	 * Soustraction d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération soustraction
	 * @param double d, le nombre à soustraire à chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments soustrait avec d.
	 */
	
	public Matrix SubtractToMatrix(Matrix m, double x)
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
	
	/**
	 * Division d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération division
	 * @param int d, le nombre à diviser chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments diviser avec d.
	 */
	
	public Matrix DivideToMatrix(Matrix m, int x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) / x));
			}
		}
		
		return a;
	}
	
	/**
	 * Division d'un chiffre à la matrice m
	 * @param Matrix m, Matrice à subir l'opération division
	 * @param double d, le nombre à diviser chaque élement de la matrice m
	 * @return Matrix m avec chaque éléments diviser avec d.
	 */
	
	public Matrix DivideToMatrix(Matrix m, double x)
	{
		Matrix a = new Matrix(m.getRowDimension(),m.getColumnDimension());
		
		for(int i = 0; i < a.getRowDimension();i++)
		{
			for(int j = 0; j < a.getColumnDimension(); j++)
			{
				a.set(i, j, (m.get(i, j) / x));
			}
		}
		
		return a;
	}
	
	/**
	 * Retourne vrai si la classe de la ligne de la matrice R est 1 sinon faux (classe 2).
	 * @param int row, le numéro de la ligne 
	 * @param int j, le numéro de la colonne
	 * @return vrai ou faux
	 */
	
	private boolean GetClass(int row, int j) {
		
		if(tsk.R.get(row, j-1) == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Retourne une matrice aléatoirement choisi
	 * 
	 * @param Matrice m
	 * @return Matrice aléatoire
	 */
	
	public Matrix returnRandomizedMatrix(Matrix m) {
		
		Matrix randMatrix = new Matrix(m.getRowDimension(), m.getColumnDimension());
		ArrayList<Integer> remainingElements = new ArrayList<Integer>(m.getRowDimension());
		
		for(int i = 0; i < m.getRowDimension(); i++)
		{
			remainingElements.add(i);
		}
		int finalPosition = 0;
		while(!remainingElements.isEmpty())
		{
			int position = new Random().nextInt(remainingElements.size());
			int random = remainingElements.get(position);
			randMatrix.set(finalPosition,0,m.get(random, 0));
			randMatrix.set(finalPosition,1,m.get(random, 1));
			remainingElements.remove(position);
			finalPosition++;
		}
		return randMatrix;
	}
	
	/**
	 * La fonction prends le ArrayList de Matrices et choisis les 225 rangées qui n'appartiennent pas à l'indice.
	 * 
	 * @param ArrayList<Matrix> agg
	 * @param int thene
	 * @return Matrice aggregation
	 */
	public Matrix aggregateExceptOne(ArrayList<Matrix> agg, int theone)
	{
		Matrix aggregation = new Matrix(225,2);
		int currentPos = 0;
		int[] col = {0,1};
		
		for(int i = 1; i < 10; i++)
		{
			if(theone != i)
			{
				aggregation.setMatrix(currentPos,(currentPos+24), col, agg.get(i));
				currentPos += 25;
			}
			
		}
		
		return aggregation;
	}
}
