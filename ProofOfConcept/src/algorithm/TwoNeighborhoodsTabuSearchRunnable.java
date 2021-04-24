package algorithm;

import java.util.ArrayList;
import java.util.List;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructAllDeltasInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class TwoNeighborhoodsTabuSearchRunnable implements Runnable {

	Blackbox myBlackbox;
	int neighborhood1;	
	int neighborhood2;
	Solution min;
	
	public TwoNeighborhoodsTabuSearchRunnable(Blackbox myBlackbox, int neighborhood1, int neighborhood2) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhood1 = neighborhood1;
	    this.neighborhood2 = neighborhood2;
	}
	
	public Solution returnSolution() {
		return this.min;
	}
	
	public static boolean notInTabuList(List<Solution> tabuList, Solution toAdd) {
		for(Solution toCheck: tabuList) {
			if(toCheck.equals(toAdd)) {
				return false;
			}
		}
		return true;
	}
	
	public static Solution findBestNeighbor(Blackbox myBlackbox, List<Solution> neighbors) {
		Solution minSol = neighbors.get(0);
		float minCost = myBlackbox.getCost(minSol);
		
		for(Solution toCheck: neighbors) {
			if(myBlackbox.getCost(toCheck) < minCost) {
				minSol = toCheck;
				minCost = myBlackbox.getCost(toCheck);
			}
		}
		return minSol;
	}	
	
	/**
	 * The tabu search method requires a blackbox that implements the construct all neighbor
	 * @param myBlackbox
	 * @return
	 */
	public static Solution tabuSearch(Blackbox myBlackbox, int neighborhood1, int neighborhood2) {
        getSolutionFromNeighborInterface neighborhood1SolutionFromNeighbor = myBlackbox.arrayListSolutionFromNeighbor.get(neighborhood1);
        constructAllDeltasInterface neighborhood1allNeighbor = myBlackbox.arrayListConstructAllNeighbors.get(neighborhood1);
		
        getSolutionFromNeighborInterface neighborhood2SolutionFromNeighbor = myBlackbox.arrayListSolutionFromNeighbor.get(neighborhood2);
        constructAllDeltasInterface neighborhood2allNeighbor = myBlackbox.arrayListConstructAllNeighbors.get(neighborhood2);

        int iterationsLeft = 100;
        List<Solution> tabuList = new ArrayList<>();
        
        Solution min = myBlackbox.constructRandomSolution();
        float minCost = myBlackbox.getCost(min);
        Solution currentSolution = min;
        float curCost = minCost;
        

		while (iterationsLeft > 0) {
			List<Delta> candidateNeighbors = neighborhood1allNeighbor.constructAllDeltas(currentSolution);
			List<Solution> candidateSolutions = new ArrayList<>();
			for(Delta d: candidateNeighbors) {
				Solution toAdd = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(currentSolution, d);
				if(notInTabuList(tabuList, toAdd)) {
					candidateSolutions.add(toAdd);
				}
			}
			while(tabuList.size() > 30) {
				tabuList.remove(0);
			}
			Solution bestNeighborFound = findBestNeighbor(myBlackbox, candidateSolutions);
			if (myBlackbox.getCost(bestNeighborFound) < minCost) {
				min = bestNeighborFound;
				minCost = myBlackbox.getCost(bestNeighborFound);
			}
			
			tabuList.add(currentSolution);
			currentSolution = bestNeighborFound;
			
			iterationsLeft--;
		}
		iterationsLeft = 50;
		while (iterationsLeft > 0) {
			List<Delta> candidateNeighbors = neighborhood2allNeighbor.constructAllDeltas(currentSolution);
			List<Solution> candidateSolutions = new ArrayList<>();
			for(Delta d: candidateNeighbors) {
				Solution toAdd = neighborhood2SolutionFromNeighbor.getSolutionFromNeighbor(currentSolution, d);
				if(notInTabuList(tabuList, toAdd)) {
					candidateSolutions.add(toAdd);
				}
			}
			while(tabuList.size() > 4) {
				tabuList.remove(0);
			}
			Solution bestNeighborFound = findBestNeighbor(myBlackbox, candidateSolutions);
			if (myBlackbox.getCost(bestNeighborFound) < minCost) {
				min = bestNeighborFound;
				minCost = myBlackbox.getCost(bestNeighborFound);
			}
			
			tabuList.add(currentSolution);
			currentSolution = bestNeighborFound;
			
			iterationsLeft--;
		}
		
		return min;
	}

	@Override
	public void run() {
		Solution res = TwoNeighborhoodsTabuSearchRunnable.tabuSearch(this.myBlackbox, this.neighborhood1, this.neighborhood2);
		this.min = res;
	}
	
}
