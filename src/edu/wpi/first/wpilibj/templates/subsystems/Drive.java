
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.TeleopArcadeDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder; 
import edu.wpi.first.wpilibj.Relay;
/**
 * Drivetrain subsystem
 */
public class Drive extends Subsystem {
    CANJaguar leftFrontJaguar,
              leftMiddleJaguar,
              leftBackJaguar,
              rightFrontJaguar,
              rightMiddleJaguar,
              rightBackJaguar;
    CANJaguar[] jags;
    double pLeftCurrent1, pRightCurrent1, pLeftCurrent2, pRightCurrent2;
    double tractionControlVariable, maxVoltSpikeParam;
    double[] priorVoltages;
    //double dt;
    //double ljerk, rjerk; //jerk variables for (bad) smooth drive
    static double[] SIGMOIDSTRETCH = {0.175, 0.325, 0.325, 0.175, 0.0, 0.0, 
                                   0.0, 0.0, 0.0, 0.0, 0.0, 0.0,  
                                   0.0};
    double[] leftTargetDeltas, rightTargetDeltas;
    double prevLeftTarget, prevRightTarget;
    double dampingConstant;
    public DoubleSolenoid shifterSolenoid;
    Encoder encoderR;
    Encoder encoderL;
    boolean shifterToggle;
    public Relay fans;
    double shiftUpThreshold, shiftDownThreshold;
    double pSpeedR, pSpeedL;
    public DoubleSolenoid.Value lowGear, highGear;
    long lastShiftTime;
    long shiftTimeThreshold;
    boolean isShifting; long shiftIdleTime;
    public Drive(){
        lastShiftTime = 0;
        pLeftCurrent1 = 0.0; pLeftCurrent2 = 0.0;
        pRightCurrent1 = 0.0; pRightCurrent2 = 0.0;
        shiftUpThreshold = 1600;
        shiftDownThreshold = 1400;
        pSpeedR = 0.0; pSpeedL = 0.0;
        isShifting = false; shiftIdleTime = 20;
        lowGear = DoubleSolenoid.Value.kReverse;
        highGear = DoubleSolenoid.Value.kForward;
        shiftTimeThreshold = 20;
        //dt = 1.0/30; //should probably be changed
        //ljerk = 100.0; rjerk = 100.0;
        leftFrontJaguar   = initJaguar(RobotMap.leftFrontJaguarID,   "leftFrontJaguar timeout");
        leftMiddleJaguar  = initJaguar(RobotMap.leftMiddleJaguarID,  "leftMiddleJaguar timeout");
        leftBackJaguar    = initJaguar(RobotMap.leftBackJaguarID,    "leftBackJaguar timeout");
        rightFrontJaguar  = initJaguar(RobotMap.rightFrontJaguarID,  "rightFrontJaguar timeout");
        rightMiddleJaguar = initJaguar(RobotMap.rightMiddleJaguarID, "rightMiddleJaguar timeout");
        rightBackJaguar   = initJaguar(RobotMap.rightBackJaguarID,   "rightBackJaguar timeout");
        try {
            leftBackJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            leftBackJaguar.configEncoderCodesPerRev(64);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            leftBackJaguar.setPID(200, 0, 0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            rightBackJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            rightBackJaguar.configEncoderCodesPerRev(64);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            rightBackJaguar.setPID(200, 0, 0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        jags = new CANJaguar[6];
        jags[0] = leftFrontJaguar;
        jags[1] = leftMiddleJaguar;  
        jags[2] = leftBackJaguar;
        jags[3] = rightFrontJaguar;
        jags[4] = rightMiddleJaguar;
        jags[5] = rightBackJaguar;
        leftTargetDeltas = new double[13];
        rightTargetDeltas = new double[13];
        for(int j = 0; j < leftTargetDeltas.length - 1; j++){
            leftTargetDeltas[j] = 0;
            rightTargetDeltas[j] = 0;
            dampingConstant = 1;        
        }
                //1 and 2 shifter
        //3 and 4 gate
        System.out.println("shifter Solenoid");
        shifterSolenoid = new DoubleSolenoid(1,2);
        
        shifterToggle = true;
        fans = new Relay(7, Relay.Direction.kForward);
        
        prevLeftTarget = 0;
        prevRightTarget = 0;
        priorVoltages = new double[6];
        tractionControlVariable = 0.2; //0.2 is not final; subject to change
        maxVoltSpikeParam = 2.0; //Again 2.0 is not final; subject to change
    }
    
    /*
    * Sets current for six-wheel jaguar drive configuration
    */
    public void tankDrive(double leftCurrent, double rightCurrent){
        boolean idle = false;
        if(isShifting){
            double time = System.currentTimeMillis();
            idle = (time-lastShiftTime <= shiftTimeThreshold);
            if(!idle){
                isShifting = false; //we are finished shifting
            }
        }
        if(idle){
            setJaguarCurrent(leftFrontJaguar,  0, "leftFrontJaguar timeout");
            setJaguarCurrent(leftMiddleJaguar, 0, "leftMiddleJaguar timeout");
            setJaguarCurrent(leftBackJaguar,   0, "leftBackJaguar timeout");
            setJaguarCurrent(rightFrontJaguar,  0, "rightFrontJaguar timeout");
            setJaguarCurrent(rightMiddleJaguar, 0, "rightMiddleJaguar timeout");
            setJaguarCurrent(rightBackJaguar,   0, "rightBackJaguar timeout");
        }
        else{
            setJaguarCurrent(leftFrontJaguar,  leftCurrent, "leftFrontJaguar timeout");
            setJaguarCurrent(leftMiddleJaguar, leftCurrent, "leftMiddleJaguar timeout");
            setJaguarCurrent(leftBackJaguar,   leftCurrent, "leftBackJaguar timeout");
            setJaguarCurrent(rightFrontJaguar,  rightCurrent, "rightFrontJaguar timeout");
            setJaguarCurrent(rightMiddleJaguar, rightCurrent, "rightMiddleJaguar timeout");
            setJaguarCurrent(rightBackJaguar,   rightCurrent, "rightBackJaguar timeout");
        }
        //always trick the smoothing into thinking we are running, even if we are idling
        //minor jerkiness is possible
        pLeftCurrent2 = pLeftCurrent1;
        pLeftCurrent1 = leftCurrent;
        pRightCurrent2 = pRightCurrent1;
        pRightCurrent1 = rightCurrent;
    }
    
    public void tankDriveAutoShift(double leftCurrent, double rightCurrent){
        double speedR = 0;
        try {
            speedR = rightBackJaguar.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        double speedL = 0;
        try {
            speedL = leftBackJaguar.getSpeed();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        //System.out.println("dl dr: " + speedL + " " + speedR);
        //decide whether a threshold has been crossed
        boolean upThreshL = (pSpeedL <= shiftUpThreshold && speedL >= shiftUpThreshold);
        boolean upThreshR = (pSpeedR <= shiftUpThreshold && speedR >= shiftUpThreshold);
        boolean downThreshL = (pSpeedL >= shiftDownThreshold && speedL <= shiftDownThreshold);
        boolean downThreshR = (pSpeedR >= shiftDownThreshold && speedR <= shiftDownThreshold);
        //time since last shift
        long time = System.currentTimeMillis();
        long timeSinceShift = time - lastShiftTime;
        boolean pastTimeThresh = (timeSinceShift >= shiftTimeThreshold);
        
        if((upThreshL && downThreshR) || (downThreshL && upThreshR)){ //do not shift if opposed
            
        }
        else if((upThreshL || upThreshR) && pastTimeThresh){
            this.shiftGear(highGear);
            //lastShiftTime = time;
            System.out.println("Upshift: " + speedL + speedR + time);
            
        }
        else if((downThreshL || downThreshR) && pastTimeThresh){
            this.shiftGear(lowGear);
            //lastShiftTime = time;
            System.out.println("Downshift" + speedL + speedR + time);
        }
        
        tankDrive(leftCurrent, rightCurrent);
        pSpeedR = speedR; pSpeedL = speedL;
    }
    
    /*
    * Implements arcade smooth drive by giving each side jerk that is constant in magnitude
    * It essentially switches a velocity v time graph from concave up and down
    * and oscillates around target currents
    */
    public void smoothDrive(double targetLeftCurrent, double targetRightCurrent){

        for(int i = leftTargetDeltas.length-1; i > 0; i--){
            leftTargetDeltas[i] = leftTargetDeltas[i-1];
            rightTargetDeltas[i] = rightTargetDeltas[i-1];
        }
        leftTargetDeltas[0] = targetLeftCurrent - prevLeftTarget;
        rightTargetDeltas[0] = targetRightCurrent - prevRightTarget;
        prevLeftTarget = targetLeftCurrent;
        prevRightTarget = targetRightCurrent;      
        for(int i = 0; i< SIGMOIDSTRETCH.length; i++){
            pLeftCurrent1 += leftTargetDeltas[i]*SIGMOIDSTRETCH[i]*dampingConstant;
            pRightCurrent1 += rightTargetDeltas[i]*SIGMOIDSTRETCH[i]*dampingConstant;
        }
        tankDriveAutoShift(pLeftCurrent1, pRightCurrent1);
    }
    
    public void arcadeDrive(double forward, double turn){
        double targetRightCurrent, targetLeftCurrent;
        targetRightCurrent = forward - turn;
        targetLeftCurrent = forward + turn;
        if(targetRightCurrent > 1){
            targetRightCurrent = 1;
        }
        else if(targetRightCurrent < -1){
            targetRightCurrent = -1;
        }
        if(targetLeftCurrent > 1){
            targetLeftCurrent = 1;
        }
        else if(targetRightCurrent < -1){
            targetLeftCurrent = -1;
        }
        smoothDrive(targetLeftCurrent, targetRightCurrent);
        //approach those currents smoothly
    }
    
    private CANJaguar initJaguar(int jagID, String error){
        CANJaguar jag = null;
        try{
            jag = new CANJaguar(jagID);
        }
        catch(CANTimeoutException ex){
            System.out.println(error + "\n" + ex.getMessage() + "");
            ex.printStackTrace();
        }
        return jag;
    }
    
    public void setJaguarCurrent(CANJaguar jag, double current, String error){
        try{
            jag.setX(current);
        }
        catch(CANTimeoutException ex){
            System.out.println(error + "\n" + ex.getMessage());
        }
    }

    public void tractionDrive(double leftCurrent, double rightCurrent) {            
            double[] voltageDifferences;
            
            voltageDifferences = computeVoltageDifferences();
            for(int i = 0; i < 6; i++){
                if(voltageDifferences[i] < maxVoltSpikeParam){ 
                    tankDrive(leftCurrent - tractionControlVariable, rightCurrent - tractionControlVariable);
                } else {
                    tankDrive(leftCurrent, rightCurrent);
                }
            }        
    }
    
    public void shiftGear(DoubleSolenoid.Value val){
        shifterSolenoid.set(val);
    }
    
   public void shifter(){
         shifterToggle = !shifterToggle;
         if(!shifterToggle){
            shifterSolenoid.set(DoubleSolenoid.Value.kForward);
         }else if(shifterToggle){
            shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
         }
   }
    
    public double[] computeVoltageDifferences() {
        double[] voltages = new double[6];
        double[] voltageDifferences = new double[6];
        
        voltages[0] = getJaguarVoltage(leftFrontJaguar, "Left Front Jaguar");
        voltages[1] = getJaguarVoltage(leftMiddleJaguar, "Left Middle Jaguar");
        voltages[2] = getJaguarVoltage(leftBackJaguar, "Left Back Jaguar");
        voltages[3] = getJaguarVoltage(rightFrontJaguar, "Right Front Jaguar");
        voltages[4] = getJaguarVoltage(rightMiddleJaguar, "Right Middle Jaguar");
        voltages[5] = getJaguarVoltage(rightBackJaguar, "Right Back Jaguar");
        
        for(int a = 0; a < 6; a++){
           voltageDifferences[a] =  voltages[a] - priorVoltages[a];
        }
        priorVoltages = voltages;
        return voltageDifferences;
    }    
    
    public static double getJaguarVoltage(CANJaguar Jaguar, String whichJag){
        double v = 0; 
        try{
            v = Jaguar.getOutputVoltage();
        } 
        catch(CANTimeoutException JagTimeout){
            System.out.println(whichJag + "\n" + JagTimeout.getMessage());       
        }
        return v;
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TeleopArcadeDrive());
    } 
}