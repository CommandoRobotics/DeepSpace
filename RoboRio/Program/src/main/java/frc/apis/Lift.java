package frc.apis;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Spark;

public class Lift {

    private static final double ELEVATION_TOLERANCE = 1;

    private Spark lift;
    private Encoder encoder;
    private DigitalInput limitSwitch;

    private boolean seekingElevation;
        private double targetElevation;
    
    public Lift(int sparkPort, int encoderPort1, int encoderPort2, boolean reverseEncoderDirection, int limitSwitchPort) {
        this(new Spark(sparkPort), new Encoder(encoderPort1, encoderPort2, reverseEncoderDirection, EncodingType.k4X),
            new DigitalInput(limitSwitchPort));
    }

    public Lift(Spark spark, Encoder encoder, DigitalInput limitSwitch) {
        this.lift = spark;
        this.encoder = encoder;
        this.limitSwitch = limitSwitch;

        this.seekingElevation = false;
        this.targetElevation = 0;
    }

    public void update(double power) {
        if(seekingElevation) {
            seekElevation(power);
        }
        
        if(limitSwitch.get()) {
            resetEncoder();
        }
    }

    public void raiseLift(double power) {
        lift.set(Math.abs(power));
    }

    public void lowerLift(double power) {
        if(limitSwitch.get()) stopLift();
        else lift.set(-Math.abs(power));
    }

    public void stopLift() {
        lift.set(0);
    }

    private void seekElevation(double power) {
        if(targetElevation - ELEVATION_TOLERANCE > encoder.getDistance()) raiseLift(power);
        else if(targetElevation + ELEVATION_TOLERANCE < encoder.getDistance()) lowerLift(power);
        else stopLift();
    }

    public void setTargetElevation(double targetElevation) {
        this.targetElevation = targetElevation;
    }

    public double getTargetElevation() {
        return targetElevation;
    }

    private void resetEncoder() {
        encoder.reset();
    }

}