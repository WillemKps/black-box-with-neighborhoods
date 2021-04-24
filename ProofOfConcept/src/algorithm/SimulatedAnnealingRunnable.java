package algorithm;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructDeltaInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class SimulatedAnnealingRunnable implements Runnable {

	Blackbox myBlackbox;
	int neighborhood;	
	Solution min;
	
	public SimulatedAnnealingRunnable(Blackbox myBlackbox, int neighborhood) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhood = neighborhood;
	}
	
	public Solution returnSolution() {
		return this.min;
	}
	
	
	@Override
	public void run() {
	    constructDeltaInterface neighborhood1ConstructNeighbor = this.myBlackbox.arrayListConstructNeighbor.get(this.neighborhood);
	    getSolutionFromNeighborInterface neighborhood1SolutionFromNeighbor = this.myBlackbox.arrayListSolutionFromNeighbor.get(this.neighborhood);
	    getCostWithNeighborInterface getCostWithNeighborInterface = this.myBlackbox.arrayListCostWithNeighbor.get(this.neighborhood);

	    
	    double T = 100; 
	    double Tmin = .001; 
	    double alpha = 0.9; 
	    int numIterations = 5000; 

	    // Generate Initial solution
	    Solution min = this.myBlackbox.constructRandomSolution(); 
		float minCost = this.myBlackbox.getCost(min);

		Solution currentSol = this.myBlackbox.constructRandomSolution(); 
		float curCost = this.myBlackbox.getCost(currentSol);

	    while (T > Tmin) { 
	        for (int i=0;i<numIterations;i++){ 
	            // Reassigns global minimum accordingly 
	            if (curCost < minCost){ 
	                min = currentSol; 
	                minCost = curCost;
	            } 
	            
	            Delta newSolDelta = neighborhood1ConstructNeighbor.constructDelta(currentSol); 
	            float newCost = getCostWithNeighborInterface.getCostWithNeighbor(currentSol, newSolDelta, curCost);
	            double ap = Math.pow(Math.E, (curCost - newCost)/T); 
	            if (ap > Math.random()) {
	            	Solution newSol = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(currentSol, newSolDelta);
	                currentSol = newSol; 
	                curCost = newCost;
	            }
	        } 
	        T *= alpha; // Decreases T, cooling phase 
	    } 
	    //System.out.println("The amount of iterations was " + iterations + ".");
	    this.min = min;
	}
}


