package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.MotorController;
import frc.robot.subsystems.SensorController;
import frc.robot.subsystems.CameraController;

public final class Main {
  private Main() {
  }

  public static HashMap<String, Double> motorControllerMap = new HashMap<String, Double>();
  public static HashMap<String, Double> sensorsMap = new HashMap<String, Double>();
  public static HashMap<String, Boolean> switchMap = new HashMap<String, Boolean>();
  
  public static void main(String... args) {
    Runnable motorControllerRunnable = new MotorController();
    Thread motorControllerThread = new Thread(motorControllerRunnable);
    motorControllerThread.setPriority(Thread.NORM_PRIORITY);
    motorControllerThread.setDaemon(true);
    motorControllerThread.start();
    
    Runnable sensorControllerRunnable = new SensorController();
    Thread sensorControllerThread = new Thread(sensorControllerRunnable);
    sensorControllerThread.setPriority(Thread.NORM_PRIORITY);
    sensorControllerThread.setDaemon(true);
    sensorControllerThread.start();

    Runnable cameraControllerRunnable = new CameraController();
    Thread cameraControllerThread = new Thread(cameraControllerRunnable);
    cameraControllerThread.setPriority(Thread.NORM_PRIORITY);
    cameraControllerThread.setDaemon(true);
    cameraControllerThread.start();

    RobotBase.startRobot(Robot::new);
  }
}