#include <SPI.h>  
#include <Pixy2.h>
Pixy2 pixy2;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1200);
  pixy2.init();
}

//Prints your location in percentages left or right of the line

float getLineRefrence() {
  float lineLocation, percentage;
  // put your main code here, to run repeatedly:
  pixy2.ccc.getBlocks();
  
  lineLocation = pixy2.ccc.blocks[0].m_x;
  percentage = (lineLocation - 157.5)/157.5;
  return percentage;
}

void loop() {
  Serial.print(getLineRefrence());
  Serial.print("%");
  Serial.print("\n");
}
