/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 * @author User
 */
public class Timeout extends Command{
    long start; long timeout;
    public Timeout(Subsystem[] requireArray, long time){
        for(int i = 0; i < requireArray.length; i++){
            requires(requireArray[i]);
        }
        timeout = time;
        start = 0;
    }

    protected void initialize() {
        start = System.currentTimeMillis();
    }

    protected void execute() {
        
    }

    protected boolean isFinished() {
    return (System.currentTimeMillis() - start >= timeout);
    }

    protected void end() {
    }

    protected void interrupted() {
        
    }
}
