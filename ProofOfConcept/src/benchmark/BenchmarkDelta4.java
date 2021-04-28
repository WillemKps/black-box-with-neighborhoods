package benchmark;

import blackbox.Delta;

public class BenchmarkDelta4  implements Delta{
	private int changedIndex1;
	private int changedIndex2;
	private int changedIndex3;
	private int changedIndex4;



	
	public BenchmarkDelta4(int i) {
		int index1 = (int)(Math.random() * ((i - 0))) + 0;
		this.changedIndex1 = index1;
		int index2 = (int)(Math.random() * ((i - 0))) + 0;
		while(index1 == index2) {
			index2 = (int)(Math.random() * ((i - 0))) + 0;
		}
		this.changedIndex2 = index2;
		int index3 = (int)(Math.random() * ((i - 0))) + 0;
		while(index3 == index2 && index3 == index1) {
			index3 = (int)(Math.random() * ((i - 0))) + 0;
		}
		this.changedIndex3 = index3;
		int index4 = (int)(Math.random() * ((i - 0))) + 0;
		while(index4 == index2 && index4 == index1 && index4 == index3) {
			index4 = (int)(Math.random() * ((i - 0))) + 0;
		}
		this.changedIndex4 = index4;
	}
	
	public int getIndex1() {
		return this.changedIndex1;
	}
	public int getIndex2() {
		return this.changedIndex2;
	}
	public int getIndex3() {
		return this.changedIndex3;
	}
	public int getIndex4() {
		return this.changedIndex4;
	}
	
	
	public void changeIndex1(int i) {
		this.changedIndex1 = i;
	}
	public void changeIndex2(int i) {
		this.changedIndex2 = i;
	}
	public void changeIndex3(int i) {
		this.changedIndex3 = i;
	}
	public void changeIndex4(int i) {
		this.changedIndex4 = i;
	}
} 