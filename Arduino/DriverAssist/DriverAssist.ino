#include "masterVisionCommunications.h"
#include "rioCommunications.h"
//#include "sensorsAPI.h"

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

void commandRio(float angle, float strafe, float distance) {
  
}

void setup() {
  //setupCommunications();
  targetSlave.setup();
  lineSlave.setup();
  ultrasonicSlave.setup();
  Serial.begin(9600);
}


void loop() {
  float drivePower = 0.0;
  float strafePower = 0.0;
  float rotatePower = 0.0;
  const float maxAllowableAngle = 30;
  const float maxAllowableDistanceInInches = 60;
  const float minAllowableDrivePower = 0.1; // Motor will burn up if we drive at less than 10%.
  float normalizedAnglePercentage;
  bool autoPilotAvailable = false;

  targetSlave.update();
  lineSlave.update();
  ultrasonicSlave.update();

  if(trackingLine()){
    if(lineAngle() < (-1 * maxAllowableAngle)){
      normalizedAnglePercentage = 1;
    } else if (lineAngle() > maxAllowableAngle) {
      normalizedAnglePercentage = -1;
    } else{
      normalizedAnglePercentage = lineAngle() / maxAllowableAngle;
    }   
    rotatePower = normalizedAnglePercentage;
    
    strafePower = lineStrafe();
    Serial.print(lineDistance());
    Serial.print("-");
    Serial.print(strafePower);
    Serial.print("-");
    Serial.print(rotatePower);
    drivePower = (1 - (abs(strafePower) + abs(rotatePower))) * lineDistance();
    if (drivePower < minAllowableDrivePower) {
      drivePower = 0;
    }
    autoPilotAvailable = true;
  } else if(trackingTarget()){

    if(targetAngle() < (-1 * maxAllowableAngle)){
      normalizedAnglePercentage = 1;
    } else if (targetAngle() > maxAllowableAngle) {
      normalizedAnglePercentage = -1;
    } else{
      normalizedAnglePercentage = targetAngle() / maxAllowableAngle;
    }
    rotatePower = normalizedAnglePercentage;
      
    rotatePower = normalizedAnglePercentage;
    strafePower = targetStrafe() / 100;
    drivePower = (1 - (abs(strafePower) + abs(rotatePower))) * targetDistance();
    autoPilotAvailable = true;
  } else {
    drivePower = 0;
    strafePower = 0;
    rotatePower = 0;
    autoPilotAvailable = false;
    //unable to do driverAssist
  }

  sendTelemetryToRio(autoPilotAvailable, drivePower, strafePower, rotatePower);

  delay(1000);
}
