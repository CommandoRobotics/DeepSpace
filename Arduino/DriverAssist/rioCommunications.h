// Provides communications to the RoboRio
// Commando Robotics - 2019 - Deep Space
#ifndef rioCommunication_h
#define rioCommunication_h

// Applies boundaries of 0.0 to 1.0 and returns the corrected value.
double apply_bounds(double value) {
  if (value < -1.0) {
    value = -1.0;
  } else if ( value > 1.0) {
    value = 1.0;
  }
  return value;
}

// Copies the number into the buffer, adding leading 0's if the number is shorter than three digits.
void addLeadingZeros(int number, char *buffer, int charactersWritten) {
  // Convert the number to a character string
  char characters[4] = {0};
  itoa(number, characters, 10);

  // If our number is shorter than 3 digits, we need to add leading zeros.
  if (number < 10) {
    buffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
  } else if (number < 100) {
    buffer[charactersWritten] = '0';
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = characters[1];
    charactersWritten = charactersWritten + 1;
  } else {
    buffer[charactersWritten] = characters[0];
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = characters[1];
    charactersWritten = charactersWritten + 1;
    buffer[charactersWritten] = characters[2];
    charactersWritten = charactersWritten + 1;
  }
}

// Adds the number to the buffer in the appropriate format for the Rio interface.
// dataCodeLetter: The character in the protocol that indicates this type of data (x=left/right, z=forward/backward, r=rotation).
// value: A percentage (0.0-1.0) representing the telemetry value (what the joystick percentage would be in this direction).
// buffer: Pointer to the send buffer.
// countOfBytesWritten: How many bytes have already been written into the buffer. This allows the function to determine where the telemetry needs to be added, so it does not overwrite existing data.
// Returns: The number of bytes written to the buffer. You should add this to countsOfBytesWritten before calling this function next time.
int addTelemetryInfoToBuffer(char dataCodeLetter, double value, char* buffer, int countOfBytesWritten) {
  // Start with the code letter
  buffer[countOfBytesWritten] = dataCodeLetter;
  countOfBytesWritten = countOfBytesWritten + 1;

  // Include the appropriate sign
  if (value >= 0.0) {
    buffer[countOfBytesWritten] = '+';
  } else {
    buffer[countOfBytesWritten] = '-';
  }
  countOfBytesWritten = countOfBytesWritten + 1;

  // Convert the number to a 0-100 whole number, required by our protocol.
  int wholeNumber = abs(value) * 100.0; // This converts the value to whole number, trimming off the decimal place (not rounding).
  addLeadingZeros(wholeNumber, buffer, countOfBytesWritten);
  countOfBytesWritten = countOfBytesWritten + 3;
  return countOfBytesWritten;
}

// Uses the serial
void sendTelemetryToRio(bool trustMe, double forward_percentage, double right_strafe_percentage, double clockwise_rotation_percentage) {
      forward_percentage = apply_bounds(forward_percentage);
      right_strafe_percentage = apply_bounds(right_strafe_percentage);
      clockwise_rotation_percentage = apply_bounds(clockwise_rotation_percentage);

      // We will send one character for the good/bad, one character and sign plus three numbers for each piece of telemtery data, plus a line ending.
      const int buffer_length = 1 + 3 * 5 + 1;
      char send_buffer[buffer_length] = {0}; // The zero in curly braces initializes the array to zero.

      if (trustMe) {
        int bytes_written = 0;
        send_buffer[bytes_written] = 'g';
        bytes_written += 1;
        bytes_written = addTelemetryInfoToBuffer('z', forward_percentage, send_buffer, bytes_written);
        bytes_written = addTelemetryInfoToBuffer('x', right_strafe_percentage, send_buffer, bytes_written);
        bytes_written = addTelemetryInfoToBuffer('r', clockwise_rotation_percentage, send_buffer, bytes_written);

        // Added a line return at the end.
        send_buffer[bytes_written] = '\n';
        bytes_written += 1;

        // Send the whole message+
        Serial.write(send_buffer, bytes_written);
        Serial.write("\n");

      } else {
        send_buffer[0] = 'b';
        send_buffer[1] = '\n';
        Serial.write(send_buffer, 2);
      }
}
#endif
