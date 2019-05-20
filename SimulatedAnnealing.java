public class SimulatedAnnealing {
	private State state;
	private double runTime;
	private double temperature;
	private double coolingFactor;
	private int n;
	private int numberOfIterations;
	private int maxNumberOfIterations;
	private final int DESIRED_COST = 0;
	
	
	public SimulatedAnnealing(int n, int maxNumberOfIterations) {
		this.n = n;
		this.maxNumberOfIterations = maxNumberOfIterations;
		state = new State(n);
		temperature = 100;
		coolingFactor = .95;
		//values were decided after testing
	}
	
	/**
	 * Solves the N-Queens problem with simulated annealing.
	 * @param printBoard - decide if the board should be printed at the end.
	 * @return true if the board was solved, false otherwise.
	 */
	public boolean solve(boolean printBoard) {
		boolean puzzleHasNotBeenSolved, iterationLimitHasNotBeenReached;
        int precedentCost = state.getCost();
        int iteration = 0;
        puzzleHasNotBeenSolved = precedentCost != DESIRED_COST;
        iterationLimitHasNotBeenReached = iteration < maxNumberOfIterations;

        long startTime = System.nanoTime();
        while(iterationLimitHasNotBeenReached && puzzleHasNotBeenSolved) {
        	state = new State(state.makeMoveOnBoard(precedentCost, temperature));
        	temperature *= coolingFactor;
            precedentCost = state.getCost();
            ++iteration;
            
        	iterationLimitHasNotBeenReached = iteration < maxNumberOfIterations;
        	puzzleHasNotBeenSolved = precedentCost != DESIRED_COST;
        }//perform the simulated annealing algorithm
        long endTime = System.nanoTime();
        
        if(printBoard) {
	        if(puzzleHasNotBeenSolved) {
	        	System.out.println("No solution");
	        }else {
	        	state.print();
	        	System.out.println();
	        }
        }//if it was requested, print the board
        numberOfIterations = iteration;
        runTime = endTime - startTime;
        
        return (!puzzleHasNotBeenSolved);
	}
	
	/**
	 * Gets the number of iterations of the simulator.
	 * @return the number of iterations the simulator stepped through.
	 */
	public int getNumberOfIterations() {
		return numberOfIterations;
	}
	
	/**
	 * Gets the run time of the simulation.
	 * @return total time to run the simulator.
	 */
	public double getRunTime() {
		return runTime;
	}
	
	/**
	 * Resets the simulated annealing simulator.
	 */
	public void reset() {
		state = new State(n);
		numberOfIterations = 0;
		runTime = 0;
	}
}