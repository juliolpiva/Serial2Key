package jlp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import jlp.fxml.MainController;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialControl {

	private SerialPort serialPort;
	private String buffer = "", buffer2 = "", buffer3 = " ";
	public boolean conectado = false, flagStatus = false, clearConect = false, firstMsg = true;
	private MainController mainController;
	
	private List<Consumer<String>> responseConsumers = new ArrayList<>();	
	
	public SerialControl (){
	}
	
	public SerialControl (MainController main){
		mainController=main;
	}
	
	
	public String[] getPorts() {
		String[] portNames = SerialPortList.getPortNames();
		return portNames;
	}
	
	public static String getFirstPort() {
		String[] portNames = SerialPortList.getPortNames();
		return portNames[0];
	}

	public boolean connectCOM(ConfigCOM config) {

		// In the constructor pass the name of the port with which we work
		serialPort = new SerialPort(config.portName);

		try {
			conectado = serialPort.openPort();
		} catch (SerialPortException e) {
			//e.printStackTrace();
		}
		if (conectado) {
			try {
				serialPort.setParams(config.baudrate, config.dataBits, config.stopBits, config.parityBit);
				
				serialPort.addEventListener(new SerialPortEventListener() {
					@Override
					public void serialEvent(SerialPortEvent serialPortEvent) {
						if (serialPortEvent.isRXCHAR()) {
							buffer3 = "";
							readCOM();
							
							if (buffer != null) {
								for (int x = 0; x < buffer.length(); x++) {
									char y = buffer.charAt(x);
									buffer2 = buffer2 + y;
									buffer3 = buffer2;
									if ((y == '\u0003')||(y == '\n')) {		// \u0003 = END OF TEXT
										buffer3 = buffer2;
										buffer2 = buffer.substring(x);
										mainController.receiveMsg(buffer3);
									}
								}
							}
						}
					}
				});

			} catch (SerialPortException ex) {
				System.err.println(ex);
			}
		}
		return conectado;
	}

	public void writeCOM(String write) {
		try {
			//System.out.println(write.substring(0, write.lastIndexOf('<')+1));
			boolean confirm = serialPort.writeString(write);
			if (confirm == false)
				System.out.println("Não foi enviado!");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	
	public void writeByte(byte write) {
		try {
			//System.out.println(write.substring(0, write.lastIndexOf('<')+1));
			boolean confirm = serialPort.writeByte(write);
			if (confirm == false)
				System.out.println("Não foi enviado!");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public void readCOM() {
		try {
			buffer = serialPort.readString();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public void disconectCOM() {
		if (conectado) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	public void delayms(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addConsumer(Consumer<String> consumer) {
		this.responseConsumers.add(consumer);
	}
}
