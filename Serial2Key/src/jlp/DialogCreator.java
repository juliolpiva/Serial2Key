package jlp;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DialogCreator {

	public final Image terminalIcon = new Image(this.getClass().getResource("images/kboard.png").toString());
	
	public void alertError(String title, String header, String content) {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle(title);
		error.setHeaderText(header);
		error.setContentText(content);
		error.show();
		Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
		stage.getIcons().add(terminalIcon);
	}

	public void alertInformation(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(terminalIcon);
	}

	public Alert alertInformationR(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(terminalIcon);
		return alert;
	}

	public Alert alertWarningR(String title, String header, String content) {
		Alert warning = new Alert(AlertType.WARNING);
		warning.setTitle(title);
		warning.setHeaderText(header);
		warning.setContentText(content);
		warning.show();
		Stage stage = (Stage) warning.getDialogPane().getScene().getWindow();
		stage.getIcons().add(terminalIcon);
		return warning;
	}

	public void alertWarning(String title, String header, String content) {
		Alert error = new Alert(AlertType.WARNING);
		error.setTitle(title);
		error.setHeaderText(header);
		error.setContentText(content);
		error.show();
		Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
		stage.getIcons().add(terminalIcon);
	}
	
	
}

