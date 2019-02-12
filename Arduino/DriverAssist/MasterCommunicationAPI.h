#include <Wire.h>

#ifndef MASTER_COMMUNICATION_API
#define MASTER_COMMUNICATION_API

const int MAX_BYTES = 8;
int table [MAX_BYTES] = {};

typedef unsigned char uchar;

void setupCommunications() {
  Wire.begin ();   
  Serial.begin (9600); 
}

void requestData(int slaveAddress, int numberOfBytes) {
  Wire.requestFrom(slaveAddress, numberOfBytes);
   for(int i = 0; i < numberOfBytes; i++) {
    table[i] = Wire.read();
   }
}

int getData(int index) {
  if(index >= 0 && index < MAX_BYTES) {
    return table[index];
  }
  return 0;
}

int * getData() {
  return table;
}

float getFloatFromBytes(char byte0, char byte1, char byte2, char byte3) {
  float f;
  char b[] = {byte0, byte1, byte2, byte3};
  memcpy(&f, &b, sizeof(f));
  return f;
}

float getFloatFromBytes(int startByte) {
  float f;
  char b[] = {table[startByte], table[startByte + 1], table[startByte + 2], table[startByte + 3]};
  memcpy(&f, &b, sizeof(f));
  return f;
}

#endif
