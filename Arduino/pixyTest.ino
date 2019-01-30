#include <SPI.h>  
#include <Pixy2.h>
Pixy2 pixy2;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1200);
  pixy2.init();
  
}

void loop() {
  // put your main code here, to run repeatedly:
  pixy2.ccc.getBlocks();
  //  Serial.print("X coordinate ");
  //  Serial.println(pixy2.ccc.blocks[0].m_x);
  
  //  HIGH = turn, LOW = nothing, HIGH && HIGH = straight
  
  if (pixy2.ccc.blocks[0].m_x > 200) {
    digitalWrite(8, HIGH); //turn left
    Serial.print("left");
//    Serial.print('\n');
//    delay(500);
  }
  
  if (pixy2.ccc.blocks[0].m_x < 100) {
    digitalWrite(9, HIGH); //turn right
    Serial.print("right");
//    Serial.print('\n');
//    delay(500);
  }
  
  if (pixy2.ccc.blocks[0].m_x > 100 && pixy2.ccc.blocks[0].m_x < 200) {
    digitalWrite(8, HIGH); 
    digitalWrite (9, HIGH);
    Serial.print("straight");
//    Serial.print('\n');
//    delay(500);
  }
}
