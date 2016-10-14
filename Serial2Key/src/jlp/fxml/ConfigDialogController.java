package jlp.fxml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import jlp.ConfigCOM;
import jlp.SerialControl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class ConfigDialogController extends AnchorPane implements Initializable {

	@FXML
	public  ComboBox<String> boxCom,boxBaudrate, boxDatabits, boxParity, boxStopbits;
	
	private SerialControl serialControl = new SerialControl();
	public List<String> listBaud = new ArrayList<>();
	public List<String> listData = new ArrayList<>();
	public List<String> listPari = new ArrayList<>();
	public List<String> listStop = new ArrayList<>();

	public ConfigDialogController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Config_Dialog.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			String linha;	
			BufferedReader file_buffer = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("config/config.txt")));

			while ((linha = file_buffer.readLine()) != null) {
				if ("Baud".equals(linha.substring(0, 4))) {
					listBaud.add(linha.substring(5));
				} else if ("Data".equals(linha.substring(0, 4))) {
					listData.add(linha.substring(5));
				} else if ("Pari".equals(linha.substring(0, 4))) {
					listPari.add(linha.substring(5));
				} else if ("Stop".equals(linha.substring(0, 4))) {
					listStop.add(linha.substring(5));
				}
			}
			file_buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boxCom.getItems().addAll(serialControl.getPorts());
		boxBaudrate.getItems().addAll(listBaud);
		boxDatabits.getItems().addAll(listData);
		boxParity.getItems().addAll(listPari);
		boxStopbits.getItems().addAll(listStop);
		
		ConfigCOM config = ConfigCOM.loadDefault();
		boxCom.setValue(config.portName);
		boxBaudrate.setValue(Integer.toString(config.baudrate));
		boxDatabits.setValue(Integer.toString(config.dataBits));
		boxParity.setValue(config.parityBit == 1 ? "Sim" : "Não");
		boxStopbits.setValue(Integer.toString(config.stopBits));
		
	}

	public String getcom() {
		return this.boxCom.getValue();
	}

	public String getbaud() {
		return this.boxBaudrate.getValue();
	}

	public String getdatabits() {
		return this.boxDatabits.getValue();
	}

	public String getparity() {
		return this.boxParity.getValue();
	}

	public String getstopbits() {
		return this.boxStopbits.getValue();
	}

}
