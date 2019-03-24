#include "masterVisionCommunications.h"
#include "rioCommunications.h"
//#include "sensorsAPI.h

masterCommunicationToSlave targetSlave(8);
masterCommunicationToSlave lineSlave(11);
masterCommunicationToSlave ultrasonicSlave(12);

bool trackingLine() {
  if (lineSlave.canSlaveBeTrusted() && ultrasonicSlave.canSlaveBeTrusted()) {
    return true;
  }
  return false;
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
  targetSlave.setup();
  lineSlave.setup();
  ultrasonicSlave.setup();
  Serial.begin(9600);
}

float angleToRotationPower(float angle) {
  return angle / 90.0;
}

float distanceToDrivePower(float distanceInInches) {
  
}


void loop() {
  float drivePower = 0.0;
  float strafePower = 0.0;
  float rotatePower = 0.0;
  const float maxAllowableAngle = 50;
  const float maxAllowableDistanceInInches = 60;
  const float minAllowableDrivePower = 0.1; // Motor will burn up if we drive at less than 10%.
  float normalizedAnglePercentage;
  char trustMe = 'b';

  targetSlave.update();
  lineSlave.update();
  ultrasonicSlave.update();

  if(trackingLine()){
    Serial.print("Tracking line\n");

  float storedLineAngle = lineAngle();

  Serial.print("Line Angle: ");
      Serial.print(storedLineAngle);
      Serial.print("\n");
    
    if(storedLineAngle < -maxAllowableAngle){
      Serial.print("Line angle more negative than maxAllowableAngle\n");
      normalizedAnglePercentage = 0.5;
    } else if (storedLineAngle > maxAllowableAngle) {
      Serial.print("Line angle more positive than maxAllowableAngle\n");
      normalizedAnglePercentage = -0.5;
    } else{
      Serial.print("Line angle in range");
      normalizedAnglePercentage = (storedLineAngle / maxAllowableAngle) * 0.5;
    }

    rotatePower = normalizedAnglePercentage;
    strafePower = lineStrafe();

    float distanceScalePercentage = lineDistance() / 12;

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

  } else if(trackingTarget()){
    Serial.print("Tracking target\n");
    
    //Serial.println("Tracking target");
    if(targetAngle() < (-1 * maxAllowableAngle)){
      normalizedAnglePercentage = 1;
    } else if (targetAngle() > maxAllowableAngle) {
      normalizedAnglePercentage = -1;
    } else{
      normalizedAnglePercentage = targetAngle() / maxAllowableAngle;
    }

  //Serial.print("Target Data: ");
  //Serial.println(targetAngle());
  //Serial.println(targetStrafe());
    
    rotatePower = normalizedAnglePercentage;
    strafePower = targetStrafe() / 100;
    drivePower = (1 - (abs(strafePower) + abs(rotatePower))) * targetDistance();
    trustMe = 'g';

  } else {

    drivePower = 0.0;
    strafePower = 0.0;
    rotatePower = 0.0;
    trustMe = 'b';
    //unable to do driverAssist
  }

  Serial.print("Drive Power: ");
  Serial.println(drivePower);
  Serial.print("Strafe Power: ");
  Serial.println(strafePower);
  Serial.print("Rotate Power: ");
  Serial.println(rotatePower);
  Serial.println("Allegedly sending telemetry");
  sendTelemetryToRio(trustMe, drivePower, strafePower, rotatePower);
  //Serial.println("Allegedly sending telemetry");
  sendTelemetryToRio((trustMe == 'g'), drivePower, strafePower, rotatePower);
  delay(500);
}
