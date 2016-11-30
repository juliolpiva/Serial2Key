package jlp;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	public static Stage stagePrimario;
	public static Scene mainScene;
	public static AnchorPane root;
	
	private TrayIcon trayIcon;
	private boolean firstTime;
	  
	@Override
	public void start(Stage primaryStage) {
		try {
			stagePrimario = primaryStage;
			createTrayIcon(stagePrimario);
			firstTime = true;
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
	
	  public void createTrayIcon(final Stage stage) {
	        if (SystemTray.isSupported()) {
	            // get the SystemTray instance
	            SystemTray tray = SystemTray.getSystemTray();
	            // load an image
	            java.awt.Image image = null;
	            try {
	                URL url = new URL("http://www.digitalphotoartistry.com/rose1.jpg");
	                image = ImageIO.read(url);
	            } catch (IOException ex) {
	                System.out.println(ex);
	            }


	            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	                @Override
	                public void handle(WindowEvent t) {
	                    hide(stage);
	                }
	            });
	            // create a action listener to listen for default action executed on the tray icon
	            final ActionListener closeListener = new ActionListener() {
	                @Override
	                public void actionPerformed(java.awt.event.ActionEvent e) {
	                    System.exit(0);
	                }
	            };

	            ActionListener showListener = new ActionListener() {
	                @Override
	                public void actionPerformed(java.awt.event.ActionEvent e) {
	                    Platform.runLater(new Runnable() {
	                        @Override
	                        public void run() {
	                            stage.show();
	                        }
	                    });
	                }
	            };
	            // create a popup menu
	            PopupMenu popup = new PopupMenu();

	            MenuItem showItem = new MenuItem("Show");
	            showItem.addActionListener(showListener);
	            popup.add(showItem);

	            MenuItem closeItem = new MenuItem("Close");
	            closeItem.addActionListener(closeListener);
	            popup.add(closeItem);
	            /// ... add other items
	            // construct a TrayIcon
	            trayIcon = new TrayIcon(image, "Title", popup);
	            // set the TrayIcon properties
	            trayIcon.addActionListener(showListener);
	            // ...
	            // add the tray image
	            try {
	                tray.add(trayIcon);
	            } catch (AWTException e) {
	                System.err.println(e);
	            }
	            // ...
	        }
	    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Some message.",
                    "Some other message.",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
    
    private void hide (final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
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

