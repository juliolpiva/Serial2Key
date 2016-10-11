package jlp.fxml;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import jlp.ConfigCOM;
import jlp.DialogCreator;
import jlp.Main;
import jlp.SerialControl;

public class MainController implements Initializable {

	@FXML
	private MenuItem btnSobre, btnConfigSave, btnConfigEdit, btnConfigLoad, btnClearScreen, btnSaveScreen;	
	@FXML
	private Label lblConfig, lblIni, lblEnd;
	@FXML
	private TextArea viewTela, viewTela2;
	@FXML
	private TextField stringIni, stringEnd;
	@FXML
	private Tab tabTerm, tabFiltro;
	@FXML
	private TabPane tabPane;
	@FXML
	private ToggleButton btnStart;
	@FXML
	private CheckBox boxKey, boxFilt, boxTerm;
	
	public static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
	
	private DialogCreator dialogCreator = new DialogCreator();
	private ConfigDialogController dialogCtrl = new ConfigDialogController();
	private ConfigCOM configAtual;
	private SerialControl serialControl = new SerialControl(this);
	private Main main = new Main();

	private String receivedMsg;
	private int  ini=2, end=7;
	private Timer timer = new Timer();

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configAtual = ConfigCOM.loadDefault();
		lblConfig.setText("Config: " + configAtual);
		stringIni.setText("2");
		stringEnd.setText("7");
	}
	
	public void actionStart(ActionEvent evt) {
		
		if(btnStart.isSelected()){
			System.out.println("Start");

			boolean conec;
			conec = serialControl.connectCOM(configAtual);
			
			if (conec) {
				int boxIni=0, boxEnd=0;
				try{
					boxIni = Integer.parseInt(stringIni.getText());
					boxEnd = Integer.parseInt(stringEnd.getText());
				}catch (NullPointerException e){	
				}
				
				if(ini > 0){
					ini = boxIni;
				}else{
					ini = 0;
				}
				
				if(end <= ini){
					end = boxIni;
				}else{
					end = boxEnd;					
				}
		
				serialControl.writeByte((byte)0x05);	// Inicializa transmissão (ENQ)		
				
				/*int delay = 1000;   // delay de 5 seg.
			    int interval = 1000;  // intervalo de 1 seg.
			   
			    timer = new Timer();
			    
	    	    timer.scheduleAtFixedRate(new TimerTask() {
		            public void run() {
		            	//System.out.println(ini+" e "+end);
		        		//receiveMsg("2 PPPPP 3");
		        		serialControl.writeCOM("QTT\n\r");
		            }
		        }, delay, interval);*/
			    			    
			} else {
				dialogCreator.alertError("Error", "Nao foi possivel Conectar", "Nao foi possivel conectar na porta selecionada");
				btnStart.setSelected(false);
			}
			
		}else{
			serialControl.disconectCOM();
			System.out.println("End");
			
			//timer.cancel();
			
		}

	}


	public void receiveMsg(String msg) {
		
		if (msg.length()<=end){
			end=msg.length()-1;
		}
		
		receivedMsg = (msg.substring(ini, end));
		
		if(boxFilt.isSelected()){
			viewTela.appendText(receivedMsg+"\n");
		}
		
		if(boxTerm.isSelected()){
			viewTela2.appendText(msg+"\n");
		}
		
		if(boxKey.isSelected()){
					try {
	        Robot robot = new Robot();

	        char[] filtredMsg = receivedMsg.toCharArray();
	        
	        for(int i=0;i<receivedMsg.length();i++){
	        	robot.keyPress(filtredMsg[i]);
		        robot.keyRelease(filtredMsg[i]);
	        }
	        
        	robot.keyPress(KeyEvent.VK_ENTER);
	        robot.keyRelease(KeyEvent.VK_ENTER);
	        
	      
		} catch (AWTException e) {
		        e.printStackTrace();
		}
			
		}
	}


	public void actionSerialSave(ActionEvent evt) {
		File arquivo = new File("config.txt");

		try {
			arquivo.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o diretorio para Salvar!");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text File", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		fileChooser.setInitialFileName("config.txt");
		File selectedFile = fileChooser.showSaveDialog(main.getprimary());

		try {
			FileWriter arq = new FileWriter(selectedFile.getAbsolutePath());
			String texto = ("Baud+" + configAtual.baudrate + System.getProperty("line.separator") + "Data+"
					+ configAtual.dataBits + System.getProperty("line.separator") + "Pari+" + configAtual.parityBit
					+ System.getProperty("line.separator") + "Stop+" + configAtual.stopBits);
			// System.out.println(texto);
			arq.write(texto);
			arq.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void actionSerialEdit(ActionEvent evt) {

		Dialog<ConfigCOM> cfgDialog = new Dialog<>();
		DialogPane dialogPane = new DialogPane();
		dialogPane.setContent((AnchorPane) dialogCtrl);

		cfgDialog.setDialogPane(dialogPane);
		cfgDialog.setTitle("Configuracao da porta serial");
		cfgDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		cfgDialog.setResultConverter(btnType -> {

			if (btnType.equals(ButtonType.OK)) {
				String port = dialogCtrl.boxCom.getValue();
				int baudrate = Integer.parseInt(dialogCtrl.boxBaudrate.getValue());
				int dataBits = Integer.parseInt(dialogCtrl.boxDatabits.getValue());
				int parity = dialogCtrl.boxParity.getValue().startsWith("S") ? 1 : 0;
				int stopBits = Integer.parseInt(dialogCtrl.boxStopbits.getValue());
				configAtual = new ConfigCOM(port, baudrate, dataBits, parity, stopBits);
			}
			return configAtual;
		});

		Stage stage = (Stage) cfgDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(dialogCreator.terminalIcon);
		cfgDialog.showAndWait();
		lblConfig.setText("Config: " + configAtual);
		
        Properties configProperties = new Properties();
        configProperties.setProperty("portName", configAtual.portName);
        configProperties.setProperty("baudrate", Integer.toString(configAtual.baudrate));
        configProperties.setProperty("dataBits", Integer.toString(configAtual.dataBits));
        configProperties.setProperty("parity", Integer.toString(configAtual.parityBit));
        configProperties.setProperty("stopBits", Integer.toString(configAtual.stopBits));
        
        try {
			configProperties.store(new FileOutputStream("resources/config/serial_port.properties"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void actionSerialLoad(ActionEvent evt) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o arquivo de programacao!");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(main.getprimary());

		try {
			configAtual = ConfigCOM.loadFromStream(new FileInputStream(selectedFile));

		} catch (IOException e) {
			e.printStackTrace();
		}

		lblConfig.setText(configAtual.toString());

	}
	
	public void actionClearScreen(ActionEvent evt) {
		if(tabFiltro.isSelected()){
			viewTela.clear();
		}else{
			viewTela2.clear();
		}
	}
	
	public void actionSaveScreen(ActionEvent evt) {
		File arquivo = new File("TerminalScreen.txt");

		try {
			arquivo.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o diretorio para Salvar!");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text File", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		fileChooser.setInitialFileName("Screen.txt");
		File selectedFile = fileChooser.showSaveDialog(main.getprimary());

		try {
			FileWriter arq = new FileWriter(selectedFile.getAbsolutePath());
			String texto;
			if(tabFiltro.isSelected()){
				texto = viewTela.getText();
			}else{
				texto = viewTela2.getText();
			}
			
			arq.write(texto);
			arq.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void actionSobre(ActionEvent evt) {
		dialogCreator.alertInformation("Sobre", "Serial2Key",
				"Desenvolvido por Julio Locatelli Piva\nVersao 1.2");
	}

}