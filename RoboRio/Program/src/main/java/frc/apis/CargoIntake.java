package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;

public class CargoIntake {

    //INTAKE MOTORS
    private Spark[] motors;

    //LIMIT SWITCH--If this triggeres, then we have a cargo in the robot
    private DigitalInput limitSwitch;

    private int intakeState;
    public static final int INTAKE_INACTIVE = 0;
    public static final int INTAKE_PULLING = 1;
    public static final int INTAKE_PUSHING = 2;

    //Minimum power at which the motors will turn
    private double minimumPower;

    //TODO: Change this to an absolute number of motor ports
    public CargoIntake(int limitSwitchPort, int... motorPorts) {
        this.limitSwitch = new DigitalInput(limitSwitchPort);

        motors = new Spark[motorPorts.length];
        for(int i = 0; i < motorPorts.length; i++) {
            this.motors[i] = new Spark(motorPorts[i]);
        }

        this.minimumPower = 0.05;
    }

    public CargoIntake(DigitalInput limitSwitch, Spark... motors) {
        this.motors = motors;
        this.limitSwitch = limitSwitch;
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
        for(Spark spark : motors) {
            spark.set(Math.abs(truePower));
        }
        intakeState = INTAKE_PULLING;
    }

    public void pushCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : minimumPower;
        for(Spark spark : motors) {
            spark.set(-Math.abs(truePower));
        }
        intakeState = INTAKE_PUSHING;
    }

    public void stopIntake() {
        for(Spark spark : motors) {
            spark.set(0);
        }
        intakeState = INTAKE_INACTIVE;
    }

    public boolean intakeIsActive() {
        return intakeState != INTAKE_INACTIVE;
    }

}