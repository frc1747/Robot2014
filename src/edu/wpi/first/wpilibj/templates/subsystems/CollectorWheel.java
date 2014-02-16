
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.CANJaguar.ControlMode;
import edu.wpi.first.wpilibj.templates.commands.JoystickCollectorControl;
//top 1 rotation per pulse
//other 2 bottom ones 64 pulses per rotation
/**
 *
 * @author hshen09 bli58
 */
public class CollectorWheel extends Subsystem {
    
    public CANJaguar Jag8;
    double feedSpeed;
    int wheelState; //-1 spitout, 0 stopped, 1 forward
    public CollectorWheel(){
        
        try{
            Jag8 = new CANJaguar(8, ControlMode.kPercentVbus);
            Jag8.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            Jag8.configEncoderCodesPerRev(1);
        }
        catch(CANTimeoutException ex){
            System.out.println(ex.getMessage());
        }
        wheelState = 0;
    }

    
    public void pickUp(){
        wheelState = 1;
        feed(.2);
    }
    
    public void eject(){
        wheelState = -1;
        feed(-.2);
    }
    
    public void idle(){
        wheelState = 0;
        feed(0);
    }
    
    public void feed(double feedSpeed){
        try{
            Jag8.setX(feedSpeed);
        }
        catch(CANTimeoutException ex){
            System.out.println(ex.getMessage());
        }
    }
    

       public void setJaguarCurrent(CANJaguar jag, double current, String error){
        try{
            jag.setX(current);
        }
        catch(CANTimeoutException ex){
            System.out.println(error + "\n" + ex.getMessage());
        }
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
      //  setDefaultCommand(new JoystickCollectorControl());
    }
}
