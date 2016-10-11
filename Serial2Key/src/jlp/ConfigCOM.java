package jlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigCOM {
    private static final String SERIAL_PORT_PROPERTIES = "/config/serial_port.properties";
   
    public final String portName;
    public final int baudrate;
    public final int dataBits;
    
    //0 or 'N': no parity, 1: ODD, 2: EVEN, 3: MARK, 4: SPACE
    public final int parityBit;
    public final int stopBits;

    public ConfigCOM(String port, int baudrate, int dataBits, int parity, int stopBits)
    {
        this.portName = port;
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.parityBit = parity;
        this.stopBits = stopBits;
    }

    public static ConfigCOM loadDefault()
    {
        Properties configProperties = new Properties();
        try
        {
            InputStream cfgStream = ConfigCOM.class.getResourceAsStream(SERIAL_PORT_PROPERTIES);
            configProperties.load(cfgStream);
           // System.out.println(configProperties);
        } catch (IOException e) 
        {
           System.err.println("Nao foi possivel carregar as configuracoes "
                   + "padrao a partir do arquivo " + SERIAL_PORT_PROPERTIES);
           System.err.println(e.getMessage());
        }
        return loadFromProperties(configProperties);
    }    

    public static ConfigCOM loadFromStream(InputStream configStream) throws IOException
    {
        Properties currentSerialCfg = new Properties();
        currentSerialCfg.load(configStream);

        return loadFromProperties(currentSerialCfg);
    }

    public static ConfigCOM loadFromProperties(Properties config)
    {
        // The first argument to getProperty is the property key, the second is
        // the default value to be used  when the Properties object doesn't have
        // the specified key.
    	
        String portNameStr = config.getProperty("portName", "COM1");
        String baudrateStr = config.getProperty("baudrate", "57600");
        String dataBitsStr = config.getProperty("dataBits", "8");
        String parityStr = config.getProperty("parityBit", "N");
        String stopBitsStr = config.getProperty("stopBits", "1");

        int baudrate = Integer.parseInt(baudrateStr);
        int dataBits = Integer.parseInt(dataBitsStr);

        char parityFirstChar = parityStr.trim().charAt(0);
        int parityBit = parityFirstChar == '0' || parityFirstChar == 'N' ? 0 : (int)parityFirstChar;
        int stopBits = Integer.parseInt(stopBitsStr);
        
        return new ConfigCOM(portNameStr, baudrate, dataBits, parityBit, stopBits);
    }

    @Override
    public String toString()
    {
        return (portName + " " + baudrate + " "
                + String.format("%d%s%d", dataBits, parityBit == 1 ? "S" : "N", stopBits));
    }
}
