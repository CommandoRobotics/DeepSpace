// Distance using Maxbotics Ultrasonic Sensors

#ifndef ULTRASONIC_API
#define ULTRASONIC_API

long getDistanceUsingUltrasonic() {
  const ulstrasonicPin = 0;
  long voltageRead = analogRead(ulstrasonicPin);
  long distanceInCentimeters = ulstrasonicPin / 2;
  return distanceInCentimeters;
}

#endif