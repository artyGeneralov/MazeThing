import java.util.ArrayList;
import java.util.Random;

public class RandomMazePlayer implements MazePlayer {

	@Override
	public RobotMaze makePlayerMove(RobotMaze m) {
		// get possible moves
		ArrayList<RobotMaze> possibleMoves = m.findAllMoves();
		// choose a random move
		Random rnd = new Random();
		int move = rnd.nextInt(possibleMoves.size());
		// make random move
		
		return possibleMoves.get(move);
	}
}
