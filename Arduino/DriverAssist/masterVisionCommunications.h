// Communications for the master Arduino to talk to slave Arduinos.
// Commando Robotics - 2019 - Deep Space

#ifndef masterVisionCommunications_h
#define masterVisionCommunications_h

#include <Wire.h>
#include "visionTransmission.h"

// Handles communication from the master to a single slave.
// Create multiple instances for each slave.
class masterCommunicationToSlave {
public:
    int address;
    visionTransmission slaveInfo;
    static const int transmissionSize = sizeof(slaveInfo);
    byte transmissionBuffer[transmissionSize];

    // Constructor. Pass in the addres of the slave this master will communicate with.
    masterCommunicationToSlave(int slaveAddress) {
        address = slaveAddress;
    }

    // Call this in your Arduino's main setup function if it is the master.
    void setup() {
        Wire.begin();
    }

    // Queries slave for the latest data
    // returns: whether the recieve attempt was succesful.
    bool update() {
        Wire.requestFrom(address, transmissionSize);
        for (int i = 0; i < transmissionSize; ++i) {
            if (Wire.available()) {
                transmissionBuffer[i] = Wire.read();
            } else {
                slaveInfo.isTrustworthy = false;
                return false;
            }
        }
        memcpy(&slaveInfo, transmissionBuffer, transmissionSize);
    }

    // Whether your values are good for the Master to follow.
    bool canSlaveBeTrusted() {
        return slaveInfo.isTrustworthy;
    }

    // Positive if the target is to the robot's right, negative if the target is to the left.
    double angleInDegreesFromSlave() {
        return slaveInfo.angleInDegrees;
    }

    // Distance from our robot to the wall
    double distanceInInchesFromSlave() {
        return slaveInfo.distanceInInches;
    }

    // A relative value from -1.0 (target is way to the robot's left) to +1.0 (target is way to the robot's right)
    double strafingPercentageFromSlave() {
        return slaveInfo.strafingPercentage;
    }
};

#endif
