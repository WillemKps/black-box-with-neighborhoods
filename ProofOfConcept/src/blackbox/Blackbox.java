package blackbox;

import java.util.ArrayList;
import java.util.List;


public interface Blackbox {
	
	/**
	 * Constructs a random solution instance.
	 * @return	Returns the random solution instance
	 */
	public Solution constructRandomSolution();
	
	/**
	 * Returns the cost of the given solution instance.
	 * @param s		The solution instance of which cost is asked
	 * @return		Float number that matches the cost of the given solution in the blackbox.
	 */
	public float getCost(Solution s);
	
	/**
	 * Interface used to ensure type of constructDelta method
	 */
	public interface constructDeltaInterface {
		public Delta constructDelta(Solution s);
	}
		
	/**
	 * List containing methods for constructing a random neighbor from a Solution.
	 * This list should match the following structure:
	 * 
	 * Ex.:	arrayListConstructNeighbor			<null,						constructDelta2, 		constructNeighbor3>
	 * 		arrayListConstructAllNeighbors		<constructAllNeighbors1,	constructAllNeighbor2, 		null>
	 * 		Delta								 WmodelDelta1,				WmodelDelta2, 				WmodelDelta3			 required
	 * 		arrayListCostWithNeighbor			 getCostWithNeighbor1		getCostWithNeighbor2		getCostWithNeighbor3	 required
	 * 		arrayListSolutionFromNeighbor		 getSolutionFromNeighbor1	getSolutionFromNeighbor2	getSolutionFromNeighbor3 required
	 */
	List<constructDeltaInterface> arrayListConstructNeighbor = new ArrayList<>();
	
	
	
	/**
	 * This function is used to clear 
	 * the arrayList containing the delta constructing functions, 
	 * the arrayList containing the neighbor constructing functions 
	 * the arrayList containing the constructing all delta's functions
	 * and the arrayList containing the cost calculator functions from a neighboring solution, it's cost and a delta
	 */	
	public void removeAllNeighborhoods();
	
	
	/**
	 * Interface used to ensure type of getSolutionFromNeighbor method
	 */
	public interface getSolutionFromNeighborInterface {
		/**
		 * Returns the solution constructed from neighbor and Delta d.
		 * @param neighbor 	The neighbor of the solution that we want to create.
		 * @param d			Delta used to find the solution (relative to the neighbor).
		 * @return			Returns the solution constructed by using the neighbor and delta d.
		 */	
		public Solution getSolutionFromNeighbor(Solution neighbor, Delta d);
	}	
	
	/**
	 * List containing methods for getting a solution from a neighbor Solution and a Delta.
	 * This list should match the following structure:
	 * 
	 * Ex.:	arrayListConstructNeighbor			<null,						constructNeighbor2, 		constructNeighbor3>
	 * 		arrayListConstructAllNeighbors		<constructAllNeighbors1,	constructAllNeighbor2, 		null>
	 * 		Delta								 WmodelDelta1,				WmodelDelta2, 				WmodelDelta3			 required
	 * 		arrayListCostWithNeighbor			 getCostWithNeighbor1		getCostWithNeighbor2		getCostWithNeighbor3	 required
	 * 		arrayListSolutionFromNeighbor		 getSolutionFromNeighbor1	getSolutionFromNeighbor2	getSolutionFromNeighbor3 required
	 */
	List<getSolutionFromNeighborInterface> arrayListSolutionFromNeighbor = new ArrayList<>();

	
	/**
	 * Interface used to ensure type of constructNeighbor method
	 *
	 */
	public interface getCostWithNeighborInterface {
		/**
		 * Returns the cost of the solution constructed with neighbor and delta d.
		 * @param neighbor		The neighbor of the solution with known cost
		 * @param d				Delta, specifies how the to search solution is related to the neighbor
		 * @param valueNeighbor Cost of the neighbor of the solution with known cost
		 * @return				Returns the cost of the to search solution
		 */
		public float getCostWithNeighbor(Solution neighbor, Delta d, float valueNeighbor);
	}
	
	/**
	 * List containing methods for getting the cost of a solution from a neighbor Solution, the cost of the neighbor and a Delta.
	 * This list should match the following structure:
	 * 
	 * Ex.:	arrayListConstructNeighbor			<null,						constructNeighbor2, 		constructNeighbor3>
	 * 		arrayListConstructAllNeighbors		<constructAllNeighbors1,	constructAllNeighbor2, 		null>
	 * 		Delta								 WmodelDelta1,				WmodelDelta2, 				WmodelDelta3			 required
	 * 		arrayListCostWithNeighbor			 getCostWithNeighbor1		getCostWithNeighbor2		getCostWithNeighbor3	 required
	 * 		arrayListSolutionFromNeighbor		 getSolutionFromNeighbor1	getSolutionFromNeighbor2	getSolutionFromNeighbor3 required
	 */
	List<getCostWithNeighborInterface> arrayListCostWithNeighbor = new ArrayList<>();

	
	/**
	 * Interface used to ensure type of constructAllNeighbors method
	 */
	public interface constructAllDeltasInterface {
		public List<Delta> constructAllDeltas(Solution s);
	}
	
	/**
	 * List containing methods for constructing all neighbors from a Solution.
	 * This list should match the following structure:
	 * 
	 * Ex.:	arrayListConstructNeighbor			<null,						constructNeighbor2, 		constructNeighbor3>
	 * 		arrayListConstructAllNeighbors		<constructAllNeighbors1,	constructAllNeighbor2, 		null>
	 * 		Delta								 WmodelDelta1,				WmodelDelta2, 				WmodelDelta3			 required
	 * 		arrayListCostWithNeighbor			 getCostWithNeighbor1		getCostWithNeighbor2		getCostWithNeighbor3	 required
	 * 		arrayListSolutionFromNeighbor		 getSolutionFromNeighbor1	getSolutionFromNeighbor2	getSolutionFromNeighbor3 required
	 */
	List<constructAllDeltasInterface> arrayListConstructAllNeighbors = new ArrayList<>();
}
