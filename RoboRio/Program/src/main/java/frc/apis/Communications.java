package frc.apis;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
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

    public Communications(int[] analogInputPorts, int[] analogOutputPorts, int[] digitalInputPorts, int[] digitalOutputPorts) {
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
    }

    public Communications(Collection<Integer> analogInputPorts, Collection<Integer> analogOutputPorts, Collection<Integer> digitalInputPorts, Collection<Integer> digitalOutputPorts) {
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
    }

    public void update() {
        Set<Integer> analogInputPorts = analogInputs.keySet();
        for(int analogInputPort : analogInputPorts) {
            analogInputs.get(analogInputPort).resetAccumulator();
        }

        Set<Integer> digitalInputPorts = digitalInputs.keySet();
        for(int digitalInputPort : digitalInputPorts) {
            SmartDashboard.putBoolean("Digital Input Port " + digitalInputPort, digitalInputs.get(digitalInputPort).get());
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

}