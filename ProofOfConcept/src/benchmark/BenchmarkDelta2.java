package benchmark;

import blackbox.Delta;

public class BenchmarkDelta2 implements Delta{
	private int changedIndex1;
	private int changedIndex2;

	
	public BenchmarkDelta2(int i) {
		int index1 = (int)(Math.random() * ((i - 0))) + 0;
		this.changedIndex1 = index1;
		int index2 = (int)(Math.random() * ((i - 0))) + 0;
		while(index1 == index2) {
			index2 = (int)(Math.random() * ((i - 0))) + 0;
		}
		this.changedIndex2 = index2;
	}
	
	public int getIndex1() {
		return this.changedIndex1;
	}
	public int getIndex2() {
		return this.changedIndex2;
	}
	
	
	public void changeIndex1(int i) {
		this.changedIndex1 = i;
	}
	public void changeIndex2(int i) {
		this.changedIndex2 = i;
	}
} 