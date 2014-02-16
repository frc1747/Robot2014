/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author ethom22 bli58
 */
public class ControllerProfile {
    public final int leftJoyHorizAxis, leftJoyVertAxis, rightJoyHorizAxis, rightJoyVertAxis;
    public final int JoyAButton, JoyBButton, JoyXButton, JoyYButton;
    public ControllerProfile(int leftJoyHorizAxis, int leftJoyVertAxis,
                             int rightJoyHorizAxis, int rightJoyVertAxis, 
                             int JoyAButton, int JoyBButton, 
                             int JoyXButton, int JoyYButton){
        this.leftJoyHorizAxis = leftJoyHorizAxis;
        this.leftJoyVertAxis = leftJoyVertAxis;
        this.rightJoyHorizAxis = rightJoyHorizAxis;
        this.rightJoyVertAxis = rightJoyVertAxis;
        this.JoyAButton = JoyAButton;
        this.JoyBButton = JoyBButton;
        this.JoyXButton = JoyXButton;
        this.JoyYButton = JoyYButton;
    }
}