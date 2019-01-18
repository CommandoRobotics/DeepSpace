package frc.apis;
    
    public class PID {
        
        private double pGain, iGain, dGain;
        private double bias;
        private double target;
        private double previousError, accumulatedError;
        
        public PID(double pGain, double iGain, double dGain) {
            this.pGain = pGain;
            this.iGain = iGain;
            this.dGain = dGain;
            
            this.bias = 0;
            this.target = 0;
            this.previousError = this.accumulatedError = 0;
        }
        
        public double update(double currentValue, double deltaTime /*Time tracking for the robot might need to be performed on a global scale for consistency across mechanisms.*/) {
            double error = currentValue - target;
            this.accumulatedError += error * deltaTime;
            double errorChange = (error - this.previousError) / deltaTime;
            this.previousError = error;
            return bias + pGain * error + iGain * accumulatedError + dGain * errorChange;
        }
        
        public void setBias(double bias) {
            this.bias = bias;
        }
                
        public double getBias() {
            return bias;
        }
        
        public void setTarget(double target) {
            this.target = target;
        }
        
        public double getTarget() {
            return target;
        }
        
        public void reset() {
            this.previousError = this.accumulatedError = 0;
        }
    
    }