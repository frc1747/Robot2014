/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author dfroh40
 */
public class TeleopArcadeDrive extends CommandBase {
    int cycleCount;
    long start;
    long lastCycle;
    long thisCycle;
    double pInputL, pInputR;
    public TeleopArcadeDrive(){
        requires(drive);
        cycleCount = 0;
        lastCycle = 0;
        thisCycle = 0;
        start = 0;
    }
    
    protected void initialize() {
        long start = System.currentTimeMillis();
    }

    protected void execute() {
        cycleCount++;
        thisCycle = System.currentTimeMillis();
        if(thisCycle - lastCycle > 100){
            lastCycle = thisCycle;
        }
        drive.arcadeDrive(-oi.getRightHorizAxis(), -oi.getLeftVertAxis());
    }

    protected boolean isFinished() {
       return false;
    }

    protected void end() {
        
    }

    protected void interrupted() {
        
    }
    
   
}
