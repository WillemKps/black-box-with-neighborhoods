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
import blackbox.WmodelBlackbox;



public class Main {
	
	public static void runExampleAlgorithms() throws InterruptedException {
    	double initialTemperature = 100;
    	double coolingFactor = 0.95;
    	int neighborhood = 0;
    	float[] res = RunSimulatedAnnealingTests.runOneBenchmarkCycle( neighborhood, initialTemperature, coolingFactor);
    	
    	double initialTemperature2 = 1000000;
    	double coolingFactor2 = 0.999995;
    	int neighborhood2 = 0;
    	float[] res2 = RunSimulatedAnnealingTests.runOneBenchmarkCycle(neighborhood2, initialTemperature2, coolingFactor2);
    	
    	int total = 0;
    	for(float result:res) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	for(float result:res2) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;

    	
    	double maxIters = 100;
    	double lengthTabuList = 30;
    	int neighborhood3 = 0;
    	float[] res3 = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood3, maxIters, lengthTabuList);
    	
    	double maxIters2 = 1000;
    	double lengthTabuList2 = 60;
    	int neighborhood4 = 0;
    	float[] res4 = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood4, maxIters2, lengthTabuList2);
    	
    	for(float result:res3) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	for(float result:res4) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	
    	int neighborhood5 = 0;
    	float[] res5 = RunBasicImprovementsTests.runOneBenchmarkCycle(neighborhood5);
    	
    	int neighborhood6 = 0;
    	float[] res6 = RunBasicImprovementsTests.runOneBenchmarkCycle(neighborhood6);
    	
    	for(float result:res5) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	for(float result:res6) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	
    	int neighborhood7 = 4;
    	int [] neighborhoods7 = {0, 1, 2};
    	double maxIters7 = 10;
    	float[] res7 = RunVariableNeighborhoodSearchRoli.runOneBenchmarkCycle(neighborhoods7, neighborhood7, maxIters7);
    
    	
    	int neighborhood8 = 4;
    	int [] neighborhoods8 = {0, 1, 2, 3};
    	double maxIters8 = 20;
    	float[] res8 = RunVariableNeighborhoodSearchRoli.runOneBenchmarkCycle(neighborhoods8, neighborhood8, maxIters8);
    	
    	for(float result:res7) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	total = 0;
    	for(float result:res8) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
	}

	public static void testTabuSearch() throws InterruptedException {
    	
    	for(double maxIters=50;maxIters<101;maxIters = maxIters + 50) {
    		for(double lengthTabuList=10;lengthTabuList<21;lengthTabuList = lengthTabuList + 10) {
    	    	int neighborhood3 = 0;
    	    	float[] res = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood3, maxIters, lengthTabuList);
    	    	
    	    	int total = 0;
    	    	for(float result:res) {
    	    		System.out.print(result + " ");
    	    		total = (int) (total + result);
    	    	}
    	    	System.out.println(" This comes to a total of : " + total);
    	    	System.out.println( "MaxIters were: " + maxIters + " length tabu list were: " + lengthTabuList);
    	    	System.out.println();
    		}
    	
    	}
    	for(double maxIters=5000;maxIters<10001;maxIters = maxIters + 1000) {
    		for(double lengthTabuList=1000;lengthTabuList<2001;lengthTabuList = lengthTabuList + 500) {
    	    	int neighborhood3 = 0;
    	    	float[] res = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood3, maxIters, lengthTabuList);
    	    	
    	    	int total = 0;
    	    	for(float result:res) {
    	    		System.out.print(result + " ");
    	    		total = (int) (total + result);
    	    	}
    	    	System.out.println(" This comes to a total of : " + total);
    	    	System.out.println( "MaxIters were: " + maxIters + " length tabu list were: " + lengthTabuList);
    	    	System.out.println();
    		}
    	}
    	
    	
	}	
	
	public void runALotOfTabuAndBasicIpmrovment() throws InterruptedException {
    	int totalAlgoritme1 = 0;
    	int totalAlgoritme2 = 0;
    	int totalAlgoritme3 = 0;
    	int totalAlgoritme4 = 0;
    	
    	for(int i = 0;i<50;i++) {
        	int total = 0;
        	double maxIters = 100;
        	double lengthTabuList = 30;
        	int neighborhood3 = 0;
        	float[] res3 = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood3, maxIters, lengthTabuList);
        	
        	double maxIters2 = 1000;
        	double lengthTabuList2 = 60;
        	int neighborhood4 = 0;
        	float[] res4 = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood4, maxIters2, lengthTabuList2);
        	
        	for(float result:res3) {
        		System.out.print(result + " ");
        		total = (int) (total + result);
        	}
        	System.out.println(" This comes to a total of : " + total);
        	totalAlgoritme1 = totalAlgoritme1 + total;
        	total = 0;
        	for(float result:res4) {
        		System.out.print(result + " ");
        		total = (int) (total + result);
        	}
        	System.out.println(" This comes to a total of : " + total);
        	totalAlgoritme2 = totalAlgoritme2 + total;
        	total = 0;
        	
        	int neighborhood5 = 0;
        	float[] res5 = RunBasicImprovementsTests.runOneBenchmarkCycle(neighborhood5);
        	
        	int neighborhood6 = 0;
        	float[] res6 = RunBasicImprovementsTests.runOneBenchmarkCycle(neighborhood6);
        	
        	for(float result:res5) {
        		System.out.print(result + " ");
        		total = (int) (total + result);
        	}
        	System.out.println(" This comes to a total of : " + total);
        	totalAlgoritme3 = totalAlgoritme3 + total;
        	total = 0;
        	for(float result:res6) {
        		System.out.print(result + " ");
        		total = (int) (total + result);
        	}
        	System.out.println(" This comes to a total of : " + total);
        	totalAlgoritme4 = totalAlgoritme4 + total;
        	total = 0;
    	}

    	System.out.println("Totaal algoritme (Tabu 100iters, 30localSearch): " + totalAlgoritme1/50);
    	System.out.println("Totaal algoritme (Tabu 1000iters, 60localSearch): " + totalAlgoritme2/50);
    	System.out.println("Totaal algoritme (BasicImprovement): " + totalAlgoritme3/50);
    	System.out.println("Totaal algoritme (Basic Improvement): " + totalAlgoritme4/50);
		
	}
	
	
	
    public static void main(String[] args) throws InterruptedException {
    	
    	Main.runExampleAlgorithms();    	
    	//Main.testTabuSearch();
    	

    	
    	/*   
    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(1);
    	
		TabuSearchRoli runnable = new TabuSearchRoli(benchmark , 0);

		runnable.run();
		System.out.println(runnable.returnSolution().toString());
		System.out.println(benchmark.getCost(runnable.returnSolution()));
    	
   	
    	int neighborhood3 = 0;
    	float[] res = RunTabuSearchTests.runOneBenchmarkCycle(neighborhood3, 1000, 10);
    	
    	int total = 0;
    	for(float result:res) {
    		System.out.print(result + " ");
    		total = (int) (total + result);
    	}
    	System.out.println(" This comes to a total of : " + total);
    	System.out.println( "MaxIters were: " + 1000 + " length tabu list were: " + 10);
    	System.out.println();

    	
    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(18);
    	int[] neig = {0, 1 , 2};
		VariableNeighborhoodSearchRoli runnable = new VariableNeighborhoodSearchRoli(benchmark, neig , 4);

		runnable.run();
		System.out.println(runnable.returnSolution().toString());
		System.out.println(benchmark.getCost(runnable.returnSolution()));
    	
		
		
    	BenchmarkBlackbox benchmark2 = new BenchmarkBlackbox(19);
    	int[] neig2 = {5, 6 , 7, 8};
		VariableNeighborhoodSearchRoli runnable2 = new VariableNeighborhoodSearchRoli(benchmark2, neig2 , 9);

		runnable2.run();
		System.out.println(runnable2.returnSolution().toString());
		System.out.println(benchmark2.getCost(runnable2.returnSolution()));
    	
    	BenchmarkBlackbox benchmark = new BenchmarkBlackbox(18);
    	int[] neig = {0, 1 , 2};
		VariableNeighborhoodSearchRoli runnable = new VariableNeighborhoodSearchRoli(benchmark, neig , 4);

		runnable.run();
		System.out.println(runnable.returnSolution().toString());
		System.out.println(benchmark.getCost(runnable.returnSolution()));
    	
    	
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
