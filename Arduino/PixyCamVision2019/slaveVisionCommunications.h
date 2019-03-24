// Communications for the slave Arduino to talk to master Arduinos.
// Commando Robotics - 2019 - Deep Space

#ifndef slaveVisionCommunications_h
#define slaveVisionCommunications_h

#include <Wire.h>
#include "visionTransmission.h"

// Communication object that handles communications by a slave for communicating to a master.
visionTransmission myI2CInfo;
static const int transmissionSize = sizeof(visionTransmission);
byte transmissionBuffer[transmissionSize];


// The Arduino will call this function if the Master requests data from this address.
// Do not call this function
static void replyToMaster() {
    Wire.write(transmissionBuffer, transmissionSize);
}
// Call this in your Arduino's main setup function if it is a slave.
// Address: Address your device will respond as a slave.
void setupVisionCommunications(int slaveAddress) {
    Wire.begin(slaveAddress);
    Wire.onRequest(replyToMaster);
}

// Places the values into a buffer that the Arduino will grab them from whenever the Master requests them.
// trustMe: Whether your values are good for the Master to follow
// angleInDegrees: Positive if the target is to the robot's right, negative if the target is to the left.
// distanceInInches: distance from our robot to the wall
// strafingPercentage: a relative value from -1.0 (target is way to the robot's left) to +1.0 (target is way to the robot's right)
void updateDataForReplyToMaster(bool trustMe, double angleInDegrees, double distanceInInches, double strafingPercentage) {
    myI2CInfo.isTrustworthy = trustMe;
    myI2CInfo.angleInDegrees = angleInDegrees;
    myI2CInfo.distanceInInches = distanceInInches;
    myI2CInfo.strafingPercentage = strafingPercentage;
    memcpy(transmissionBuffer, &myI2CInfo, transmissionSize);
}

#endif
