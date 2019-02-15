// Provides communications to the RoboRio
// Commando Robotics - 2019 - Deep Space
#ifndef rioCommunication_h
#define rioCommunication_h

// Applies boundaries of 0.0 to 1.0 and returns the corrected value.
double apply_bounds(double value) {
  if (value < 0.0) {
    value = 0.0;
  } else if ( value > 1.0) {
    value = 1.0;
  }
  return value;
}

// Copies the number into the buffer, adding leading 0's if the number is shorter than three digits.
void addLeadingZeros(int number, char* theBuffer, int charactersWritten) {
  // Convert the number to a character string
  char characters[4] = {0};
  itoa(number, characters, 10);
  
  // If our number is shorter than 3 digits, we need to add leading zeros.
  if (number < 10) {
    theBuffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
  } else if (number < 100) {
    theBuffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = characters[1];
    charactersWritten = charactersWritten + 1;
  } else {
    theBuffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = characters[1];
    charactersWritten = charactersWritten + 1;
    theBuffer[charactersWritten] = characters[2];
    charactersWritten = charactersWritten + 1;
  }
}

// Adds the number to the buffer in the appropriate format for the Rio interface.
// dataCodeLetter: The character in the protocol that indicates this type of data (x=left/right, z=forward/backward, r=rotation).
// value: A percentage (0.0-1.0) representing the telemetry value (what the joystick percentage would be in this direction).
// buffer: Pointer to the send buffer.
// countOfBytesWritten: How many bytes have already been written into the buffer. This allows the function to determine where the telemetry needs to be added, so it does not overwrite existing data.
// Returns: The number of bytes written to the buffer. You should add this to countsOfBytesWritten before calling this function next time.
int addTelemetryInfoToBuffer(char dataCodeLetter, double value, char* theBuffer, int countOfBytesWritten) {
  // Start with the code letter
  theBuffer[countOfBytesWritten] = dataCodeLetter;
  countOfBytesWritten = countOfBytesWritten + 1;

  // Include the appropriate sign
  if (value >= 0.0) {
    theBuffer[countOfBytesWritten] = '+';
  } else {
    theBuffer[countOfBytesWritten] = '-';
  }
  countOfBytesWritten = countOfBytesWritten + 1;

  // Handle the case that we got too big of a number.
  if (abs(value) > 1.0) {
    value = 1.0;
  }

  // Convert the number to a 0-100 whole number, required by our protocol.
  int wholeNumber = value * 100.0; // This converts the value to whole number, trimming off the decimal place (not rounding).

  addLeadingZeros(wholeNumber, theBuffer, countOfBytesWritten);
  countOfBytesWritten = countOfBytesWritten + 3;
  
  return countOfBytesWritten;
}

// Returns the number of characters required to represent the nu
int number_of_digits(int value) {
  // Determine if we need
  int signLength = 0;
  if (value < 0) {
      signLength = 1;
  }
  int numberOfPlaces = 0;
  if (value == 100 || value == -100) {
    numberOfPlaces = 3;
  } else if (value == 0) {
    numberOfPlaces = 1;
  } else {
      numberOfPlaces = 2;
  }

  return signLength + numberOfPlaces;
}

// Uses the serial
void sendTelemetryToRio(bool trustMe, double forward_percentage, double right_strafe_percentage, double clockwise_rotation_percentage) {
      forward_percentage = apply_bounds(forward_percentage);
      right_strafe_percentage = apply_bounds(right_strafe_percentage);
      clockwise_rotation_percentage = apply_bounds(clockwise_rotation_percentage);

      // We will send one character for the good/bad, and up to four characters for each number (0-100) with a signs
      const int buffer_length = 1 + 3 * 4;
      char send_buffer[buffer_length] = {0}; // The zero in curly braces initializes the array to zero.
      if (trustMe) {
        int bytes_written = 0;
        send_buffer[bytes_written] = 'g';
        Serial.write(send_buffer, bytes_written);
        bytes_written += 1;
        bytes_written += addTelemetryInfoToBuffer('z', forward_percentage, send_buffer, bytes_written);
        //Serial.println("Stuck 3");
        bytes_written += addTelemetryInfoToBuffer('x', right_strafe_percentage, send_buffer, bytes_written);
        //Serial.println("Stuck 4");
        bytes_written += addTelemetryInfoToBuffer('r', clockwise_rotation_percentage, send_buffer, bytes_written);
        //Serial.println("Stuck 5");

        // Added a line return at the end.
        send_buffer[bytes_written] = '\n';
        bytes_written += 1;
        Serial.write(send_buffer, bytes_written);


      } else {
        send_buffer[0] = 'b';
        send_buffer[1] = '\n';
        Serial.write(send_buffer, 2);
      }
}
#endif
