

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class SNLP extends Application
{
	public static ArrayList<String> inputs = new ArrayList<String>();
	public static Scene superScene;

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("GameGUI.fxml"));
			superScene = new Scene(root,1000,600);
//			superScene.setRoot(root);
//			primaryStage.setFullScreen(true);
//			primaryStage.setFullScreenExitHint("");
			superScene.getStylesheets().add(getClass().getResource("GameGUI.css").toExternalForm());
			primaryStage.setTitle("Space Ninja Lazer Pirates");
			primaryStage.setScene(superScene);
			superScene.setOnKeyPressed(new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent key)
				{
					String code = key.getCode().toString();
					if(!inputs.contains(code))
					{
						inputs.add(code);
					}
				}
			});

			
			superScene.setOnKeyReleased(new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent key)
				{
					String code = key.getCode().toString();
					inputs.remove(code);
				}
			});
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
