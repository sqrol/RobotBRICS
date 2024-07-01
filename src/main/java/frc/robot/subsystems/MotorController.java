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

    private TitanQuad MOTOR_RIGHT;
    private TitanQuad MOTOR_LEFT;
    private TitanQuad MOTOR_ROTATE;
    private TitanQuad MOTOR_LIFT;

    private TitanQuadEncoder ENC_RIGHT;
    private TitanQuadEncoder ENC_LEFT;
    private TitanQuadEncoder ENC_ROTATE;
    private TitanQuadEncoder ENC_LIFT;

    private Servo SERVO_GRAB;
    private Servo SERVO_GRIP_ROTATE;
    private ServoContinuous SERVO_GLIDE;

    private PID PID_RIGHT = new PID(0.066, 0.57, 0.0, -100, 100); 
    private PID PID_LEFT = new PID(0.066, 0.57, 0.0, -100, 100);  // 0.067, 0.43, 0.0, -100, 100
    private PID PID_ROTATE = new PID(0.069, 0.59, 0.0, -100, 100); 
    private PID PID_LIFT = new PID(0.066, 0.57, 0.0, -100, 100);

    public double previousDegrees = 0;

    private double encRightResetValue = 0;
    private double encLeftResetValue = 0;
    private double encLiftResetValue = 0;
    private double encRotateResetValue = 0;

    // Переменные для функции setGlidePosition()
    private boolean lastStateA = false;
    private boolean lastStateD = false;

    private boolean flag = false;
    private int direction = 0; // Направление вращения: 1 - вперед, -1 - назад

    private boolean flagStep1 = false;
    private boolean flagStep2 = false;

    private boolean glideStop = false;

    private static double currentGlidePosition = 0;
    
    private static boolean liftStop, rotateStop = false;

    private static final double[][] arrOfpercentForLift = { { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 }, { 0, 4, 15, 30, 40, 50, 60, 70, 80, 90, 100 } };

    private static final double[][] arrOfPosForLift = { { 0, 4, 15, 30, 40, 50, 60, 70, 80, 90, 100 }, { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 } };

    private static final double[][] speedForLift = { { 0, 30, 50, 100, 200, 330, 500, 700, 850, 1000 }, { 0, 2, 5, 7, 15, 26, 34, 35, 50, 70 } };

    private static final double[][] arrOfPosForRotate = { { 0, 500, 1500, 2000 }, { 0, 45, 90, 110 } };

    private static final double[][] speedForRotate =  { { 0, 5, 10, 14, 17, 26, 39, 50, 62, 70 }, { 0, 6, 15, 25, 35, 47, 60, 70, 77, 85 } };

    private static final double[][] speedForGlideServo = { { 0, 1, 2, 4, 6, 8, 10 }, { 0, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18}};

    public MotorController() {
        try {
            MOTOR_RIGHT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_RIGHT);
            MOTOR_LEFT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_LEFT);
            MOTOR_ROTATE = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_ROTATE);
            MOTOR_LIFT = new TitanQuad(Constants.TITAN_ID, Constants.MOTOR_LIFT); 
        
            ENC_RIGHT = new TitanQuadEncoder(MOTOR_RIGHT, Constants.ENC_RIGHT, Constants.DIST_PER_TICK);
            ENC_LEFT = new TitanQuadEncoder(MOTOR_LEFT, Constants.ENC_LEFT, Constants.DIST_PER_TICK);
            ENC_ROTATE = new TitanQuadEncoder(MOTOR_ROTATE, Constants.ENC_ROTATE, Constants.DIST_PER_TICK);
            ENC_LIFT = new TitanQuadEncoder(MOTOR_LIFT, Constants.ENC_LIFT, Constants.DIST_PER_TICK);
        
            SERVO_GRAB = new Servo(Constants.SERVO_GRAB);
            SERVO_GRIP_ROTATE = new Servo(Constants.SERVO_GRIP_ROTATE);
            SERVO_GLIDE = new ServoContinuous(Constants.SERVO_GLIDE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

                    
                    setGlidePosition(Main.sensorsMap.get("targetGlidePos"));
                }

                encodersValueHandler();

                encodersValueResetHandler(); 
                
                countGlidePosition();

                Main.motorControllerMap.put("servoGrabAngle", getServoGrabAngle());
                Main.motorControllerMap.put("updateTime", motorsUpdateTime);      

                Thread.sleep(10);
            } catch (Exception e) {
                System.err.println("!!!An error occurred in MotorController: " + e.getMessage());
                e.printStackTrace();
            }
            motorsUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
    }

    private void encodersValueHandler() {
        Main.motorControllerMap.put("rpmRight", ENC_RIGHT.getSpeed());
        Main.motorControllerMap.put("rpmLeft", ENC_LEFT.getSpeed());
        Main.motorControllerMap.put("rpmRotate", ENC_ROTATE.getSpeed());
        Main.motorControllerMap.put("rpmLift", ENC_LIFT.getSpeed());

        Main.motorControllerMap.put("encRight", getEncRight()); 
        Main.motorControllerMap.put("encLeft", getEncLeft());
        Main.motorControllerMap.put("encRotate", getEncRotate());
        Main.motorControllerMap.put("encLift", getEncLift());
    }

    private void encodersValueResetHandler() {
        
        if(Main.motorControllerMap.get("resetDriveEncs") == 1.0) {
            resetEncLeft();
            resetEncRight();
            Main.motorControllerMap.put("resetDriveEncs", 0.0);
        }

        if (Main.motorControllerMap.get("resetEncs") == 1.0) {
            resetEncRight();
            resetEncLeft();
            resetEncRotate();
            resetEncLift();
            Main.motorControllerMap.put("resetEncs", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncRotate") == 1.0) {
            resetEncRotate();
            Main.motorControllerMap.put("resetEncRotate", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncLift") == 1.0) {
            resetEncLift();
            Main.motorControllerMap.put("resetEncLift", 0.0);
        }

        if(Main.motorControllerMap.get("resetPID") == 1.0) {
            PID_RIGHT.reset();
            PID_LEFT.reset();
            PID_ROTATE.reset();
            PID_LIFT.reset();
            Main.motorControllerMap.put("resetPID", 0.0);
        }
    }

    private double getEncRight() {
        return ENC_RIGHT.getEncoderDistance() - encRightResetValue;
    }

    private double getEncLeft() {
        return ENC_LEFT.getEncoderDistance() - encLeftResetValue;
    }

    private double getEncRotate() {
        return ENC_ROTATE.getEncoderDistance() - encRotateResetValue;
    }

    private double getEncLift() {
        return ENC_LIFT.getEncoderDistance() - encLiftResetValue;
    }

    private void resetEncRight() {
        encRightResetValue = ENC_RIGHT.getEncoderDistance();
    }

    private void resetEncLeft() {
        encLeftResetValue = ENC_LEFT.getEncoderDistance();
    }

    private void resetEncRotate() {
        encRotateResetValue = ENC_ROTATE.getEncoderDistance();
    }

    private void resetEncLift() {
        encLiftResetValue = ENC_LIFT.getEncoderDistance();
    }

    private void setLiftPosition(double pos) {
        double currentPos = -Main.motorControllerMap.get("encLift");
        double percentPos = Functions.TransitionFunction(currentPos, arrOfpercentForLift);
        double encPos = Functions.TransitionFunction(pos, arrOfPosForLift);
        double speed = Functions.TransitionFunction(currentPos - encPos, speedForLift);
        liftStop = Functions.BooleanInRange(currentPos - encPos, -0.8, 0.8);
        
        Main.motorControllerMap.put("currentLiftPos", percentPos);

        SmartDashboard.putNumber("currentPos - encPos", currentPos - encPos);

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
            PID_RIGHT.calculate(-ENC_RIGHT.getSpeed() / 2.3, speed);
            Main.motorControllerMap.put("rightPID", PID_RIGHT.getOutput());
            MOTOR_RIGHT.set(Main.motorControllerMap.get("rightPID"));
        }
    }

    private void setLeftMotorSpeed(double speed) {
        if(speed == 0.0) {
            PID_LEFT.reset();
            MOTOR_LEFT.set(0);
        } else {
            PID_LEFT.calculate(-ENC_LEFT.getSpeed() / 2.3, speed);
            Main.motorControllerMap.put("leftPID", PID_LEFT.getOutput());
            MOTOR_LEFT.set(Main.motorControllerMap.get("leftPID"));
        }
    }

    private void setRotateMotorSpeed(double speed) {
        if (speed == 0.0) {
            PID_ROTATE.reset();
            MOTOR_ROTATE.set(0);
        } else {
            PID_ROTATE.calculate(-ENC_ROTATE.getSpeed() / 2, speed);
            Main.motorControllerMap.put("rotatePID", PID_ROTATE.getOutput());
            MOTOR_ROTATE.set(Main.motorControllerMap.get("rotatePID"));
        }
    }

    private void setLiftMotorSpeed(double speed) {
        if(speed == 0.0) {
            PID_LIFT.reset();
            MOTOR_LIFT.set(0);
        } else {
            PID_LIFT.calculate(-ENC_LIFT.getSpeed() / 2.1, speed);
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
        boolean currentStateA = Main.sensorsMap.get("cobraSignal0") > 2000;
        boolean currentStateD = Main.sensorsMap.get("cobraSignal3") > 2000;

        if (currentStateA != lastStateA || currentStateD != lastStateD) {

            // Проверяем переход от currentStateD к currentStateA
            if (lastStateD && !currentStateD && currentStateA) {
                currentGlidePosition++;
            }

            // Проверяем переход от currentStateA к currentStateD
            if (lastStateA && !currentStateA && currentStateD) {
                currentGlidePosition--;
            }
            
        }

        lastStateA = currentStateA;
        lastStateD = currentStateD;

        Main.sensorsMap.put("currentGlidePos", currentGlidePosition);
    }

    private void setGlidePosition(double targetGlidePosition) {

        double glideDiff = currentGlidePosition - targetGlidePosition;
        double glideSpeed = Functions.TransitionFunction(glideDiff, speedForGlideServo);
        glideStop = targetGlidePosition == currentGlidePosition;

        setGlideServoSpeed(-glideSpeed);

        Main.switchMap.put("glideStop", glideStop);
    }

    // private void setGlidePosition(double position) { 
    //     boolean blackLineDetect = Main.sensorsMap.get("cobraVoltage") > 2.0;
    //     double glideServoSpeed = Functions.TransitionFunction(position - currentGlidePosition, speedForGlideServo);

    //     glideStop = false;

    //     direction = position > currentGlidePosition;

    //     Main.sensorsMap.put("currentGlidePos", currentGlidePosition);

    //     if (position != currentGlidePosition) {
    //         Main.motorControllerMap.put("glideServoSpeed", glideServoSpeed);
    //         glideStop = false;
    //     } else {
    //         glideStop = true;
    //     }

    //     if (blackLineDetect && !blackLineFlag) {
    //         if (direction) {
    //             currentGlidePosition++; 
    //         } else {
    //             currentGlidePosition--;
    //         }
    //         blackLineFlag = true;
    //     }

    //     if (!blackLineDetect && blackLineFlag) {
    //         blackLineFlag = false;
    //     }

    //     Main.switchMap.put("glideStop", glideStop);
    // }
}