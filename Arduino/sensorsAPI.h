//include this file to get our sensor information
//commando robotics 2019

#ifndef SENSORS_API
#define SENSORS_API
int trigPinLeft = 11;    // Trigger
int echoPinLeft = 12;    // Echo
int trigPinRight = 9;    // Trigger
int echoPinRight = 10;    // Echo
long duration, cm, inches;

void setupAPI(){
 setupLimitSwitch();
 setupTouchlessEncoder();
 setupElegooUltrasonicSensor();
}

//Returns the distance in centimeters
//Requires that the ultrasonic sensor is plugged into pin a0
//long getDistanceUsingUltrasonic() {
//  const ulstrasonicPin = 0;
//  long voltageRead = analogRead(ulstrasonicPin);
//  long distanceInCentimeters = ulstrasonicPin / 2;
//  return distanceInCentimeters;
//}

void setupLimitSwitch(){
  pinMode( 6 , INPUT );
}

//Returns true/false is limit switch activated
//Requires that the limit switch is plugged into pin a6
bool limitSwitchPressed(){
  if( digitalRead(6) == LOW ){
  return true;
  }else{
  return false;
  }

void setupTouchlessEncoder(){
  pinMode( 5 , INPUT );
}

//Returns true/false is line in front of encoder
//Requires that the limit switch is plugged into pin a5
bool encoderSeesMark(){
  if( digitalRead(5) == LOW ){
  return true;
  }else{
  return false;
  }

void setupElegooUltrasonicSensor(){
  Serial.begin (9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

long ultrasonicLeft(){
  digitalWrite(trigPinLeft, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPinLeft, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinLeft, LOW);
  pinMode(echoPinleft, INPUT);
  duration = pulseIn(echoPinLeft, HIGH);
  inches = (duration/2) / 74;   // Divide by 74 or multiply by 0.0135
  return in;
}

long ultrasonicRight(){
  digitalWrite(trigPinRight, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPinRight, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPinRight, LOW);
  pinMode(echoPinRight, INPUT);
  duration = pulseIn(echoPinRight, HIGH);
  inches = (duration/2) / 74;   // Divide by 74 or multiply by 0.0135
  return in;
}


#endif