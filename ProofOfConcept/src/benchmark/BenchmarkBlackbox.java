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
	int amountOfNeighborhoodsAdded = 0;
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
		this.addImplementedMethodsNeighborhood2();
		this.addImplementedMethodsNeighborhood3();
		this.addImplementedMethodsNeighborhood4();
		this.addImplementedMethodsNeighborhood5();
	}
	
	public int getAmountOfNeighborhoodsAdded(){
		return this.amountOfNeighborhoodsAdded;
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
		this.amountOfNeighborhoodsAdded++;
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
	
	
	
	
	//************************************************** Neighborhood 2 ********************************************************************
	// Changing two random value ex: 0110 -> 1111
		
	
	public Delta constructNeighbor2(Solution s) {
		this.amountConstructDelta++;
		return new BenchmarkDelta2(getCandidateSolutionLength());
	}
	
	public Solution getSolutionFromNeighbor2(Solution neighbor, BenchmarkDelta2 d) {
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
		//changing two index of the newly created array
		int index1 = d.getIndex1();
		if(copyArray[index1]) {
			copyArray[index1] = false;
		}
		  else {
			copyArray[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(copyArray[index2]) {
			copyArray[index2] = false;
		}
		  else {
			copyArray[index2] = true;		  
		}
		this.amountGetSolutionFromNeighbor++;
		return new BenchmarkSolution(copyArray);
	}	
	
	public float getCostWithNeighbor2(Solution neighbor, BenchmarkDelta2 d, float valueNeighbor) {
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
		int index1 = d.getIndex1();
		if(toChange[index1]) {
			toChange[index1] = false;
		}
		  else {
			  toChange[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(toChange[index2]) {
			toChange[index2] = false;
		}
		  else {
			  toChange[index2] = true;		  
		}
		float res = this.testWmodel.applyAsInt(toChange);
		
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor2(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n-1; i++) {
			for(int j = i+1; j < n; j++) {
				Delta newDelta = new BenchmarkDelta2(n);
				((BenchmarkDelta2) newDelta).changeIndex1(i);
				((BenchmarkDelta2) newDelta).changeIndex2(j);
				neighbors.add(newDelta);				
			}
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood2() {
		this.amountOfNeighborhoodsAdded++;
		BenchmarkBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor2(s);
			    }
			});
		  BenchmarkBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor2(neighbor, (BenchmarkDelta2) d);
			    }
			});
		  BenchmarkBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor2(neighbor, (BenchmarkDelta2) d, valueNeighbor);
			    }
			});
		  BenchmarkBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor2(s);
			    }
			});
	}
		
	
	
	//************************************************** Neighborhood 3 ********************************************************************
	// Changing three random value ex: 0110 -> 1101
		
	
	public Delta constructNeighbor3(Solution s) {
		this.amountConstructDelta++;
		return new BenchmarkDelta3(getCandidateSolutionLength());
	}
	
	public Solution getSolutionFromNeighbor3(Solution neighbor, BenchmarkDelta3 d) {
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
		//changing two index of the newly created array
		int index1 = d.getIndex1();
		if(copyArray[index1]) {
			copyArray[index1] = false;
		}
		  else {
			copyArray[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(copyArray[index2]) {
			copyArray[index2] = false;
		}
		  else {
			copyArray[index2] = true;		  
		}
		int index3 = d.getIndex3();
		if(copyArray[index3]) {
			copyArray[index3] = false;
		}
		  else {
			copyArray[index3] = true;		  
		}
		this.amountGetSolutionFromNeighbor++;
		return new BenchmarkSolution(copyArray);
	}	
	
	public float getCostWithNeighbor3(Solution neighbor, BenchmarkDelta3 d, float valueNeighbor) {
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
		int index1 = d.getIndex1();
		if(toChange[index1]) {
			toChange[index1] = false;
		}
		  else {
			  toChange[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(toChange[index2]) {
			toChange[index2] = false;
		}
		  else {
			  toChange[index2] = true;		  
		}
		int index3 = d.getIndex3();
		if(toChange[index3]) {
			toChange[index3] = false;
		}
		  else {
			  toChange[index3] = true;		  
		}
		float res = this.testWmodel.applyAsInt(toChange);
		
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor3(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n -2; i++) {
			for(int j = i+1; j < n-1; j++) {
				for(int p = j+1; p < n; p++) {
					Delta newDelta = new BenchmarkDelta3(n);
					((BenchmarkDelta3) newDelta).changeIndex1(i);
					((BenchmarkDelta3) newDelta).changeIndex2(j);
					((BenchmarkDelta3) newDelta).changeIndex3(p);
					neighbors.add(newDelta);	
				}
			}
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood3() {
		this.amountOfNeighborhoodsAdded++;
		BenchmarkBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor3(s);
			    }
			});
		  BenchmarkBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor3(neighbor, (BenchmarkDelta3) d);
			    }
			});
		  BenchmarkBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor3(neighbor, (BenchmarkDelta3) d, valueNeighbor);
			    }
			});
		  BenchmarkBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor3(s);
			    }
			});
	}
	
	//************************************************** Neighborhood 4 ********************************************************************
	// Changing three random value ex: 0110 -> 1001
		
	
	public Delta constructNeighbor4(Solution s) {
		this.amountConstructDelta++;
		return new BenchmarkDelta4(getCandidateSolutionLength());
	}
	
	public Solution getSolutionFromNeighbor4(Solution neighbor, BenchmarkDelta4 d) {
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
		//changing two index of the newly created array
		int index1 = d.getIndex1();
		if(copyArray[index1]) {
			copyArray[index1] = false;
		}
		  else {
			copyArray[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(copyArray[index2]) {
			copyArray[index2] = false;
		}
		  else {
			copyArray[index2] = true;		  
		}
		int index3 = d.getIndex3();
		if(copyArray[index3]) {
			copyArray[index3] = false;
		}
		  else {
			copyArray[index3] = true;		  
		}
		int index4 = d.getIndex4();
		if(copyArray[index4]) {
			copyArray[index4] = false;
		}
		  else {
			copyArray[index4] = true;		  
		}
		this.amountGetSolutionFromNeighbor++;
		return new BenchmarkSolution(copyArray);
	}	
	
	public float getCostWithNeighbor4(Solution neighbor, BenchmarkDelta4 d, float valueNeighbor) {
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
		int index1 = d.getIndex1();
		if(toChange[index1]) {
			toChange[index1] = false;
		}
		  else {
			  toChange[index1] = true;		  
		}
		int index2 = d.getIndex2();
		if(toChange[index2]) {
			toChange[index2] = false;
		}
		  else {
			  toChange[index2] = true;		  
		}
		int index3 = d.getIndex3();
		if(toChange[index3]) {
			toChange[index3] = false;
		}
		  else {
			  toChange[index3] = true;		  
		}
		int index4 = d.getIndex4();
		if(toChange[index4]) {
			toChange[index4] = false;
		}
		  else {
			  toChange[index4] = true;		  
		}
		float res = this.testWmodel.applyAsInt(toChange);
		
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor4(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n -3; i++) {
			for(int j = i+1; j < n-2; j++) {
				for(int p = j+1; p < n-1; p++) {
					for(int u = p+1; u < n; u++) {
						Delta newDelta = new BenchmarkDelta4(n);
						((BenchmarkDelta4) newDelta).changeIndex1(i);
						((BenchmarkDelta4) newDelta).changeIndex2(j);
						((BenchmarkDelta4) newDelta).changeIndex3(p);
						((BenchmarkDelta4) newDelta).changeIndex4(u);
						neighbors.add(newDelta);
					}
				}
			}
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood4() {
		this.amountOfNeighborhoodsAdded++;
		BenchmarkBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor4(s);
			    }
			});
		  BenchmarkBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor4(neighbor, (BenchmarkDelta4) d);
			    }
			});
		  BenchmarkBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor4(neighbor, (BenchmarkDelta4) d, valueNeighbor);
			    }
			});
		  BenchmarkBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor4(s);
			    }
			});
	}
						
	
	//************************************************** Neighborhood 5 ********************************************************************
	// Changing two random value OR one random value ex: 0110 -> 1111 and 0110 -> 1110
		
	
	public Delta constructNeighbor5(Solution s) {
		this.amountConstructDelta++;
		return new BenchmarkDelta5(getCandidateSolutionLength());
	}
	
	public Solution getSolutionFromNeighbor5(Solution neighbor, BenchmarkDelta5 d) {
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
		//changing two index of the newly created array
		int index1 = d.getIndex1();
		if(copyArray[index1]) {
			copyArray[index1] = false;
		}
		  else {
			copyArray[index1] = true;		  
		}
		if(d.changesTwoIndex()) {
			int index2 = d.getIndex2();
			if(copyArray[index2]) {
				copyArray[index2] = false;
			}
			  else {
				copyArray[index2] = true;		  
			}
		}

		this.amountGetSolutionFromNeighbor++;
		return new BenchmarkSolution(copyArray);
	}	
	
	public float getCostWithNeighbor5(Solution neighbor, BenchmarkDelta5 d, float valueNeighbor) {
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
		int index1 = d.getIndex1();
		if(toChange[index1]) {
			toChange[index1] = false;
		}
		  else {
			  toChange[index1] = true;		  
		}
		if(d.changesTwoIndex()) {
			int index2 = d.getIndex2();
			if(toChange[index2]) {
				toChange[index2] = false;
			}
			  else {
				  toChange[index2] = true;		  
			}
		}
		float res = this.testWmodel.applyAsInt(toChange);
		
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor5(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int n = testWmodel.get_candidate_solution_length();
		for(int i = 0; i < n-1; i++) {
			for(int j = i+1; j < n; j++) {
				Delta newDelta = new BenchmarkDelta5(n);
				((BenchmarkDelta5) newDelta).changeIndex1(i);
				((BenchmarkDelta5) newDelta).changeIndex2(j);
				neighbors.add(newDelta);				
			}
		}
		for(int i = 0; i < n; i++) {
			Delta newDelta = new BenchmarkDelta5(n);
			((BenchmarkDelta5) newDelta).changeIndex1(i);
			((BenchmarkDelta5) newDelta).changeIndex2(-1);
			neighbors.add(newDelta);
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood5() {
		this.amountOfNeighborhoodsAdded++;
		BenchmarkBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor5(s);
			    }
			});
		  BenchmarkBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor5(neighbor, (BenchmarkDelta5) d);
			    }
			});
		  BenchmarkBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor5(neighbor, (BenchmarkDelta5) d, valueNeighbor);
			    }
			});
		  BenchmarkBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor5(s);
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




	



