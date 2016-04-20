import java.io.IOException;
import java.util.Scanner;

public class Game {
	
	private final String HUMAN = "human";
	private final String COMPUTER = "computer";
	private Board b;
	private Scanner reader = new Scanner(System.in);  // Reading from System.in
	
	public Game() throws InterruptedException, IOException {
		b = new Board();
		while(!b.gameOver()) {
			personMove(HUMAN);
			System.out.println("Human Score: " +b.scores.get(HUMAN) + "Computer Score " + b.scores.get(COMPUTER));
			System.out.println("Boardscore: " + b.boardScore);
			if(b.gameOver()) {
				break;
			}
			else {
				computerMove();
			}
		}
		
		reader.close();
	}
		
	private void personMove(String team) throws IOException, InterruptedException {
		int move = 0;
		while(!(move > 0 && move < 8)) { // read in the person's move
			System.out.println("Human enter a number: ");
			move = reader.nextInt(); // Scans the next token of the input as an int.
		}
		move--;
		
		while(!b.putPiece(team, move)) {
			System.out.println("Move unavailable");
		}
		b.drawBoard();
	}

	private void computerMove() throws IOException, InterruptedException {
		Board tempBoard = b;
		AI ai = new AI();
		Move tempMove = new Move(0, 0);
		Move bestMove = ai.minimax(tempBoard, 8, -1000000000, 1000000000, COMPUTER, tempMove);
		b.putPiece(bestMove.yMove, bestMove.xMove);
		b.drawBoard();
	}
}
