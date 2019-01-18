package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ControllerManager {

    private List<TrackedJoystick> trackedJoysticks;

    public ControllerManager() {
        this.trackedJoysticks = new ArrayList<>();
    }

    public void loadControlScheme(ControlScheme controlScheme) {
        this.trackedJoysticks.clear();

        Map<Integer, int[]> joystickEntries = controlScheme.getTrackedJoysticks();
        Set<Integer> joystickPorts = joystickEntries.keySet();
        for(int port : joystickPorts) {
            TrackedJoystick trackedJoystick = new TrackedJoystick(port);
            trackedJoystick.trackButtons(joystickEntries.get(port));
            this.trackedJoysticks.add(trackedJoystick);
        }
    }

    public void update() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }
    }

    public void trackJoystick(TrackedJoystick joystickToTrack) {
        trackedJoysticks.add(joystickToTrack);
    }

    public TrackedJoystick getTrackedJoystick(int joystick) {
        return trackedJoysticks.get(joystick);
    }

}