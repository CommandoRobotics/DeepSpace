#include <Wire.h>

void setup() {
  const int CAMERA_ADDRESS = 8;
  const int LINE_ADDRESS = 9;
  int returnedData;
  int randomAnalogPin = 1; //this is the pin that should be used for the random number generator. THIS MUST BE AN EMPTY ANALOG PIN ON THE ARDUINO
  int randomNumber;
  Wire.begin();
  Serial.begin(9600);
  randomSeed(analogRead(randomAnalogPin));
}

void loop() {

  void requestData(int targetDeviceInput){
    if(targetDeviceInput == 1){
      int targetDevice = CAMERA_ADDRESS;
    } else if(targetDeviceInput == 2){
      int targetDevice = LINE_ADDRESS;
    }

    if(targetDevice == CAMERA_ADDRESS || targetDevice == LINE_ADDRESS){
      if(targetDevice == CAMERA_ADDRESS){
        Wire.requestFrom(CAMERA_ADDRESS, 1);
      } else if(targetDevice == LINE_ADDRESS){
        Wire.requestFrom(LINE_ADDRESS, 1);
      }

      int i2cReturn = Wire.read();
      return i2cReturn;
      
    }

   void sendData(int targetDeviceInput, int dataToTransfer){
    if(targetDeviceInput == 1){
      int targetDevice = CAMERA_ADDRESS;
    } else if(targetDeviceInput == 2){
      int targetDevice = LINE_ADDRESS;
    }

    Wire.beginTransmission(targetDevice);
    Wire.write(dataToTransfer);
    Wire.endTransmission();
    
   }

  }
  
  Serial.println("Requesting Data from Camera");
  Serial.println("Camera Says");
  Serial.println(requestData(1));
  delay(1000);
  Serial.println("Requesting Data from Line");
  Serial.println("Line Says");
  Serial.println(requestData(2));
  delay(1000);
  randomNumber = random(1, 9);
  Serial.println("Sending data to Camera");
  sendData(1, randomNumber);
  Serial.println("Sent data to Camera");
  Serial.println(randomNumber);
  delay(1000);
  randomNumber = random(1,9);
  Serial.println("Sending data to Line");
  sendData(2, randomNumber);
  Serial.println("Sent data to Line");
  Serial.println(randomNumber);
  delay(1000);
  
  
  
}
