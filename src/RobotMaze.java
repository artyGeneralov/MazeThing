import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import utils.Vector2;

public class RobotMaze {
	private int rows, cols;

	public enum MazeTiles {
		E(Color.YELLOW), // empty
		X(Color.BLACK), // wall
		R(Color.RED), // player
		G(Color.MAGENTA); // target

		private final Color color;

		MazeTiles(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return this.color;
		}
	}

	public enum MazeMoves {
		Up, Down, Left, Right
	}

	MazeTiles[][] board;

	class Location {
		Vector2 robotLocation;
		Vector2 targetLocation;

		Location(Vector2 robotLocation, Vector2 targetLocation) throws LoobsException {
			if (!locInBounds(robotLocation))
				throw new LoobsException("Location " + robotLocation.toString() + " Out Of Bounds For Robot!");
			if (!locInBounds(targetLocation))
				throw new LoobsException("Location " + targetLocation.toString() + " Out Of Bounds For Target!");
			this.robotLocation = new Vector2(robotLocation);
			this.targetLocation = new Vector2(targetLocation);
		}

		Location(Location otherLocation) throws LoobsException {
			this(otherLocation.robotLocation, otherLocation.targetLocation);
		}

		boolean locInBounds(Vector2 loc) {
			boolean locInBounds = true;
			if (loc.getX() > rows)
				locInBounds = false;
			if (loc.getX() < 0)
				locInBounds = false;
			if (loc.getY() > cols)
				locInBounds = false;
			if (loc.getY() < 0)
				locInBounds = false;
			return locInBounds;
		}

		Vector2 getTargetLocation() {
			return this.targetLocation;
		}

		Vector2 getRobotLocation() {
			return this.robotLocation;
		}

		boolean isOnTarget() {
			return robotLocation.equals(targetLocation);
		}
	}

	Location locations;

	// Constructors:
	public RobotMaze(int rows, int cols) throws LoobsException {
		if (rows < 0 || cols < 0)
			throw new LoobsException("rows and cols cannot be < 0!");
		this.rows = rows;
		this.cols = cols;
		board = new MazeTiles[rows][cols];
		fillEmptyMaze();
	}

	// Copy
	public RobotMaze(RobotMaze maze) throws LoobsException {
		if (rows < 0 || cols < 0)
			throw new LoobsException("rows and cols cannot be < 0!");
		this.rows = maze.rows;
		this.cols = maze.cols;
		board = new MazeTiles[rows][cols];
		try {
			this.locations = new Location(maze.locations);
		} catch (LoobsException e) {
			System.err.println(e.toString() + " In RobotMaze CopyConstructor");
		}
		fillMazeByGrid(maze.board);
	}

	// Public methods
	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public Color tileColorAt(int row, int col) {
		return board[row][col].getColor();
	}

	public void loadBoard(String filePath) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = br.readLine();
			String[] splitLine = line.split(" ");
			int rows = Integer.parseInt(splitLine[0]);
			int cols = Integer.parseInt(splitLine[1]);

			MazeTiles[][] tBoard = new MazeTiles[rows][cols];
			for (int i = 0; i < rows; i++) {
				line = br.readLine();
				splitLine = line.split(" ");
				for (int j = 0; j < cols; j++) {
					tBoard[i][j] = MazeTiles.valueOf(splitLine[j]);
				}
			}

