

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class GUIController
{
	@FXML BorderPane root;
	@FXML BorderPane centerPane;
	GameCanvas canvas = new GameCanvas(1000,600);

	public GUIController()
	{
		System.out.println("controller");
	}


	public void initialize()
	{
		centerPane.setCenter(canvas);
	}

}
