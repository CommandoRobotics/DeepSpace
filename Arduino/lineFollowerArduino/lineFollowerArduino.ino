// Commando Robotics 2019 - Deep Space

#include "slaveVisionCommunications.h"
#include <math.h>
#include <Pixy2.h>
#include <Wire.h>
Pixy2 pixy2;

bool canISeeLine() {
  return true;
}

float getLineRefrence() {
  float lineLocation; 
  double percentage;
  int percentageTwo;
  pixy2.ccc.getBlocks();
  
  lineLocation = pixy2.ccc.blocks[0].m_x;
  percentage = (lineLocation - 157)/157;
  Serial.println(percentage);
  return percentage;
}

void setupPixyCam(){
  pixy2.init();
}

void setup() {
  Serial.begin(9600);
  setupVisionCommunications(11);
  setupPixyCam();
  Wire.begin(11);
}

//print
void loop() {
  double strafingPercentage = 100;
  Serial.print(getLineRefrence());
  Serial.print("%");
  Serial.print("\n");
  delay(1000);
  updateDataForReplyToMaster(canISeeLine(), 0,0, getLineRefrence());
}
