#include "MasterCommunicationAPI.h"
#include "masterVisionCommunications.h"
#include "sensorsAPI.h"

masterCommunicationToSlave targetSlave(2);
masterCommunicationToSlave lineSlave(3);

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

void floatToByteArray(float val, byte* bytes_array){
  union {
    float float_variable;
    byte temp_array[4];
  } u;
  u.float_variable = val;
  memcpy(bytes_array, u.temp_array, 4);
}

void setup(){
  setupElegooUltrasonicSensor();
  setupCommunications();
  targetSlave.setup();
  lineSlave.setup();
  Serial.begin(19200);
}

void sendByteArraySerial(byte bytes_array[], int numberOfBytes){
  for(int i = 0; i < numberOfBytes; i++) {
    Serial.print(bytes_array[i]);
  }
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
    drivePower = 656;
    strafePower = 54;
    rotatePower = 4.5;
    //unable to do driverAssist
  }

  byte drivePowerBytes[4], strafePowerBytes[4], rotatePowerBytes[4];
  floatToByteArray(drivePower, drivePowerBytes);
  floatToByteArray(strafePower, strafePowerBytes);
  floatToByteArray(rotatePower, rotatePowerBytes);
  digitalWrite(LED_BUILTIN,LOW);
  delay(1000);
  sendByteArraySerial(drivePowerBytes, 4);
  digitalWrite(LED_BUILTIN,HIGH);
  sendByteArraySerial(strafePowerBytes, 4);
  digitalWrite(LED_BUILTIN,LOW);
  sendByteArraySerial(rotatePowerBytes, 4);
  digitalWrite(LED_BUILTIN,HIGH);
}
