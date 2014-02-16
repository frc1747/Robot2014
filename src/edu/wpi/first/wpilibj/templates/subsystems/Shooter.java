/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates.subsystems;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Shooter extends Subsystem {
    CANJaguar Jag9, Jag7;
    int state, shooter_time;
    double shooterSpeed, speedtolerance;
    boolean fire, autolowspeed, autohighspeed;
    

    public Shooter(){
     try{
            Jag9 = new CANJaguar(9, CANJaguar.ControlMode.kPercentVbus);
        }
        catch (CANTimeoutException ex){
            //System.out.println(9);
        }
     try{
            Jag7 = new CANJaguar(7, CANJaguar.ControlMode.kPercentVbus);
        }
        catch (CANTimeoutException ex){
            //System.out.println(9);
        }
        try {
            Jag9.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Jag9.configEncoderCodesPerRev(1);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Jag9.setPID(200, 0, 0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        try {
            Jag7.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Jag7.configEncoderCodesPerRev(1);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Jag7.setPID(200, 0, 0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        shooterSpeed = 0;
        fire = false;
        autolowspeed = false;
        autohighspeed = false;
        speedtolerance = 0.01;
    }

        public void setspeed(double speedToBeSet){
            try{
                Jag9.setX(speedToBeSet);
                Jag7.setX(-1*speedToBeSet);
            }catch(CANTimeoutException ex){
                System.out.println(ex.getMessage());
            }
        }
        
        public boolean checkIfReadyToFire(double targetSpeed){
            return (shooterSpeed > targetSpeed-speedtolerance && shooterSpeed < targetSpeed+speedtolerance);
        }
    
     
    protected void initDefaultCommand() {
        //setDefaultCommand(new SpinUpShooter(0.0));
    }
}