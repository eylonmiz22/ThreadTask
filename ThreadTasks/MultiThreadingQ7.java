import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadingQ7
{
	final static int N = 5000000;
	public static long sum = 0;
	public static boolean flag = false;
	public static Lock lock = new ReentrantLock();
	public static Condition finishedSum = lock.newCondition();
	public static ExecutorService executor = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args)
	{		
		executor.execute(new MySum(N));
		executor.execute(new PrintSum());
		executor.shutdown();
	}
	
	public static void calculateSum(int num)
	{
		lock.lock();
		flag = false;
		
		for(int i = 1; i <= num; i++)
		{
			sum += i;
		}
		
		flag = true;
		finishedSum.signal();
		lock.unlock();
	}
	
	public static class PrintSum implements Runnable
	{		
		@Override
		public void run()
		{
			lock.lock();
			try
			{
				while(!flag)
				{
					finishedSum.await();
				}
				System.out.println("sum = " + sum);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				lock.unlock();
			}
		}
	}

	public static class MySum implements Runnable
	{
		private int num;
		
		public MySum(int num)
		{
			this.num = num;
		}
		
		@Override
		public void run() 
		{
			calculateSum(this.num);
		}
	}
}
