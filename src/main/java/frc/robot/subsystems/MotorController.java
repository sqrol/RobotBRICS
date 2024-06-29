package frc.robot.subsystems;

import com.studica.frc.ServoContinuous;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.Maths.Common.PID;

public class MotorController implements Runnable {

    public static double motorsUpdateTime; 
    public double previousDegrees = 0;

    // Переменные для функции setGlidePosition()
    private static boolean lastStateA;
    private static boolean lastStateB;
    private static int glidePosition;

    private boolean blackLineFlag, direction = false;  
    private boolean glideStop = false;

    private static double currentGlidePosition = 0;
    
    private static boolean liftStop, rotateStop = false;

    private static final double[][] arrOfpercentForLift = { { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 }, 
                                                                { 0, 4, 15, 30, 40, 55, 70, 80, 90, 100 } };

    private static final double[][] arrOfPosForLift = { { 0, 4, 15, 30, 40, 50, 60, 70, 80, 90, 100 }, 
                                                        { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 } };

    private static final double[][] speedForLift = { { 0, 5, 20, 50, 70, 100, 330, 500, 700, 1000 },
                                                      { 0, 1.2, 2, 5, 7, 20, 39, 55, 70, 80 } };

    private static final double[][] arrOfPosForRotate = { { -1, 500, 1500, 2000 }, { -1, 45, 90, 110 } };

    private static final double[][] speedForRotate =  { { 0, 0.5, 2, 10, 17, 26, 39, 50, 62, 70 }, 
                                                        { 0, 2, 5, 20, 35, 47, 60, 70, 77, 85 } };

