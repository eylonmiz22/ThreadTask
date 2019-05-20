import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MultMatrixForkJoin 
{	
	public static Integer[][] matrix;
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter rows' and columns' number to create a multiplication board: ");
		int n = in.nextInt();
		in.close();
		matrix = new Integer[n][n];
		
		ForkJoinPool pool = new ForkJoinPool();
		RecursiveAction task = new ColMultTask(1, n);
		pool.invoke(task);

	    System.out.println();
	    printMatrix();
	}
	
	public static void printMatrix()
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
			{
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static class ColMultTask extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;
		private final static int THRESHOLD = Runtime.getRuntime().availableProcessors(); 
		private int up;
		private int down;
		
		public ColMultTask(int up, int down)
		{
			this.up = up;
			this.down = down;
		}
		
		@Override
		protected void compute() 
		{
			if(this.down - this.up + 1 < THRESHOLD)
			{
				ForkJoinPool pool = new ForkJoinPool();
				RecursiveAction task;
				for(int j = 1; j <= matrix[0].length; j++)
				{
					task = new RowMultTask(1, matrix.length, j);
					pool.invoke(task);
				}
			}
			else
			{
				int mid = this.down / 2;
				
	    		RecursiveAction upperPart = new ColMultTask(this.up, mid);
	    		
	    		RecursiveAction lowerPart = new ColMultTask(mid + 1, this.down);
	    		
	    		invokeAll(upperPart, lowerPart);
			}
		}
		
	}
	
	public static class RowMultTask extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;
		private final static int THRESHOLD = Runtime.getRuntime().availableProcessors(); 
		private int right;
		private int left;
		private int currentRow;
	
		public RowMultTask(int left, int right, int currentRow)
		{
			this.left = left;
			this.right = right;
			this.currentRow = currentRow;
		}

		@Override
		public void compute()
		{
			if(this.right - this.left < THRESHOLD)
			{
				for(int j = this.left; j <= this.right; j++)
				{
					matrix[this.currentRow - 1][j - 1] = this.currentRow * j;
				}
			}
			else
			{
				int mid = this.right / 2;
				
	    		RecursiveAction leftPart = new RowMultTask(this.left, mid, this.currentRow);
	    		
	    		RecursiveAction rightPart = new RowMultTask(mid + 1, this.right, this.currentRow);
	    		
	    		invokeAll(leftPart, rightPart);
			}
		}
	}
}
