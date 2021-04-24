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
	
	

    public static void main(String[] args) throws InterruptedException {

    	double initialTemperature = 100;
    	double coolingFactor = 0.95;
    	float[] res = RunSimulatedAnnealingTests.runOneBenchmarkCycle(initialTemperature, coolingFactor);
    	
    	double initialTemperature2 = 10000;
    	double coolingFactor2 = 0.99995;
    	float[] res2 = RunSimulatedAnnealingTests.runOneBenchmarkCycle(initialTemperature2, coolingFactor2);
    	
    	for(float result:res) {
    		System.out.print(result + " ");
    	}
    	System.out.println();
    	for(float result:res2) {
    		System.out.print(result + " ");
    	}
    	
    	
    	double maxIters = 1000;
    	double lengthTabuList = 30;
    	float[] res3 = RunSimulatedAnnealingTests.runOneBenchmarkCycle(maxIters, lengthTabuList);
    	
    	double maxIters2 = 10000;
    	double lengthTabuList2 = 60;
    	float[] res4 = RunSimulatedAnnealingTests.runOneBenchmarkCycle(maxIters2, lengthTabuList2);
    	
    	for(float result:res3) {
    		System.out.print(result + " ");
    	}
    	System.out.println();
    	for(float result:res4) {
    		System.out.print(result + " ");
    	}
    	
    	float[] res5 = RunBasicImprovementsTests.runOneBenchmarkCycle();
    	

    	float[] res6 = RunBasicImprovementsTests.runOneBenchmarkCycle();
    	
    	for(float result:res5) {
    		System.out.print(result + " ");
    	}
    	System.out.println();
    	for(float result:res6) {
    		System.out.print(result + " ");
    	}
    	
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
