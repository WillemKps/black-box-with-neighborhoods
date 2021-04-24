package algorithm;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;
import blackbox.Blackbox;



public class Main {
	
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
	public static List<SimulatedAnnealingRoli> makeAllRunnableClasses(List<BenchmarkBlackbox> benchmarkProblems) {
		List<SimulatedAnnealingRoli> runnables = new ArrayList<SimulatedAnnealingRoli>();
		int counter = 0;
		for(BenchmarkBlackbox blackbox:benchmarkProblems) {
	    	SimulatedAnnealingRoli runnable = new SimulatedAnnealingRoli(blackbox, counter);
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
	
	public static void runOneBenchmarkCycle() throws InterruptedException {
    	long startTime = System.currentTimeMillis();
		List<BenchmarkBlackbox> benchmarks = makeAllBenchmarkProblems();
		List<SimulatedAnnealingRoli> runnables = makeAllRunnableClasses(benchmarks);
		List<Thread> threads = makeAllThreadClasses(runnables);
		runAllThreads(threads, benchmarks, runnables);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Running the algorithm on the 19 benchmark instances took " + (endTime - startTime) + " milliseconds");
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

    public static void main(String[] args) throws InterruptedException {

    	long startTime = System.currentTimeMillis();
    	int threadid = 11;
    	Blackbox threadmyBlackbox = new BenchmarkBlackbox(threadid);
    	SimulatedAnnealingRoli runnable1 = new SimulatedAnnealingRoli(threadmyBlackbox, 0);
    	Thread thread1 = new Thread(runnable1);
    	
    	int threadid2 = 1;
    	Blackbox threadmyBlackbox2 = new BenchmarkBlackbox(threadid2);
    	TabuSearchRoli runnable2 = new TabuSearchRoli(threadmyBlackbox2, 1);
    	Thread thread2 = new Thread(runnable2);

    	thread1.start();
    	thread2.start();
    	thread1.join();
    	thread2.join();
    	
    	
    	
    	System.out.println("This is the cost of found solution with the SA algorithm: " + threadmyBlackbox.getCost(runnable1.returnSolution()));
    	System.out.println(runnable1.returnSolution().toString());
    	System.out.println("This is the cost of found solution with the Tabu algorithm: " + threadmyBlackbox2.getCost(runnable2.returnSolution()));
    	System.out.println(runnable2.returnSolution().toString());
    	
    	long endTime = System.currentTimeMillis();
    	System.out.println("That took " + (endTime - startTime) + " milliseconds");
    	System.out.println();

    	
    	List<String> testje = ((BenchmarkBlackbox) threadmyBlackbox2).getArrayListCurrentSol();
		//do something with your ArrayList 
    	String fileName = "C:\\Users\\wille\\Documents\\Master\\2deJaar\\Thesis\\BenchmarkData\\testje2.txt";
    	Main.writeAway(testje, fileName);
    	
    	Main.printAllBenchmarkRequests((BenchmarkBlackbox) threadmyBlackbox);
    	Main.printAllBenchmarkRequests((BenchmarkBlackbox) threadmyBlackbox2);
    	
    	
    	threadmyBlackbox2.removeAllNeighborhoods();
    	
    	System.out.print("Imade a change");
    	System.out.println();
    	System.out.println();
    	System.out.println();
    	System.out.println();
    	runOneBenchmarkCycle();
    	
    	/*
    	Blackbox myBlackbox = new WmodelBlackbox();
    	Solution bestWithSimAnn= simulatedAnnealing(myBlackbox,0);
    	System.out.println("This is the cost of found solution with the SA algorithm: " + myBlackbox.getCost(bestWithSimAnn));
    	System.out.println(bestWithSimAnn.toString());

    	System.out.println();
    	Solution bestWithTabu= tabuSearch(myBlackbox);
    	System.out.println("This is the cost of found solution with the tabu algorithm: " + myBlackbox.getCost(bestWithTabu));
    	System.out.println(bestWithTabu.toString());
    	
    	System.out.println(((WmodelBlackbox) myBlackbox).getCandidateSolutionLength());
    	
    	
    	long startTime = System.currentTimeMillis();
    	
    	Blackbox myBlackbox2 = new TspBlackbox929Cities();
    	Solution bestTspWithSimAnn= simulatedAnnealing(myBlackbox2,2);
    	System.out.println("This is the cost of found solution with the SA algorithm: " + myBlackbox2.getCost(bestTspWithSimAnn));
    	//System.out.println(bestTspWithSimAnn.toString());

    	long endTime = System.currentTimeMillis();
    	System.out.println("That took " + (endTime - startTime) + " milliseconds");
    	System.out.println();
    	
    	startTime = System.currentTimeMillis();

    	Blackbox myBlackbox3 = new TspBlackbox929CitiesNotUsingDelta();
    	Solution bestTspWithSimAnnNotEfficient= simulatedAnnealing(myBlackbox3,3);
    	System.out.println("This is the cost of found solution with the SA algorithm: " + myBlackbox3.getCost(bestTspWithSimAnnNotEfficient));
    	//System.out.println(bestTspWithSimAnnNotEfficient.toString());
    	
    	endTime = System.currentTimeMillis();
    	System.out.println("That took " + (endTime - startTime) + " milliseconds");
    	System.out.println();
		*/
    }
	
}
