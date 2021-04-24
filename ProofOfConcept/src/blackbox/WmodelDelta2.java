package blackbox;

public class WmodelDelta2 implements Delta {
	private int changedIndex1;
	private int changedIndex2;
	
	// change two random index (of the last 4) ex.: 0110 1111 -> 0110 1100
	public WmodelDelta2(int i) {
		int tryChangedIndex1 = (int)(Math.random() * (4)) + (i - 4);
		int tryChangedIndex2 = (int)(Math.random() * (4)) + (i - 4);

		while(tryChangedIndex1 == tryChangedIndex2) {
			tryChangedIndex2 = (int)(Math.random() * (4)) + (i - 4);
		}

		if(tryChangedIndex1 < tryChangedIndex2) {
			this.changedIndex1 = tryChangedIndex1;
			this.changedIndex2 = tryChangedIndex2;
		}
		else {
			this.changedIndex1 = tryChangedIndex2;
			this.changedIndex2 = tryChangedIndex1;
		}
		
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
