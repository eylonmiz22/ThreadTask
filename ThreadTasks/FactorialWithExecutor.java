import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FactorialWithExecutor
{
	private static long result = 0;

	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a number to calculate it's factorial: ");
		int number = in.nextInt();
		in.close();
		result = number;
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i = number - 1; i > 0; i--)
		{
			executor.execute(new MultArguments(i));
		}
	    executor.shutdown();
	    
	    // Wait until all tasks are finished
	    while (!executor.isTerminated()) {}
	    
		System.out.println("The result is: " + result);
	}

	public static class MultArguments implements Runnable
	{
		private static Lock lock = new ReentrantLock(); // Create a lock
		private int number;
	
		public MultArguments(int number)
		{
			this.number = number;
		}
	
		@Override
		public void run()
		{
			lock.lock();
			if(this.number > 0)
			{
				result *= this.number;
			}
			lock.unlock();
		}
	}
}
