/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.subsystems.Gate;

/**
 *
 * @author cland29
 */
public class Shoot extends CommandGroup {
    Subsystem[] req;
    double collectorSpeed;
    long gateSwitchTime;
    public Shoot(double speed){
        collectorSpeed = .8;
        gateSwitchTime = 20; //in ms
        req = new Subsystem[1];
        req[0] = CommandBase.gate;
        boolean isIn = CommandBase.gate.sol1.get() == Gate.gateIn;
        if(isIn){
            this.addSequential(new SetGatePosition(Gate.gateOut));
            this.addSequential(new Timeout(req, gateSwitchTime));    
        }
        this.addSequential(new SetGatePosition(Gate.gateIn));
        this.addSequential(new SetCollectorSpeed(collectorSpeed));
        this.addSequential(new Timeout(req, 500));
        this.addSequential(new SetShooterSpeed(0));
        this.addSequential(new SetCollectorSpeed(0));
    }
    
}