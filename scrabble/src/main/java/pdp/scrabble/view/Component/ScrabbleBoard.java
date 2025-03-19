package pdp.scrabble.view.Component;

import static java.lang.Integer.min;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ScrabbleBoard extends Canvas {
    private final int CELL_SIZE;
    private final int BOARD_SIZE;

    private int width;
    private int height;
    
    private GraphicsContext gc;
    private Canvas canvas;

    public ScrabbleBoard(int width, int height, int size) {
        this.width = width;
        this.height = height;

        

        BOARD_SIZE = size;
        CELL_SIZE = (min(width, height)/2) / BOARD_SIZE;

        gc = this.getGraphicsContext2D();

        this.setWidth(width);
        this.setHeight(height);
        drawBoard();
    }
    
    public void drawBoard() {
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (int i = 0; i <= BOARD_SIZE; i++) {
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
            gc.strokeLine(0, i * CELL_SIZE, BOARD_SIZE * CELL_SIZE, i * CELL_SIZE);
        }
    }
}
