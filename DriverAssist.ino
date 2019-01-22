#include "ultrasonicAPI.h"

void setup(){
  
}

void driverAssist(){

  if(/*vision target is more than 1 foot 4 inches away*/ && getDistanceUsingUltrasonic() > 42){
    if(/*vision target is on the left*/){
      //tell chassis to move left
    } else if(/*vision target is on the right*/){
      //tell chassis to move right
    } else if(/*vision target is centered*/){
      //tell chassis to move straight
    } else {
      //alert driver of vision tracking failure
    }
  } else if(getDistanceUsingUltrasonic() < 42){
    if(/*vision target is more than 2 degrees to the left*/){
      //strafe left slowly
    } else if(/*vision target is more than 2 degrees to the right*/){
      //strafe right slowly
    } else if(/*vision target is between 0 and 1 degrees off*/){
      if(/*left color sensor detects line*/){
        //turn to the left a tiny bit
      } else if(/*right color sensor detects line*/){
        //turn to the right a tiny bit
      } else if(/*neither color sensor detects line*/){
        //drive straight
      }
    }
  } else if(getDistanceUsingUltrasonic() < ){
    //tell rio clear to drop object
  } else {
    //alert driver of line up failure
  }
  
}

void loop(){
  driverAssist();
}