package blackbox;


public class WmodelSolution implements Solution{

	boolean[] sol;
	
	public WmodelSolution(boolean[] array) {
		this.sol = array;
	}
	
	public boolean[] getBooleanArray() {
		return this.sol;
	}
	
	@Override
	public String toString() {
		String result;
		String array = "";
		for (int i = 0; i < this.sol.length; i++) {
			if(this.sol[i]) {
				  array = array + "1";
			  }
			  else {
				  array = array + "0";
			  }
			if((i+1)%4==0 && i!=0) {
				array = array + " ";
			}
		} 
		result = "Wmodel solution, array: " + array;
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 

        if (!(o instanceof WmodelSolution)) { 
            return false; 
        } 
          
        // typecast o so that we can compare data members  
        WmodelSolution c = (WmodelSolution) o; 
        boolean[] arrayOne = this.sol;
        boolean[] arrayTwo = c.getBooleanArray();
        
        // Compare the data members and return accordingly  
        for (int i = 0; i < arrayOne.length; i++) {
			if(arrayOne[i] != arrayTwo[i]) {
				return false;
			}
		} 
        return true; 
	}
}
