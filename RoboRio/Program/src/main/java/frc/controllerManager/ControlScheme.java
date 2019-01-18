package frc.controllerManager;

import java.util.Map;
import java.util.HashMap;

public class ControlScheme {

    private Map<Integer, int[]>  joysticksToTrack;

    public ControlScheme() {
        this.joysticksToTrack = new HashMap<>();
    }

    public Map<Integer, int[]> getTrackedJoysticks() {
        return joysticksToTrack;
    }

    public void addJoystick(int port, int... buttonsToTrack) {
        joysticksToTrack.put(port, buttonsToTrack);
    }

}