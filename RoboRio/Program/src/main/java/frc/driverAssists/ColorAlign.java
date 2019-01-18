package frc.driverAssists;

import frc.apis.MecanumChassis;

public class ColorAlign {
    private int programPart = MOVE_LEFT_OR_RIGHT;
    //1 is move left/right to find line, 2 is move the opposite direction a little bit, 3 is follow the line
    private int originalTargetPosition = TARGET_POSITION_UNKNOWN;
    //1 is left, 2 is right, 3 is failure to determine position of target
    private static final int MOVE_LEFT_OR_RIGHT = 1;
    private static final int FOLLOW_LINE = 2;

    private static final int TARGET_ON_LEFT = 1;
    private static final int TARGET_ON_RIGHT = 2;
    private static final int TARGET_CENTERED = 3;
    private static final int TARGET_POSITION_UNKNOWN = 4;


    public void colorAlign(MecanumChassis chassis){
        leftLight.enableLed(true);
        rightLight.enableLed(true);
        if(programPart == MOVE_LEFT_OR_RIGHT) {
            if(/*vision target is on the left*/){
                originalTargetPosition = TARGET_ON_LEFT;
            } else if(/*vision target is on the right*/){
                originalTargetPosition = TARGET_ON_RIGHT;
            } else if(/*vision target is centered*/) {
                originalTargetPosition = TARGET_CENTERED;
            } else {
                System.out.println("Failed to determine position of target");
                originalTargetPosition = TARGET_POSITION_UNKNOWN;
            }

            if(originalTargetPosition == TARGET_ON_LEFT) {
                if(rightLight.getLightDetected() < 20){
                    chassis.strafeLeft(0.5);
                } 
            } else if(originalTargetPosition == TARGET_ON_RIGHT){
                if(leftLight.getLightDetected() < 20) {
                    chassis.strafeRight(0.5);
                }
            } else if(originalTargetPosition == TARGET_CENTERED) {
                chassis.stop();
                programPart = FOLLOW_LINE;
                System.out.println("Line detected");
            }
        } else if(programPart == FOLLOW_LINE){ //might do vision tracking for this instead of ultrasonic
            if(frontUltraSensor.getUltrasonicLevel() > 20){
                if(leftLight.getLightDetected() > 20){
                    chassis.rotateCounterClockwise(2); //may want to use custom rotation here instead of API
                } else if(rightLight.getLightDetected() > 20){
                    chassis.rotateClockwise(2); //may want to use custom rotation here instead of API
                }
                chassis.driveForward(0.2);
            } else {
                stop();
            }
        }

        //somehow include if driver puts in any input that it will fail

    }
}