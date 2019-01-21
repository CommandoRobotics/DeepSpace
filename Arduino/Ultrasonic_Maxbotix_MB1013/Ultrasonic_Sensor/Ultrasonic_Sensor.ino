
#include "ultrasonicAPI.h"

//const int anPin = 0;
//long anVolt, cm;


void setup() {
  Serial.begin(9600);
}

//void read_sensor(){
//  anVolt = analogRead(anPin);
//  cm = anVolt/2;
//}

//long getDistanceUsingUltrasonic() {
//const ulstrasonicPin = 0;
//  long voltageRead = analogRead(ulstrasonicPin);
//  long distanceInCentimeters = ulstrasonicPin / 2;
//  return distanceInCentimeters;
//}

void print_range(){
  Serial.print("Range = ");
  Serial.print(getDistanceUsingUltrasonic());
  Serial.print(" cm ");
  Serial.print('\n');
}

void loop() {
//  read_sensor();
  print_range();
  delay(100);
}
