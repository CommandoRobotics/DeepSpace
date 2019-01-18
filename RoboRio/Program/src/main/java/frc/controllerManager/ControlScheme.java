package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ControlScheme {

    private List<Integer> joysticksToTrack;
    private Map<Integer, int[]> axesToTrack;
    private Map<Integer, int[]>  buttonsToTrack;

    public ControlScheme() {
        this.joysticksToTrack = new ArrayList<>();
        this.axesToTrack = new HashMap<>();
        this.buttonsToTrack = new HashMap<>();
    }

    public List<Integer> getTrackedJoystickPorts() {
        return joysticksToTrack;
    }

    public Map<Integer, int[]> getTrackedAxes() {
        return axesToTrack;
    }

    public Map<Integer, int[]> getTrackedButtons() {
        return buttonsToTrack;
    }

    public void addJoystick(int port, int[] axesToTrack, int[] buttonsToTrack) {
        joysticksToTrack.add(port);
        this.axesToTrack.put(port, axesToTrack);
        this.buttonsToTrack.put(port, buttonsToTrack);
    }

}