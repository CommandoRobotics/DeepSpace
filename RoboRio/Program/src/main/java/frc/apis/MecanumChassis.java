package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class MecanumChassis extends MecanumDrive {

    //NOTE ON ORIENTATION
    //The front of our robot is considered to be the side with the hatch mechanism
    
    //NOTE ON ROTATIONS
    /*The positive X axis points ahead, the positive Y axis points right, and the positive Z axis points down.
    *Rotations follow the right-hand rule, so clockwise rotation around the Z axis is positive.
    */

    //TOLERANCES
    private static final double ROTATION_TOLERANCE = 0.5;

    //CHASSIS STATE
    private boolean seekingAngle;
        private double targetAngle;

    //GYROSCOPE COMPONENT
    private boolean hasGyroscope;
        private ADIS16448_IMU gyroscope;
    
    public MecanumChassis(int frontLPort, int rearLPort, int frontRPort, int rearRPort) {
        super(new Spark(frontLPort), new Spark(rearLPort), new Spark(frontRPort), new Spark(rearRPort));

        this.seekingAngle = false;
            this.targetAngle = 0;
        
        this.hasGyroscope = false;
    }

    public void update() {
        if(seekingAngle) {
            rotateToAngle(targetAngle, 0.5);
        }
    }

    public void addGyroscope(ADIS16448_IMU gyroscope) {
        this.gyroscope = gyroscope;
    }

    public void driveMecanum(double lateralSpeed, double forwardSpeed, double angle) {
        super.driveCartesian(lateralSpeed, forwardSpeed, angle);
    }

    public void driveMecanumPolar(double forwardSpeed, double angle, double rotationRate) {
        super.drivePolar(forwardSpeed, angle, rotationRate);
    }

    public void stop() {
        driveMecanum(0, 0, 0);
    }

    public void driveForwards(double power) {
        driveMecanum(0, Math.abs(power), 0);
    }

    public void driveBackwards(double power) {
        driveMecanum(0, -Math.abs(power), 0);
    }

    public void strafeLeft(double power) {
        driveMecanum(-Math.abs(power), 0, 0);
    }

    public void strafeRight(double power) {
        driveMecanum(Math.abs(power), 0, 0);
    }

    public void driveClockwiseInCircularPath(double power, double rotationRate) {
        driveMecanum(0, power, Math.abs(rotationRate));
    }

    public void driveCounterClockwiseInCircularPath(double power, double rotationRate) {
        driveMecanum(0, power, -Math.abs(rotationRate));
    }

    public void driveWhileRotatingClockwse(double power, double angle, double rotationRate) {
        driveMecanumPolar(power, angle, Math.abs(rotationRate));
    }

    public void driveWhileRotatingCounterClockwise(double power, double angle, double rotationRate) {
        driveMecanumPolar(power, angle, -Math.abs(rotationRate));
    }

    public void rotateClockwise(double rotationRate) {
        driveMecanum(0, 0, Math.abs(rotationRate));
    }

    public void rotateCounterClockwise(double rotationRate) {
        driveMecanum(0, 0, -Math.abs(rotationRate));
    }

    public void rotateByAngle(double targetRotation, double rotationRate) {
        if(!hasGyroscope) return;
        rotateToAngle(gyroscope.getAngleZ() + targetRotation, rotationRate);
    }

    public void rotateToAngle (double targetAngle, double rotationRate) {
        if(!hasGyroscope) return;

        double currentAngle = gyroscope.getAngleZ();
        if(currentAngle < targetAngle - ROTATION_TOLERANCE) rotateClockwise(rotationRate);
        else if(currentAngle > targetAngle + ROTATION_TOLERANCE) rotateCounterClockwise(rotationRate);
    }
}