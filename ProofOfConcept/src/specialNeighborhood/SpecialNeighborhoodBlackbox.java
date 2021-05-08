package specialNeighborhood;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import blackbox.Blackbox;
import blackbox.Delta;
import blackbox.Solution;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective_Boolean;

public class SpecialNeighborhoodBlackbox implements Blackbox {
	WModel_SingleObjective_Boolean testWmodel;
	int amountOfNeighborhoodsAdded = 0;
	Random random;
	int amountGetCostWithNeighbor = 0;
	int amountConstructDelta = 0;
	int amountGetSolutionFromNeighbor = 0;
	int amountGetCost = 0;
	int amountConstructRandomSolution = 0;
	int amountConstructAllDelta = 0;
	List<boolean[]> solutionsBigList = new ArrayList<boolean[]>();
	
	public SpecialNeighborhoodBlackbox(int _n, int _mu, int _nu, int _gamma) {
		this.testWmodel = new WModel_SingleObjective_Boolean(_n,_mu,_nu,_gamma); //fitness value = amount of bits that needs to change in order to get 010101010101...
		this.random = new Random();
		int length = _n * _mu;
		this.initializeSolutionsBigList(length);
		this.writeAwayValuesSpecialNeighborhood();
		this.addImplementedMethodsNeighborhood1();
		this.addImplementedMethodsNeighborhood2();
		
	}
	
	private void initializeSolutionsBigList(int length) {
		int amountOfSolutionToAdd = (int) Math.pow(2,length);
		System.out.println("Begon initializing big arraylist may take very long time if _n and _mu are big");
		boolean[] previousSol = new boolean[length];
		this.solutionsBigList.add(previousSol);
		amountOfSolutionToAdd--;
		while(amountOfSolutionToAdd != 0) {
			boolean[] solToTry = this.returnCopyOfArray(previousSol);
			for(int indexToChange = 0; indexToChange < length; indexToChange++) {
				if(solToTry[indexToChange]) {
					solToTry[indexToChange] = false;
				}
				else {
					  solToTry[indexToChange] = true;		  
				}
				if(!bigListContains(solToTry)) {
					break;
				}
				else {
					if(solToTry[indexToChange]) {
						solToTry[indexToChange] = false;
					}
					else {
						  solToTry[indexToChange] = true;		  
					}
				}
			}
			previousSol = solToTry;
			this.solutionsBigList.add(previousSol);
			amountOfSolutionToAdd--;
		}
		System.out.println("Finished");
	}

	private boolean bigListContains(boolean[] other) {
		for(boolean[] toTest: this.solutionsBigList) {
			if(this.equalBooleanArrays(toTest, other)) {
				return true;
			}
		}
		return false;
	}
	
	private int bigListIndexOfArray(boolean[] other) {
		int res = 0;
		for(boolean[] toTest: this.solutionsBigList) {
			if(this.equalBooleanArrays(toTest, other)) {
				return res;
			}
			res++;
		}
		return res;
	}

	public int getAmountOfNeighborhoodsAdded(){
		return this.amountOfNeighborhoodsAdded;
	}
	
	@Override
	public Solution constructRandomSolution() {
		int n = testWmodel.get_candidate_solution_length();
	    boolean[] GeneratedSolution = new boolean[n];
	    SpecialNeighborhoodBlackbox.randomize(GeneratedSolution, random);
	    this.amountConstructRandomSolution++;
		return new SpecialNeighborhoodSolution(GeneratedSolution);
	}
	
	public Solution constructSolution10100() {
		int n = testWmodel.get_candidate_solution_length();
	    boolean[] GeneratedSolution = new boolean[n];
	    GeneratedSolution[0] = true;
	    GeneratedSolution[1] = false;
	    GeneratedSolution[2] = true;
	    GeneratedSolution[3] = false;
	    GeneratedSolution[4] = false;
	    this.printBooleanArray(GeneratedSolution);
	    this.amountConstructRandomSolution++;
		return new SpecialNeighborhoodSolution(GeneratedSolution);
	}
	
	@Override
	public float getCost(Solution s) {
		boolean[] boolArray = ((SpecialNeighborhoodSolution) s).getBooleanArray();
		float res = this.testWmodel.applyAsInt(boolArray);
		this.amountGetCost++;
		return res;
	}

	@Override
	public void removeAllNeighborhoods() {
		SpecialNeighborhoodBlackbox.arrayListConstructNeighbor.clear();
		SpecialNeighborhoodBlackbox.arrayListSolutionFromNeighbor.clear();
		SpecialNeighborhoodBlackbox.arrayListCostWithNeighbor.clear();
		SpecialNeighborhoodBlackbox.arrayListConstructAllNeighbors.clear();
	}
	
	//************************************************** Neighborhood 1 ********************************************************************
	// Special neighborhood a solution is a neighbor if they are adjacent in this.solutionsBigList the last and first solution are also neighbors
	
