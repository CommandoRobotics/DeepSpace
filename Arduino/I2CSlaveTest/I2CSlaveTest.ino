#include <Wire.h>
int i2cAddress = 1; //This sets the address of the device

void setup() {
  int randomAnalogPin = 1; //this is the pin that should be used for the random number generator. THIS MUST BE AN EMPTY ANALOG PIN ON THE ARDUINO
  Wire.begin(i2cAddress);
  Serial.begin(9600);
  Wire.onRequest(requestEvent);
  Wire.onReceive(receiveEvent);
  randomSeed(analogRead(randomAnalogPin));
}

void loop() {

}

void requestEvent(){
  int randomNumber = random(1,9);
  Wire.write(randomNumber);
  Serial.println("Sent to master");
  Serial.println(randomNumber);
}

void receiveEvent(int howMany){
  Serial.println("Received from master");
  Serial.println(Wire.read());
}
