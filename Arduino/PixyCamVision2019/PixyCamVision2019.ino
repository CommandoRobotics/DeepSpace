#include <Pixy2.h>
#include <math.h>
#include <Wire.h>
#include "slaveVisionCommunications.h"
#include "visionTransmission.h"

//Pixy object
Pixy2 pixy;


double realWidth = 2;
double realHeight = 5.5;
double calHF = 277.75;
double calWF = 380;

//Reported calculated distance, pixels, etc. T=total or average
double allObjHDistance[10]; 
double allObjWDistance[10]; 
int allObjPixelHeight[10]; 
int allObjPixelWidth[10]; 
int allObjX[10];
int allObjY[10];
double pi = 3.14159265358979323846264338327950288419716939937510;
boolean reliable;

//In order to create groups of two seperate identities, I have created a group structure to store two different identities; 
struct Group {
  double averageDis;
  double averageX;
  double xBetween;
  int leftObj;
  int rightObj;
};
struct Group group[3]; 
int best; //used for best, or most realtive group (pairs/groups are the same thing)

//Reporting Variables: Vairiables used for commuMnicating/reporting height etc.
double angleToTurn;
int angledLeftRight; //0 = centered, 1 = left, 2 = right, 3 = failed
double disLeft;
double disRight;
double disForward;



void setup() {
  Serial.begin(9600);
  pixy.init();
  setupVisionCommunications(8);
}

//Current methods: getObjectsProperties(), calculateDistance(), nearestPair(), getPairs(), getBestPair(), getAngledLeftRight(), getParallel(), setAll(), transactData(),
void loop() {

  //VISION
  
  reliable = true;
  pixy.ccc.getBlocks();
  getBestPair();        //Runs getObjectsProperties, calculateDistance(), getPairs(), and nearestPair() at once
  getAngledLeftRight(); //Gets if we are angled left right or centered to the target
  moveToTarget();       //Gets angle to turn to be parallel and the distance left/right and forward we need to travel to get to the target every time

  Serial.print("RELIABLE??? ");
  Serial.println(reliable);
  
  updateDataForReplyToMaster(reliable, angleToTurn, group[best].averageDis, getPercentOff()); //Sends data over I2c

  //DEBUGGING CODE  
  
//Serial.print("Distance Right ");
//  //Serial.println(disRight);
  Serial.print("Angle to turn ");
  Serial.println(angleToTurn);
//  Serial.print("Distnance Forward ");
//  Serial.println(disForward);
  Serial.print("Angled number ");
  Serial.println(angledLeftRight);
//  Serial.println();
  Serial.print("Obj right X ");
  Serial.println(allObjX[group[best].rightObj]);
  Serial.print("Obj left X ");
  Serial.println(allObjX[group[best].leftObj]);
//  Serial.print("group X ");
//  Serial.println(group[best].averageX);
//  Serial.println();
//  Serial.print("best: ");
//  Serial.println(best);
//  Serial.print("distance to center ") ;
//  Serial.println(allObjHDistance[0]);
//  Serial.println(allObjHDistance[1]);
  Serial.print("Distnace to average center ");
  Serial.println(group[best].averageDis);
  Serial.print("parallel?? ");
  Serial.println(getParallel());
//  Serial.println();
//  Serial.print("number of obj detected: ");
// Serial.println(pixy.ccc.getBlocks());
//  Serial.print("Left object X position: "); 
//  Serial.print(allObjX[group[best].leftObj]);
//  Serial.print(" Left object Dis: "); 
//  Serial.println(allObjHDistance[group[best].leftObj]);
//  Serial.print("Rigth object X position: "); 
//  Serial.print(allObjX[group[best].rightObj]);
//  Serial.print(" Right object Dis: "); 
//  Serial.println(allObjHDistance[group[best].rightObj]);
  Serial.print("getPercentOff ");
  Serial.println(getPercentOff());
  Serial.println();
}


//Runs all methods required to get a best pair
void getBestPair() { 
  pixy.ccc.getBlocks();
  getObjectsProperties();
  calculateDistance();
  getPairs();
  nearestPair();
}


//Gets up to 10 objects properties (height, width etc.)
void getObjectsProperties() {
  for (int n=0; n<pixy.ccc.getBlocks(); n++) {
    allObjPixelHeight[n] = pixy.ccc.blocks[n].m_height;
    allObjPixelWidth[n] = pixy.ccc.blocks[n].m_width;
    allObjX[n] = pixy.ccc.blocks[n].m_x;
    allObjY[n] = pixy.ccc.blocks[n].m_y;
    //Serial.print("Object in Height Order ");
    //Serial.println(allObjPixelHeight[n]);
    //Serial.print("Object in Width Order ");
    //Serial.println(allObjPixelWidth[n]);
  }
}


//calculates distance for up to 10 objects
void calculateDistance() {
  for (int n=0; n<pixy.ccc.getBlocks(); n++) {
    allObjHDistance[n] = (calHF*realHeight)/allObjPixelHeight[n];
    allObjWDistance[n] = (calWF*realWidth)/allObjPixelWidth[n];
  }
}


