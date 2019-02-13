// Interace to the AutoPilot Arduino.
// Commando Robotics - 2019 - Deep Space


// Declare a variable for timeout we required the Arduino to send by before we declare it to be dead or the data too old to drive by.
// Declare a variable to store the time we last got 'good' data from the Arduino.
// Declare a byte array big enough to hold our message (1 byte four good + 5 bytes for each  value = 16 bytes total).
// Declare a count of how many valid bytes you've recieved (in case you get part of a message one time you check and the rest the next time).

// The following variables will come from the Arduino. Declare a variable for each.
// Is the AutoPilot data good/bad. If the AutoPilot knows it should not be trusted, it tells us.
// z% - The percentage (0-100) we want to drive foward (negative indicates backwards).
// y% - The percentage (0-100) we want to strafe right (negative indicates left).
// rotation% - The percentage (0-100) we want to rotate right/clockwise (negative indicates counter-clockwise/left)

// Call the following code repeatedly.
// Check for data on the serial line.
// Process the telemtry (see below).
// If the time of the last Arduino message plus the timeout has passed, indicate the data as old and set motor powers to zero.

// Processing telemtry
// Get serial data
// If you already have some data (the count variable above is greater than zero), continue to copy the bytes into the array.
// If you do not have any data yet (the count variable is zero), read until you get a 'g' in it and copy the serial data into the message buffer.
// If you have a complete message (the count variable should equal the max size of the buffer), then set the motors appropriately and set the 'last recieved message' variable to NOW.