    private static final double[][] speedForGlideServo = { { 0, 1, 2, 4, 6, 8, 10 }, 
                                                        { 0, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18}};

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            double startTime = Timer.getFPGATimestamp();
            try {

                if(Main.switchMap.get("EMSButton")) {
                    setRightMotorSpeed(0.0);
                    setLeftMotorSpeed(0.0);
                    setRotateMotorSpeed(0.0);
                    setLiftMotorSpeed(0.0);

                    SERVO_GLIDE.setDisabled();
                    SERVO_GRAB.setDisabled();
                    SERVO_GRIP_ROTATE.setDisabled(); 

                } else {
                    setAxisSpeed(Main.motorControllerMap.get("speedX"), Main.motorControllerMap.get("speedZ"));
                    setRotateMotorSpeed(Main.motorControllerMap.get("rotateSpeed"));
                    setLiftMotorSpeed(Main.motorControllerMap.get("liftSpeed"));

                    setServoGrab(Main.motorControllerMap.get("servoGrab"));
                    setServoGripRotate(Main.motorControllerMap.get("servoGripRotate"));   
                    
                    setRotatePosition(Main.motorControllerMap.get("targetRotateDegree"));
                    setLiftPosition(Main.motorControllerMap.get("targetLiftPos"));

                    Main.switchMap.put("glideStop", setGlidePosition(Main.sensorsMap.get("targetGlidePos")));
                }

                encodersValueResetHandler(); // Работа со сбросом энкодеров

                encodersValueHandler(); // Работа со значениями энкодера

                Main.motorControllerMap.put("servoGrabAngle", getServoGrabAngle());
                Main.motorControllerMap.put("updateTime", motorsUpdateTime);      
                
                countGlidePosition();
                SmartDashboard.putNumber("CountPos: ", glidePosition); 

                Thread.sleep(5);
            } catch (Exception e) {
                System.err.println("!!!An error occurred in MotorController: " + e.getMessage());
                e.printStackTrace();
            }
            motorsUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
    }

    private final TitanQuad MOTOR_RIGHT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_RIGHT);
    private final TitanQuad MOTOR_LEFT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_LEFT);
    private final TitanQuad MOTOR_ROTATE = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_ROTATE);
    private final TitanQuad MOTOR_LIFT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_LIFT); 

    private final TitanQuadEncoder ENC_RIGHT = new TitanQuadEncoder(MOTOR_RIGHT, Constants.ENC_RIGHT, Constants.DIST_PER_TICK);
    private final TitanQuadEncoder ENC_LEFT = new TitanQuadEncoder(MOTOR_LEFT, Constants.ENC_LEFT, Constants.DIST_PER_TICK);
    private final TitanQuadEncoder ENC_ROTATE = new TitanQuadEncoder(MOTOR_ROTATE, Constants.ENC_ROTATE, Constants.DIST_PER_TICK);
    private final TitanQuadEncoder ENC_LIFT = new TitanQuadEncoder(MOTOR_LIFT, Constants.ENC_LIFT, Constants.DIST_PER_TICK);

    private final PID PID_RIGHT = new PID(0.02, 1.74, 0.0, -100, 100); 
    private final PID PID_LEFT = new PID(0.0112, 1.34, 0.0, -100, 100);  // 0.067, 0.43, 0.0, -100, 100
    private final PID PID_ROTATE = new PID(0.051, 0.43, 0.0, -100, 100); 
    private final PID PID_LIFT = new PID(0.051, 0.43, 0.0, -100, 100);

    private final Servo SERVO_GRAB = new Servo(Constants.SERVO_GRAB);
    private final Servo SERVO_GRIP_ROTATE = new Servo(Constants.SERVO_GRIP_ROTATE);
    private final ServoContinuous SERVO_GLIDE = new ServoContinuous(Constants.SERVO_GLIDE);
    
    private void setLiftPosition(double pos) {
        double currentPos = -Main.motorControllerMap.get("encLift");
        double percentPos = Functions.TransitionFunction(currentPos, arrOfpercentForLift);
        double encPos = Functions.TransitionFunction(pos, arrOfPosForLift);
        double speed = Functions.TransitionFunction(currentPos - encPos, speedForLift);
        liftStop = Functions.BooleanInRange(currentPos - encPos, -0.2, 0.2);
        
        Main.motorControllerMap.put("currentLiftPos", percentPos);

        if (Main.switchMap.get("limitSwitch") && speed > 0) {
            Main.motorControllerMap.put("liftSpeed", 0.0);
            Main.motorControllerMap.put("resetEncLift", 1.0);
            liftStop = true;
        } else if (speed < 0 && currentPos < -3000) {
            Main.motorControllerMap.put("liftSpeed", 0.0);
            liftStop = true;
        } else if (liftStop && !Main.switchMap.get("limitSwitch")) {
            Main.motorControllerMap.put("liftSpeed", 0.0);
        } else {
            Main.motorControllerMap.put("liftSpeed", speed);
            liftStop = false;
        }

        Main.switchMap.put("liftStop", liftStop);
    }

    private void encodersValueHandler() {
        Main.motorControllerMap.put("rpmRight", ENC_RIGHT.getSpeed());
        Main.motorControllerMap.put("rpmLeft", ENC_LEFT.getSpeed());
        Main.motorControllerMap.put("rpmRotate", ENC_ROTATE.getSpeed());
        Main.motorControllerMap.put("rpmLift", ENC_LIFT.getSpeed());

        Main.motorControllerMap.put("encRight", ENC_RIGHT.getEncoderDistance()); 
        Main.motorControllerMap.put("encLeft", ENC_LEFT.getEncoderDistance());
        Main.motorControllerMap.put("encRotate", ENC_ROTATE.getEncoderDistance());
        Main.motorControllerMap.put("encLift", ENC_LIFT.getEncoderDistance());
    }

    private void encodersValueResetHandler() {
        // Зачем отдельный сброс для пидов? // вызывается в StartPos один раз
        if(Main.motorControllerMap.get("resetPID") == 1.0) {
            PID_RIGHT.reset();
            PID_LEFT.reset();
            PID_ROTATE.reset();
            PID_LIFT.reset();
            Main.motorControllerMap.put("resetPID", 0.0);
        }

        if (Main.motorControllerMap.get("resetEncs") == 1.0) {
            ENC_RIGHT.reset();
            ENC_LEFT.reset();
            ENC_ROTATE.reset();
            ENC_LIFT.reset();
            Main.motorControllerMap.put("resetEncs", 0.0);
        }

        if(Main.motorControllerMap.get("resetDriveEncs") == 1.0) {
            ENC_RIGHT.reset();
            ENC_LEFT.reset();
            Main.motorControllerMap.put("resetDriveEncs", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncRotate") == 1.0) {
            ENC_ROTATE.reset();
            Main.motorControllerMap.put("resetEncRotate", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncLift") == 1.0) {
            ENC_LIFT.reset();
            Main.motorControllerMap.put("resetEncLift", 0.0);
        }
    }

    private void setRotatePosition(double degree) {
        
        double currentRotatePos = -Main.motorControllerMap.get("encRotate");
        
        double rotateDegree = Functions.TransitionFunction(currentRotatePos, arrOfPosForRotate);
        double speed = Functions.TransitionFunction(rotateDegree - degree, speedForRotate);
        
        rotateStop = Functions.BooleanInRange(degree - rotateDegree, -0.2, 0.2);

        Main.motorControllerMap.put("currentRotateDegree", rotateDegree);

        if (rotateStop) {
            Main.motorControllerMap.put("rotateSpeed", 0.0);
            SmartDashboard.putNumber("rotateCheck", 3);
            rotateStop = true;
        } else {
            SmartDashboard.putNumber("rotateCheck", 4);
            Main.motorControllerMap.put("rotateSpeed", speed);
        }

        if ((speed > 0 && currentRotatePos > 1600) || (speed < 0 && currentRotatePos < -1600)) {
            Main.motorControllerMap.put("rotateSpeed", 0.0);
            SmartDashboard.putNumber("rotateCheck", 324);
            rotateStop = true;
        } 
        Main.switchMap.put("rotateStop", rotateStop);
    }

    private void setAxisSpeed(double x, double z) {
        double right = x + z;
        double left = -x + z;

        setRightMotorSpeed(right);
        setLeftMotorSpeed(left);
    }

    private void setRightMotorSpeed(double speed) {
        if (speed == 0.0) {
            PID_RIGHT.reset();
            MOTOR_RIGHT.set(0);
        } else {
            PID_RIGHT.calculate(-ENC_RIGHT.getSpeed(), speed);
            Main.motorControllerMap.put("rightPID", PID_RIGHT.getOutput());
            MOTOR_RIGHT.set(Main.motorControllerMap.get("rightPID"));
        }
    }

    private void setLeftMotorSpeed(double speed) {
        if(speed == 0.0) {
            PID_LEFT.reset();
            MOTOR_LEFT.set(0);
        } else {
            PID_LEFT.calculate(-ENC_LEFT.getSpeed(), speed);
            Main.motorControllerMap.put("leftPID", PID_LEFT.getOutput());
            MOTOR_LEFT.set(Main.motorControllerMap.get("leftPID"));
        }
    }

    private void setRotateMotorSpeed(double speed) {
        if (speed == 0.0) {
            PID_ROTATE.reset();
            MOTOR_ROTATE.set(0);
        } else {
            PID_ROTATE.calculate(-ENC_ROTATE.getSpeed(), speed);
            Main.motorControllerMap.put("rotatePID", PID_ROTATE.getOutput());
            MOTOR_ROTATE.set(Main.motorControllerMap.get("rotatePID"));
        }
    }

    private void setLiftMotorSpeed(double speed) {
        if(speed == 0.0) {
            PID_LIFT.reset();
            MOTOR_LIFT.set(0);
        } else {
            PID_LIFT.calculate(-ENC_LIFT.getSpeed(), speed);
            Main.motorControllerMap.put("liftPID", PID_LIFT.getOutput());
            MOTOR_LIFT.set(Main.motorControllerMap.get("liftPID"));
        }
    }

    private void setServoGrab(double angle) {
        try {
            SERVO_GRAB.setAngle(angle);
        } catch (Exception e) {
            System.out.println("Error in setServoGrab");
            e.printStackTrace();
        } 
    }

    private double getServoGrabAngle() {
        try {
            return SERVO_GRAB.getAngle();
        } catch (Exception e) {
            return 0;
        }
        
    }

    private void setServoGripRotate(double angle) {
        try {
            SERVO_GRIP_ROTATE.setAngle(angle);
        } catch (Exception e) {
            System.out.println("Error in setServoGripRotate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setGlideServoSpeed(double speed) {
        try {
            SERVO_GLIDE.setSpeed(speed);
        } catch (Exception e) {
            System.out.println("Error in setGlideServoSpeed: " + e.getMessage());
            e.printStackTrace();
        }  
    }

    // Если Софа все же перестанет забивать хуй (это пиздец! как с ней общаться?)
    // на мои просьбы переделать проводку датчика черной линии новый метод для работы с Glide

    private void countGlidePosition() {
        // Получение значений с датчика черной линии
        boolean currentStateA = Main.sensorsMap.get("cobraSignal0") > 2000 && Main.sensorsMap.get("cobraSignal1") > 2000;
        boolean currentStateB = Main.sensorsMap.get("cobraSignal2") > 2000 && Main.sensorsMap.get("cobraSignal3") > 2000;

        if (currentStateA != lastStateA || currentStateB != lastStateB) {
            if (currentStateA != lastStateA) {
                if (currentStateA == currentStateB) {
                    glidePosition++;
                } else {
                    glidePosition--;
                }
            }

            if (currentStateB != lastStateB) {
                if (currentStateA != currentStateB) {
                    glidePosition++;
                } else {
                    glidePosition--;
                }
            }
        }

        // Обновляем предыдущие значения
        lastStateA = currentStateA;
        lastStateB = currentStateB;
    }

    private boolean setGlidePosition(Double targetGlidePosition) {

        double glideCurrentDiff = glidePosition - targetGlidePosition;
        double glideServoSpeed = Functions.TransitionFunction(glideCurrentDiff, speedForGlideServo);
        boolean glideStop = targetGlidePosition == glidePosition;

        setGlideServoSpeed(-glideServoSpeed);

        return glideStop;
    }

}