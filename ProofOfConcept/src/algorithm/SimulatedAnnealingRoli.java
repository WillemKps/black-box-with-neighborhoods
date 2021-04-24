package algorithm;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructDeltaInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class SimulatedAnnealingRoli implements Runnable {

	Blackbox myBlackbox;
	int neighborhood;	
	Solution min;
    double temperature = 1000;
    double coolingFactor = 0.9995;
	
    public void changeTemperature(double temp) {
    	this.temperature = temp;
    }
    
    public void changeCoolingFactor(double coolFac) {
    	this.temperature = coolFac;
    }
    
	public SimulatedAnnealingRoli(Blackbox myBlackbox, int neighborhood) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhood = neighborhood;
	}
	
	public Solution returnSolution() {
		return this.min;
	}
	
    public static double probability(double f1, double f2, double temp) {
        if (f2 < f1) return 1;
        return Math.exp((f1 - f2) / temp);
    }
    
    public double updateTemperature(double temp) {
    	return temp * this.coolingFactor;
    }
	
	@Override
	public void run() {
	    constructDeltaInterface neighborhood1ConstructNeighbor = this.myBlackbox.arrayListConstructNeighbor.get(this.neighborhood);
	    getSolutionFromNeighborInterface neighborhood1SolutionFromNeighbor = this.myBlackbox.arrayListSolutionFromNeighbor.get(this.neighborhood);
	    getCostWithNeighborInterface getCostWithNeighborInterface = this.myBlackbox.arrayListCostWithNeighbor.get(this.neighborhood);

	    // Generate Initial solution
	    Solution min = this.myBlackbox.constructRandomSolution(); 
		float minCost = this.myBlackbox.getCost(min);
		Solution curr = min;
		float currCost = minCost;
		double t = this.temperature;

	    while (t > 1) {
	    	//make a random neighbor
	    	Delta newSolDelta = neighborhood1ConstructNeighbor.constructDelta(curr); 
            float newCost = getCostWithNeighborInterface.getCostWithNeighbor(curr, newSolDelta, currCost);

            //if neighbor better than best solution , take it
	        if (newCost < minCost) {
	        	curr = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(curr, newSolDelta);
	        	currCost = newCost;
	        	min = curr;
	        	minCost = currCost;
	        }    
	        else {
	        	//accept new solution with a certain probability, depending on currCost, bestCost and temperature
		        if (Math.random() < probability(currCost, newCost, t)) {
		            curr = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(curr, newSolDelta);
	                currCost = newCost;
		        }
	        }
	        t = updateTemperature(t);
	        
	    }
	    this.min = min;
	}
}