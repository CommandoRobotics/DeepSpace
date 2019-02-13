package frc.apis;

public class SerialData {

    private byte[] data;
    private byte[] incomingData;
    private int validBytes;
    private static final int SERIAL_DATA_LENGTH = 16;
    private static final char[] START_CHARACTERS = new char[]{'g', 'b'};

    private long timeDataReceived;
    private static final long MAXIMUM_DATA_INTEGRITY_TIME = 2_000_000_000; //Nanoseconds

    public SerialData() {
        data = new byte[SERIAL_DATA_LENGTH];
        incomingData = new byte[SERIAL_DATA_LENGTH];
        validBytes = 0;
        timeDataReceived = 0;
    }

    public SerialData(byte[] data) {
        data = new byte[SERIAL_DATA_LENGTH];
        incomingData = new byte[SERIAL_DATA_LENGTH];
        setData(data);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        for(int i = 0; i < data.length; i++) {
            byte inputByte = data[i];

            if(startCharacter(inputByte)) {
                validBytes = 0;
                data = incomingData.clone();
                incomingData = new byte[]{SERIAL_DATA_LENGTH};
            }

            this.incomingData[validBytes] = data[i];
        }

        this.timeDataReceived = System.nanoTime();
    }    

    public boolean dataGood() {
        return System.nanoTime() - timeDataReceived < MAXIMUM_DATA_INTEGRITY_TIME;
    }

    public boolean startCharacter(byte input) {
        for(char character : START_CHARACTERS) {
            if((char) input == character) return true;
        }

        return false;
    }

}