package algorithm;

import specialNeighborhood.SpecialNeighborhoodBlackbox;

public class specialNeighborHoodMain {

	public static void printAllBenchmarkRequests(SpecialNeighborhoodBlackbox benchmarkBlackbox) {
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
    	
    	SpecialNeighborhoodBlackbox benchmark = new SpecialNeighborhoodBlackbox(5, 1, 2, 0);
    	int [] neighborhoods = {0, 1};
    	
    	SimulatedAnnealingRoli runnable = new SimulatedAnnealingRoli(benchmark ,0);
		runnable.changeCoolingFactor(0.8);
		runnable.changeTemperature(12);
		runnable.run();
		System.out.println();
		System.out.println("This is the end solution: ");
		System.out.println(runnable.returnSolution().toString());
		System.out.println(benchmark.getCost(runnable.returnSolution()));
		
		printAllBenchmarkRequests(benchmark);


    }
}
