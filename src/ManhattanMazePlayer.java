import java.util.ArrayList;

public class ManhattanMazePlayer implements MazePlayer {

	@Override
	public RobotMaze makePlayerMove(RobotMaze m) {
		ArrayList<RobotMaze> possibleMoves = m.findAllMoves();
		RobotMaze minimalMaze = null;
		int minimalDistance = Integer.MAX_VALUE;
		for(RobotMaze maze : possibleMoves) {
			int distance = maze.manhattanDistance();
			if(distance < minimalDistance) {
				minimalDistance = distance;
				minimalMaze = maze;
			}
		}
		return minimalMaze;
	}
}
