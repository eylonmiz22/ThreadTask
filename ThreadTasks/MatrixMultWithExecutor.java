import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MatrixMultWithExecutor 
{
	private static int n;
	private static int[][] matrix;
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter rows' and columns' number to create a multiplication board: ");
		n = in.nextInt();
		in.close();
		matrix = new int[n][n];
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i = 1; i <= n; i++)
		{
			executor.execute(new MultiplicationRowTask(i));
		}
	    executor.shutdown();
	    // Wait until all tasks are finished
	    while (!executor.isTerminated())
	    {
	    }
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
	
	public static class MultiplicationRowTask implements Runnable
	{
		private static Lock lock = new ReentrantLock(); // Create a lock
		private int[] rowArr;
		private int currentRow;
	
		public MultiplicationRowTask(int currentRow)
		{
			this.currentRow = currentRow;
			this.rowArr = new int[n];
			for(int i = 0; i < n; i ++)
			{
				this.rowArr[i] = (i + 1);
			}
		}
	
		@Override
		public void run()
		{
			lock.lock();
			for(int i = 0; i < n; i++)
			{
				this.rowArr[i] *= this.currentRow;
				matrix[i][this.currentRow - 1] = this.rowArr[i];
			}
			lock.unlock();
		}
	}
}
