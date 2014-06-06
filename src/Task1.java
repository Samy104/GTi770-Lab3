import Jama.Matrix;

public class Task1 {

	public static void ExecuteTask1(Matrix x)
	{
		
	}
	
	private static double Mean(Matrix x, int column)
	{
		double data = 0;
		for(int i = 0; i < x.getRowDimension(); i++)
		{
			data += x.get(column, i);
		}
		
		return data/x.getRowDimension();
	}
}
