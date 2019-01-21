package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Joystick;

public class TrackedJoystick extends Joystick {

    private double deadZone;

    private boolean receivedInput;

    private List<Integer> axesToTrack;
    private Map<Integer, Boolean> axisJustMoved;
    private Map<Integer, Boolean> axisMoved;

    private List<Integer> buttonsToTrack;
    private Map<Integer, Boolean> buttonJustPressed;
    private Map<Integer, Boolean> buttonHeld;

    public TrackedJoystick(int portNumber) {
        super(portNumber);

        this.deadZone = 0.1;

        this.axesToTrack = new ArrayList<>();
        this.axisJustMoved = new HashMap<>();
        this.axisMoved = new HashMap<>();

        this.buttonsToTrack = new ArrayList<>();
        this.buttonJustPressed = new HashMap<>();
        this.buttonHeld = new HashMap<>();

        this.receivedInput = false;
    }

    public void update() {
        this.receivedInput = false;
        
        for(int axisToTrack : axesToTrack) {
            boolean axisMoved = this.getRawAxis(axisToTrack) > deadZone;
            
            if(axisMoved) receivedInput = true;
            this.axisMoved.put(axisToTrack, axisMoved);
            axisJustMoved.put(axisToTrack, !axisJustMoved.get(axisToTrack) && axisMoved);
        }

        for(int buttonToTrack : buttonsToTrack) {
            boolean buttonPressed = this.getRawButton(buttonToTrack);
            
            if(buttonPressed) receivedInput = true;
            this.buttonHeld.put(buttonToTrack, buttonPressed);
            buttonJustPressed.put(buttonToTrack, !buttonJustPressed.get(buttonToTrack) && buttonPressed);
        }
    }

    public void setDeadZone(double deadZone) {
        this.deadZone = Math.abs(deadZone);
    }

    public double getDeadZone() {
        return deadZone;
    }

    public boolean receivedInput() {
        return receivedInput;
    }

    public void trackAxes(int... axesToTrack) {
        this.axesToTrack.clear();
        this.axisJustMoved.clear();
        this.axisMoved.clear();

        for(int axisToTrack : axesToTrack) {
            this.axesToTrack.add(axisToTrack);
            this.axisJustMoved.put(axisToTrack, false);
            this.axisMoved.put(axisToTrack, false);
        }
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

    public boolean axisWasJustPressed(int axis) {
        if(!axisJustMoved.containsKey(axis)) return false;
        return axisJustMoved.get(axis);
    }

    public boolean axisIsHeld(int axis) {
        if(!axisMoved.containsKey(axis)) return false;
        return axisMoved.get(axis);
    }

    @Override
    public double getRawAxis(int axis) {
        double power = super.getRawAxis(axis);
        return (Math.abs(power) > deadZone) ? power : 0;
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