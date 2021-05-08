package specialNeighborhood;

import blackbox.Delta;

public class SpecialNeighborhoodDelta1 implements Delta{
	boolean[] newSolutionBooleanArray;
	
	public SpecialNeighborhoodDelta1(boolean[] newSolution) {
		this.newSolutionBooleanArray = newSolution;
	}
	
	public boolean[] getBooleanArray() {
		return this.newSolutionBooleanArray;
	}
}
