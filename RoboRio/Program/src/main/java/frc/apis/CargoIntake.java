package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;

public class CargoIntake {

    //INTAKE MOTORS
    private Spark motor;

    //LIMIT SWITCH--If this triggeres, then we have a cargo in the robot
    private DigitalInput limitSwitch;

    private int intakeState;
    public static final int INTAKE_INACTIVE = 0;
    public static final int INTAKE_PULLING = 1;
    public static final int INTAKE_PUSHING = 2;

    //Minimum power at which the motors will turn
    private double minimumPower;

    //TODO: Change this to an absolute number of motor ports
    public CargoIntake(int motorPort, int limitSwitchPort) {
        this(new Spark(motorPort), new DigitalInput(limitSwitchPort));
    }

    public CargoIntake(Spark motor, DigitalInput limitSwitch) {
        this.motor = motor;
        this.limitSwitch = limitSwitch;
        this.minimumPower = 0.05;
    }

    public void update(double power) {
        if(limitSwitch.get()) {
            //If the limit switch is active, we have cargo loaded. We can stop spinning the wheels.
            stopIntake();
        }

        switch(intakeState) {
            case INTAKE_PULLING:
                pullCargo(power);
                break;
            case INTAKE_PUSHING:
                pushCargo(power);
                break;
            default:
                stopIntake();
                break;
        }
    }

    public double getMinimumPower() {
        return minimumPower;
    }

    public void setMinimumPower(double minimumPower) {
        this.minimumPower = Math.abs(minimumPower);
    }

    public void pullCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : minimumPower;
        motor.set(Math.abs(truePower));
        intakeState = INTAKE_PULLING;
    }

    public void pushCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : -minimumPower;
        motor.set(-Math.abs(truePower));
        intakeState = INTAKE_PUSHING;
    }

    public void stopIntake() {
        motor.set(0);
        intakeState = INTAKE_INACTIVE;
    }

    public boolean intakeIsActive() {
        return intakeState != INTAKE_INACTIVE;
    }

}