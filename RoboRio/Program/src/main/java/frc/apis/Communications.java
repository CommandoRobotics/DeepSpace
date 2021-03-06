package frc.apis;

import frc.apis.SerialData;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Communications {

    private Map<Integer, AnalogInput> analogInputs;
    private Map<Integer, DigitalInput> digitalInputs;
    private Map<Integer, AnalogOutput> analogOutputs;
    private Map<Integer, DigitalOutput> digitalOutputs;

    private Map<Integer, SerialPort> serialPorts;
    private Map<Integer, SerialData> serialData;

    public Communications(int[] analogInputPorts, int[] analogOutputPorts, int[] digitalInputPorts, int[] digitalOutputPorts, int[] serialPorts) {
        AnalogInput.setGlobalSampleRate(62500);

        analogInputs = new HashMap<>();
        for(int analogInputPort : analogInputPorts) {
            AnalogInput analogInput = new AnalogInput(analogInputPort);
            analogInput.setOversampleBits(4);
            analogInput.setAverageBits(2);
            analogInputs.put(analogInputPort, analogInput);
        }

        analogOutputs = new HashMap<>();
        for(int analogOutputPort : analogOutputPorts) {
            analogOutputs.put(analogOutputPort, new AnalogOutput(analogOutputPort));
        }

        digitalInputs = new HashMap<>();
        for(int digitalInputPort : digitalInputPorts) {
            digitalInputs.put(digitalInputPort, new DigitalInput(digitalInputPort));
            SmartDashboard.putBoolean("Digital Input Port " + digitalInputPort, false);
        }

        digitalOutputs = new HashMap<>();
        for(int digitalOutputPort : digitalOutputPorts) {
            digitalOutputs.put(digitalOutputPort, new DigitalOutput(digitalOutputPort));
        }

        this.serialPorts = new HashMap<>();
        this.serialData = new HashMap<>();
        for(int serialPort : serialPorts) {
            System.out.println("Adding serial port " + serialPort);
            addUSBConnection(serialPort);
        }
    }

    public Communications(Collection<Integer> analogInputPorts, Collection<Integer> analogOutputPorts, Collection<Integer> digitalInputPorts, Collection<Integer> digitalOutputPorts, Collection<Integer> serialPorts) {
        
        analogInputs = new HashMap<>();
        for(int analogInputPort : analogInputPorts) {
            analogInputs.put(analogInputPort, new AnalogInput(analogInputPort));
        }

        analogOutputs = new HashMap<>();
        for(int analogOutputPort : analogOutputPorts) {
            analogOutputs.put(analogOutputPort, new AnalogOutput(analogOutputPort));
        }

        digitalInputs = new HashMap<>();
        for(int digitalInputPort : digitalInputPorts) {
            digitalInputs.put(digitalInputPort, new DigitalInput(digitalInputPort));
            SmartDashboard.putBoolean("Digital Input Port " + digitalInputPort, false);
        }

        digitalOutputs = new HashMap<>();
        for(int digitalOutputPort : digitalOutputPorts) {
            digitalOutputs.put(digitalOutputPort, new DigitalOutput(digitalOutputPort));
        }

        this.serialPorts = new HashMap<>();
        this.serialData = new HashMap<>();
        for(int serialPort : serialPorts) {
            System.out.println("Adding serial port " + serialPort);
            addUSBConnection(serialPort);
        }
    }

    public void addUSBConnection(int whichPort) {
        if(whichPort == 0) {
            serialPorts.put(0, new SerialPort(9600, SerialPort.Port.kMXP));
            serialData.put(0, new SerialData());
        }
        if(whichPort == 1) {
            serialPorts.put(1, new SerialPort(9600, SerialPort.Port.kUSB1));
            serialData.put(1, new SerialData());
        }
        if(whichPort == 2) {
            serialPorts.put(2, new SerialPort(9600, SerialPort.Port.kUSB2));
            serialData.put(2, new SerialData());
        }
    }

    public void update() {
        Set<Integer> analogInputPorts = analogInputs.keySet();
        for(int analogInputPort : analogInputPorts) {
            analogInputs.get(analogInputPort).resetAccumulator();
        }

        Set<Integer> digitalInputPorts = digitalInputs.keySet();
        for(int digitalInputPort : digitalInputPorts) {
            SmartDashboard.putBoolean("Limit Switch " + digitalInputPort, digitalInputs.get(digitalInputPort).get());
        }

        Set<Integer> serialInputPorts = serialPorts.keySet();
        for(int serialInputPort : serialInputPorts) {
            //if(serialPorts.get(serialInputPort).getBytesReceived() == 0) continue;
            String serialInput = serialPorts.get(serialInputPort).readString();
            serialData.get(serialInputPort).processData(serialInput);
        }
    }

    public void reset() {
        Set<Integer> serialInputPorts = serialPorts.keySet();
        for(int serialInputPort : serialInputPorts) {
            //if(serialPorts.get(serialInputPort).getBytesReceived() == 0) continue;
            serialPorts.get(serialInputPort).reset();
        }
    }

    public long getAnalogPortInput(int analogPort) {
        return analogInputs.containsKey(analogPort) ? analogInputs.get(analogPort).getAccumulatorCount() : 0;
    }

    public void sendAnalogPortOutput(int analogPort, double voltage) {
        if(analogOutputs.containsKey(analogPort))
            analogOutputs.get(analogPort).setVoltage(voltage);
    }

    public boolean getDigitalPortInput(int digitalPort) {
        return digitalInputs.containsKey(digitalPort) ? digitalInputs.get(digitalPort).get() : false;
    }

    public void sendDigitalPortOutput(int digitalPort, boolean value) {
        if(digitalOutputs.containsKey(digitalPort))
            digitalOutputs.get(digitalPort).set(value);
    }

    public SerialData getSerialData(int serialPort) {
        return serialData.containsKey(serialPort) ? serialData.get(serialPort) : new SerialData();
    }

    public String getSerialPortString(int serialPort) {
        return serialPorts.containsKey(serialPort) ? serialPorts.get(serialPort).readString() : "Communications.java: (No string sent)";
    }

    public void sendSerialPortOutput(int serialPort, byte[] data) {
        if(serialPorts.containsKey(serialPort))
            serialPorts.get(serialPort).write(data, data.length);
    }

}