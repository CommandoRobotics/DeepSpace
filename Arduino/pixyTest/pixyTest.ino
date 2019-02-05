#include <SPI.h>  
#include <Pixy2.h>
Pixy2 pixy2;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1200);
  pixy2.init();
}

//Prints YOUR location in refrence to the Line

void loop() {
  // put your main code here, to run repeatedly:
  pixy2.ccc.getBlocks();
  
  
  if (pixy2.ccc.blocks[0].m_x > 0 && pixy2.ccc.blocks[0].m_x < 50) {
    digitalWrite(6, HIGH); //turn left a lot
    Serial.print("1 1/4 in to the left");
    Serial.print('\n');
    delay(500);
  } 
  if(pixy2.ccc.blocks[0].m_x > 50 && pixy2.ccc.blocks[0].m_x < 125) {
    digitalWrite(7, HIGH); //turn left a little
    Serial.print("1/2 in to the left");
    Serial.print('\n');
    delay(500);
  }
  if (pixy2.ccc.blocks[0].m_x > 125 && pixy2.ccc.blocks[0].m_x < 175) {
    digitalWrite(6, HIGH); 
    digitalWrite (7, HIGH);
    digitalWrite(8, HIGH); 
    digitalWrite (9, HIGH); //go straight
    Serial.print("straight");
    Serial.print('\n');
    delay(500);
  }
  if (pixy2.ccc.blocks[0].m_x > 175 && pixy2.ccc.blocks[0].m_x < 250) {
    digitalWrite(8, HIGH); //turn right a little
    Serial.print("1/2 in to the right");
    Serial.print('\n');
    delay(500);
  }
  if (pixy2.ccc.blocks[0].m_x > 250 && pixy2.ccc.blocks[0].m_x < 300) {
    digitalWrite(9, HIGH); //turn right a lot
    Serial.print("1 1/4 in to the right");
    Serial.print('\n');
    delay(500);
  }
}
