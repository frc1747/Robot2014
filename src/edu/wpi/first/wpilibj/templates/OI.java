
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.LifeGateSequence;
import edu.wpi.first.wpilibj.templates.commands.LiftGate;
import edu.wpi.first.wpilibj.templates.commands.SpinUpShooter;
import edu.wpi.first.wpilibj.templates.commands.PickUp;
import edu.wpi.first.wpilibj.templates.commands.Shift;
import edu.wpi.first.wpilibj.templates.commands.SpitOut;
import edu.wpi.first.wpilibj.templates.commands.StopGate;
import edu.wpi.first.wpilibj.templates.commands.TestCommand;

public class OI {
    public Joystick joy;
    Joystick controller;
    JoystickButton topLeftTrigger;
    JoystickButton bottomLeftTrigger;
    JoystickButton topRightTrigger;
    JoystickButton bottomRightTrigger;
    JoystickButton bButton;       
    public OI(){
        controller = new Joystick(1);
        joy = new Joystick(2);
        topLeftTrigger = new JoystickButton(controller, 5);
        bottomLeftTrigger = new JoystickButton(controller, 7);
        topRightTrigger = new JoystickButton(controller, 6);
        bottomRightTrigger = new JoystickButton(controller, 8);
        
        topRightTrigger.whenPressed(new SpinUpShooter(1));
        bottomRightTrigger.whenPressed(new SpinUpShooter(2));
       // topRightTrigger.whenPressed(new PickUp());
        bottomRightTrigger.whenPressed(new SpitOut());
        bButton = new JoystickButton(controller, RobotMap.controllerProfile.JoyAButton);
        bButton.whenPressed(new LiftGate());//.whenPressed(new LifeGateSequence());
        //bButton.whenReleased(new StopGate());
        topLeftTrigger.whenPressed(new Shift());
        bottomLeftTrigger.whenPressed(new TestCommand(7));
        //topRightTrigger.whenPressed(new PickUp());
        bottomRightTrigger.whenPressed(new TestCommand(8));
        //drive.shifter(oi.getAButton());
        
    }
    
    public double getLeftVertAxis(){
        return controller.getRawAxis(RobotMap.controllerProfile.leftJoyVertAxis);
    }
    
    public double getLeftHorizAxis(){
        return controller.getRawAxis(RobotMap.controllerProfile.leftJoyHorizAxis);
    }
    
    public double getRightVertAxis(){
        return controller.getRawAxis(RobotMap.controllerProfile.rightJoyVertAxis);
    }
    
    public double getRightHorizAxis(){
        return controller.getRawAxis(RobotMap.controllerProfile.rightJoyHorizAxis);
    }
    public double getBackAxis(){
        return controller.getRawAxis(3);
    }
    
    public boolean getAButton(){
        return controller.getRawButton(RobotMap.controllerProfile.JoyAButton);
    }
    
    public boolean getBButton(){
        return controller.getRawButton(RobotMap.controllerProfile.JoyBButton);
    }
    
    public boolean getXButton(){
        return controller.getRawButton(RobotMap.controllerProfile.JoyXButton);
    }
    
    public boolean getYButton(){
        return controller.getRawButton(RobotMap.controllerProfile.JoyYButton);
    }
}