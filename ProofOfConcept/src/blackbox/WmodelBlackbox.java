package blackbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import benchmark.BenchmarkBlackbox;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective_Boolean;

public class WmodelBlackbox implements Blackbox {
	WModel_SingleObjective_Boolean testWmodel;
	Random random;
	
	public WmodelBlackbox() {
		final int _n = 25;  	//Length of the bit string after the introduction of neutrality (LengthOriginal / _mu = _n)
		final int _mu = 2;		//amount of neutrality (1 is no neutrality)	neutrality = fitness of neighbors very similar
		final int _nu = 4;		//amount of epistasis (2 is no additional epistatis) epistasis = two interact epistatically when the contribution they have to the fitness depends on the other bit
		final int _gamma = 12;	//amount of ruggedness (small changes should lead small changes in objective values this is not the case in a rugged search space)
		this.testWmodel = new WModel_SingleObjective_Boolean(_n,_mu,_nu,_gamma); //fitness value = amount of bits that needs to change in order to get 010101010101...
		this.random = new Random();
		this.addImplementedMethodsNeighborhood1();
		this.addImplementedMethodsNeighborhood2();
	}

	
	@Override
	public Solution constructRandomSolution() {
		int n = testWmodel.get_candidate_solution_length();
	    boolean[] GeneratedSolution = new boolean[n];
	    WmodelBlackbox.randomize(GeneratedSolution, random);
		return new WmodelSolution(GeneratedSolution);
	}
	
	@Override
	public float getCost(Solution s) {
		boolean[] boolArray = ((WmodelSolution) s).getBooleanArray();
		return this.testWmodel.applyAsInt(boolArray);
	}

	@Override
	public void removeAllNeighborhoods() {
		BenchmarkBlackbox.arrayListConstructNeighbor.clear();
		BenchmarkBlackbox.arrayListSolutionFromNeighbor.clear();
		BenchmarkBlackbox.arrayListCostWithNeighbor.clear();
		BenchmarkBlackbox.arrayListConstructAllNeighbors.clear();
	}
	
	
//************************************************** Neighborhood 1 ********************************************************************
// Changing one random value ex: 0110 -> 1110
	
	
	public Delta constructNeighbor1(Solution s) {
		return new WmodelDelta1(50);
	}
	
	public Solution getSolutionFromNeighbor1(Solution neighbor, WmodelDelta1 d) {
		boolean[] originalArray = ((WmodelSolution) neighbor).getBooleanArray();
		int n = testWmodel.get_candidate_solution_length();
		boolean[] copyArray = new boolean[n];
		//copying all the values into the array that will be changed
		for (int i = 0; i < originalArray.length; i++) {
			if(originalArray[i]) {
				copyArray[i] = true;
			}
			else {
				copyArray[i] = false;
			}
		} 
		//changing the index of the newly created array
		int index = d.getIndex();
		if(copyArray[index]) {
			copyArray[index] = false;
		}
		  else {
			copyArray[index] = true;		  
		}
		return new WmodelSolution(copyArray);
	}	
	
	public float getCostWithNeighbor1(Solution neighbor, WmodelDelta1 d, float valueNeighbor) {
		boolean[] originalArray = ((WmodelSolution) neighbor).getBooleanArray();
		int n = testWmodel.get_candidate_solution_length();
		boolean[] toChange = new boolean[n];
		//copying all the values into the array that will be changed
		for (int i = 0; i < originalArray.length; i++) {
			if(originalArray[i]) {
				toChange[i] = true;
			}
			else {
				toChange[i] = false;
			}
		} 
		//changing the index of the newly created array
		int index = d.getIndex();
		if(toChange[index]) {
			toChange[index] = false;
		}
		  else {
			toChange[index] = true;		  
		}
		return this.testWmodel.applyAsInt(toChange);// and evaluate it
	}
	
	public List<Delta> constructAllNeighbor1(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n; i++) {
			Delta newDelta = new WmodelDelta1(n);
			((WmodelDelta1) newDelta).changeIndex(i);
			neighbors.add(newDelta);
		}
		Collections.shuffle(neighbors, this.random);
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood1() {
		  WmodelBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor1(s);
			    }
			});
		  WmodelBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor1(neighbor, (WmodelDelta1) d);
			    }
			});
		  WmodelBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor1(neighbor, (WmodelDelta1) d, valueNeighbor);
			    }
			});
		  WmodelBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor1(s);
			    }
			});
	}

