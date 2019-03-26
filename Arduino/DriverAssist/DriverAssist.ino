#include "masterVisionCommunications.h"
#include "rioCommunications.h"
//#include "sensorsAPI.h

masterCommunicationToSlave targetSlave(8);
masterCommunicationToSlave lineSlave(11);
masterCommunicationToSlave ultrasonicSlave(12);

bool trackingLine() {
  return (lineSlave.canSlaveBeTrusted() && ultrasonicSlave.canSlaveBeTrusted());
}

float lineAngle() {
  return ultrasonicSlave.angleInDegreesFromSlave();
}

float lineDistance() {
  return ultrasonicSlave.distanceInInchesFromSlave();
}

float lineStrafe() {
  return lineSlave.strafingPercentageFromSlave();
}

bool trackingTarget() {
  return targetSlave.canSlaveBeTrusted();
}

float targetAngle() {
  return targetSlave.angleInDegreesFromSlave();
}

float targetDistance() {
  return targetSlave.distanceInInchesFromSlave();
}

float targetStrafe() {
  return targetSlave.strafingPercentageFromSlave();
}

bool ultrasonicHasVision() {
  return ultrasonicSlave.canSlaveBeTrusted();
}

float ultrasonicAngle() {
  return ultrasonicSlave.angleInDegreesFromSlave();
}

float ultrasonicDistance() {
  return ultrasonicSlave.distanceInInchesFromSlave();
}

float ultrasonicStrafe() {
  return ultrasonicSlave.strafingPercentageFromSlave();
}

void commandRio(float angle, float strafe, float distance) {

}

void setup() {
  //setupCommunications();
  Serial.begin(9600);
  
  targetSlave.setup();
  lineSlave.setup();
  ultrasonicSlave.setup();
}

float angleToRotationPower(float angle) {
  return angle / 90.0;
}

float distanceToDrivePower(float distanceInInches) {
  
}


const float maxAllowableAngle = 50;
const float maxAllowableDistanceInInches = 60;
const float minAllowableDrivePower = 0.1; // Motor will burn up if we drive at less than 10%.

const float maxAllowableDrivePower = 0.5;


float convertToJoystickPower(float power, float maxPowerThreshold) {
  if(power > maxPowerThreshold) return 1;
  return power / maxPowerThreshold;
}

void loop() {
  float drivePower = 0.0;
  float strafePower = 0.0;
  float rotatePower = 0.0;
  
  float normalizedAnglePercentage;
  char trustMe = 'b';

  targetSlave.update();
  lineSlave.update();
  ultrasonicSlave.update();

  if(trackingLine()){
    Serial.print("Tracking line\n");

    float storedLineAngle = lineAngle();

    Serial.print("Stored Line Angle" );
    Serial.println(storedLineAngle);
    
    if(storedLineAngle < -maxAllowableAngle){
      normalizedAnglePercentage = 0.5;
    } else if (storedLineAngle > maxAllowableAngle) {
      normalizedAnglePercentage = -0.5;
    } else{
      normalizedAnglePercentage = (storedLineAngle / maxAllowableAngle) * 0.5;
    }
    
    rotatePower = normalizedAnglePercentage;
    strafePower = lineStrafe();
    
    float distanceFromLine = lineDistance();

    float distanceScalePercentage = distanceFromLine / 12;

    Serial.print("Line Strafe: ");
    Serial.println(strafePower);

    if(distanceScalePercentage > 1){
      distanceScalePercentage = 1;
    } else if(distanceScalePercentage < 0.25){
      distanceScalePercentage = 0.25;
    }
    
    drivePower = (1 - (abs(strafePower) + abs(rotatePower))) * distanceScalePercentage;
    
    if (drivePower < minAllowableDrivePower) {
      drivePower = 0;
    }

    trustMe = 'g';

    drivePower = convertToJoystickPower(drivePower, 0.65);
    strafePower = convertToJoystickPower(strafePower, 0.60);
    rotatePower = convertToJoystickPower(rotatePower, 0.30);
    
    Serial.println(convertToJoystickPower(drivePower, 0.75));
    Serial.println(convertToJoystickPower(strafePower, 0.60));
    Serial.println(convertToJoystickPower(rotatePower, 0.30));


  } else if(trackingTarget()){
    Serial.print("Tracking target\n");
    
    if(targetAngle() < (-1 * maxAllowableAngle)){
      normalizedAnglePercentage = 1;
    } else if (targetAngle() > maxAllowableAngle) {
      normalizedAnglePercentage = -1;
    } else{
      normalizedAnglePercentage = targetAngle() / maxAllowableAngle;
    }
    
    rotatePower = normalizedAnglePercentage;
    strafePower = targetStrafe();

    float distanceFromTarget = targetDistance();

    float distanceScalePercentage = distanceFromTarget / 12;

    if(distanceScalePercentage > 1){
      distanceScalePercentage = 1;
    } else if(distanceScalePercentage < 0.25){
      distanceScalePercentage = 0.25;
    }
    
    drivePower = (0.75 - (abs(strafePower) + abs(rotatePower))) * distanceScalePercentage;
    trustMe = 'g';

    drivePower = convertToJoystickPower(drivePower, 0.65);
    strafePower = convertToJoystickPower(strafePower, 0.60);
    rotatePower = convertToJoystickPower(rotatePower, 0.30);

  } else {

    drivePower = 0.0;
    strafePower = 0.0;
    rotatePower = 0.0;
    trustMe = 'b';
    //unable to do driverAssist
  }
  
  Serial.println("Allegedly sending telemetry");
  
  bool trustArduino = trustMe == 'g';
  
  sendTelemetryToRio(trustArduino, drivePower, strafePower, rotatePower);
  delay(500);
}
