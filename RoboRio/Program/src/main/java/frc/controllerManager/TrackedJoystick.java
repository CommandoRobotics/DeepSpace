package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Joystick;

public class TrackedJoystick extends Joystick {

    private List<Integer> buttonsToTrack;
    private Map<Integer, Boolean> buttonJustPressed;
    private Map<Integer, Boolean> buttonHeld;

    private boolean receivedInput;

    public TrackedJoystick(int portNumber) {
        super(portNumber);
        this.buttonsToTrack = new ArrayList<>();
        this.buttonJustPressed = new HashMap<>();
        this.buttonHeld = new HashMap<>();

        this.receivedInput = false;
    }

    public void trackButtons(int... buttonsToTrack) {
        this.buttonsToTrack.clear();
        this.buttonJustPressed.clear();
        this.buttonHeld.clear();

        for(int buttonToTrack : buttonsToTrack) {
            this.buttonsToTrack.add(buttonToTrack);
            this.buttonJustPressed.put(buttonToTrack, false);
            this.buttonHeld.put(buttonToTrack, false);
        }
    }

    public void update() {
        this.receivedInput = false;

        for(int buttonToTrack : buttonsToTrack) {
            boolean buttonPressed = this.getRawButton(buttonToTrack);
            
            if(buttonPressed) receivedInput = true;
            this.buttonHeld.put(buttonToTrack, buttonPressed);
            buttonJustPressed.put(buttonToTrack, !buttonJustPressed.get(buttonToTrack) && buttonPressed);
        }
    }

    public boolean receivedInput() {
        return receivedInput;
    }

    public boolean buttonWasJustPressed(int button) {
        if(!buttonJustPressed.containsKey(button)) return false;
        return buttonJustPressed.get(button);
    }

    public boolean buttonIsHeld(int button) {
        if(!buttonHeld.containsKey(button)) return false;
        return buttonHeld.get(button);
    }

}