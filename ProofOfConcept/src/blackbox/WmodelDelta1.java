package blackbox;

public class WmodelDelta1 implements Delta{
	private int changedIndex;
	
	public WmodelDelta1(int i) {
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
