
package tsp929Efficient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import benchmark.BenchmarkBlackbox;
import blackbox.Delta;
import blackbox.Solution;
import blackbox.WmodelBlackbox;
import blackbox.WmodelDelta1;
import blackbox.Blackbox;
import blackbox.Blackbox.constructAllDeltasInterface;
import blackbox.Blackbox.constructDeltaInterface;
import blackbox.Blackbox.getCostWithNeighborInterface;
import blackbox.Blackbox.getSolutionFromNeighborInterface;

public class TspBlackbox929Cities implements Blackbox {
	

	private float[][] matrix;
	private boolean[] originalArray;
	
	public TspBlackbox929Cities() {
		this.matrix = readMatrix();
		addImplementedMethodsNeighborhood1();
		System.out.println("Initialized tsp problem");
	}
	
	
	//optimal solution around 90 000 I think
	private float[][] readMatrix(){
		float[][] values = new float[929][929];
		try(BufferedReader in = new BufferedReader(new FileReader("resources/tour929.txt"))) {
		    String str;
		    int rowIndex = 0;
		    while ((str = in.readLine()) != null) {
		    	String[] res = str.split(",");
		    	for (int columnIndex = rowIndex; columnIndex < res.length;columnIndex++) {
		    		String toAdd = res[columnIndex];
		    		values[rowIndex][columnIndex] = Float.valueOf(toAdd);
		    		values[columnIndex][rowIndex] = Float.valueOf(toAdd);
		    	}
		    	rowIndex++;
		    }
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return values;
	}

	@Override
	public void removeAllNeighborhoods() {
		BenchmarkBlackbox.arrayListConstructNeighbor.clear();
		BenchmarkBlackbox.arrayListSolutionFromNeighbor.clear();
		BenchmarkBlackbox.arrayListCostWithNeighbor.clear();
		BenchmarkBlackbox.arrayListConstructAllNeighbors.clear();
	}
	
/*
******************************* BLACKBOX METHODS *******************************
 */
	
	@Override
	public TspSolutionInstance constructRandomSolution() {
		TspSolutionInstance randomInstance = new TspSolutionInstance(929);
		return randomInstance;
	}


	
	@Override
	public float getCost(Solution s) {
	    float totalCost = 0;
	    List<Integer> path = ((TspSolutionInstance) s).getPath();
	    for (int i = 0; i < path.size() - 1;i++) {
	    	int rowIndex = path.get(i);
	    	int columnIndex = path.get(i + 1);
	    	totalCost = totalCost + matrix[rowIndex][columnIndex];
	    }
	    totalCost = totalCost + matrix[path.get(path.size() - 1)][path.get(0)];
	    return  totalCost;
		}
	
//************************************************** Neighborhood 1 ********************************************************************
// Changing two random edges
// ex:  	0-1-2-3-4-5-6	= [0 1 2 3 4 5 6]
//			|___________|	with swapIndex1 = 1 (edge between 1 and 2) and swapIndex2 = 4 (edge between 4 and 5)
//
//   		  |¯¯¯¯¯|
//becomes 	0-1 2-3-4 5-6	= [0 1 4 3 2 5 6]
//			|   |_____| |  
//			|___________|
	/**
	 * This methods takes two random index that are 2 not next to each other
	 */
	public TspDelta constructNeighbor1(Solution s) {
		return new TspDelta(929);
	}
	
	public float getCostWithNeighbor1(Solution neighbor, Delta d, float valueNeighbor) {
		float result = valueNeighbor;
		int swapIndexOne = ((TspDelta) d).getIndexOne();
		int swapIndexTwo = ((TspDelta) d).getIndexTwo();
		List<Integer> path = ((TspSolutionInstance) neighbor).getPath();
		
		int aCity = path.get(swapIndexOne);
		int bCity = path.get(swapIndexOne + 1);
		int cCity = path.get(swapIndexTwo);
		int dCity;
		if(swapIndexTwo + 1 == 929) {
			dCity = path.get(0);
		}
		else {
			dCity = path.get(swapIndexTwo + 1);
		}

		//remove edge a-b and c-d
		result = result - matrix[aCity][bCity];
		result = result - matrix[cCity][dCity];
		//add edge a-c and b-d
		result = result + matrix[aCity][cCity];
		result = result + matrix[bCity][dCity];
		return result;
	}

	public TspSolutionInstance getSolutionFromNeighbor1(Solution neighbor, Delta d) {
		int swapIndexOne = ((TspDelta) d).getIndexOne();
		int swapIndexTwo = ((TspDelta) d).getIndexTwo();
		List<Integer> neighborPath = ((TspSolutionInstance) neighbor).getPath();
		
		TspSolutionInstance randomInstance = new TspSolutionInstance(929);
		List<Integer>  newPath = new ArrayList<>();

		int aCityIndex = swapIndexOne;
		int bCityIndex = swapIndexOne + 1;
		int cCityIndex = swapIndexTwo;
		int dCityIndex;
		if(swapIndexTwo + 1 == 929) {
			dCityIndex = 0;
		}
		else {
			dCityIndex = swapIndexTwo + 1;
		}
		
		//add all city untill index a	(from example 0 1 )
		for (int i = 0; i <= aCityIndex ; i++) {
			newPath.add(neighborPath.get(i));
		}	

		//add city at index c then city index c -1 until index b + 1 and index b	(from example 0 1 4 3 2)
		for (int i = cCityIndex; i >= bCityIndex ; i--) {
			newPath.add(neighborPath.get(i));
		}
		
		if(dCityIndex != 0) {
			//add city d and the ones after d (from example 0 1 4 3 2 5 6)
			for (int i = dCityIndex; i < 929 ; i++) {
				newPath.add(neighborPath.get(i));
			}
		}
		randomInstance.changePath(newPath);
		return randomInstance;
	}

	public void addImplementedMethodsNeighborhood1() {
		  TspBlackbox929Cities.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor1(s);
			    }
			});
		  TspBlackbox929Cities.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor1(neighbor, (TspDelta) d);
			    }
			});
		  TspBlackbox929Cities.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor1(neighbor, (TspDelta) d, valueNeighbor);
			    }
			});
		  TspBlackbox929Cities.arrayListConstructAllNeighbors.add(null);
	}
	
	
}
