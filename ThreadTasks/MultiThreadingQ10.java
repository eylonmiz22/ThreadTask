
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MultiThreadingQ10 
{
	final static int N = 5000;
	
	public static void main(String[] args)
	{
		RecursiveTask<Integer> task = new SumForkJoin(1, N);
		ForkJoinPool pool = new ForkJoinPool();
		System.out.println("The result is: " + (pool.invoke(task) + N));
	}

	public static class SumForkJoin extends RecursiveTask<Integer>
	{
		private static final long serialVersionUID = 1L;
		private final static int THRESHOLD = 10 * Runtime.getRuntime().availableProcessors();
		private int low;
		private int high;
		
		public SumForkJoin(int low, int high)
		{
		    this.low = low;
		    this.high = high;
		}
	
	    @Override
	    public Integer compute()
	    { 
	    	if ((this.high - this.low) < THRESHOLD)
	    	{ 
	    		int sum = 0;
	    		for (int i = this.low; i < this.high; i++)
	    		{
	    			sum += i;
	    		}
	    		return sum;
	    	} 
	    	else
	     	{ 
	    		int mid = (this.low + this.high) / 2;
	    		
	    		RecursiveTask<Integer> left = new SumForkJoin(this.low, mid);
	    		
	    		RecursiveTask<Integer> right = new SumForkJoin(mid + 1, this.high);
	    		
	    		invokeAll(right, left);
	    		
	    		return new Integer(left.join() + mid + right.join());
	     	}
	    }
	}
}
