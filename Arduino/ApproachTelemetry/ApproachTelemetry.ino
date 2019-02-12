// Code for the "final approach" toward a scoring element.
// Commando Robotics 2019 - Deep Space

#include "slaveVisionCommunications.h"
#include <math.h>
#include <Pixy2.h>
#include <Wire.h>
Pixy2 pixy2;

int trigPinLeft = 11;    // Trigger
int echoPinLeft = 12;    // Echo
int trigPinRight = 9;    // Trigger
int echoPinRight = 10;    // Echo

float previousLeftDistance = 0;
float previousRightDistance = 0;
float left;
float right;

const float maxAllowableDelta = 10; // Any greater distance than this between reads is thrown out.
 
bool trustworthy = false;

//begin setups
void setupElegooUltrasonicSensor(){
  pinMode(trigPinLeft, OUTPUT);
  pinMode(trigPinRight, OUTPUT);
  pinMode(echoPinLeft, INPUT);
  pinMode(echoPinRight, INPUT);
}

void setupPixyCam(){
  pixy2.init();
}
//end setups

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

int getDistance() {
  int center;
  center = (left+right)/2.0;
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
    return abs(convertToDegrees(angleInRadians));
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
    trustworthy = false;
  } else if (rightDelta > maxAllowableDelta) {
    trustworthy = false;
  } else {
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

//begin pixy cam code
float getLineRefrence() {
  float lineLocation; 
  int percentage;
  // put your main code here, to run repeatedly:
  pixy2.ccc.getBlocks();
  
  lineLocation = pixy2.ccc.blocks[0].m_x;
  percentage = (lineLocation - 157.5)/157.5;
  return abs(percentage*100);
}

void receivedData() {
  
}
//end pixy cam code

//setup
void setup() {
    Serial.begin(9600);
  setupVisionCommunications(12);
  setupElegooUltrasonicSensor();
  //setupPixyCam();
  Wire.begin(12);
}

//print
void loop() {
  bool trustMe = true;
  double angle = 15.0;
  double distance = 12.0;
  double strafingPercentage = 100;

  updateUltrasonic();
  if (isUltrasonicGood()) {
    trustMe = true;
    Serial.print(left);
    Serial.print(",");
    Serial.print(right);
    Serial.print (" ");
    Serial.print(getDistance());
    Serial.print("in. @");
    Serial.print(getRotationInDegrees());
    Serial.print("°");
    //Serial.print(getLineRefrence());
    Serial.print("%");
    Serial.print("\n");
    updateDataForReplyToMaster(trustMe, getRotationInDegrees(), getDistance(), 0);
  } else {
    trustMe = false;
    Serial.print("bad data");
    updateDataForReplyToMaster(trustMe, 0, 0, 0);
  }
  Serial.print("\n");
  delay(1000);
}
