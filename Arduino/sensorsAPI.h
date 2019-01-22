//include this file to get our sensor information
//commando robotics 2019

#ifndef SENSORS_API
#define SENSORS_API

void setupAPI(){
 setupLimitSwitch();
 setupTouchlessEncoder();
}

//Returns the distance in centimeters
//Requires that the ultrasonic sensor is plugged into pin a0
long getDistanceUsingUltrasonic() {
  const ulstrasonicPin = 0;
  long voltageRead = analogRead(ulstrasonicPin);
  long distanceInCentimeters = ulstrasonicPin / 2;
  return distanceInCentimeters;
}

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






#endif