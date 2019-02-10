// Code for initially finding and moving toward the scoring element.
// Commando Robotics 2019 - Deep Space

#include "slaveVisionCommunications.h"

void setup() {
  setupVisionCommunications(3);
}

void loop() {
  // Change these variables based on what your actual values are from the sensors.
  bool trustMe = true;
  double angle = 15.0;
  double distance = 12.0;
  double strafingPercentage = 100;
  updateDataForReplyToMaster(trustMe, angle, distance, strafingPercentage);
}
