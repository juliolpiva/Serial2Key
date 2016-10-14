package jlp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Stage stagePrimario;
	public static Scene mainScene;
	public static AnchorPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stagePrimario = primaryStage;
			root = FXMLLoader.load(getClass().getResource("fxml/Main_Pane.fxml"));
			mainScene = new Scene(root,400,300);
			
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Serial2Keyboard");
			primaryStage.getIcons().add(new Image(this.getClass().getResource("images/kboard.png").toString()));
			primaryStage.setMinWidth(400);
			primaryStage.setMinHeight(300);
			primaryStage.show();
			
			
			/*primaryStage.setOnCloseRequest(event -> {
			    System.exit(0);
			});*/
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Stage getstage(){
		return stagePrimario;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public Stage getprimary(){
		return stagePrimario;
	}	
	
	public void setTitle(String title){
		stagePrimario.setTitle(title);
	}
	
	public void setScene (Scene scene){
		stagePrimario.setScene(scene);
	}
	
	public void setprimaryScene (){
		stagePrimario.setScene(mainScene);
		stagePrimario.setTitle("Serial2Keyboard");
		stagePrimario.getIcons().add(new Image(this.getClass().getResource("image/terminal-icon.png").toString()));
	}

	public void setRoot (Parent root){
		Scene scene = new Scene(root,900,630);
		stagePrimario.setScene(scene);
	}
}

