package algorithm;

import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;

public class RunBasicImprovementsTests {
	public static List<BenchmarkBlackbox> makeAllBenchmarkProblems() {
		List<BenchmarkBlackbox> benchmarkProblems = new ArrayList<BenchmarkBlackbox>();
		for(int id = 1; id<20;id++) {
	    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(id);
	    	benchmarkProblems.add(benchmark);
		}
		return benchmarkProblems;
	}
	public static List<BasicImprovementRoli> makeAllRunnableClasses(int neighborhood, List<BenchmarkBlackbox> benchmarkProblems) {
		List<BasicImprovementRoli> runnables = new ArrayList<BasicImprovementRoli>();
		int counter = 0;
		for(BenchmarkBlackbox blackbox:benchmarkProblems) {
	    	BasicImprovementRoli runnable = new BasicImprovementRoli(blackbox, counter + neighborhood);
	    	counter = counter + blackbox.getAmountOfNeighborhoodsAdded();
	    	runnables.add(runnable);
		}
		return runnables;
	}
	public static List<Thread> makeAllThreadClasses(List<BasicImprovementRoli> runnables) {
		List<Thread> threads = new ArrayList<Thread>();
		for(BasicImprovementRoli runnable:runnables) {
	    	Thread thread = new Thread(runnable);
	    	threads.add(thread);
		}
		return threads;
	}
	
	public static float[] runAllThreads(List<Thread> threads, List<BenchmarkBlackbox> benchmarkProblems, List<BasicImprovementRoli> runnables) throws InterruptedException {
		float bestResults[] = new float[benchmarkProblems.size()];
		for(Thread thread:threads) {
			thread.start();
		}
		for(Thread thread:threads) {
			thread.join();
		}
		int counter = 1;
		for(BasicImprovementRoli runnable:runnables) {
			float bestRes = (benchmarkProblems.get(counter - 1)).getCost(runnable.returnSolution());
			bestResults[counter-1] = bestRes;
			//System.out.println("Cost of the best solution for benchmark problem " + counter + ": " + bestRes);
			//printAllBenchmarkRequests(benchmarkProblems.get(counter - 1));
			//System.out.println();
			counter++;
		}
		return bestResults;
	}
	
	public static float[] runOneBenchmarkCycle(int neighborhood) throws InterruptedException {
    	long startTime = System.currentTimeMillis();
		List<BenchmarkBlackbox> benchmarks = makeAllBenchmarkProblems();
		List<BasicImprovementRoli> runnables = makeAllRunnableClasses(neighborhood, benchmarks);
		List<Thread> threads = makeAllThreadClasses(runnables);
		float[]  res = runAllThreads(threads, benchmarks, runnables);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Running basic improvement on the 19 benchmark instances took " + (endTime - startTime) + " milliseconds");
    	benchmarks.get(0).removeAllNeighborhoods();
    	return res;
	}
	
	public static void printAllBenchmarkRequests(BenchmarkBlackbox benchmarkBlackbox) {
		System.out.print("GetCostWithNeighbor: ");
		System.out.println(benchmarkBlackbox.getAmountGetCostWithNeighbor());
		
		System.out.print("ConstructDelta: ");
		System.out.println(benchmarkBlackbox.getAmountConstructDelta());
		
		System.out.print("GetSolutionFromNeighbor: ");
		System.out.println(benchmarkBlackbox.getAmountGetSolutionFromNeighbor());
		
		System.out.print("GetCost: ");
		System.out.println(benchmarkBlackbox.getAmountGetCost());

		System.out.print("ConstructRandomSolution: ");
		System.out.println(benchmarkBlackbox.getAmountConstructRandomSolution());
		
		System.out.print("ConstructAllDelta: ");
		System.out.println(benchmarkBlackbox.getAmountConstructAllDelta());
	}
}
