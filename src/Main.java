/**
 * GTI770 - Laboratoire 3
 * Équipe 3
 * @author Christopher Lariviere, Samy Lemcelli
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Jama.Matrix;

public class Main {
	
	private static String fileString = "data.txt";
	private static int numberDataByLine = 6;
	
	/**
	 * Objectif: Main sert à créer l'objet Tâche 2 (qui instantie la tâche 1 et la validation croisée k-fold)
	 * @param null
	 */
	
	public static void main(String[] args) {
		Matrix matrix = ReadData();
				
		Task2 task2 = new Task2(matrix);
		task2.ExecuteTask2();
	}
	
	/**
	 * Objectif: Lire les données d'un fichier texte
	 * @return la matrice originale provenant du fichier texte
	 */
	
	public static Matrix ReadData()
	{
		Matrix matrix = null;
		try
		{
			String lineData = "";
			BufferedReader br = new BufferedReader(new FileReader(fileString));
		    try 
		    {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) 
		        {
		            sb.append(line);
		            //sb.append(System.lineSeparator());
		            sb.append(" ");
		            line = br.readLine();
		        }
		        lineData = sb.toString();
		    } 
		    catch(Exception ex)
		    {
		    	System.out.println(ex.getMessage());
		    }
		    finally 
		    {
		        br.close();
		    }
		    
		    if(lineData != "")
		    {
		    	String[] data = lineData.split(" ");
		    	double[][] datas = new double[data.length/numberDataByLine][numberDataByLine];
		    	for(int i = 0; i < data.length/numberDataByLine; i++)
		    	{
		    		for(int j = 0; j < numberDataByLine; j++)
		    		{
		    			datas[i][j] = Double.parseDouble(data[i * numberDataByLine + j]);
		    		}
		    	}
		    	
		    	matrix = new Matrix(datas, data.length/numberDataByLine, numberDataByLine);
		    }
		    
		}
		catch (IOException exception)
		{
			
		}
		
		return matrix;
	}
}
