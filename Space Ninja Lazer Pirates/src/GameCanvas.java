

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameCanvas extends Canvas
{
	private GraphicsContext gc;
	private GameLoop gameLoop;

	public GameCanvas(int x, int y)
	{
		gc = this.getGraphicsContext2D();
		this.setHeight(y);
		this.setWidth(x);
		init();
	}

	private void init()
	{
		gc = this.getGraphicsContext2D();
	    gameLoop = new GameLoop(this);
	    gameLoop.start();
	}

}
