/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author dfroh40
 */
public class Gate extends Subsystem {
    public static DoubleSolenoid.Value gateIn = DoubleSolenoid.Value.kReverse;
    public static DoubleSolenoid.Value gateOut = DoubleSolenoid.Value.kForward;
    public DoubleSolenoid sol1;
   // DigitalInput IRSensor;
    public Compressor compressor;
    public Gate(){
        sol1 = new DoubleSolenoid(3,4);
        compressor = new Compressor(1, 8);
    }
    
    public void activateGate(){
        if(sol1.get() == gateOut){
            sol1.set(gateIn);
        }
        else if(sol1.get() == DoubleSolenoid.Value.kOff){
            sol1.set(gateIn);
        }
        else{
            sol1.set(gateOut);
        }
    }
    
    public void setGate(DoubleSolenoid.Value val){
        sol1.set(val);
    }
    
    public void stopGate(){
        sol1.set(DoubleSolenoid.Value.kOff);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
      //  setDefaultCommand(new JoystickCollectorControl());
    }
}
    