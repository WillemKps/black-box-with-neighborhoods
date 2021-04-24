package tsp929Efficient;

import blackbox.Delta;

public class TspDelta implements Delta {
	
	private int swapIndexOne;
	private int swapIndexTwo;

	
	public TspDelta(int i) {
		int swapIndexOneTry = (int)(Math.random() * ((i - 0))) + 0;
		int toTry = (int)(Math.random() * ((i - 0))) + 0;
		while(toTry - swapIndexOneTry == 1 || toTry - swapIndexOneTry == -1 || toTry == swapIndexOneTry || (swapIndexOneTry == 0 && toTry == 28)|| (swapIndexOneTry == 28 && toTry == 0)) {
			toTry = (int)(Math.random() * ((i - 0))) + 0;
		}
		int swapIndexTwoTry = toTry;
		if(swapIndexOneTry < swapIndexTwoTry) {
			this.swapIndexOne = swapIndexOneTry;
			this.swapIndexTwo = swapIndexTwoTry;
		}
		else {
			this.swapIndexOne = swapIndexTwoTry;
			this.swapIndexTwo = swapIndexOneTry;
		}
	}
	
	public int getIndexOne() {
		return this.swapIndexOne;
	}
	
	public int getIndexTwo() {
		return this.swapIndexTwo;
	}
	
}
