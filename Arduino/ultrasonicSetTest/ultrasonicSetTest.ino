#include <math.h>

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
  return center;
}

double convertToDegrees(double r) {
  return r*180.0/3.14159;
}

double getRotationInDegrees() {
  double disBetweenSensors = 10; //Believe it or not this is the distance between the sensors on the robot
  double angleInRadians = atan(abs(left-right)/(disBetweenSensors)); //returns angle to be parallel
  if (left < right) {
    return convertToDegrees(angleInRadians);
  } else if (right < left) {
    return -(convertToDegrees(angleInRadians));
  } else if (right == left) {
    return 0;
  }
}

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

void setup() {
    Serial.begin (9600);
  setupElegooUltrasonicSensor();
}

void loop() {
  updateUltrasonic();
  if (isUltrasonicGood()) {
    Serial.print(left);
    Serial.print(",");
    Serial.print(right);
    Serial.print (" ");
    Serial.print(getDistance());
    Serial.print("in. @");
    Serial.print(getRotationInDegrees());
    Serial.print("°");
  } else {
    Serial.print("bad data");
  }
  Serial.print("\n");
  delay(1000);
}
