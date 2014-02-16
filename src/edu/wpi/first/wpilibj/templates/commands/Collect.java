/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.templates.commands.SetGatePosition;
/**
 *
 * @author User
 */
public class Collect extends CommandGroup{
    public Collect(double collectorSpeed, DoubleSolenoid.Value gatePosition){
        this.addParallel(new SetGatePosition(gatePosition));
        this.addParallel(new SetCollectorSpeed(collectorSpeed));
    }
}
