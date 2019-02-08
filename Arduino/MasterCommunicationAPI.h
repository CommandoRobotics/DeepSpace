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

#endif
