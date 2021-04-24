package benchmark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective_Boolean;

public class BenchmarkBlackbox implements Blackbox {
	WModel_SingleObjective_Boolean testWmodel;
	Random random;
	int amountGetCostWithNeighbor = 0;
	int amountConstructDelta = 0;
	int amountGetSolutionFromNeighbor = 0;
	int amountGetCost = 0;
	int amountConstructRandomSolution = 0;
	int amountConstructAllDelta = 0;
	
	public BenchmarkBlackbox(int ID) {
		int _n = 0;
		int _mu = 0;
		int _nu = 0;
		int _gamma = 0;
		try(BufferedReader in = new BufferedReader(new FileReader("resources/parametersBenchmarkInstanties.txt"))) {
		    String str;
		    int rowIndex = 1;
		    while ((str = in.readLine()) != null) {
		    	if(rowIndex == ID) {
			    	String[] res = str.split(" ");
					_n = Integer.valueOf(res[1]);  	//Length of the bit string after the introduction of neutrality (LengthOriginal / _mu = _n)
					_mu = Integer.valueOf(res[2]);		//amount of neutrality (1 is no neutrality)	neutrality = fitness of neighbors very similar
					_nu = Integer.valueOf(res[4]);		//amount of epistasis (2 is no additional epistatis) epistasis = two interact epistatically when the contribution they have to the fitness depends on the other bit
					_gamma = Integer.valueOf(res[7]);	//amount of ruggedness (small changes should lead small changes in objective values this is not the case in a rugged search space)
			    }
		    	rowIndex++;
		    }
		}
		catch (IOException e) {
			System.out.println(e);
		}
		this.testWmodel = new WModel_SingleObjective_Boolean(_n,_mu,_nu,_gamma); //fitness value = amount of bits that needs to change in order to get 010101010101...
		this.random = new Random();
		this.addImplementedMethodsNeighborhood1();
	}
	
	@Override
	public Solution constructRandomSolution() {
		int n = testWmodel.get_candidate_solution_length();
	    boolean[] GeneratedSolution = new boolean[n];
	    BenchmarkBlackbox.randomize(GeneratedSolution, random);
	    this.amountConstructRandomSolution++;
		return new BenchmarkSolution(GeneratedSolution);
	}
	
	@Override
	public float getCost(Solution s) {
		boolean[] boolArray = ((BenchmarkSolution) s).getBooleanArray();
		float res = this.testWmodel.applyAsInt(boolArray);
		
		this.amountGetCost++;
		return res;
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
		this.amountConstructDelta++;
		return new BenchmarkDelta1(getCandidateSolutionLength());
	}
	
	public Solution getSolutionFromNeighbor1(Solution neighbor, BenchmarkDelta1 d) {
		boolean[] originalArray = ((BenchmarkSolution) neighbor).getBooleanArray();
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
		this.amountGetSolutionFromNeighbor++;
		return new BenchmarkSolution(copyArray);
	}	
	
	public float getCostWithNeighbor1(Solution neighbor, BenchmarkDelta1 d, float valueNeighbor) {
		boolean[] originalArray = ((BenchmarkSolution) neighbor).getBooleanArray();
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
		float res = this.testWmodel.applyAsInt(toChange);
		
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor1(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n; i++) {
			Delta newDelta = new BenchmarkDelta1(n);
			((BenchmarkDelta1) newDelta).changeIndex(i);
			neighbors.add(newDelta);
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood1() {
		BenchmarkBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor1(s);
			    }
			});
		  BenchmarkBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor1(neighbor, (BenchmarkDelta1) d);
			    }
			});
		  BenchmarkBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor1(neighbor, (BenchmarkDelta1) d, valueNeighbor);
			    }
			});
		  BenchmarkBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor1(s);
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

			public int getAmountGetCostWithNeighbor() {
				return this.amountGetCostWithNeighbor;
			}
			public int getAmountConstructDelta() {
				return this.amountConstructDelta;
			}
			public int getAmountGetSolutionFromNeighbor() {
				return this.amountGetSolutionFromNeighbor;
			}
			public int getAmountGetCost() {
				return this.amountGetCost;
			}
			public int getAmountConstructRandomSolution() {
				return this.amountConstructRandomSolution;
			}
			public int getAmountConstructAllDelta() {
				return this.amountConstructAllDelta;
			}
}




	



