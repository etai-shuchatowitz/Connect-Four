import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
	
	private char[][] board = new char[6][7];
	private static final int WEIGHT = 100;
	
	public HashMap<String, Integer> scores = new HashMap<String, Integer>();
	private HashMap<String, Character> letters;
	
	int boardScore = 0;
	
	public Board() throws IOException, InterruptedException {
		scores.put("human", 0);
		scores.put("computer", 0);
		letters = new HashMap<>();
		letters.put("human", 'H');
		letters.put("computer", 'C');
		initializeBoard();
		drawBoard();
	}
	
	// Initialize the board with * in every spot.
	private void initializeBoard() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				board[i][j] = '*';
			}
		}
	}
	
	public boolean putPiece(int y, int x) {
		board[y][x] = 'C';
		evaluateBoard();
		boardScore += 1;
		return true;
	}
	
	public boolean putPiece(String team, int x) {
		
		int y = findYSpot(x);
		
		if(y == -1) {
			return false;
		}
		
		else {
			if(team.equals("human")) {
				board[y][x] = 'H';
				boardScore -= 1;
			}
			else {
				board[y][x] = 'C';
				boardScore += 1;
			}
			evaluateBoard();
			
			return true;
		}
	}

	public boolean removePiece(Move m) {
		int x = m.xMove;
		int y = m.yMove;

		if(board[y][x] == '*') {
			return false;
		}
		
		else {
			board[y][x] = '*';
			evaluateBoard();
			return true;
		}
		
	}
	 
	 /* Find the spot to put the piece in a given column.
	 * @param y the column to search
	 * @return the spot to put the piece in. Return -1 if unavailable.
	 */

	public int findYSpot(int x) {
		
		for(int i = 5; i >= 0; i--) {
			if(board[i][x] == '*') {
				return i;
			}
		}
		return -1;
	}

	/*
	 * Draw the board
	 */
	public void drawBoard() throws IOException, InterruptedException {
		clear();
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 7; x++) {
				System.out.print(board[y][x]);
			}
			System.out.println("");
		}
	}

	/*
	 * Clear the screen
	 */
	private void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

	/* Check the sum of the diagonals
	 * @param team The team to evaluate
	 * @return the diagonal sum for the given team.
	*/ 
	private int forwardDiagonalSum(String team, String oppositeTeam) {
		char letter = letters.get(team);
		char oppositeLetter = letters.get(oppositeTeam);
		int number = 0;
		
		for(int row = 3; row < 6; row++) { //repeat this process for each row
			
			for(int column = 0; column < 4; column++) { //repeat for each column
				
				int counter = 0;
				char[] piece = new char[4];
				piece[0] = board[row][column];
				if(piece[0] == letter) {
					counter++;
				}
				
				for(int tally = 1; tally < 4; tally++) {
					
					if(row-1 < 0) { //if we exceed the board, stop!
						break;
					}
					else {
						piece[tally] = board[row-tally][column+tally]; 
						// if I found a piece of the right color
						if(piece[tally] == letter ) {
							if(counter == 0) {
								counter = 1;
							}
							// if it's consecutive
							if(tally-1 >= 0) {
								if(piece[tally-1] == letter) {
									counter++;
								}
							}
						}
						// If it's a blocked sequence, reset the counter
						else if(piece[tally] == oppositeLetter){
							if(counter > 1) {
								counter = 1;
							}
						}
					}
				}
				switch(counter) {
				case 1:
					number += WEIGHT*.01;
					break;
				case 2:
					number += WEIGHT *.1;
					break;
				case 3:
					number += 5*WEIGHT;
					break;
				case 4:
					number += WEIGHT*1000;
					break;
				}
			}
			
		}
		return number;
	}
	
	/* Check the sum of the diagonals
	 * @param team The team to evaluate
	 * @return the diagonal sum for the given team.
	*/ 
	private int backwardsDiagonalSum(String team, String oppositeTeam) {
		char letter = letters.get(team);
		char oppositeLetter = letters.get(oppositeTeam);
		int number = 0;
		
		for(int row = 3; row < 6; row++) { //repeat this process for each row
			
			for(int column = 6; column > 2; column--) { //repeat for each column
				
				int counter = 0;
				char[] piece = new char[4];
				piece[0] = board[row][column];
				if(piece[0] == letter) {
					counter++;
				}
				
				for(int tally = 1; tally < 4; tally++) {
					
					if(row-1 < 0) { //if we exceed the board, stop!
						break;
					}
					else {
						piece[tally] = board[row-tally][column-tally]; 
						// if i found a piece of the right color
						if(piece[tally] == letter ) {
							if(counter == 0) {
								counter = 1;
							}
							// if it's consecutive
							if(tally-1 >= 0) {
								if(piece[tally-1] == letter) {
									counter++;
								}
							}
						}
						else if(piece[tally] == oppositeLetter) {
							if(counter > 1) {
								counter = 1;
							}
						}
					}
				}
				switch(counter) {
				case 1:
					number += WEIGHT *.01;
					break;
				case 2:
					number += WEIGHT * .1;
					break;
				case 3:
					number += 5*WEIGHT;
					break;
				case 4:
					number += WEIGHT*1000;
					break;
				}
			}
			
		}
		return number;
	}

	/* Check the sum of the verticals
	 * @param team The team to evaluate
	 * @return the diagonal sum for the given team.
	 */
	private int verticalSum(String team, String oppositeTeam) {
		
		char letter = letters.get(team);
		char oppositeLetter = letters.get(oppositeTeam);
		int number = 0;
		
		for(int column = 0; column < 7; column++) { // repeat this process for each row
			
			for(int row = 0; row < 3; row++) { // go through each set of 4
				
				int counter = 0;
				char[] piece = new char[4];
				piece[0] = board[row][column];
				if(piece[0] == letter) {
					counter++;
				}
				
				for(int tally = row+1; tally < row+4; tally++) { // find each element
					
					piece[tally-row] = board[tally][column];
					if(piece[tally-row] == letter) { // if found appropriate team piece
						if(counter == 0) {
							counter = 1;
						}
						if(piece[tally-row-1] == letter) { // if consecutive
							counter++;
						}
					}
					else if(piece[tally-row] == oppositeLetter) {
						if(counter > 1) {
							counter = 1;
						}
					}
					
				}
				switch(counter) {
				case 1:
					number += WEIGHT *.01;
					break;
				case 2:
					number += WEIGHT * .1;
					break;
				case 3:
					number += 5*WEIGHT;
					break;
				case 4:
					number += WEIGHT*1000;
					break;
				}
				
			}
		}
		return number;
	}

	/* Check the sum of the horizontal
	 * @param team The team to evaluate
	 * @return the diagonal sum for the given team.
	 */
	private int horizontalSum(String team, String oppositeTeam) {
		
		char letter = letters.get(team);
		char oppositeLetter = letters.get(oppositeTeam);
		int number = 0;
		
		for(int row = 0; row < 6; row++) { // repeat this process for each row
			
			for(int column = 0; column < 4; column++) { // go through each set of 4
				
				int counter = 0;
				char[] piece = new char[4];
				piece[0] = board[row][column];
				if(piece[0] == letter) {
					counter++;
				}
				
				for(int tally = column+1; tally < column+4; tally++) { // find each element
					
					piece[tally-column] = board[row][tally];
					if(piece[tally-column] == letter) {
						if(counter == 0) {
							counter = 1;
						}
						// If consecutive, increase counter
						if(piece[tally-column-1] == letter) {
							counter++;
						}
					}
					else if(piece[tally-column] == oppositeLetter) {
						if(counter > 1) {
							counter = 1;
						}
					}
				}
				switch(counter) {
				case 1:
					number += WEIGHT *.01;
					break;
				case 2:
					number += WEIGHT * .1;
					break;
				case 3:
					number += 5*WEIGHT;
					break;
				case 4:
					number += WEIGHT*1000;
					break;
				}
			}
		}
		return number;
	}

	/* Evaluate the board and check what it's total is.
	 */
	private void evaluateBoard() {

		//Reset the scores to zero
		int compScore = 0;
		int humScore = 0;
		
		//evaluate each direction
		humScore += forwardDiagonalSum("human", "computer");
		humScore += backwardsDiagonalSum("human", "computer");
		humScore += verticalSum("human", "computer");
		humScore += horizontalSum("human", "computer");

		compScore += forwardDiagonalSum("computer", "human");
		compScore += backwardsDiagonalSum("computer", "human");
		compScore += verticalSum("computer", "human");
		compScore += horizontalSum("computer", "human");
		
		boardScore = compScore - humScore;
		scores.put("human", humScore);
		scores.put("computer", compScore);
	}
	/*
	Check to see if the game is over.
	A game over is determined as (a) Human wins (b) Computer wins (c) The board is full
	@return The status of the game
	 */
	public boolean gameOver() {
		if(boardFull()) {
			System.out.println("Tie game!");
			return true;
		}
		else {
			if(boardScore >= 50000) {
				System.out.println("COMPUTER WINS!");
				return true;
			}
			else if(boardScore <= -50000) {
				System.out.println("HUMAN WINS!");
				return true;
			}
			else {
				return false;
			}
		}
	}
	/*
	Check to see whether the board is full or not.
	@return The status of the board
	 */
	private boolean boardFull() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				if(board[i][j] == '*') {
					return false;
				}
			}
		}
		
		return true;
	}
		
		
}
