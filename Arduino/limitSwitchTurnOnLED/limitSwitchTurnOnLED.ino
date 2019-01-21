void setup() {
  // put your setup code here, to run once:
  pinMode( 6 , INPUT );
  pinMode(LED_BUILTIN, OUTPUT); //LED FOR TEST

  digitalWrite(LED_BUILTIN, LOW);
}

void loop() {
  // put your main code here, to run repeatedly:
  if( digitalRead(6) == LOW ){
      digitalWrite(LED_BUILTIN, HIGH);
  }else{
      digitalWrite(LED_BUILTIN, LOW);
  }
}
