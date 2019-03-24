// Data we will send back and forth for vision information.

#ifndef visionTransmission_h
#define visionTransmission_h

class visionTransmission {
    public:
    bool isTrustworthy; // Whether the slave said it should be trusted.
    double angleInDegrees; // Angle (in degrees) sent from the slave.
    double distanceInInches; // Distance (in inches) sent from the slave.
    double strafingPercentage; // Amount the slae said to strafe (-1.0 to 1.0).

    // Constructor
    visionTransmission();
};

#endif
