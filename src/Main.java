

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Jama.Matrix;

public class Main {
	
	private static String fileString = "data.txt";
	private static int numberDataByLine = 6;
	
	public static void main(String[] args) {
		Matrix matrix = ReadData();
		
		Matrix class1 = SplitMatrices(matrix, true);
		Matrix class2 = SplitMatrices(matrix, false);

		
		Task2 task2 = new Task2(matrix);
		task2.ExecuteTask2();
	}
	
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

	public static Matrix SplitMatrices(Matrix originalMatrix, boolean getClassOne)
	{
		int rowIndex = -1;
		double newClass = 2d;
		Matrix matrix = null;
		
		
		for(int i = 0; i < originalMatrix.getRowDimension(); i++)
		{
			if(originalMatrix.get(i, 0) == newClass)
			{
				rowIndex = i;
				break;
			}
		}
		
		if(rowIndex != -1)
		{
			if(getClassOne)
			{
				matrix = originalMatrix.getMatrix(0, rowIndex-1, 0, numberDataByLine-1);
				return matrix;
			}
			else
			{
				matrix = originalMatrix.getMatrix(rowIndex, originalMatrix.getRowDimension() - 1, 0, numberDataByLine-1);
				return matrix;
			}
		}
		else
		{
			System.out.println("Can't find two classes !");
			return null;
		}
	}
}
