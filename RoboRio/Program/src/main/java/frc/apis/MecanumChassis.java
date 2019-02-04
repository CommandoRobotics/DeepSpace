package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MecanumChassis extends MecanumDrive {

    //NOTE ON ORIENTATION
    //The front of our robot is considered to be the side with the hatch mechanism
    
    //NOTE ON ROTATIONS
    /*The positive X axis points ahead, the positive Y axis points right, and the positive Z axis points down.
    *Rotations follow the right-hand rule, so clockwise rotation around the Z axis is positive.
    */

    //TOLERANCES
    private static final double ROTATION_TOLERANCE = 0.5;

    //SPARKS
    private Spark frontLeft, rearLeft, frontRight, rearRight;

    //CHASSIS STATE
    private boolean seekingAngle;
        private double targetAngle;

    //GYROSCOPE COMPONENT
    private boolean hasGyroscope;
        private ADIS16448_IMU gyroscope;
   
    public MecanumChassis(Spark frontL, Spark rearL, Spark frontR, Spark rearR) {
        super(frontL, rearL, frontR, rearR);

        this.frontLeft = frontL;
        this.rearLeft = rearL;
        this.frontRight = frontR;
        this.rearRight = rearR;

        updateSmartDashboard();

        this.seekingAngle = false;
            this.targetAngle = 0;
        
        this.hasGyroscope = false;
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Chassis Motor Front Left", frontLeft.get());
        SmartDashboard.putNumber("Chassis Motor Front Right", frontRight.get());
        SmartDashboard.putNumber("Chassis Motor Rear Left", rearLeft.get());
        SmartDashboard.putNumber("Chassis Motor Rear Right", rearRight.get());
    }

    public void update() {
        if(seekingAngle) {
            rotateToAngle(targetAngle, 0.5);
        }

        updateSmartDashboard();
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