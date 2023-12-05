import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	
	private final int SQUARE_SIZE = 40;
	static RobotMaze eMaze;
	Canvas canvas;
	
	static ArrayList<RobotMaze> movesList;
	SearchMazePlayer sPlayer = new SearchMazePlayer();
	
    @Override
    public void start(Stage primaryStage) {
    	canvas = new Canvas(eMaze.getCols() * SQUARE_SIZE + (2 * eMaze.getCols()), eMaze.getRows() * SQUARE_SIZE + ( 2 * eMaze.getRows()));
    	drawGrid(canvas.getGraphicsContext2D());
    	
    	StackPane root = new StackPane(canvas);
    	Scene scene = new Scene(root);
    	
    	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5f), e -> updateMaze()));
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	timeline.play();
    	
    	primaryStage.setScene(scene);
    	primaryStage.show();
    }
    
    private void updateMaze() {
    	//RandomMazePlayer randPlayer = new RandomMazePlayer();
    	//eMaze = randPlayer.makePlayerMove(eMaze);
    	
    	//ManhattanMazePlayer manhattanPlayer = new ManhattanMazePlayer();
    	//eMaze = manhattanPlayer.makePlayerMove(eMaze);

    	eMaze = sPlayer.makePlayerMove(eMaze);
    	
    	drawGrid(canvas.getGraphicsContext2D());
    }

    private void drawGrid(GraphicsContext gc) {
    	for(int row = 0; row < eMaze.getRows(); row++) {
    		for(int col = 0; col < eMaze.getCols(); col++) {
    			Color color = eMaze.tileColorAt(row, col);
    			gc.setFill(color);
    			gc.fillRect(col * SQUARE_SIZE + 6, row * SQUARE_SIZE + 6, SQUARE_SIZE-2, SQUARE_SIZE-2);
    		}
    	}
    }
    
    public static void main(String[] args) {
		try {
			eMaze = new RobotMaze(3, 5);
			eMaze.loadBoard("RMazeTricky4by5.txt");

		} catch (LoobsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(eMaze);
        launch(args); // Launch the JavaFX application
    }
}
