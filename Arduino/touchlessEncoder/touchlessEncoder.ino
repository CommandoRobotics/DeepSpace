void setup() {
  pinMode( 5 , INPUT );
  pinMode(LED_BUILTIN, OUTPUT); //LED FOR TEST

  digitalWrite(LED_BUILTIN, LOW);
}

void loop() {
  if( digitalRead(5) == LOW ){
      digitalWrite(LED_BUILTIN, HIGH);
  }else{
      digitalWrite(LED_BUILTIN, LOW);
  }
}
