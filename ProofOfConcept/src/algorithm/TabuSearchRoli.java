package algorithm;

import java.util.ArrayList;
import java.util.List;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.Blackbox.constructAllDeltasInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class TabuSearchRoli implements Runnable {

	Blackbox myBlackbox;
	int neighborhood1;
	Solution min;
	double maxIterations = 100;
	double lengthTabuList = 50;
	
	public TabuSearchRoli(Blackbox myBlackbox, int neighborhood1) {
	    this.myBlackbox = myBlackbox;
	    this.neighborhood1 = neighborhood1;
	}
    public void changeLengthTabuList(double length) {
    	this.lengthTabuList = length;
    }
    
    public void changeMaxIterations(double iters) {
    	this.maxIterations = iters;
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
	
	public static float[] removeElementFromArray(float[] array, int index) {
		float[] copy = new float[array.length - 1];

		for (int i = 0, j = 0; i < array.length; i++) {
		    if (i != index) {
		        copy[ j++] = array[i];
		    }
		}
		return copy;
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

	public void updateTabuList(List<Solution> tabuList, Solution currentSolution) {
		if(tabuList.size() == this.lengthTabuList) {
			tabuList.remove(0);
		}
		tabuList.add(currentSolution);
	}
	
	
	/**
	 * The tabu search method requires a blackbox that implements the construct all neighbor
	 * @param myBlackbox
	 * @return
	 */
	public Solution tabuSearch(Blackbox myBlackbox, int neighborhood1) {
        getSolutionFromNeighborInterface neighborhood1SolutionFromNeighbor = myBlackbox.arrayListSolutionFromNeighbor.get(neighborhood1);
        constructAllDeltasInterface neighborhood1allNeighbor = myBlackbox.arrayListConstructAllNeighbors.get(neighborhood1);
        getCostWithNeighborInterface getCostWithNeighborInterface = this.myBlackbox.arrayListCostWithNeighbor.get(neighborhood1);
        double iterationsLeft = this.maxIterations;
        
        //Generate initial solution
        Solution min = myBlackbox.constructRandomSolution();
        float minCost = myBlackbox.getCost(min);
        Solution currentSolution = min;
        float curCost = minCost;
        // System.out.println("The start cost: " + curCost);
        //initiate empty tabu list
        List<Solution> tabuList = new ArrayList<>();

		while (iterationsLeft > 0) {
			List<Delta> candidateNeighbors = neighborhood1allNeighbor.constructAllDeltas(currentSolution);

			//search for best neighbor that is not in tabu list
			//make a list of all the cost
			float f[] = new float[candidateNeighbors.size()];
			int counter = 0;
			for (Delta delta : candidateNeighbors){
				f[counter] = getCostWithNeighborInterface.getCostWithNeighbor(currentSolution, delta, curCost);
				counter++;
			}
			//Search for minimum then check if in tabu list
			Solution newSolution = null;
			float newCost = -1;
			while(f.length != 0) {
				float minimum = f[0];
				int minIndex = 0;
			    for(int i=1;i<f.length;i++) {
			    	if(minimum > f[i]) {
			    		minimum = f[i];
			    		minIndex = i;
			    	}
			    }
		        //System.out.println("The mnimum cost: " + minimum + " at itertions left: " + iterationsLeft);
				//check if in tabu list if not add it and take it as newSolution
				newSolution = neighborhood1SolutionFromNeighbor.getSolutionFromNeighbor(currentSolution, candidateNeighbors.get(minIndex));
				if (notInTabuList(tabuList, newSolution)) {
					newCost = f[minIndex];
					break;
				}
				else {
			        //System.out.println("removed an element");
					f = removeElementFromArray(f, minIndex);
					candidateNeighbors.remove(minIndex);
					newSolution = null;
				}
				
			}
			
			//update the tabulist
			if(newSolution != null) {
				this.updateTabuList(tabuList, newSolution);
				//this.printTabuList(tabuList);
				currentSolution = newSolution;
			}
			else {
				break;
			}
			//check if the newSolution is better than best solution
			if (newCost < minCost) {
				min = newSolution;
				minCost = newCost;
		        //System.out.println("The best cost was updated: " + newCost + "at itertions left: " + iterationsLeft);
		        

				
			}
			
			iterationsLeft--;
		}
		return min;
	}

	private void printTabuList(List<Solution> tabuList) {
		for(Solution sol:tabuList) {
			System.out.println(sol.toString());
		}
		System.out.println();
	}
	@Override
	public void run() {
		Solution res = this.tabuSearch(this.myBlackbox, this.neighborhood1);
		this.min = res;
	}
}