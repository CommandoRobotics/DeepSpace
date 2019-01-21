void setup(){
  
}

void driverAssist(){
  
  if(/*vision target is more than 1 foot 4 inches away*/){
    if(/*vision target is on the left*/){
      
    } else if(/*vision target is on the right*/){
      
    } else if(/*vision target is centered*/){
      
    } else {
      //alert driver of vision tracking failure
    }
  } else if(/*ultrasonic sensor is less than 1 ft 4 inches away but more than 2 inches away*/){
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
  } else if(/*ultrasonic sensor is less than or equal to 2 inches away*/){
    //tell rio clear to drop object
  } else {
    //alert driver of line up failure
  }
  
}

void loop(){
  driverAssistAutoAlign();
}