package algorithm;

import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;
import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructAllDeltasInterface;
import blackbox.Blackbox.constructDeltaInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;


/**
 * Implements variable neighborhood search, this is most effective when the neighborhoods functions (int[] neighborhoods) are 
 * arranged from simple neighborhoods (low cardinality) to more difficulte neighborhoods (higher cardinality).
 * 
 * @author Willem
 *
 */
public class VariableNeighborhoodSearchRoli implements Runnable {

	Blackbox myBlackbox;
	int[] neighborhoods;
	int localSearchNeighborhood;
	Solution min;
	double maxIters = 100;
	
	public VariableNeighborhoodSearchRoli(Blackbox myBlackbox, int [] neighborhoods, int localSearchneighborhood) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhoods = neighborhoods;
	    this.localSearchNeighborhood = localSearchneighborhood;
	}
	
	public Solution returnSolution() {
		return this.min;
	}
	
	public void changeMaxIters(double max){
		this.maxIters = max;
	}
	
	@Override
	public void run() {
		//initialize neighborhoods
		List<getSolutionFromNeighborInterface> getSolutionFromDeltaList = new ArrayList<getSolutionFromNeighborInterface>();
		List<getCostWithNeighborInterface> getCostWithNeighborList = new ArrayList<getCostWithNeighborInterface>();
		List<constructDeltaInterface> constructDeltaList = new ArrayList<constructDeltaInterface>();
		for(int neighborhood:neighborhoods) {
			getSolutionFromDeltaList.add(myBlackbox.arrayListSolutionFromNeighbor.get(neighborhood));
			getCostWithNeighborList.add(myBlackbox.arrayListCostWithNeighbor.get(neighborhood));
			constructDeltaList.add(myBlackbox.arrayListConstructNeighbor.get(neighborhood));
		}	    
		int kMax = this.neighborhoods.length;
        
		
		//initialize local search neighborhoods
		getSolutionFromNeighborInterface LSgetSolutionFromDelta = myBlackbox.arrayListSolutionFromNeighbor.get(localSearchNeighborhood);
		constructAllDeltasInterface LSconstructAllDeltas = myBlackbox.arrayListConstructAllNeighbors.get(localSearchNeighborhood);
		getCostWithNeighborInterface LSgetCostWithNeighbor = myBlackbox.arrayListCostWithNeighbor.get(localSearchNeighborhood);

        //Generate initial solution
        Solution currentSolution = myBlackbox.constructRandomSolution();
        float currCost = myBlackbox.getCost(currentSolution);
        double iterationsLeft = this.maxIters;
		while (iterationsLeft > 0) {
			int k = 1;
			
			while (k < kMax + 1) {
				//Shaking fase choose a random solution in the k neighborhood of current Solution
		        Delta newSolDelta = constructDeltaList.get(k-1).constructDelta(currentSolution);
		        float newSolCost = getCostWithNeighborList.get(k-1).getCostWithNeighbor(currentSolution, newSolDelta, currCost);
		        Solution newSol = getSolutionFromDeltaList.get(k-1).getSolutionFromNeighbor(currentSolution, newSolDelta);
		        
		        //local search this solution untill reaching local optimum 
		        //the local search we use here is chosen by the user and not neceserally in the previous k neighborhoods defined in 
		        //Mladenovic, N., & Hansen, P. (1997). Variable neighborhood search.
		        //Computers & operations research, 24(11), 1097-1100.
		        while (true) {
					List<Delta> candidateNeighbors = LSconstructAllDeltas.constructAllDeltas(newSol);
					boolean changed = false;
					//search for the first improvement
					while(candidateNeighbors.size() != 0) {
						int randomIndex = (int) Math.floor(Math.random()*(candidateNeighbors.size()));
						float potentialCost = LSgetCostWithNeighbor.getCostWithNeighbor(newSol, candidateNeighbors.get(randomIndex), newSolCost);
						if (potentialCost < newSolCost) {
							newSolCost = potentialCost;
							changed = true;
							newSol = LSgetSolutionFromDelta.getSolutionFromNeighbor(newSol, candidateNeighbors.get(randomIndex));
							break;
						}
						candidateNeighbors.remove(randomIndex);
					}
					
					//check if a better solution was found, if not break out of while loop
					if(!changed) {
						break;
					}
		        }
		        //move
		        if(newSolCost < currCost) {
		        	currentSolution = newSol;
		        	currCost = newSolCost;
		        }
		        else {
		        	k++;
		        } 
			}
			iterationsLeft--;
		}
		this.min = currentSolution;
	}
}