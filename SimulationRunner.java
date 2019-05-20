public class SimulationRunner {
	private SimulatedAnnealing annealingRunner;
	private GeneticAlgorithm geneticRunner;
	private int numberOfTests, n;
	private double numberOfSolvedForSimulatedAnnealing, numberOfSolvedForGeneticAlgorithm;
	private double iterationsForSimulatedAnnealing, iterationsForGeneticAlgorithm, runTimeForSimulatedAnnealing, runTimeForGeneticAlgorithm;
	private final double SECONDS_OVER_NANOSECONDS = 1_000_000_000;
	private final boolean PRINT_BOARD = true;
	
	public SimulationRunner(int numberOfTests, int maxNumberOfIterations, int n) {
		annealingRunner = new SimulatedAnnealing(n, maxNumberOfIterations);
		geneticRunner = new GeneticAlgorithm(n, maxNumberOfIterations);

		this.numberOfTests = numberOfTests;
		this.n = n;
		
		iterationsForGeneticAlgorithm = 0;
		iterationsForSimulatedAnnealing = 0;
		
		numberOfSolvedForSimulatedAnnealing = 0;
		numberOfSolvedForGeneticAlgorithm = 0;
		
		runTimeForGeneticAlgorithm = 0;
		runTimeForSimulatedAnnealing = 0;
	}
	
	public void runSimulatedAnnealing() {
		System.out.println("Running Simulated Annealing.");
		for(int iteration = 0; iteration < numberOfTests; ++iteration) {
			if(annealingRunner.solve(PRINT_BOARD)) {
				++numberOfSolvedForSimulatedAnnealing;
			}
			iterationsForSimulatedAnnealing += annealingRunner.getNumberOfIterations();
			runTimeForSimulatedAnnealing += annealingRunner.getRunTime();
			annealingRunner.reset();
		}
		System.out.println("Average number of iterations: " + (iterationsForSimulatedAnnealing/numberOfTests));
		System.out.println("Average run time: " + (runTimeForSimulatedAnnealing/numberOfTests/SECONDS_OVER_NANOSECONDS) + " seconds");
		System.out.println("Solve rate: " + (numberOfSolvedForSimulatedAnnealing/numberOfTests * 100) +"%");
		
	}
	
	public void runGeneticAlgorithm() {
		System.out.println("Running Genetic Algorithm.");
		for(int iteration = 0; iteration < numberOfTests; ++iteration) {
			if(geneticRunner.solve(PRINT_BOARD)) {
				++numberOfSolvedForGeneticAlgorithm;
			}
			iterationsForGeneticAlgorithm += geneticRunner.getNumberOfIterations();
			runTimeForGeneticAlgorithm += geneticRunner.getRunTime();
			geneticRunner.reset();
		}
		System.out.println("Average number of iterations: " + (iterationsForGeneticAlgorithm/numberOfTests));
		System.out.println("Average run time: " + (runTimeForGeneticAlgorithm/numberOfTests/SECONDS_OVER_NANOSECONDS) + " seconds");
		System.out.println("Solve rate: " + (numberOfSolvedForGeneticAlgorithm/numberOfTests * 100) +"%");
	}
}