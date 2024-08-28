package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.subsystems.MotorController;
import frc.robot.subsystems.SensorController;
import frc.robot.StateMachine.CoreEngine.CommandAdapter;
import frc.robot.StateMachine.CoreEngine.IState;

public class Robot extends TimedRobot {

  private Command adapter = new CommandAdapter();

  @Override
  public void robotInit() {
    try {
      StateMachine.states.clear();
      Thread.sleep(10);
      
      initMaps();
      Thread.sleep(10);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    sendSmartDashBoard();
  }
  
  @Override
  public void disabledInit() { 
    if (adapter != null) {
      adapter.cancel();
    }
  }

  @Override
  public void autonomousInit() {
    if (adapter != null) {
      adapter.schedule();
    }
  }

  @Override 
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (adapter != null) {
      adapter.schedule();
    }
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  private void initMaps() {

    Main.stringMap.put("detectedFruit", "none");

    Main.sensorsMap.put("currentGlidePos", 0.0);
    Main.switchMap.put("startButton", false);
    Main.switchMap.put("EMSButton", false);
    Main.switchMap.put("limitSwitchLift", false);
    Main.switchMap.put("limitSwitchGlide", false);

    Main.switchMap.put("initLift", false);
    Main.switchMap.put("initGlide", false);

    Main.switchMap.put("liftStop", false);
    Main.switchMap.put("glideStop", false);
    Main.switchMap.put("rotateStop", false);

    Main.switchMap.put("trackImageArea", true);

    Main.switchMap.put("resizeForGlide", false);

    Main.sensorsMap.put("indicationMode", 1.0);

    Main.sensorsMap.put("sharpRight", 0.0);
    Main.sensorsMap.put("sharpLeft", 0.0);

    Main.sensorsMap.put("sonicRight", 0.0);
    Main.sensorsMap.put("sonicLeft", 0.0);

    Main.sensorsMap.put("updateTime", SensorController.sensorsUpdateTime);
    Main.sensorsMap.put("updateTimeCamera", 0.0);

    Main.sensorsMap.put("objectFind", 0.0);
    Main.sensorsMap.put("camTask", 0.0);
    Main.sensorsMap.put("posZ", 0.0);
    Main.sensorsMap.put("resetGyro", 0.0);
    Main.sensorsMap.put("srcGyro", 0.0);

    Main.sensorsMap.put("targetGlidePos", 0.0);

    Main.motorControllerMap.put("targetLiftPos", 0.0); // Строка 125
    Main.motorControllerMap.put("currentLiftPos", 0.0); // Строка 124
    
    Main.motorControllerMap.put("targetRotateDegree", 0.0);
    Main.motorControllerMap.put("currentRotateDegree", 0.0);

    Main.motorControllerMap.put("encRight", 0.0);
    Main.motorControllerMap.put("encLeft", 0.0);
    Main.motorControllerMap.put("encRotate", 0.0);
    Main.motorControllerMap.put("encLift", 0.0);
    Main.motorControllerMap.put("resetAllEncoders", 0.0);

    Main.motorControllerMap.put("resetDriveEncs", 0.0);
    Main.motorControllerMap.put("resetEncRight", 0.0);
    Main.motorControllerMap.put("resetEncLeft", 0.0);
    Main.motorControllerMap.put("resetEncRotate", 0.0);
    Main.motorControllerMap.put("resetEncLift", 0.0);

    Main.motorControllerMap.put("rpmRight", 0.0);
    Main.motorControllerMap.put("rpmLeft", 0.0);
    Main.motorControllerMap.put("rpmRotate", 0.0);
    Main.motorControllerMap.put("rpmLift", 0.0);

    Main.motorControllerMap.put("rightPID", 0.0);
    Main.motorControllerMap.put("leftPID", 0.0);
    Main.motorControllerMap.put("rotatePID", 0.0);
    Main.motorControllerMap.put("liftPID", 0.0);
    Main.motorControllerMap.put("resetPID", 0.0);

    Main.motorControllerMap.put("currentLiftPos", 0.0);
    Main.motorControllerMap.put("targetLiftPos", 0.0);

    Main.motorControllerMap.put("servoGrab", 55.0);
    Main.motorControllerMap.put("grabServoAngle", 0.0);

    Main.motorControllerMap.put("servoGripRotate", 140.0);
    Main.motorControllerMap.put("glideServoSpeed", 0.0);

    Main.motorControllerMap.put("speedX", 0.0);
    Main.motorControllerMap.put("speedZ", 0.0);
    Main.motorControllerMap.put("rotateSpeed", 0.0);
    Main.motorControllerMap.put("liftSpeed", 0.0);

    Main.motorControllerMap.put("useOneSide", 0.0);

    Main.sensorsMap.put("cobraVoltage", 0.0);

    Main.motorControllerMap.put("posX", 0.0);
    Main.motorControllerMap.put("updateTime", MotorController.motorsUpdateTime);

    Main.motorControllerMap.put("glideMode", 0.0);
    Main.motorControllerMap.put("setGlideSpeed", 0.0);

    Main.camMap.put("targetFound", 0.0);
    Main.camMap.put("currentCenterX", 0.0);
    Main.camMap.put("currentCenterY", 0.0);
    Main.camMap.put("glideMoveMode", 0.0);

    Main.camMap.put("glideObjectFound", 0.0);

    Main.camMap.put("targetAngle", 0.0);

    Main.camMap.put("currentColorIndex", 0.0);
    Main.camMap.put("targetColorFound", 0.0);


    Main.camMap.put("branchesChecked", 0.0); // 3.0 - upper, 2.0 - middle, 3.0 - lower

    Main.motorControllerMap.put("servoGrabAngle", 0.0);

    Main.motorControllerMap.put("lastGlidePosition", 0.0);
    Main.motorControllerMap.put("currentRotateDegree", 0.0);

  }

  private void sendSmartDashBoard() {

    SmartDashboard.putNumber("targetColorFound", Main.camMap.get("targetColorFound"));

    SmartDashboard.putString("detectedFruit", Main.stringMap.get("detectedFruit"));

    SmartDashboard.putNumber("targetAngle", Main.camMap.get("targetAngle"));
    
    SmartDashboard.putBoolean("targetFound", Main.camMap.get("targetFound") == 1.0);
    SmartDashboard.putNumber("currentCenterX", Main.camMap.get("currentCenterX"));
    SmartDashboard.putNumber("currentCenterY", Main.camMap.get("currentCenterY"));

    SmartDashboard.putNumber("sharpRight", Main.sensorsMap.get("sharpRight"));
    SmartDashboard.putNumber("sharpLeft", Main.sensorsMap.get("sharpLeft"));

    SmartDashboard.putNumber("sonicLeft", Main.sensorsMap.get("sonicLeft"));
    SmartDashboard.putNumber("sonicRight", Main.sensorsMap.get("sonicRight"));

    SmartDashboard.putBoolean("isResetZ", Main.sensorsMap.get("resetGyro") == 1.0);
    SmartDashboard.putBoolean("isResetEncs", Main.motorControllerMap.get("resetAllEncoders") == 1.0);

    SmartDashboard.putNumber("rpmRight", Main.motorControllerMap.get("rpmRight"));
    SmartDashboard.putNumber("rpmLeft", Main.motorControllerMap.get("rpmLeft"));
    SmartDashboard.putNumber("rpmRotate", Main.motorControllerMap.get("rpmRotate"));
    SmartDashboard.putNumber("rpmLift", Main.motorControllerMap.get("rpmLift"));

    SmartDashboard.putNumber("encRight", Main.motorControllerMap.get("encRight"));
    SmartDashboard.putNumber("encLeft", Main.motorControllerMap.get("encLeft"));
    SmartDashboard.putNumber("encRotate", Main.motorControllerMap.get("encRotate"));
    SmartDashboard.putNumber("encLift", Main.motorControllerMap.get("encLift"));

    SmartDashboard.putNumber("speedX", Main.motorControllerMap.get("speedX"));
    SmartDashboard.putNumber("speedZ", Main.motorControllerMap.get("speedZ"));

    SmartDashboard.putNumber("posX", Main.motorControllerMap.get("posX"));
    SmartDashboard.putNumber("posZ", Main.sensorsMap.get("posZ"));

    SmartDashboard.putNumber("camTask", Main.sensorsMap.get("camTask"));

    SmartDashboard.putNumber("index", StateMachine.index);
    SmartDashboard.putNumber("updateTimeMotors", Main.motorControllerMap.get("updateTime"));
    SmartDashboard.putNumber("updateTimeSensors", Main.sensorsMap.get("updateTime"));
    SmartDashboard.putNumber("updateTimeCamera", Main.sensorsMap.get("updateTimeCamera"));

    SmartDashboard.putNumber("cobraVoltage", Main.sensorsMap.get("cobraVoltage"));

    SmartDashboard.putNumber("targetGlidePos", Main.sensorsMap.get("targetGlidePos"));
    SmartDashboard.putNumber("currentGlidePos", Main.sensorsMap.get("currentGlidePos"));
    SmartDashboard.putNumber("targetLiftPos", Main.motorControllerMap.get("targetLiftPos"));
    SmartDashboard.putNumber("currentLiftPos", Main.motorControllerMap.get("currentLiftPos"));

    SmartDashboard.putBoolean("EMS", Main.switchMap.get("EMSButton"));
    SmartDashboard.putBoolean("startButton", Main.switchMap.get("startButton"));
    SmartDashboard.putBoolean("limitSwitchLift", Main.switchMap.get("limitSwitchLift"));
    SmartDashboard.putBoolean("limitSwitchGlide", Main.switchMap.get("limitSwitchGlide"));

    SmartDashboard.putBoolean("trackImageArea", Main.switchMap.get("trackImageArea"));

    SmartDashboard.putBoolean("liftStop", Main.switchMap.get("liftStop"));
    SmartDashboard.putBoolean("glideStop", Main.switchMap.get("glideStop"));
    SmartDashboard.putBoolean("rotateStop", Main.switchMap.get("rotateStop"));

    SmartDashboard.putBoolean("initLift", Main.switchMap.get("initLift"));
    SmartDashboard.putBoolean("initGlide", Main.switchMap.get("initGlide"));

    SmartDashboard.putNumber("objectFind", Main.sensorsMap.get("objectFind"));

    SmartDashboard.putNumber("currentColorIndex", Main.camMap.get("currentColorIndex"));

    SmartDashboard.putNumber("iterationTime", StateMachine.iterationTime);

    if (StateMachine.states.size() > 0) {
      SmartDashboard.putString("currentState", StateMachine.currentState.getClass().getSimpleName());
      
    } else {
      SmartDashboard.putString("currentState", "null");
    }
  }
}