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
public class SetCollectorSpeed extends CommandBase {
    double speed;
    boolean hasRun;
    public SetCollectorSpeed(double collectorSpeed){
        hasRun = false;
        requires(collectorWheel);
        speed = collectorSpeed;
        
    }
    protected void initialize() {
        hasRun = false;
    }

    protected void execute() {
        if(!hasRun){
            /*switch(collectorState){
                case 1: collectorWheel.eject();
                        break;
                case 2: collectorWheel.idle();
                        break;
                case 3: collectorWheel.pickUp();
            }*/
            collectorWheel.feed(speed);
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
        end();
    }
    
}
