import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import Jama.Matrix;


public class Validation {
	
	private Matrix Z = null;
	
	public Validation(Matrix z)
	{
		this.Z = returnRandomizedMatrix(z);
		
		double total = 0;
		
		ArrayList<Matrix> arrayMatrix = new ArrayList<Matrix>(10);
		double moyenne = 0;
		double totaux[] = new double[10];
		
		
		// Populate array list
		for(int i=0; i<10; i++)
		{
			arrayMatrix.add(this.Z.getMatrix(25*i, (25*i+24), 0, 1));
		}
		
		for(int k = 0; k < 10; k++)
		{
			@SuppressWarnings("null")
			Matrix w = entrainerModele(aggregateExceptOne(arrayMatrix, k), 3);
			total = evaluerModele(w,arrayMatrix.get(k));
			totaux[k] = total;
			moyenne += total;
		}
		moyenne = moyenne/10;
		
		double sumVar = 0;
		for(int t = 0; t < 10; t++)
		{
			sumVar += Math.pow(totaux[t]-(moyenne/25),2);
		}
		sumVar = sumVar/10;
		
		System.out.println("L'erreur moyen " + moyenne);
		System.out.println("Taux d'erreur moyen " + moyenne/25);
		System.out.println("Variance de taux d'erreur moyen " + sumVar);
		
	}
	
	/**
	 * La fonction évaluerModèle retourne l'erreur 
	 * sous la forme 1/n SUM(XI - YI)^2
	 * 
	 * @param Matrice w
	 * @param Matrice x
	 * @return l'erreur (soit empirique ou generale)
	 */
	public double evaluerModele(Matrix w, Matrix x){	
		
        
        double sum = 0;
        for(int i=0;i<x.getRowDimension();i++){
                
                double valX1 = x.get(i,0);
                double valX2 = x.get(i,1);
                double val = 0;
                for(int j=0;j < w.getRowDimension();j++){
                        val += w.get(j,0) * Math.pow(valX1,j);
                }
                sum += Math.pow((valX2 - val),2);
        }
        
        return sum/225;
	}
	
	public Matrix entrainerModele(Matrix m, int polynomeRegression){
		Matrix x = m.getMatrix(0, m.getRowDimension()-1, 0, 0);
		Matrix y = m.getMatrix(0, m.getRowDimension()-1, 1, 1);
		return getWMatrix(vander(x,polynomeRegression),y);
	}

	/**
	 * Retourne une matrice aléatoirement choisi
	 * @author Samy Lemcelli
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
	
	/* 
	 * * Convertie une matrice ordinaire en un matrice vandermonde
	 * 
	 * @param Matrice x
	 * @param polynome de regression
	 * @return Matrice Vandermonde
	 */
	
	public Matrix vander(Matrix x, int regression){
		Matrix vandermonde = new Matrix(x.getRowDimension(),regression+1);
		for (int i = 0; i < x.getRowDimension(); i++) {
			vandermonde.set(i,0,1);
            for (int j = 1; j <= regression; j++) {
                vandermonde.set(i,j,Math.pow(x.get(i,0), j));
            }
        }
		return vandermonde;
	}
	
	/**
	 * Est la fonction w = (X transposée X)^-1 fois X transposé fois y
	 * 
	 * @param Matrice vanderMonde
	 * @param Matrice y
	 * @return matrice W
	 */
	
	public Matrix getWMatrix(Matrix vander, Matrix y)
	{
		return (vander.transpose().times(vander)).inverse().times(vander.transpose()).times(y);
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
	
	public void printFile(String input, String poly){
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("data"+poly+".txt"), "utf-8"));
		    
		    writer.append(input);
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
}
