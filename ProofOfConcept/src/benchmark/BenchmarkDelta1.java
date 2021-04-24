package benchmark;

import blackbox.Delta;

public class BenchmarkDelta1 implements Delta{
	private int changedIndex;
	
	public BenchmarkDelta1(int i) {
		int index = (int)(Math.random() * ((i - 0))) + 0;
		this.changedIndex = index;
	}
	
	public int getIndex() {
		return this.changedIndex;
	}
	
	public void changeIndex(int i) {
		this.changedIndex = i;
	}
} 