//Determine best pair available (closest to center)
void nearestPair() { 
  if (pixy.ccc.getBlocks() == 2) {
    best = 0;
  } else {
    best = getMiddleVal(group[0].xBetween, group[1].xBetween, group[2].xBetween);
  }
}


//Take the number of objects, determine which are "pairs" of the same hatch using distance between
//them and then store them as "group" structures to be used later. 
void getPairs() {
  if (pixy.ccc.getBlocks() < 2) {
    reliable = false;
  } else if (pixy.ccc.getBlocks() == 2) {
    if (allObjX[0]>allObjX[1]) {     //If only two objects, must be pair, (might get rid of later)
      group[0].averageX = abs((allObjX[0]+allObjX[1])/2);
      group[0].averageDis = abs((allObjHDistance[0]+allObjHDistance[1])/2);
      group[0].leftObj = 1;
      group[0].rightObj = 0;
    } else { 
      group[0].averageX = abs((allObjX[1]+allObjX[0])/2);
      group[0].averageDis = abs((allObjHDistance[1]+allObjHDistance[0])/2);
      group[0].leftObj = 0;
      group[0].rightObj = 1;
    }
  } else if (pixy.ccc.getBlocks() > 2) {      //if more than two objects
    int FIRSTClosest = 0;
    int secondClosest = 1;
    int  thirdClosest = 2;
    double FIRSTClosestDis  = 10000000;
    double secondClosestDis = 100000000;
    double thirdClosestDis  = 1000000000;
    boolean tie = false;
    for (int n=0; n<pixy.ccc.getBlocks(); n++) {
      //Determine the three closest objects and store them to be used next
      if (allObjHDistance[n] < FIRSTClosestDis) {
        thirdClosestDis = secondClosestDis;
        secondClosestDis = FIRSTClosestDis;
        FIRSTClosestDis = allObjHDistance[n]; 
        thirdClosest = secondClosest;
        secondClosest = FIRSTClosest;
        FIRSTClosest = n; 
      } else if (allObjHDistance[n] < secondClosestDis) {
        thirdClosest = secondClosest;
        secondClosest = n;
        thirdClosestDis = secondClosestDis;
        secondClosestDis = allObjHDistance[n];
      } else if (allObjHDistance[n] < thirdClosestDis) {
        thirdClosest = n;
        thirdClosestDis = allObjHDistance[n];
      } else if (allObjHDistance[n] == FIRSTClosestDis || allObjHDistance[n] == secondClosestDis || allObjHDistance[n] == thirdClosestDis) {
        tie = true;
        reliable = false;
      }   
    }
    
    //Sets up the first group between the three closest obj
    if (allObjX[FIRSTClosest]<allObjX[secondClosest]) { //make group between first and second obj found [0]
      group[0].averageX = abs((allObjX[FIRSTClosest]+allObjX[secondClosest])/2);
      group[0].averageDis = abs((allObjHDistance[FIRSTClosest]+allObjHDistance[secondClosest])/2);
      group[0].leftObj = FIRSTClosest;
      group[0].rightObj = secondClosest;
      group[0].xBetween = abs(allObjX[FIRSTClosest]-allObjX[secondClosest]);
    } else {
      group[0].averageX = abs((allObjX[FIRSTClosest]+allObjX[secondClosest])/2);
      group[0].averageDis = abs((allObjHDistance[FIRSTClosest]+allObjHDistance[secondClosest])/2);
      group[0].leftObj = secondClosest;
      group[0].rightObj = FIRSTClosest;
      group[0].xBetween = abs(allObjX[FIRSTClosest]-allObjX[secondClosest]);
    }

    //Sets up the second group between the three closest obj
    if (allObjX[secondClosest]<allObjX[thirdClosest]) { //make group between second and third object [1]
      group[1].averageX = abs((allObjX[secondClosest]+allObjX[thirdClosest])/2);
      group[1].averageDis = abs((allObjHDistance[secondClosest]+allObjHDistance[thirdClosest])/2);
      group[1].leftObj = secondClosest;
      group[1].rightObj = thirdClosest;
      group[1].xBetween = abs(allObjX[secondClosest]-allObjX[thirdClosest]);
    } else {
      group[1].averageX = abs((allObjX[secondClosest]+allObjX[thirdClosest])/2);
      group[1].averageDis = abs((allObjHDistance[secondClosest]+allObjHDistance[thirdClosest])/2);
      group[1].leftObj = secondClosest;
      group[1].rightObj = thirdClosest;
      group[1].xBetween = abs(allObjX[secondClosest]-allObjX[thirdClosest]);
    }
    
    //Sets the thrid group between the three closest obj
    if (allObjX[FIRSTClosest]<allObjX[thirdClosest]) { //make group between first and third object [2]
      group[2].averageX = abs((allObjX[FIRSTClosest]+allObjX[thirdClosest])/2);
      group[2].averageDis = abs((allObjHDistance[FIRSTClosest]+allObjHDistance[thirdClosest])/2);
      group[2].leftObj = FIRSTClosest;
      group[2].rightObj = thirdClosest;
      group[2].xBetween = abs(allObjX[FIRSTClosest]-allObjX[thirdClosest]);
    } else {
      group[2].averageX = abs((allObjX[FIRSTClosest]+allObjX[thirdClosest])/2);
      group[2].averageDis = abs((allObjHDistance[FIRSTClosest]+allObjHDistance[thirdClosest])/2);
      group[2].leftObj = thirdClosest;
      group[2].rightObj = FIRSTClosest;
      group[2].xBetween = abs(allObjX[FIRSTClosest]-allObjX[thirdClosest]);
    }
  }
}


