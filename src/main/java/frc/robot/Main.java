package frc.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.subsystems.MotorController;
import frc.robot.subsystems.SensorController;
import frc.robot.Logic.LogicCore;
import frc.robot.Logic.TreeTraverse;
import frc.robot.subsystems.CameraController;

public final class Main {
  private Main() {
  }

  public static HashMap<String, Double> motorControllerMap = new HashMap<String, Double>();
  public static HashMap<String, Double> sensorsMap = new HashMap<String, Double>();
  public static HashMap<String, Boolean> switchMap = new HashMap<String, Boolean>();
  public static HashMap<String, Double> camMap = new HashMap<String, Double>();
  public static HashMap<String, String> stringMap = new HashMap<String, String>();
  public static HashMap<String, Double> joystickMap = new HashMap<String, Double>();

  public static TreeTraverse traverse = new TreeTraverse();
  public static LogicCore logic = new LogicCore();
  public static Joystick joystick = new Joystick(GamepadConstants.DRIVE_USB_PORT);
  
  public static void main(String... args) {
    Runnable motorControllerRunnable = new MotorController();
    Thread motorControllerThread = new Thread(motorControllerRunnable);
    motorControllerThread.setDaemon(true);
    motorControllerThread.start();
    
    Runnable sensorControllerRunnable = new SensorController();
    Thread sensorControllerThread = new Thread(sensorControllerRunnable);
    sensorControllerThread.setDaemon(true);
    sensorControllerThread.start();

    Runnable cameraControllerRunnable = new CameraController();
    Thread cameraControllerThread = new Thread(cameraControllerRunnable);
    cameraControllerThread.setDaemon(true);
    cameraControllerThread.start();

    // try {
    //   System.setErr(new PrintStream(new File("/home/pi/Desktop/log.txt")));
    // } catch (FileNotFoundException e) {
    //     e.printStackTrace();
    // }

    RobotBase.startRobot(Robot::new);
  }
}