//************************************************** Neighborhood 2 ********************************************************************	
// change two random index (of the last 4) ex.: 0110 1111 -> 0110 1100
// this is a "bad" neighborhood since there can only happen changes in the last 4 bits and the first (n-4) remain the same!
	
	
	public Delta constructNeighbor2(Solution s) {
		return new WmodelDelta2(50);
	}
	
	public Solution getSolutionFromNeighbor2(Solution neighbor, WmodelDelta2 d) {
		boolean[] originalArray = ((WmodelSolution) neighbor).getBooleanArray();
		int n = testWmodel.get_candidate_solution_length();
		boolean[] copyArray = new boolean[n];
		//copying all the values into the array that will be changed
		for (int i = 0; i < originalArray.length; i++) {
			if(originalArray[i]) {
				copyArray[i] = true;
			}
			else {
				copyArray[i] = false;
			}
		} 
		//changing the index of the newly created array
		int changedIndex1 = d.getIndex1();
		int changedIndex2 = d.getIndex2();
		if(copyArray[changedIndex1]) {
			copyArray[changedIndex1] = false;
		}
		  else {
			copyArray[changedIndex1] = true;		  
		}
		if(copyArray[changedIndex2]) {
			copyArray[changedIndex2] = false;
		}
		  else {
			copyArray[changedIndex2] = true;		  
		}
		return new WmodelSolution(copyArray);
	}	
	
	
	public float getCostWithNeighbor2(Solution neighbor, WmodelDelta2 d, float valueNeighbor) {
		boolean[] originalArray = ((WmodelSolution) neighbor).getBooleanArray();
		int n = testWmodel.get_candidate_solution_length();
		boolean[] toChange = new boolean[n];
		//copying all the values into the array that will be changed
		for (int i = 0; i < originalArray.length; i++) {
			if(originalArray[i]) {
				toChange[i] = true;
			}
			else {
				toChange[i] = false;
			}
		} 
		//changing the index of the newly created array
		int changedIndex1 = d.getIndex1();
		int changedIndex2 = d.getIndex2();
		if(toChange[changedIndex1]) {
			toChange[changedIndex1] = false;
		}
		  else {
			toChange[changedIndex1] = true;		  
		}
		if(toChange[changedIndex2]) {
			toChange[changedIndex2] = false;
		}
		  else {
			toChange[changedIndex2] = true;		  
		}
		return this.testWmodel.applyAsInt(toChange);// and evaluate it
	}
	
	public List<Delta> constructAllNeighbor2(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();

		Delta delta1 = new WmodelDelta2(n);
		((WmodelDelta2) delta1).changeIndex1(n - 4);
		((WmodelDelta2) delta1).changeIndex2(n - 3);
		neighbors.add(delta1);
		
		Delta delta2 = new WmodelDelta2(n);
		((WmodelDelta2) delta2).changeIndex1(n - 4);
		((WmodelDelta2) delta2).changeIndex2(n - 2);
		neighbors.add(delta2);
		
		Delta delta3 = new WmodelDelta2(n);
		((WmodelDelta2) delta3).changeIndex1(n - 4);
		((WmodelDelta2) delta3).changeIndex2(n - 1);
		neighbors.add(delta3);
		
		Delta delta4 = new WmodelDelta2(n);
		((WmodelDelta2) delta4).changeIndex1(n - 3);
		((WmodelDelta2) delta4).changeIndex2(n - 2);
		neighbors.add(delta4);
		
		Delta delta5 = new WmodelDelta2(n);
		((WmodelDelta2) delta5).changeIndex1(n - 3);
		((WmodelDelta2) delta5).changeIndex2(n - 1);
		neighbors.add(delta5);
		
		Delta delta6 = new WmodelDelta2(n);
		((WmodelDelta2) delta6).changeIndex1(n - 2);
		((WmodelDelta2) delta6).changeIndex2(n - 1);
		neighbors.add(delta6);
		
		Collections.shuffle(neighbors, this.random);
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood2() {
		  WmodelBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor2(s);
			    }
			});
		  WmodelBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor2(neighbor, (WmodelDelta2) d);
			    }
			});
		  WmodelBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor2(neighbor, (WmodelDelta2) d, valueNeighbor);
			    }
			});
		  WmodelBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor2(s);
			    }
			});
	}
	
	
//************************************************** Extra functions ********************************************************************	
	  /**
	   * Randomize a given array
	   *
	   * @param array
	   *          the array
	   * @param random
	   *          the random number generator
	   */
	  protected static final void randomize(final boolean[] array,
	      final Random random) {
	    for (int i = array.length; (--i) >= 0;) {
	      array[i] = random.nextBoolean();
	    }
	  }
	
	  public static final void changeOneRandomValue(final boolean[] toChange) {
		  Random rand = new Random();
		  int random_index = rand.nextInt(toChange.length);
		  if(toChange[random_index]) {
			  toChange[random_index] = false;
		  }
		  else {
			  toChange[random_index] = true;		  }
	  }	  
		
		public int getCandidateSolutionLength() {
			return testWmodel.get_candidate_solution_length();
		}
}