	public Delta constructNeighbor1(Solution s) {
		this.amountConstructDelta++;
		//getIndex
		int index = this.bigListIndexOfArray(((SpecialNeighborhoodSolution) s).getBooleanArray());
		//take neighbor to the right
		if(this.random.nextBoolean()) {
			if(index == this.solutionsBigList.size() - 1) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(0));
			}
			return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index + 1));
		}
		//take neighbor to the left
		else {
			if(index == 0) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 1));
			}
			return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index - 1));
		}
	}
	
	public Solution getSolutionFromNeighbor1(Solution neighbor, SpecialNeighborhoodDelta1 d) {
		this.amountGetSolutionFromNeighbor++;
		return new SpecialNeighborhoodSolution(d.getBooleanArray());
	}	
	
	public float getCostWithNeighbor1(Solution neighbor, SpecialNeighborhoodDelta1 d, float valueNeighbor) {
		float res = this.testWmodel.applyAsInt(d.getBooleanArray());
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor1(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int index = this.bigListIndexOfArray(((SpecialNeighborhoodSolution) s).getBooleanArray());
		if(index == this.solutionsBigList.size() - 1) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(0)));
		}
		else {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index + 1)));
		}
		if(index == 0) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 1)));
		}
		else {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index - 1)));
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood1() {
		this.amountOfNeighborhoodsAdded++;
		SpecialNeighborhoodBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor1(s);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor1(neighbor, (SpecialNeighborhoodDelta1) d);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor1(neighbor, (SpecialNeighborhoodDelta1) d, valueNeighbor);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
			    @Override
			    public List<Delta> constructAllDeltas(Solution s) {
			        return constructAllNeighbor1(s);
			    }
			});
	}
	
	
	//************************************************** Neighborhood 2 ********************************************************************
	// Special neighborhood a solution is a neighbor if they are 2away from the solution in this.solutionsBigList the last and first solution are also neighbors
	
	public Delta constructNeighbor2(Solution s) {
		this.amountConstructDelta++;
		//getIndex
		int index = this.bigListIndexOfArray(((SpecialNeighborhoodSolution) s).getBooleanArray());
		//take neighbor to the right
		if(this.random.nextBoolean()) {
			if(index == this.solutionsBigList.size() - 1) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(1));
			}
			if(index == this.solutionsBigList.size() - 2) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(0));
			}
			return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index + 2));
		}
		//take neighbor to the left
		else {
			if(index == 0) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 2));
			}
			if(index == 1) {
				return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 1));
			}
			return new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index - 2));
		}
	}
	
	public Solution getSolutionFromNeighbor2(Solution neighbor, SpecialNeighborhoodDelta1 d) {
		this.amountGetSolutionFromNeighbor++;
		return new SpecialNeighborhoodSolution(d.getBooleanArray());
	}	
	
	public float getCostWithNeighbor2(Solution neighbor, SpecialNeighborhoodDelta1 d, float valueNeighbor) {
		float res = this.testWmodel.applyAsInt(d.getBooleanArray());
		this.amountGetCostWithNeighbor++;		
		return res;
	}
	
	public List<Delta> constructAllNeighbor2(Solution s){
		List<Delta> neighbors = new ArrayList<>();
		int index = this.bigListIndexOfArray(((SpecialNeighborhoodSolution) s).getBooleanArray());
		if(index == this.solutionsBigList.size() - 1) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(1)));
		}
		else if(index == this.solutionsBigList.size() - 2) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(0)));
		}
		else {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index + 1)));
		}
		if(index == 0) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 2)));
		}
		else if(index == 1) {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(this.solutionsBigList.size() - 1)));
		}
		else {
			neighbors.add(new SpecialNeighborhoodDelta1(this.solutionsBigList.get(index - 1)));
		}
		Collections.shuffle(neighbors, this.random);
		this.amountConstructAllDelta++;
		return neighbors;
	}
	
	public void addImplementedMethodsNeighborhood2() {
		this.amountOfNeighborhoodsAdded++;
		SpecialNeighborhoodBlackbox.arrayListConstructNeighbor.add(new constructDeltaInterface() {
			    @Override
			    public Delta constructDelta(Solution s) {
			        return constructNeighbor2(s);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListSolutionFromNeighbor.add(new getSolutionFromNeighborInterface() {
			    @Override
			    public Solution getSolutionFromNeighbor(Solution neighbor, Delta d) {
			        return getSolutionFromNeighbor2(neighbor, (SpecialNeighborhoodDelta1) d);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListCostWithNeighbor.add(new getCostWithNeighborInterface() {
			    @Override
			    public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor) {
			        return getCostWithNeighbor2(neighbor, (SpecialNeighborhoodDelta1) d, valueNeighbor);
			    }
			});
		SpecialNeighborhoodBlackbox.arrayListConstructAllNeighbors.add(new constructAllDeltasInterface() {
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
			
			public void printBooleanArray(boolean[] sol) {
				String array = "";
				for (int i = 0; i < sol.length; i++) {
					if(sol[i]) {
						  array = array + "1";
					  }
					  else {
						  array = array + "0";
					  }
					if((i+1)%4==0 && i!=0) {
						array = array + " ";
					}
				} 
				System.out.println(array);
			}
			
			public boolean equalBooleanArrays(boolean[] arrayOne, boolean[] arrayTwo) {
		        if(arrayOne.length != arrayTwo.length) {
		        	return false;
		        }
		        for (int i = 0; i < arrayOne.length; i++) {
					if(arrayOne[i] != arrayTwo[i]) {
						return false;
					}
				} 
		        return true; 
			}
			
			public boolean[] returnCopyOfArray(boolean[] originalArray) {
				boolean[] toChange = new boolean[originalArray.length];
				//copying all the values into the array that will be changed
				for (int i = 0; i < originalArray.length; i++) {
					if(originalArray[i]) {
						toChange[i] = true;
					}
					else {
						toChange[i] = false;
					}
				}
				return toChange; 
			}
			
			private void writeAwayValuesSpecialNeighborhood() {
				List<String> listValues = new ArrayList<String>();
				for(boolean[] toWriteAway: this.solutionsBigList) {
					float res = this.testWmodel.applyAsInt(toWriteAway);
					int in = (int) res;
					listValues.add(String.valueOf(in));
				}
				writeAway(listValues, "objectiveValues.txt");
			}
			public static void writeAway(List<String> currentSolutions, String fileName){
				Path out = Paths.get(fileName);
				try {
					Files.write(out, currentSolutions,Charset.defaultCharset());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
}