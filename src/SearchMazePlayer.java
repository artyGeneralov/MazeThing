import java.util.ArrayList;

public class SearchMazePlayer implements MazePlayer {

	ArrayList<RobotMaze> movesToFinish = new ArrayList<RobotMaze>();
	ArrayList<RobotMaze> visitedSquares = new ArrayList<RobotMaze>();
	boolean foundPath = false;

	@Override
	public RobotMaze makePlayerMove(RobotMaze m) {

		if (movesToFinish.isEmpty() && !m.locations.robotLocation.equals(m.locations.targetLocation)) {
			// fill the moves list
			createListOfMoves(m);
			System.out.println("here");
		} else if (m.locations.robotLocation.equals(m.locations.targetLocation)) {

			return m;
		}
		System.out.println(" " + m.locations.robotLocation + " " + m.locations.targetLocation);
		RobotMaze nextMove = movesToFinish.get(0);
		movesToFinish.remove(0);
		return nextMove;
	}

	// Fills out movesToFinish
	public void createListOfMoves(RobotMaze m) {

		if(foundPath == true) return;
		// check that this spot was not yet visited
		for (RobotMaze maze : visitedSquares) {
			if (maze.equals(m)) {
				movesToFinish.remove(m);
				return;
			}
		}

		// add it to visited
		visitedSquares.add(m);

		// if we are on the target, return.
		if (m.locations.robotLocation.equals(m.locations.targetLocation)) {
			foundPath = true;
			return;
		}

		// make a list of all the possible moves from current location
		ArrayList<RobotMaze> possibleMoves = m.findAllMoves();

		// for each possible move:
		// 1. make the move
		// 2. add the move to the moves list
		// 3. if we are on a square that has already been visited, remove move from
		// moves list, return
		// 4. if we are on the target, return
		// 5. if we are on an empty unvisited spot, recurse.

		for (RobotMaze move : possibleMoves) {
			movesToFinish.add(move);
			createListOfMoves(move);
			if(!foundPath)
				movesToFinish.remove(move);
		}
	}

}
