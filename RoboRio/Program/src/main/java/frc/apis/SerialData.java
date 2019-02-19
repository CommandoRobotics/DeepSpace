package frc.apis;

public class SerialData {

    private String data;
    private StringBuilder incomingData;
    private int numValidCharacters;
    private static final char[] START_CHARACTERS = new char[]{'g', 'b'};

    private long timeDataReceived;
    private static final long MAXIMUM_DATA_INTEGRITY_TIME = 2_000_000_000; //Nanoseconds

    public SerialData() {
        data = "";
        incomingData = new StringBuilder();
        numValidCharacters = 0;
        timeDataReceived = 0;
    }

    public SerialData(String data) {
        data = "";
        incomingData = new StringBuilder();
        numValidCharacters = 0;
        timeDataReceived = 0;
        processData(data);
    }

    public String getData() {
        return data;
    }

    public void processData(String incomingData) {
        for(int i = 0; i < incomingData.length(); i++) {
            char incomingChar = incomingData.charAt(i);

            if(startCharacter(incomingChar) && numValidCharacters > 0) {
                numValidCharacters = 0;
                if(this.incomingData.charAt(0) == 'g') {
                    data = this.incomingData.toString();
                    this.timeDataReceived = System.nanoTime();
                } else {
                    System.out.println("Got a bad data indicator.");
                }
                this.incomingData.setLength(0);
            }

            numValidCharacters++;
            this.incomingData.append(incomingChar);
        }

        System.out.println("Current Data: " + this.data);
        System.out.println("Incoming Data: " + data);

    }    

    public boolean dataGood() {
        return System.nanoTime() - timeDataReceived < MAXIMUM_DATA_INTEGRITY_TIME;
    }

    public boolean startCharacter(char input) {
        for(char character : START_CHARACTERS) {
            if(input == character) return true;
        }

        return false;
    }

    public long getTimeDataReceived() {
        return timeDataReceived;
    }

    public static double parsePercentage(SerialData serialData, char startingChar) {
        String data = serialData.getData();
        int startingCharIndex = -1;
        
        for(int i = 0; i < data.length(); i++) {
            if(data.charAt(i) == startingChar) {
                startingCharIndex = i;
                break;
            }
        }

        if(startingCharIndex == -1) return 0;

        double percentage = Double.parseDouble(data.subSequence(startingCharIndex + 2, startingCharIndex + 5).toString()) / 100;
        if(data.charAt(startingCharIndex + 1) == '-') percentage *= -1;

        return percentage;
    }

}