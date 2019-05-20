import java.util.Scanner;
public class NQueens {
	public static void main(String[] args) {
		SimulationRunner runner;
		Scanner inputReader = new Scanner(System.in);
		final int SIMULATED_ANNEALING_CHOICE = 1;
		int userChoice, numberOfTests, maxNumberOfIterations, n;
		
		System.out.println("Pick an algorithm to run: ");
		System.out.println("1) Simulated Annealing");
		System.out.println("2) Genetic Algorithm");
		System.out.print("Enter a number: ");
		userChoice = inputReader.nextInt();

		System.out.println("How many queens?");
		System.out.print("Enter a number: ");
		n = inputReader.nextInt();

		System.out.println("How many tests?");
		System.out.print("Enter a number: ");
		numberOfTests = inputReader.nextInt();

		System.out.println("How many iterations?");
		System.out.print("Enter a number: ");
		maxNumberOfIterations = inputReader.nextInt();

		runner = new SimulationRunner(numberOfTests, maxNumberOfIterations, n);
		
		if(userChoice == SIMULATED_ANNEALING_CHOICE) {
			runner.runSimulatedAnnealing();
		}else {
			runner.runGeneticAlgorithm();
		}
		
		inputReader.close();
	}
	
}