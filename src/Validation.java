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
		
		Matrix w = entrainerModele(this.Z.getMatrix(0, 24, 0, 1), 3);
		
		double total = 0;
		
		for(int k = 0; k < 10; k++)
		{
			total += evaluerModele(w,z.getMatrix(25+20*k, (20*k+44), 0, 1));
		}
		
		System.out.println("L'erreur moyen " + total/10);
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
        
        return sum;
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
