// Wire - Version: Latest
#include <Wire.h>

void setup() {

}

void colorAlign(){
  int MOVE_LEFT_OR_RIGHT = 1;
  int FOLLOW_LINE = 2;
  int TARGET_ON_LEFT = 1;
  int TARGET_ON_RIGHT = 2;
  int TARGET_CENTERED = 3;
  int TARGET_POSITION_UNKNOWN = 4;
  int programPart = MOVE_LEFT_OR_RIGHT;
  
  if(programPart == MOVE_LEFT_OR_RIGHT){
    if(/*vision target is on the left*/){
      originalTargetPosition = TARGET_ON_LEFT;
    } else if(/*vision target is on the right*/){
      originalTargetPosition = TARGET_ON_RIGHT;
    } else if(/*vision target is centered*/){
      originalTargetPosition = TARGET_CENTERED;
    } else {
      //tell rio to alert driver of vision tracking error
      originalTargetPosition = TARGET_POSITION_UNKNOWN;
    }
    
    if(originalTargetPosition = TARGET_ON_LEFT){
      if(/*right sensor does NOT detect line*/){
        //tell rio to strafe left
      } else {
        //tell rio to stop
        programPart = FOLLOW_LINE;
      }
    } else if(originalTargetPosition = TARGET_ON_RIGHT){
      if(/*left sensor does NOT detect line*/){
        //tell rio to strafe right
      } else {
        //tell rio to stop
        programPart = FOLLOW_LINE;
      }
    } else if(originalTargetPosition = TARGET_CENTERED){
      //tell rio to stop
      programPart = FOLLOW_LINE;
    }
    
  } else if(programPart == FOLLOW_LINE){
    if(/*ultrasonic sensor is more than 5 cm away*/){
      if(/*left light sensor detects line*/){
        //tell rio to go forward while turning left
      } else if(/*right light sensor detects line*/){
        //tell rio to go forward while turning right
      } else if(/*left sensor does not detect line and neither does right sensor*/){
        //tell rio to go forward
      } else if(/*left sensor detects line and so does right sensor*/){
        //tell rio to alert driver of color sensor error (most likely cause: color sensor became dislodged)
      }
    } else {
      //tell rio to stop driving
    }
  }
  
  
  
}

void loop() {
  colorAlign();
  
}