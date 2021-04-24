package algorithm;

import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;

public class RunTabuSearchTests {
	public static List<BenchmarkBlackbox> makeAllBenchmarkProblems() {
		List<BenchmarkBlackbox> benchmarkProblems = new ArrayList<BenchmarkBlackbox>();
		for(int id = 1; id<20;id++) {
	    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(id);
	    	benchmarkProblems.add(benchmark);
		}
		return benchmarkProblems;
	}
	public static List<TabuSearchRoli> makeAllRunnableClasses(List<BenchmarkBlackbox> benchmarkProblems, double maxIters, double length) {
		List<TabuSearchRoli> runnables = new ArrayList<TabuSearchRoli>();
		int counter = 0;
		for(BenchmarkBlackbox blackbox:benchmarkProblems) {
	    	TabuSearchRoli runnable = new TabuSearchRoli(blackbox, counter);
	    	runnable.changeLengthTabuList(length);
	    	runnable.changeMaxIterations(maxIters);
	    	runnables.add(runnable);
	    	counter++;
		}
		return runnables;
	}
	public static List<Thread> makeAllThreadClasses(List<TabuSearchRoli> runnables) {
		List<Thread> threads = new ArrayList<Thread>();
		for(TabuSearchRoli runnable:runnables) {
	    	Thread thread = new Thread(runnable);
	    	threads.add(thread);
		}
		return threads;
	}
	
	public static float[] runAllThreads(List<Thread> threads, List<BenchmarkBlackbox> benchmarkProblems, List<TabuSearchRoli> runnables) throws InterruptedException {
		float bestResults[] = new float[benchmarkProblems.size()];
		for(Thread thread:threads) {
			thread.start();
		}
		for(Thread thread:threads) {
			thread.join();
		}
		int counter = 1;
		for(TabuSearchRoli runnable:runnables) {
			float bestRes = (benchmarkProblems.get(counter - 1)).getCost(runnable.returnSolution());
			bestResults[counter-1] = bestRes;
			System.out.println("Cost of the best solution for benchmark problem " + counter + ": " + bestRes);
			printAllBenchmarkRequests(benchmarkProblems.get(counter - 1));
			System.out.println();
			counter++;
		}
		return bestResults;
	}
	
	public static float[] runOneBenchmarkCycle(double maxIters, double lengthTabu) throws InterruptedException {
    	long startTime = System.currentTimeMillis();
		List<BenchmarkBlackbox> benchmarks = makeAllBenchmarkProblems();
		List<TabuSearchRoli> runnables = makeAllRunnableClasses(benchmarks, maxIters, lengthTabu);
		List<Thread> threads = makeAllThreadClasses(runnables);
		float[]  res = runAllThreads(threads, benchmarks, runnables);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Running the algorithm on the 19 benchmark instances took " + (endTime - startTime) + " milliseconds");
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
