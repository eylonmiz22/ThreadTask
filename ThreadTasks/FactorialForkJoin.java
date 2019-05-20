import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FactorialForkJoin 
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a number to calculate it's factorial: ");
		int number = in.nextInt();
		in.close();
		RecursiveTask<Integer> task = new FactorialTask(0, number);
		ForkJoinPool pool = new ForkJoinPool();
		System.out.println("The result is: " + pool.invoke(task));
	}

	public static class FactorialTask extends RecursiveTask<Integer>
	{
		private static final long serialVersionUID = 1L;
		private final static int THRESHOLD = Runtime.getRuntime().availableProcessors();
		private int low;
		private int high;
		
		public FactorialTask(int low, int high)
		{
		    this.low = low;
		    this.high = high;
		}
	
	    @Override
	    public Integer compute()
	    { 
	    	if ((this.high - this.low) < THRESHOLD)
	    	{ 
	    		int result = this.high;
	    		for (int i = 1; i < (this.high - this.low); i++)
	    		{
	    			result *= (this.high - i);
	    		}
	    		return result;
	    	} 
	    	else
	     	{ 
	    		int mid = (this.low + this.high) / 2;
	    		int rest = (this.low + this.high) % 2;
	    		
	    		RecursiveTask<Integer> left = new FactorialTask(this.low, mid);
	    		
	    		RecursiveTask<Integer> right = new FactorialTask(mid + 1, this.high);
	    		
	    		invokeAll(right, left);
	    		
	    		return new Integer(left.join() * (mid + rest) * right.join());
	     	}
	    }
	}
}
