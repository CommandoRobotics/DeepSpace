void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

// Distance using Maxbotics Ultrasonic Sensors
  //const int ulstrasonicPin = 0;
#ifndef ULTRASONIC_API
#define ULTRASONIC_API

long getDistanceUsingUltrasonic() {
  const int ulstrasonicPin = 0;
  long voltageRead = analogRead(ulstrasonicPin);
  long distanceInCentimeters = voltageRead / 2;
  return distanceInCentimeters;
}

#endif

void loop() {
  // put your main code here, to run repeatedly:
  double ultrasonicDistance = getDistanceUsingUltrasonic();
  Serial.println(ultrasonicDistance);
}