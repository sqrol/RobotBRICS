package frc.robot.subsystems;

import com.studica.frc.ServoContinuous;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.MotorSafety;
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

    private double encRightResetValue = 0;
    private double encLeftResetValue = 0;
    private double encRotateResetValue = 0;

    // Переменные для функции setGlidePosition()
    private boolean flag = false;
    private double glideTimer = 0; 

    private boolean glideStop = false;

    private static double currentGlidePosition = 0;
    private static double currentRotatePosition = 0;
    
    private static boolean liftStop, rotateStop = false;

    private static boolean fristCall = true; 

    private static final double[][] arrOfPosForLift = { { 0, 4, 15, 30, 40, 50, 60, 70, 80, 90}, { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 } };

    private static final double[][] speedForLift = { { 0, 30, 50, 100, 200, 330, 500, 700, 850, 1000 }, { 0, 8, 15, 20, 25, 35, 40, 50, 60, 100 } };

    private static final double[][] arrOfPosForRotate = { { 0, 500, 1500, 2000 }, { 0, 45, 90, 110 } };

    private static final double[][] speedForRotate =  { { 0, 5, 10, 14, 17, 26, 39, 50, 62, 70 }, { 0, 6, 15, 25, 35, 47, 60, 70, 77, 85 } };

    private static final double[][] speedForGlideServo = { { 0, 1, 2, 4, 6, 8, 10 }, { 0, 0.24, 0.24, 0.24, 0.24, 0.24, 0.24}};

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

                if (fristCall) {
                    resetEncRight();
                    resetEncLeft();
                    resetEncRotate();
                    fristCall = false;
                }

                if(Main.switchMap.get("EMSButton")) {
                    setRightMotorSpeed(0.0);
                    setLeftMotorSpeed(0.0);
                    setRotateMotorSpeed(0.0);
                    setLiftMotorSpeed(0.0);

                    SERVO_GLIDE.setDisabled();
                    SERVO_GRAB.setDisabled();
                    SERVO_GRIP_ROTATE.setDisabled(); 
                } else {
                    setAxisSpeed (
                                Main.motorControllerMap.get("speedX"), 
                                Main.motorControllerMap.get("speedZ"),  
                                Main.motorControllerMap.get("useOneSide")
                    );

                    setRotateMotorSpeed(Main.motorControllerMap.get("rotateSpeed"));

                    setLiftPosition(Main.motorControllerMap.get("targetLiftPos"));

                    setLiftMotorSpeed(Main.motorControllerMap.get("liftSpeed"));

                    setServoGrab(Main.motorControllerMap.get("servoGrab"));
                    setServoGripRotate(Main.motorControllerMap.get("servoGripRotate"));   
                    
                    setRotatePosition(Main.motorControllerMap.get("targetRotateDegree"));

                    if (Main.motorControllerMap.get("glideMode") == 0.0) {
                        setGlidePosition(Main.sensorsMap.get("targetGlidePos"));
                    } else {
                        setGlideSpeed(Main.motorControllerMap.get("setGlideSpeed"));
                    }     
                }

                encodersValueHandler();

                encodersValueResetHandler(); 

                Main.motorControllerMap.put("currentGlidePos", currentGlidePosition);
                Main.motorControllerMap.put("currentRotatePosition", currentRotatePosition);

                Main.motorControllerMap.put("servoGrabAngle", getServoGrabAngle());
                Main.motorControllerMap.put("updateTime", motorsUpdateTime);      

                Thread.sleep(10);
            } catch (Exception e) {
                System.err.println("!!!An error occurred in MotorController: " + e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(50); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
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

        if (Main.motorControllerMap.get("resetAllEncoders") == 1.0) {
            resetEncRight();
            resetEncLeft();
            resetEncRotate();
            Main.motorControllerMap.put("resetAllEncoders", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncRotate") == 1.0) {
            resetEncRotate();
            Main.motorControllerMap.put("resetEncRotate", 0.0);
        }

        if(Main.motorControllerMap.get("resetEncLift") == 1.0) {
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
        return -ENC_ROTATE.getEncoderDistance();
    }

    private double getEncLift() {
        return -ENC_LIFT.getEncoderDistance();
    }

    private void resetEncRight() {
        encRightResetValue = ENC_RIGHT.getEncoderDistance();
    }

    private void resetEncLeft() {
        encLeftResetValue = ENC_LEFT.getEncoderDistance();
    }

    private void resetEncRotate() {
        ENC_ROTATE.reset();
    }

    private void setLiftPosition(double targetPosition) {

        double outLiftSpeed = 0.0;
        double liftEncoder = getEncLift();

        SmartDashboard.putNumber("targetPosition", targetPosition);

        if (Main.switchMap.get("initLift")) {
            if (Main.switchMap.get("limitSwitchLift")) {
                outLiftSpeed = 0.0;
                ENC_LIFT.reset();
            } else {
                outLiftSpeed = 60.0; 
            }
        } else {
            double convertPosToEncs = Functions.TransitionFunction(targetPosition, arrOfPosForLift);
            outLiftSpeed = Functions.TransitionFunction(liftEncoder - convertPosToEncs, speedForLift);
            liftStop = Functions.BooleanInRange(liftEncoder - convertPosToEncs, -5, 5);
        }

        if (outLiftSpeed > 0.0 && Main.switchMap.get("limitSwitchLift")) {
            SmartDashboard.putNumber("checkLift", 1);
            ENC_LIFT.reset();
            outLiftSpeed = 0.0;
        }
        if (outLiftSpeed < 0.0 && liftEncoder < -3000) {
            SmartDashboard.putNumber("checkLift", 2);
            outLiftSpeed = 0.0;
        }
        if (liftStop && !Main.switchMap.get("initLift")) {
            SmartDashboard.putNumber("checkLift", 3);
            outLiftSpeed = 0.0;
        }

        Main.motorControllerMap.put("liftSpeed", outLiftSpeed);
        Main.switchMap.put("liftStop", liftStop);
    }

    private void setRotatePosition(double degree) {
        
        double currentRotatePos = getEncRotate();
        double rotateDegree = Functions.TransitionFunction(currentRotatePos, arrOfPosForRotate);
        double rotateSpeed = Functions.TransitionFunction(rotateDegree - degree, speedForRotate);
        
        rotateStop = Functions.BooleanInRange(degree - rotateDegree, -1, 1);

        Main.motorControllerMap.put("currentRotateDegree", rotateDegree);

        if (rotateStop) {
            rotateSpeed = 0.0;
            rotateStop = true;
        } else {
            Main.motorControllerMap.put("rotateSpeed", rotateSpeed);
        }

        if ((rotateSpeed > 0 && currentRotatePos > 1600) || (rotateSpeed < 0 && currentRotatePos < -1600)) {
            rotateSpeed = 0.0;
            rotateStop = true;
        } 

        Main.motorControllerMap.put("rotateSpeed", rotateSpeed);
        Main.switchMap.put("rotateStop", rotateStop);
    }

    private void setAxisSpeed(double x, double z, double useOneSide) {
        if (useOneSide == 1.0) {
            setRightMotorSpeed(x);
            setLeftMotorSpeed(z);
        } else {
            double right = x + z;
            double left = -x + z;

            setRightMotorSpeed(right);
            setLeftMotorSpeed(left);
        }
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

    private void setServoGrab(double targetPosition) {
        try {
            SERVO_GRAB.setAngle(targetPosition);
        } catch (Exception e) {
            System.out.println("Error in setServoGrab: " + e.getMessage());
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

    private void countGlidePosition(boolean direction) {
        boolean blackLineDetect = Main.sensorsMap.get("cobraVoltage") > 2.0;

        if (blackLineDetect && !flag && Timer.getFPGATimestamp() - glideTimer > 0.1) {
            flag = true;
            glideTimer = Timer.getFPGATimestamp();
          
        }
        if (!blackLineDetect && flag && Timer.getFPGATimestamp() - glideTimer > 0.1) {
            flag = false;
            glideTimer = Timer.getFPGATimestamp();
            if (direction)
                currentGlidePosition++;
            else
                currentGlidePosition--;
            }

        Main.sensorsMap.put("currentGlidePos", (double) currentGlidePosition);
    }

    private void setGlidePosition(double targetGlidePosition) {
        boolean limitSwitchGlide = Main.switchMap.get("limitSwitchGlide");
        double glideDiff = currentGlidePosition - targetGlidePosition;
        double glideSpeed = Functions.TransitionFunction(glideDiff, speedForGlideServo);
        glideStop = targetGlidePosition == currentGlidePosition;

        if(Main.switchMap.get("initGlide")) {
            glideSpeed = 0.32;
            if(limitSwitchGlide) {
                glideSpeed = 0;
                Main.switchMap.put("initGlide", false);
            }
        }

        if(targetGlidePosition == 0 && !limitSwitchGlide && Main.switchMap.get("initGlide")) {
            glideStop = false;
            glideSpeed = 0.32;
        }

        if(limitSwitchGlide && glideSpeed > 0) {
            currentGlidePosition = 0;
            setGlideServoSpeed(0);
            glideStop = true;
        } 

        if (targetGlidePosition > currentGlidePosition) {
            countGlidePosition(true);
        } else if(targetGlidePosition < currentGlidePosition && targetGlidePosition != 0){
            countGlidePosition(false);
        }

        setGlideServoSpeed(-glideSpeed);

        Main.switchMap.put("glideStop", glideStop);
    }

    private void setGlideSpeed(double inSpeed) {
        double glideSpeed = inSpeed;

        if (inSpeed == 0.0) {
            SERVO_GLIDE.setDisabled();
            return; 
        }

        if(inSpeed < 0 && Main.switchMap.get("limitSwitchGlide")) {
            glideSpeed = 0;
        }

        if (inSpeed > 0.0) {
            countGlidePosition(true);
        } else {
            countGlidePosition(false);
        }

        setGlideServoSpeed(glideSpeed);
    }
}