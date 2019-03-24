// Code for the "final approach" toward a scoring element.
// Commando Robotics 2019 - Deep Space

#include "slaveVisionCommunications.h"
#include <math.h>
#include <Wire.h>

int trigPinLeft = 11;    // Trigger
int echoPinLeft = 12;    // Echo
int trigPinRight = 9;    // Trigger
int echoPinRight = 10;    // Echo

float previousLeftDistance = 0;
float previousRightDistance = 0;
float left;
float right;

int numberOfSamples = 5;
float leftDistances[5] = {0};
float rightDistances[5] = {0};

int leftSamples = 0;
int rightSamples = 0;
int leftBadSamples = 0;
int rightBadSamples = 0;


const float maxAllowableDelta = 10; // Any greater distance than this between reads is thrown out.
 
bool trustworthy = false;

void setupElegooUltrasonicSensor(){
  pinMode(trigPinLeft, OUTPUT);
  pinMode(trigPinRight, OUTPUT);
  pinMode(echoPinLeft, INPUT);
  pinMode(echoPinRight, INPUT);
}

float ultrasonicLeft(){
  float inches, duration;
  digitalWrite(trigPinLeft, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPinLeft, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinLeft, LOW);
  pinMode(echoPinLeft, INPUT);
  duration = pulseIn(echoPinLeft, HIGH);
  inches= (duration/2.0) / 74.0;   // Divide by 74 or multiply by 0.0135
  return inches;
}

float averagedUltrasonicLeft(){
  float newDistance = ultrasonicLeft();
  leftDistances[leftSamples] = newDistance;
  leftSamples = leftSamples + 1;
  if (leftSamples > (numberOfSamples - 1)) {
    leftSamples = 0;
  }
  float totalDistance = 0;
  for (int i = 0; i < numberOfSamples; ++ i) {
  totalDistance = leftDistances[i] + totalDistance;
  }
  float averageDistance = totalDistance / numberOfSamples;
  return averageDistance;
}

float ultrasonicRight(){
  float inches, duration;
  digitalWrite(trigPinRight, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPinRight, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinRight, LOW);
  pinMode(echoPinRight, INPUT);
  duration = pulseIn(echoPinRight, HIGH);
  inches = (duration/2.0) / 74.0;   // Divide by 74 or multiply by 0.0135
  return inches;
}

float averagedUltrasonicRight(){
  float newDistance = ultrasonicRight();
  rightDistances[rightSamples] = newDistance;
  rightSamples = rightSamples + 1;
  if (rightSamples > (numberOfSamples - 1)) {
    rightSamples = 0;
  }
  float totalDistance = 0;
  for (int i = 0; i < numberOfSamples; ++ i) {
    totalDistance = rightDistances[i] + totalDistance;
  }
  float averageDistance = totalDistance / numberOfSamples;
  return averageDistance;
}

int getDistance() {
  int center;
  center = (averagedUltrasonicLeft()+averagedUltrasonicRight())/2.0;
  if (center > 255) {
    center = 255;
  }

  return center;
}

// being rotation
double convertToDegrees(double r) {
  return r*180.0/3.14159;
}

double getRotationInDegrees() {
  double disBetweenSensors = 10; //Believe it or not this is the distance between the sensors on the robot
  double angleInRadians = atan(abs(left-right)/(disBetweenSensors)); //returns angle to be parallel
  if (left < right) {
    return -abs(convertToDegrees(angleInRadians));
  } else if (right < left) {
    return abs(convertToDegrees(angleInRadians));
  } else if (right == left) {
    return 0;
  }
}
//end rotation

bool isUltrasonicGood() {
  float leftDelta = abs(previousLeftDistance - left);
  float rightDelta = abs(previousRightDistance - right);

  if (leftDelta > maxAllowableDelta) {
    leftBadSamples = leftBadSamples + 1;
  } else {
    leftBadSamples = 0;
  }
  if (rightDelta > maxAllowableDelta) {
    rightBadSamples = rightBadSamples + 1;
  } else {
    rightBadSamples = 0;
  }
  if (leftBadSamples > 3  || rightBadSamples > 3){
    trustworthy = false;
  }
  else {
    trustworthy = true;
  }
  
  previousLeftDistance = left;
  previousRightDistance = right;

  return trustworthy;
}

void updateUltrasonic() {
  left = ultrasonicLeft();
  right = ultrasonicRight();
}

//setup
void setup() {
  Serial.begin(9600);
  setupVisionCommunications(12);
  setupElegooUltrasonicSensor();
}

//print
void loop() {
  bool trustMe = true;
  double angle = 15.0;
  double distance = 12.0;
  updateUltrasonic();
  if (isUltrasonicGood()) {
    trustMe = true;
//    Serial.print(left);
//    Serial.print(",");
//    Serial.print(right);
//    Serial.print (" ");
    Serial.print(getDistance());
    Serial.print("in. @");     
    Serial.print(getRotationInDegrees());
    Serial.print(" ");
    Serial.print("\n");

    updateDataForReplyToMaster(trustMe, getRotationInDegrees(), getDistance(), 0);
  } else {
    trustMe = false;
    Serial.print("bad data");
    updateDataForReplyToMaster(trustMe, 0, 0, 0);
  }
  Serial.print("\n");
}
