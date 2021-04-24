package algorithm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;

public class RunSimulatedAnnealingTests {
	public static void writeAway(List<String> currentSolutions, String fileName){
		Path out = Paths.get(fileName);
		try {
			Files.write(out, currentSolutions,Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<BenchmarkBlackbox> makeAllBenchmarkProblems() {
		List<BenchmarkBlackbox> benchmarkProblems = new ArrayList<BenchmarkBlackbox>();
		for(int id = 1; id<20;id++) {
	    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(id);
	    	benchmarkProblems.add(benchmark);
		}
		return benchmarkProblems;
	}
	public static List<SimulatedAnnealingRoli> makeAllRunnableClasses(List<BenchmarkBlackbox> benchmarkProblems, double initialTemperature, double coolingFactor) {
		List<SimulatedAnnealingRoli> runnables = new ArrayList<SimulatedAnnealingRoli>();
		int counter = 0;
		for(BenchmarkBlackbox blackbox:benchmarkProblems) {
	    	SimulatedAnnealingRoli runnable = new SimulatedAnnealingRoli(blackbox, counter);
	    	runnable.changeCoolingFactor(coolingFactor);
	    	runnable.changeTemperature(initialTemperature);
	    	runnables.add(runnable);
	    	counter++;
		}
		return runnables;
	}
	public static List<Thread> makeAllThreadClasses(List<SimulatedAnnealingRoli> runnables) {
		List<Thread> threads = new ArrayList<Thread>();
		for(SimulatedAnnealingRoli runnable:runnables) {
	    	Thread thread = new Thread(runnable);
	    	threads.add(thread);
		}
		return threads;
	}
	
	public static float[] runAllThreads(List<Thread> threads, List<BenchmarkBlackbox> benchmarkProblems, List<SimulatedAnnealingRoli> runnables) throws InterruptedException {
		float bestResults[] = new float[benchmarkProblems.size()];
		for(Thread thread:threads) {
			thread.start();
		}
		for(Thread thread:threads) {
			thread.join();
		}
		int counter = 1;
		for(SimulatedAnnealingRoli runnable:runnables) {
			float bestRes = (benchmarkProblems.get(counter - 1)).getCost(runnable.returnSolution());
			bestResults[counter-1] = bestRes;
			System.out.println("Cost of the best solution for benchmark problem " + counter + ": " + bestRes);
			printAllBenchmarkRequests(benchmarkProblems.get(counter - 1));
			System.out.println();
			counter++;
		}
		return bestResults;
	}
	
	public static float[] runOneBenchmarkCycle(double initialTemperature, double coolingFactor) throws InterruptedException {
    	long startTime = System.currentTimeMillis();
		List<BenchmarkBlackbox> benchmarks = makeAllBenchmarkProblems();
		List<SimulatedAnnealingRoli> runnables = makeAllRunnableClasses(benchmarks, initialTemperature, coolingFactor);
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