			if (checkMazeCorrect(tBoard)) {
				this.board = tBoard;
				this.rows = rows;
				this.cols = cols;
			} else {
				throw new IOException("Invalid input of maze in the file");
			}
			calibrateLocations();
		}
	}

	public RobotMaze makeMove(RobotMaze maze, MazeMoves move) throws LoobsException {
		RobotMaze newMaze = new RobotMaze(maze);
		int curRow = maze.locations.getRobotLocation().getX();
		int curCol = maze.locations.getRobotLocation().getY();
		switch (move) {
		case Up:
			if (curRow == 0 || maze.board[curRow - 1][curCol] == MazeTiles.X)
				throw new LoobsException("Cannot make move, out of bounds");
			else {
				newMaze.board[curRow - 1][curCol] = MazeTiles.R;
				newMaze.board[curRow][curCol] = MazeTiles.E;
				newMaze.locations.robotLocation.moveUp();
			}
			break;
		case Down:
			if (curRow == maze.rows - 1 || maze.board[curRow + 1][curCol] == MazeTiles.X)
				throw new LoobsException("Cannot make move, out of bounds");
			else {
				newMaze.board[curRow + 1][curCol] = MazeTiles.R;
				newMaze.board[curRow][curCol] = MazeTiles.E;
				newMaze.locations.robotLocation.moveDown();
			}
			break;
		case Left:
			if (curCol == 0 || maze.board[curRow][curCol - 1] == MazeTiles.X)
				throw new LoobsException("Cannot make move, out of bounds");
			else {
				newMaze.board[curRow][curCol - 1] = MazeTiles.R;
				newMaze.board[curRow][curCol] = MazeTiles.E;
				newMaze.locations.robotLocation.moveLeft();
			}
			break;
		case Right:
			if (curCol == maze.cols - 1 || maze.board[curRow][curCol + 1] == MazeTiles.X)
				throw new LoobsException("Cannot make move, out of bounds");
			else {
				newMaze.board[curRow][curCol + 1] = MazeTiles.R;
				newMaze.board[curRow][curCol] = MazeTiles.E;
				newMaze.locations.robotLocation.moveRight();
			}
			break;
		default:
			break;
		}
		return newMaze;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(rows);
		sb.append(" " + cols);
		sb.append('\n');
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (j == cols - 1)
					sb.append(board[i][j].name());
				else
					sb.append(board[i][j].name() + " ");
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public ArrayList<RobotMaze> findAllMoves() {
		ArrayList<RobotMaze> movesList = new ArrayList<RobotMaze>();
		for (int i = 0; i < 4; i++) {
			addMoveIfPossible(movesList, MazeMoves.Up);
			addMoveIfPossible(movesList, MazeMoves.Down);
			addMoveIfPossible(movesList, MazeMoves.Left);
			addMoveIfPossible(movesList, MazeMoves.Right);
		}
		return movesList;
	}

	public int manhattanDistance() {
		int distance = Math.abs(locations.robotLocation.getX() - locations.targetLocation.getX())
				+ Math.abs(locations.robotLocation.getY() - locations.targetLocation.getY());
		return distance;
	}

	public boolean equals(RobotMaze other) {
		boolean equals = true;

		// check if size is the same
		if (this.rows != other.rows || this.cols != other.cols)
			equals = false;
		// check if board is the same
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (this.board[i][j] != other.board[i][j])
					equals = false;
			}
		}

		// check if location of r and t is the same
		if (!this.locations.robotLocation.equals(other.locations.robotLocation)
				|| !this.locations.targetLocation.equals(other.locations.targetLocation)) {
			equals = false;
		}

		return equals;
	}

	// Private methods

	private void addMoveIfPossible(ArrayList<RobotMaze> list, MazeMoves move) {
		try {
			RobotMaze possibleMove = makeMove(this, move);
			if (possibleMove != null)
				list.add(possibleMove);
		} catch (LoobsException e) {
			System.err.println(e.toString() + " In addMoveIfPossible, move impossible.");
		}
	}

	private void fillEmptyMaze() {
		board[0][0] = MazeTiles.R;

		for (int i = 0; i < rows; i++) {
			for (int j = 1; j < cols; j++) {
				if (i == rows - 1 && j == cols - 1) {
					board[i][j] = MazeTiles.G;
				}
				board[i][j] = MazeTiles.E;
			}
		}
		try {
			this.locations = new Location(new Vector2(0, 0), new Vector2(rows - 1, cols - 1));
		} catch (LoobsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Location?
	}

	// Check Maze Correctness
	private boolean checkMazeCorrect(MazeTiles[][] mazeBoard) {
		boolean isCorrect = true;
		int countR = 0, countG = 0;
		for (int i = 0; i < mazeBoard.length; i++) {
			for (int j = 0; j < mazeBoard[0].length; j++) {
				if (mazeBoard[i][j] == MazeTiles.G)
					countG++;
				if (mazeBoard[i][j] == MazeTiles.R)
					countR++;
				if (countG > 1 || countR > 1) {
					isCorrect = false;
					break;
				}
			}
		}
		return isCorrect;
	}

	private void calibrateLocations() {
		Vector2 rLoc = new Vector2(0, 0), tLoc = new Vector2(0, 0);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (board[i][j] == MazeTiles.R)
					rLoc = new Vector2(i, j);
				else if (board[i][j] == MazeTiles.G)
					tLoc = new Vector2(i, j);
			}
		}

		try {
			this.locations = new Location(rLoc, tLoc);
		} catch (LoobsException e) {

		}
	}

	private void fillMazeByGrid(MazeTiles[][] otherBoard) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				board[i][j] = otherBoard[i][j];
			}
		}
	}
}
