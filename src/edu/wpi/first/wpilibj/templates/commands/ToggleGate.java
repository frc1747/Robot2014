/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author User
 */
public class ToggleGate extends CommandBase{
    boolean hasRun;
    public ToggleGate(){
        requires(gate);
    }
    protected void initialize() {
        hasRun = false;
    }

    protected void execute() {
        if(!hasRun){
            gate.activateGate();
        }
        hasRun = true;
    }

    protected boolean isFinished() {
        return true;
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void end() {
        hasRun = false;
        //collector.stopGate();
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void interrupted() {
       // collector.stopGate();
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
