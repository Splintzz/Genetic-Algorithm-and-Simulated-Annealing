public class State {
	private int[] queenPlacements;
	private int boardLength;
	
	public State(int n) {
		boardLength = n;
		queenPlacements = new int[n];
		generateRandomState();
	}
	
	public State(int[] board) {
		boardLength = board.length;
		queenPlacements = new int[boardLength];
		for(int row = 0; row < boardLength; ++row) {
			queenPlacements[row] = board[row];
		}
	}
	
	public State(State state) {
		boardLength = state.boardLength;
		queenPlacements = new int[boardLength];
		this.setQueenPlacements(state.queenPlacements);
	}
	
	/**
	 * Generates a random board state.
	 */
	public void generateRandomState() {
		for (int row = 0; row < boardLength; ++row) {
			queenPlacements[row] = (int) (Math.random() * boardLength);
		}
	}
	
	/**
	 * Makes a move on the board with simulated annealing. 
	 * @param precedentCost - the cost of the board currently.
	 * @param temperature - the temperature factor.
	 * @return a new state with a new board.
	 */
	public State makeMoveOnBoard(int precedentCost, double temperature) {
		while(true) {
			State newState = new State(this);
			
			int chosenRow = (int)(Math.random() * boardLength);
			int chosenColumn = (int)(Math.random() * boardLength);
			int previousRow = newState.getQueenPlacement(chosenColumn);
			//get criteria for making a new move
			
			newState.moveQueen(chosenRow, chosenColumn);
			int cost = newState.getCost();
			
			if(cost < precedentCost) {
				return newState;
			}//if the cost is better than the previous, return the state.
			
			int delta_E = precedentCost - cost;
	        double acceptedProbability = Math.exp(delta_E / temperature);
	
	        if (Math.random() < acceptedProbability) {
	            return newState;
	        }//simulated annealing aspect 
	
	        newState.moveQueen(previousRow, chosenColumn);
	        //no new state was returned, so put the queen back in its original spot
		}
	}
	
	
	/**
	 * Returns the value of a the row the queen is at in a column.
	 * @param column - the column of the desired row.
	 * @return the row at the specified column.
	 */
	public int getQueenPlacement(int column) {
		return queenPlacements[column];
	}
	
	/**
	 * Calculates the cost of a state, based on the numbers of misplaced queens.
	 * @return the cost of the state.
	 */
	public int getCost() {
		int cost = 0;
		
		for(int row = 0; row < boardLength; ++row) {
			for(int column = row + 1; column < boardLength; ++column) {
				boolean sameRow = queenPlacements[row] == queenPlacements[column];
				boolean sameDiagonal = (Math.abs(queenPlacements[row] - queenPlacements[column])) == (column - row);
				if (sameRow || sameDiagonal) {
					++cost;
				}
			}
		}
		return cost;
	}
	
	/**
	 * Calculates the fitness of the node.
	 * @return the fitness value of the node.
	 */
	public int getFitness() {
		return getHighestFitness(boardLength) - getCost();
	}
	
	/**
	 * Calculates the highest fitness a node can have.
	 * @param n - the size of the board in the state.
	 * @return the highest achievable fitness
	 */
    public int getHighestFitness(int n) {
        return n*(n-1)/2;
    }
    
    /**
     * Determines if a state has poor fitness.
     * @return true if the node is below half of the highest fitness level.
     */
    public boolean isSlow() {
		return (getFitness()) < (getHighestFitness(boardLength)/2);
	}
	
	/**
	 * Puts a queen on a new square.
	 * @param newRow - the new row the queen will be on.
	 * @param newColumn - the new column the queen will be on.
	 */
	public void moveQueen(int newRow, int newColumn) {
		queenPlacements[newColumn] = newRow;
	}
	
	/**
	 * Gets the board length.
	 * @return the length of the board.
	 */
	public int getBoardLength() {
		return queenPlacements.length;
	}
	
	/**
	 * Changes the board of the state to a new board.
	 * @param newBoard - the board the queenPlacements should conform to.
	 */
	public void setQueenPlacements(int[] newBoard) {
		int n = newBoard.length;
		for(int row = 0; row < n; ++row) {
			queenPlacements[row] = newBoard[row];
		}
	}
	
	/**
	 * Prints out the board in a chess board like format.
	 */
	public void print() {
		String[][] board = new String[boardLength][boardLength];
		final String QUEEN = "Q";
		
		for(int row = 0; row < boardLength; ++row) {
			board[queenPlacements[row]][row] = QUEEN;
		}//convert one dimensional array to two dimensional array
		
		for(int row = boardLength-1; row >-1; --row) {
			for(int column = 0; column < boardLength; ++column) {
				if(board[row][column] == null) {
					System.out.print(" |");
				}else {
					System.out.print(board[row][column]+"|");
				}
			}
			System.out.println();
			
		}//print out two dimensional array with queen placements
	}
}