// Distance using Maxbotics Ultrasonic Sensors

#ifndef ULTRASONIC_API
#define ULTRASONIC_API

long getDistanceUsingUltrasonic() {
  const int ultrasonicPin = 0;
  long voltageRead = analogRead(ultrasonicPin);
  long distanceInCentimeters = voltageRead / 2;
  return distanceInCentimeters;
}

#endif
