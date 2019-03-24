// Commando Robotics 2019 - Deep Space

#include "slaveVisionCommunications.h"
#include <math.h>
#include <Pixy2.h>
#include <Wire.h>
Pixy2 pixy2;

float lineLocation; 

bool canISeeLine() {
  return true;
}

float getLineReference() {
 // float lineLocation; 
  double percentage;
  pixy2.ccc.getBlocks();
  
  lineLocation = pixy2.ccc.blocks[0].m_x;
  percentage = (lineLocation - 157)/157;
  return -percentage;
}

void setupPixyCam(){
  pixy2.init();
  pixy2.setLamp(4,1);
}

void setup() {
  Serial.begin(1200);
  setupVisionCommunications(11);
  setupPixyCam();
  Wire.begin(11);
}

//print
void loop() {
  double strafingPercentage = 100;
  Serial.print(getLineReference());
  //Serial.print("%");
  Serial.print("\n");
 // delay (1200);
  updateDataForReplyToMaster(canISeeLine(), 0,0, getLineReference());
}
