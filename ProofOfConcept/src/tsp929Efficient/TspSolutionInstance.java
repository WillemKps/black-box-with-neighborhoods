package tsp929Efficient;

import java.util.ArrayList;
import java.util.List;

import blackbox.Solution;

public class TspSolutionInstance implements Solution {

	private List<Integer> path = new ArrayList<>();
	
	
	/**
	 * Constructor creating a random path for the TspSOlutionInstance
	 */
	protected TspSolutionInstance(int amountOfCities) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < amountOfCities;i++) {
			list.add(i);
	    }
		java.util.Collections.shuffle(list);
		this.path = list;
	}
	
	public List<Integer>  getPath() {
		return this.path;
	}
	
	
	protected void changePath(List<Integer> pathToChange) {
		this.path = pathToChange;
	}

	@Override
	public String toString() {
		String result = "";
		for(int i=0; i< this.path.size() - 1; i++) {
			result = result + this.path.get(i) + " -> ";
		}
		return result;
	}
}
