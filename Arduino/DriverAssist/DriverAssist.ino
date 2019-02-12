#include "masterVisionCommunications.h"
//#include "sensorsAPI.h"

masterCommunicationToSlave targetSlave(8);
masterCommunicationToSlave lineSlave(12);

bool trackingLine() {
  lineSlave.canSlaveBeTrusted();
  return false;
}

float lineAngle() {
  return 0.0;
}

float lineDistance() {
  return 100.0;
}

float lineStrafe() {
  return 0;
}

bool trackingTarget(){
  return false;
}

float targetAngle(){
  return 0.0;
}

float targetDistance(){
  return 100.0;
}

float targetStrafe(){
  return 0;
}

void commandRio(float angle, float strafe, float distance) {
  
}

void setup() {
  //setupCommunications();
  targetSlave.setup();
  lineSlave.setup();
  Serial.begin(9600);
}


void loop() {
  float drivePower;
  float strafePower;
  float rotatePower;
  const float maxAllowableAngle = 30;
  const float maxAllowableDistanceInInches = 60;
  const float minAllowableDrivePower = 0.1; // Motor will burn up if we drive at less than 10%.
  float normalizedAnglePercentage;

  targetSlave.update();
  lineSlave.update();

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
    drivePower = 1 - (abs(strafePower) + abs(rotatePower));
    if (drivePower < minAllowableDrivePower) {
      drivePower = 0;
    }
    
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
    drivePower = (1 - (strafePower + rotatePower)) * targetDistance();

  } else {
    drivePower = 0;
    strafePower = 0;
    rotatePower = 0;
    //unable to do driverAssist
  }

  //Serial.write(drivePower);
  //Serial.write(strafePower);
  //Serial.write(rotatePower);

  Serial.write("\n");
  if (lineSlave.canSlaveBeTrusted()) {
    Serial.write("Good");
    Serial.print(lineSlave.angleInDegreesFromSlave());
    Serial.print("° ");
    Serial.print(lineSlave.distanceInInchesFromSlave());
    Serial.print("in ");
    Serial.print(lineSlave.strafingPercentageFromSlave());
    Serial.print("%\n");
  } else {
    Serial.write("Bad");
  }
  Serial.write("\n");
  delay(1000);
 
}
