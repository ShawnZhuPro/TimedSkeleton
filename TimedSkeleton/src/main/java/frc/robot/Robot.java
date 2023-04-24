// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  // Actuators
  private static final WPI_TalonSRX leftDriveTalon = new WPI_TalonSRX(0);
  private static final WPI_TalonSRX rightDriveTalon = new WPI_TalonSRX(1);
  private static final VictorSPX leftDriveVictor = new VictorSPX(2);
  private static final VictorSPX rightDriveVictor = new VictorSPX(3);

  // Joysticks
  private Joystick joy1 = new Joystick(0);

  // Timer
  private double startTime;

  @Override
  public void robotInit() {

    // Inverted settings
    leftDriveTalon.setInverted(false);
    rightDriveTalon.setInverted(true);

    // Victor setup (follows the talons)
    leftDriveVictor.follow(leftDriveTalon);
    rightDriveVictor.follow(rightDriveTalon);
    leftDriveVictor.setInverted(InvertType.FollowMaster);
    rightDriveVictor.setInverted(InvertType.FollowMaster);

    // Initialize encoders
    leftDriveTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    rightDriveTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    leftDriveTalon.setSensorPhase(true);
    rightDriveTalon.setSensorPhase(true);

    // Resets encoders to 0
    leftDriveTalon.setSelectedSensorPosition(0,0,10);
    rightDriveTalon.setSelectedSensorPosition(0,0,10);

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {

    startTime = Timer.getFPGATimestamp();

  }

  @Override
  public void autonomousPeriodic() {

    /**
     * Gets the time (in seconds)
     * This starts the timer right when the robot is on, but we 
     *   want it to get time when autonomous mode starts, so we 
     *   have to store the time in a global variable, "startTime"
     * After we store the time in this variable, we can now subtract
     *   the curernt time by the start time to find the elapsed time
     *   since autonomous mode started
     */
    double time = Timer.getFPGATimestamp();

    /**
     * For the first 3 seconds of auto, the robot drives forward
     *    at 50% power and stops after 3 seconds
     */
    if(time - startTime < 3){
      leftDriveTalon.set(ControlMode.PercentOutput, 0.5);
      rightDriveTalon.set(ControlMode.PercentOutput, 0.5);
    } else {
      leftDriveTalon.set(ControlMode.PercentOutput, 0);
      rightDriveTalon.set(ControlMode.PercentOutput, 0);
    }
    
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {

    /** 
     * For this axis, up is negative and down is positive
     * The speed is slowed down to 60% and the turn is slowed down
     *   to 30% for better controllability
     */
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = joy1.getRawAxis(4) * 0.3;

    /**
     * Based on the desired speed and turn, we have to calculate the 
     *   power output to the left and right sides of the chassis
     */
    double leftPower = speed + turn;
    double rightPower = speed - turn;

    leftDriveTalon.set(ControlMode.PercentOutput, leftPower);
    rightDriveTalon.set(ControlMode.PercentOutput, rightPower);

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {

  }
}