//I know distance to pairs and thats it. Time to process angled left/right/centered/parallel to formula/ not parallel/ dis left/right/forward/angle
int getAngledLeftRight() {
  //check if angled left right or centered
  if (abs(allObjPixelWidth[group[best].leftObj] - allObjPixelWidth[group[best].rightObj]) <= 5 && group[best].averageDis>=20 && getParallel() && abs(group[best].averageX-160)<20) { //If distance is greater than 15, check if pixel difference is less than 3
    angledLeftRight = 0;
  } else if (abs(allObjPixelWidth[group[best].leftObj] - allObjPixelWidth[group[best].rightObj]) <= 12 && group[best].averageDis<20 && getParallel() && abs(group[best].averageX-160)<20) { //If distance is less than 15, check if pixel difference is less than 7
    angledLeftRight = 0;
  } else if (getParallel()) {
     if (abs(group[best].averageX-160) <= 10) {
      angledLeftRight = 0;
     } else if (group[best].averageX < 160) {
      angledLeftRight = 1;
     } else if (group[best].averageX >= 160) {
      angledLeftRight = 2;
     }
  } else if (allObjPixelWidth[group[best].leftObj] > allObjPixelWidth[group[best].rightObj] || allObjPixelHeight[group[best].leftObj] > allObjPixelHeight[group[best].rightObj] ) {
    angledLeftRight = 1;
  } else if (allObjPixelWidth[group[best].rightObj] > allObjPixelWidth[group[best].leftObj] || allObjPixelHeight[group[best].rightObj] > allObjPixelHeight[group[best].leftObj] ) {
    angledLeftRight = 2;
  } else {
    angledLeftRight = 3;
    reliable = false;
  }  
}


//get if we are parallel to the wall using width of ALL object pairs
boolean getParallel() {
  return (abs(allObjPixelWidth[group[best].leftObj] - allObjPixelWidth[group[best].rightObj]) < 4 && abs(allObjPixelHeight[group[best].leftObj] - allObjPixelHeight[group[best].rightObj]) < 5);
}

//Get the percent off the X value in order to strafe
double getPercentOff() {
  double percentOff;
  //group[best].averageX = 80;
  if (angledLeftRight==1) {
    percentOff = -(abs(group[best].averageX-160))/160;
  } else if (angledLeftRight == 2) {
    percentOff = (abs(group[best].averageX-160))/160;
    Serial.println(-(abs(group[best].averageX-160))/160);
  } else if (angledLeftRight == 0) {
    percentOff = 0;
  }
  return percentOff;
}


//Gets the middle distance value between pairs and then returns the correct pair
int getMiddleVal(double first, double second, double third) {
  if ((first >= second && first <= third) || (first <= second && first >= third)) {return 0;}
  else if ((second >= first && second <= third) || (second <= first && second >= third)) {return 1;}
  else if ((third >= second && third <= first) || (third <= second && third >= first)) {return 2;} 
}


//Determines the angle to turn to be parallel, the ditance right/left, and the distance forward needed to line up with the target
void moveToTarget() {
  double insideAngle;
    if (angledLeftRight == 1) {
      double c = allObjHDistance[group[best].leftObj];
      double b = 5.63;
      double a = group[best].averageDis;
      insideAngle = acos((pow(a,2)+pow(b,2)-pow(c,2))/(2*a*b));
      Serial.print("Angle is ");
      Serial.println((insideAngle*180)/pi);
      angleToTurn = 90-((insideAngle*180)/pi);
      Serial.print("Angle to turn to be parallel is ");
      Serial.println(angleToTurn);
      disRight = ((cos(insideAngle)*a)*180)/pi;
      disForward = ((sin(insideAngle)*a)*180)/pi;
    } else if (angledLeftRight == 2) {
      double c = allObjHDistance[group[best].rightObj];
      double b = 5.63;
      double a = group[best].averageDis;
      insideAngle = acos((pow(a,2)+pow(b,2)-pow(c,2))/(2*a*b));
      Serial.print("Angle is ");
      Serial.println(-((insideAngle*180)/pi));
      angleToTurn = ((insideAngle*180)/pi)-90;
      Serial.print("Angle to turn to be parallel is ");
      Serial.println(angleToTurn);
      disRight = ((cos(insideAngle)*a)*180)/pi;
      disForward = ((sin(insideAngle)*a)*180)/pi;
    } else if (angledLeftRight == 0) {
      insideAngle = 0;
      angleToTurn = 0;
    }
}
