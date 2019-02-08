#ifndef MASTER_COMMUNICATION_API
#define MASTER_COMMUNICATION_API

const int MAX_BYTES = 6;
int[] table = new int[MAX_BYTES];

void setupCommunications() {
  Wire.begin ();   
  Serial.begin (9600); 
}

void requestData(int slaveAddress, int numberOfBytes) {
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

float getVisionPixyFloat() {
  requestData(8, MAX_BYTES);
  float f;
  uchar b[] = {getData(5), getData(4), getData(3), getData(2)};
  memcpy(&f, &b, sizeof(f));
  return f;
}

bool getVisionActive() {
  requestData(8, MAX_BYTES);
  return (int) getData(0) == 1;
}

#endif
