package frc.apis;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class Communications {

    private Map<Integer, AnalogInput> analogInputs;
    private Map<Integer, DigitalInput> digitalInputs;

    public Communications(int[] analogInputPorts, int[] digitalInputPorts) {
        analogInputs = new HashMap<>();
        for(int analogInputPort : analogInputPorts) {
            analogInputs.put(analogInputPort, new AnalogInput(analogInputPort));
        }

        digitalInputs = new HashMap<>();
        for(int digitalInputPort : digitalInputPorts) {
            digitalInputs.put(digitalInputPort, new DigitalInput(digitalInputPort));
        }
    }

    public Communications(Collection<Integer> analogInputPorts, Collection<Integer> digitalInputPorts) {
        analogInputs = new HashMap<>();
        for(int analogInputPort : analogInputPorts) {
            analogInputs.put(analogInputPort, new AnalogInput(analogInputPort));
        }

        digitalInputs = new HashMap<>();
        for(int digitalInputPort : digitalInputPorts) {
            digitalInputs.put(digitalInputPort, new DigitalInput(digitalInputPort));
        }
    }

    public int getAnalogPortInput(int analogPort) {
        return analogInputs.containsKey(analogPort) ? analogInputs.get(analogPort).getValue() : 0;
    }

    public boolean getDigitalPortInput(int digitalPort) {
        return digitalInputs.containsKey(digitalPort) ? digitalInputs.get(digitalPort).get() : false;
    }

}