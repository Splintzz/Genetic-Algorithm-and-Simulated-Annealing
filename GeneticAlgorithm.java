import java.util.Arrays;
import java.util.Comparator;
public class GeneticAlgorithm {
	private State[] population;
	private int n;
	private int numberOfIterations;
	private double runTime;
	private int highestFitness;
	private int maxNumberOfIterations;
	private final int POPULATION_SIZE = 20;
	private final double CHANCE_FOR_MUTATION = 0.4;
	
	
	public GeneticAlgorithm(int n, int maxNumberOfIterations) {
		numberOfIterations = 0;
		runTime = 0.0;
		this.n = n;
		this.maxNumberOfIterations = maxNumberOfIterations;
		highestFitness = n*(n-1)/2;
		population = new State[POPULATION_SIZE];
		getStartingPopulation();	
	}
	
	/**
	 * Performs the genetic algorithm.
	 * @param printBoard - decide whether to print the board or not.
	 * @return true if the board was solved, false otherwise.
	 */
	public boolean solve(boolean printBoard) {
		boolean puzzleHasNotBeenSolved, maxIterationsHasNotBeenReached;
		int iteration = 0;
		int fittestMember = POPULATION_SIZE - 1;	//population is sorted in ascending order so the most fit member will always be last
		long startTime, endTime = 0;
		puzzleHasNotBeenSolved = !maxFitnessReached();
		maxIterationsHasNotBeenReached = iteration < maxNumberOfIterations;
	
		startTime = System.nanoTime();
		while(puzzleHasNotBeenSolved && maxIterationsHasNotBeenReached) {
			population = reproduceNewGeneration();
			if(maxFitnessReached()) {
				puzzleHasNotBeenSolved = false;
			}
			
			++iteration;
			maxIterationsHasNotBeenReached = (iteration < maxNumberOfIterations);
		}
		endTime = System.nanoTime();
		if(printBoard) {
	        if(puzzleHasNotBeenSolved) {
	        	System.out.println("No solution");
	        }else {
	        	population[fittestMember].print();
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
	 * Resets the genetic algorithm simulator.
	 */
	public void reset() {
		getStartingPopulation();
		numberOfIterations = 0;
		runTime = 0.0;
	}
	
	/**
	 * Gets the initial population, that is decided randomly.
	 */
	private void getStartingPopulation() {
		for(int populationMember = 0; populationMember < POPULATION_SIZE; ++populationMember) {
			population[populationMember] = new State(n);
		}
	}
	
	/**
	 * Gets the new population generated from the previous populations' genetics.
	 * @return the new population.
	 */
	private State[] reproduceNewGeneration() {
		final int NUMBER_OF_OFFSPRING = POPULATION_SIZE + POPULATION_SIZE;
		State[] newPopulation = new State[NUMBER_OF_OFFSPRING];
		State[] firstHalf = getFirstHalfOfNewPopulation(population);
		State[] secondHalf = getSecondHalfOfNewPopulation(population);
		int acceptedOffspring = 0;
		
		for(int offspring = 0; offspring < POPULATION_SIZE; ++offspring) {
			newPopulation[acceptedOffspring] = firstHalf[offspring];
			++acceptedOffspring;
		}
		
		for(int offspring = 0; offspring < POPULATION_SIZE; ++offspring) {
			newPopulation[acceptedOffspring] = secondHalf[offspring];
			++acceptedOffspring;
		}
		
		Arrays.sort(newPopulation, Comparator.comparingInt(State::getFitness));
		//sort population by fitness in ascending order
		newPopulation = killOffWeak(newPopulation);
		return newPopulation;
	}
	
	/**
	 * Generates half of the new population that will be produced. This 
	 * half is based on selection, and the states with similar fitness 
	 * mating with each other.
	 * @param population - current population.
	 * @return a new population that will be half of the new population.
	 */
	private State[] getFirstHalfOfNewPopulation(State[] population) {
		State[] newPopulation = new State[POPULATION_SIZE];
		boolean inReproductionSeason = true;
		boolean shouldMutate;
		int parentOne = 0;
		int parentTwo = 1;
		
		while(inReproductionSeason) {
			State firstChild = copulate(population[parentOne], population[parentTwo]);
			State secondChild = copulate(population[parentOne], population[parentTwo]);
			//each parent produces two kids
			
			boolean firstChildSlow = firstChild.isSlow();
			boolean secondChildSlow = secondChild.isSlow();
			
			shouldMutate = (firstChildSlow || secondChildSlow) ? (CHANCE_FOR_MUTATION > (Math.random()/2)) : (CHANCE_FOR_MUTATION > Math.random());
			//if the child has poor fitness, the mutation probability is higher
			
			if(shouldMutate) {
				firstChild = mutate(firstChild, firstChildSlow);
				secondChild = mutate(secondChild, secondChildSlow);
			}//mutation based on probability, as well as if a child has a poor fitness
			
			newPopulation[parentOne] = firstChild;
			newPopulation[parentTwo] = secondChild;
			//add the newly generated children to the new population
			
			++parentOne;
			++parentTwo;
			//parents are incremented together to mate with similar fitnesses
			inReproductionSeason = (parentTwo < POPULATION_SIZE);
		}
		
		return newPopulation;
	}
	
	/**
	 * Generates half of the new population that will be produce. This
	 * half focuses on variation, and random states mating with random states.
	 * @param population - current population.
	 * @return a new population that will be half of the new population.
	 */
	private State[] getSecondHalfOfNewPopulation(State[] population) {
		State[] newPopulation = new State[POPULATION_SIZE];
		boolean inReproductionSeason = true;
		boolean shouldMutate;
		int parentOne = (int)(Math.random()*POPULATION_SIZE);
		int parentTwo = (int)(Math.random()*POPULATION_SIZE);
		int iteration = 0;
		
		while(inReproductionSeason) {
			State firstChild = copulate(population[parentOne], population[parentTwo]);
			State secondChild = copulate(population[parentOne], population[parentTwo]);
			//each parent produces two kids
			
			boolean firstChildSlow = firstChild.isSlow();
			boolean secondChildSlow = secondChild.isSlow();
			
			shouldMutate = (firstChildSlow || secondChildSlow) ? (CHANCE_FOR_MUTATION > (Math.random()/2)) : (CHANCE_FOR_MUTATION > Math.random());
			//if the child has poor fitness, the mutation probability is higher
			
			if(shouldMutate) {
				firstChild = mutate(firstChild, firstChildSlow);
				secondChild = mutate(secondChild, secondChildSlow);
			}//mutation based on probability, as well as if a child has a poor fitness
			
			newPopulation[iteration] = firstChild;
			++iteration;
			newPopulation[iteration] = secondChild;
			++iteration;
			//add the newly generated children to the new population
			
			parentOne = (int)(Math.random()*(POPULATION_SIZE));
			parentTwo = (int)(Math.random()*(POPULATION_SIZE));
			//next parents are radomly decided
			inReproductionSeason = (iteration < POPULATION_SIZE);
		}
		
		return newPopulation;
	}
	
	/**
	 * Gets rid of the least fit states in the population.
	 * @param population - the current population.
	 * @return the population with the best fit states.
	 */
	private State[] killOffWeak(State[] population) {
		State[] strongerPopulation = new State[POPULATION_SIZE];
		for(int weakOffspring = population.length-1, strongOffspring = 0; strongOffspring < POPULATION_SIZE; --weakOffspring, ++strongOffspring) {
			strongerPopulation[strongOffspring] = population[weakOffspring];
		}
		return strongerPopulation;
	}
	
	/**
	 * Checks to see if the maximum fitness has been reached.
	 * @return true if the board has been solved.
	 */
	private boolean maxFitnessReached() {
		final int fittestMember = POPULATION_SIZE - 1;
		return (population[fittestMember].getFitness() == highestFitness);
		//only need to check highest fitness level, which is last.
	}
	
	/**
	 * Changes a childs' genetics slightly.
	 * @param child - the child being mutated.
	 * @return the mutated child.
	 */
	private State mutate(State child, boolean isSlow) {
		final int mutationsForSlowChild = 3;
		final int mutationsForRegularChild = 1;
		int numberOfMutationsToPerform = (isSlow) ? mutationsForSlowChild : mutationsForRegularChild;
		
		for(int numberOfMutations = 0; numberOfMutations < numberOfMutationsToPerform; ++numberOfMutations) {
			int positionToMutate = (int)(Math.random()*n);
			int mutation = (int)(Math.random()*n);
			
			child.moveQueen(mutation, positionToMutate);
		}//children with less fitness will receive more mutations
		
		return child;
	}
	
	/**
	 * Creates a new child based on two parents genetics.
	 * @param parentOne - the parent where the first half of the childs' genetics will come from.
	 * @param parentTwo - the parent where the second half of the childs' genetics will come from.
	 * @return the newly born child.
	 */
	private State copulate(State parentOne, State parentTwo) {
		int[] childsGenetics = new int[n];
		int trait = 0;
		int geneticsFromParentOne = (int)(Math.random()*n);
		int geneticsFromParentTwo = n - geneticsFromParentOne;
		//get the cutoff point for inheritance
		
		for(int firstPortionOfGenetics = 0; firstPortionOfGenetics < geneticsFromParentOne; ++firstPortionOfGenetics, ++trait) {
			childsGenetics[trait] = parentOne.getQueenPlacement(firstPortionOfGenetics);
		}//transfer parent one genetics to child
		
		for(int secondPortionOfGenetics = 0; secondPortionOfGenetics < geneticsFromParentTwo; ++secondPortionOfGenetics, ++trait) {
			childsGenetics[trait] = parentTwo.getQueenPlacement(trait);
		}//transfer parent two genetics to child
		
		State loveChild = new State(childsGenetics);
		return loveChild;
	}
}