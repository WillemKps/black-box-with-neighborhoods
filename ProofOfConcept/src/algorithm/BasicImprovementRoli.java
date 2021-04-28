package algorithm;

import java.util.List;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructAllDeltasInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class BasicImprovementRoli implements Runnable {

	Blackbox myBlackbox;
	int neighborhood;	
	Solution min;
	

	public BasicImprovementRoli(Blackbox myBlackbox, int neighborhood) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhood = neighborhood;
	}
	
	public Solution returnSolution() {
		return this.min;
	}
	
	
	@Override
	public void run() {
        getSolutionFromNeighborInterface neighborhood1SolutionFromNeighbor = myBlackbox.arrayListSolutionFromNeighbor.get(this.neighborhood);
        constructAllDeltasInterface neighborhood1allNeighbor = myBlackbox.arrayListConstructAllNeighbors.get(this.neighborhood);
        getCostWithNeighborInterface getCostWithNeighborInterface = this.myBlackbox.arrayListCostWithNeighbor.get(this.neighborhood);
        
        //Generate initial solution
        Solution currentSolution = myBlackbox.constructRandomSolution();
        float curCost = myBlackbox.getCost(currentSolution);

		while (true) {
			List<Delta> candidateNeighbors = neighborhood1allNeighbor.constructAllDeltas(currentSolution);
			boolean changed = false;
			//search for the first improvement
			while(candidateNeighbors.size() != 0) {
				int randomIndex = (int) Math.floor(Math.random()*(candidateNeighbors.size()));
				float potentialCost = getCostWithNeighborInterface.getCostWithNeighbor(currentSolution, candidateNeighbors.get(randomIndex), curCost);
				if (potentialCost < curCost) {
					curCost = potentialCost;
					changed = true;
					currentSolution = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(currentSolution, candidateNeighbors.get(randomIndex));
					break;
				}
				candidateNeighbors.remove(randomIndex);
			}
			
			//check if a better solution was found, if not break out of while loop
			if(!changed) {
				break;
			}
		}
	    this.min = currentSolution;
	}
} 