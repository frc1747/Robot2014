/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author dfroh40
 */
public class SetGatePosition extends CommandBase{
    DoubleSolenoid.Value gatePosition;
    boolean hasRun;
    public SetGatePosition(DoubleSolenoid.Value gatePosition){
        this.gatePosition = gatePosition;
        requires(gate);
        hasRun = false;
    }
    
    protected void initialize() {
        hasRun = false;
    }

    protected void execute() {
        if(!hasRun){
            gate.setGate(gatePosition);
            hasRun = true;
        }
    }

    protected boolean isFinished() {
        return hasRun;
    }
    
    protected void end() {
        hasRun = false;
    }

    protected void interrupted() {
        
    }
    
}
