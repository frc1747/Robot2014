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
public class SetShooterSpeed extends CommandBase{
    double shooterSpeed;
    boolean hasRun;
    public SetShooterSpeed(double shooterSpeed){
        hasRun = false;
        requires(shooter);
        this.shooterSpeed = shooterSpeed;
    }
    protected void initialize() {
        hasRun = false;
    }

    protected void execute() {
        if(!hasRun){
            shooter.setspeed(shooterSpeed);
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
