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
        bytes_written += 1;

        // Convert each number to a 0-100 whole number (no decimal).
        int forward_whole_number = forward_percentage * 100.0;
        int strafe_whole_number = right_strafe_percentage * 100.0;
        int rotation_whole_number = clockwise_rotation_percentage * 100.0;

        // Convert each number to a string, copy it into the buffer (without the ending line break), and then do the next number.
        char temporary_buffer[4] = {0};
        // Foward
        send_buffer[bytes_written] = 'z';
        bytes_written += 1;
        itoa(forward_whole_number, temporary_buffer, 10);
        memcpy(&send_buffer[bytes_written], temporary_buffer, number_of_digits(forward_whole_number));
        bytes_written += number_of_digits(forward_whole_number);
        // Strafe
        send_buffer[bytes_written] = 'x';
        bytes_written += 1;
        itoa(strafe_whole_number, temporary_buffer, 10);
        memcpy(&send_buffer[bytes_written], temporary_buffer, number_of_digits(strafe_whole_number));
        bytes_written += number_of_digits(strafe_whole_number);
        // Rotation
        send_buffer[bytes_written] = 'r';
        bytes_written += 1;
        itoa(rotation_whole_number, temporary_buffer, 10);
        memcpy(&send_buffer[bytes_written], temporary_buffer, number_of_digits(rotation_whole_number));
        bytes_written += number_of_digits(rotation_whole_number);
        // Added a line return at the end.
        send_buffer[bytes_written] = '\n';
        bytes_written += 1;

        // Send the whole message
        Serial.write(send_buffer, bytes_written);

      } else {
        send_buffer[0] = 'b';
        send_buffer[1] = '\n';
        Serial.write(send_buffer, buffer_length);
      }
}
#endif
