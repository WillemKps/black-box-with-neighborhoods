package benchmark;

import blackbox.Delta;

public class BenchmarkDelta5 implements Delta{
	private int changedIndex1;
	private int changedIndex2 = -1;

	
	/**
	 * Makes a the delta for neighborhood 5, a bit string is change in 1 values OR 2 values.
	 * amount of solutions with 1 index changes = n 
	 * 0000 -> 0001, 0010, 0100, 1000
	 * amount of solutions with 1 index changes = n-1 + n-2 + ... + 2 + 1 = (n*(n-1))/2
	 * 0000 -> 0011, 0101, 1001, 0110, 1010, 1100
	 * n + (n*(n-1))/2 is the total amount of solutions and every new solution has the same amount of chance to be picked
	 * @param n
	 */
	
	public BenchmarkDelta5(int n) {
		int totalAmountOfPossibilities = n + (n*(n-1))/2;
		float chanceToBeSingleBitFlip = 1/totalAmountOfPossibilities*n;
		if (Math.random() < chanceToBeSingleBitFlip) {
			int index1 = (int)(Math.random() * ((n - 0))) + 0;
			this.changedIndex1 = index1;
		}
		else {
			int index1 = (int)(Math.random() * ((n - 0))) + 0;
			this.changedIndex1 = index1;
			int index2 = (int)(Math.random() * ((n - 0))) + 0;
			while(index1 == index2) {
				index2 = (int)(Math.random() * ((n - 0))) + 0;
			}
			this.changedIndex2 = index2;
		}
	}
	
	public int getIndex1() {
		return this.changedIndex1;
	}
	public int getIndex2() {
		return this.changedIndex2;
	}
	 public boolean changesOneIndex() {
		 return this.changedIndex2 == -1;
	 }
	 public boolean changesTwoIndex() {
		 return !(this.changedIndex2 == -1);
	 }
	
	public void changeIndex1(int i) {
		this.changedIndex1 = i;
	}
	public void changeIndex2(int i) {
		this.changedIndex2 = i;
	}
} 