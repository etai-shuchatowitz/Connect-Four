import java.io.IOException;

public class AI {
	
	private static final int WEIGHT = 100;
  
	public Move minimax(Board b, int depth, int alpha, int beta, String player, Move move) throws IOException, InterruptedException {

		// Base case
		if(depth == 0 || b.boardScore >= 50000) {
			return move;
		}
		
		else if(player.equals("computer")) {
			
			Move maxMove = new Move(0,0);
			maxMove.moveScore = WEIGHT*-10000;
			Move returnMove = new Move(0,0);
			
			//Do this for every possible column
			for(int x = 0; x < 7; x++) {
				
				//If column is filled, move on.
				while(b.findYSpot(x) == -1) {
					x++;
					if(x > 6) {
						x = 0;
					}
				}
				
				Move m = new Move(b.findYSpot(x), x); // Create the move
				m.moveScore = b.boardScore;
				b.putPiece(m.yMove, m.xMove); // Put the piece down

				Move miniMaxMove = minimax(b, depth-1, alpha, beta, "human", m);

				
				if(miniMaxMove.moveScore >= maxMove.moveScore) {
					maxMove = miniMaxMove;
					returnMove = m;
				}
				b.removePiece(m); //Remove the piece
				
				//Adjust for alpha-beta pruning
				alpha = Math.max(alpha, maxMove.moveScore);
				if (alpha >= beta) {
					break;
				}
				
			}			
			return returnMove;
		}
		
		else {
			Move minMove = new Move(0, 0);
			minMove.moveScore = WEIGHT*10000;

			Move returnMove = new Move(0,0);
			
			//Do this for every possible column
			for(int x = 0; x < 7; x++) {
				
				// If column is filled, move on
				while(b.findYSpot(x) == -1) {
					x++;
					if(x>6) {
						x = 0;
					}
				}

				Move m = new Move(b.findYSpot(x), x); // Make the move

				b.putPiece(player, m.xMove); // Put the piece down
				m.moveScore = b.boardScore;
				Move miniMaxMove = minimax(b, depth-1, alpha, beta, "computer", m);
				
				if(miniMaxMove.moveScore <= minMove.moveScore) {
					minMove = miniMaxMove;
					returnMove = m;
				}
				b.removePiece(m);
				
				//Adjust for alpha-beta pruning
				beta = Math.min(beta, minMove.moveScore);

				if(alpha >= beta) {
					break;
				}
				
			}
			return returnMove;
		}
	}
}

