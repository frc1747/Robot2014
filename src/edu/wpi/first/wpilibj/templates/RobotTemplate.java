/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.LiftGate;
import edu.wpi.first.wpilibj.templates.commands.StopGate;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    boolean buttonDown;
    Command autonomousCommand;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        //autonomousCommand = new ExampleCommand();
        buttonDown = false;
        // Initialize all subsystems
        CommandBase.init();
        Watchdog.getInstance().feed();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        //autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Watchdog.getInstance().feed();
    }

    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //autonomousCommand.cancel();
        CommandBase.drive.shifterSolenoid.set(CommandBase.drive.lowGear);
        CommandBase.gate.compressor.start();
        //System.out.println(CommandBase.gate.compressor.enabled());
        //System.out.println("ran");
       // CommandBase.collector.
        CommandBase.gate.sol1.set(ComandBase.gate.gateIn);
       
        CommandBase.drive.fans.set(Relay.Value.kOn);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Watchdog.getInstance().feed();
        boolean pButtonDown = buttonDown;
        buttonDown = CommandBase.oi.getAButton();
        //System.out.println(CommandBase.oi.getBackAxis());
        try {
            // System.out.println(CommandBase.oi.joy.getRawAxis(1) + " " + CommandBase.oi.joy.getRawAxis(2) + " " + CommandBase.oi.joy.getRawAxis(3));
            System.out.println(CommandBase.collectorWheel.Jag8.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        CommandBase.shooter.setspeed((CommandBase.oi.joy.getRawAxis(3)-1.0)/-2.0);
        CommandBase.collectorWheel.feed(CommandBase.oi.getBackAxis());
        if(CommandBase.gate.compressor.getPressureSwitchValue()){
            CommandBase.gate.compressor.stop();
        }
        else{
            CommandBase.gate.compressor.start();
        }
         //System.out.println(CommandBase.oi.getAButton());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
        Watchdog.getInstance().feed();
    }
}
