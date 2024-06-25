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
    
    private static boolean liftStop, rotateStop = false;

    private static final double[][] arrOfpercentForLift = { { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 }, 
                                                                    { -1, 0, 15, 30, 40, 55, 70, 80, 90, 100 } };

    private static final double[][] arrOfPosForLift = { { -1, 0, 15, 30, 40, 55, 70, 80, 90, 100 }, 
                                                    { 0, 600, 900, 1200, 1500, 1800, 2100, 2400, 2900, 3200 } };

    private static final double[][] speedForLift = { { 0, 20, 50, 70, 100, 330, 500, 700, 1000 },
                                                      { 0, 2, 5, 7, 20, 39, 55, 70, 80 } };

    private static final double[][] arrOfPosForRotate = { { -1, 500, 1500, 2000 }, { -1, 45, 90, 110 } };

    private static final double[][] speedForRotate =  { { 0, 0.5, 2, 10, 17, 26, 39, 50, 62, 70 }, 
                                                        { 0, 2, 5, 20, 35, 47, 60, 70, 77, 85 } };
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            double startTime = Timer.getFPGATimestamp();
            try {
                if(Main.motorControllerMap.get("resetPID") == 1.0) {
                    resetPIDRight();
                    resetPIDLeft();
                    resetPIDRotate();
                    resetPIDLift();
                    Main.motorControllerMap.put("resetPID", 0.0);
                }

                if (Main.motorControllerMap.get("resetEncs") == 1.0) {
                    resetEncRight();
                    resetEncLeft();
                    resetEncRotate();
                    resetEncLift();
                    Main.motorControllerMap.put("resetEncs", 0.0);
                }

                if(Main.motorControllerMap.get("resetEncRight") == 1.0) {
                    resetEncRight();
                    Main.motorControllerMap.put("resetEncRight", 0.0);
                }

                if(Main.motorControllerMap.get("resetEncRight") == 1.0) {
                    resetEncRight();
                    Main.motorControllerMap.put("resetEncRight", 0.0);
                }

                if(Main.motorControllerMap.get("resetEncLeft") == 1.0) {
                    resetEncLeft();
                    Main.motorControllerMap.put("resetEncLeft", 0.0);
                }

                if(Main.motorControllerMap.get("resetEncRotate") == 1.0) {
                    resetEncRotate();
                    Main.motorControllerMap.put("resetEncRotate", 0.0);
                }

                if(Main.motorControllerMap.get("resetEncLift") == 1.0) {
                    resetEncLift();
                    Main.motorControllerMap.put("resetEncLift", 0.0);
                }

                Main.motorControllerMap.put("rpmRight", ENC_RIGHT.getSpeed());
                Main.motorControllerMap.put("rpmLeft", ENC_LEFT.getSpeed());
                Main.motorControllerMap.put("rpmRotate", ENC_ROTATE.getSpeed());
                Main.motorControllerMap.put("rpmLift", ENC_LIFT.getSpeed());

                Main.motorControllerMap.put("encRight", ENC_RIGHT.getEncoderDistance()); 
                Main.motorControllerMap.put("encLeft", ENC_LEFT.getEncoderDistance());
                Main.motorControllerMap.put("encRotate", ENC_ROTATE.getEncoderDistance());
                Main.motorControllerMap.put("encLift", ENC_LIFT.getEncoderDistance());

                setAxisSpeed(Main.motorControllerMap.get("speedX"), Main.motorControllerMap.get("speedZ"));
                setRotateMotorSpeed(Main.motorControllerMap.get("rotateSpeed"));
                setLiftMotorSpeed(Main.motorControllerMap.get("liftSpeed"));

                setServoGrab(Main.motorControllerMap.get("servoGrab"));
                setServoGripRotate(Main.motorControllerMap.get("servoGripRotate"));    
                setGlideServoSpeed(Main.motorControllerMap.get("glideServoSpeed"));

                if(Main.motorControllerMap.get("targetRotateDegree") != 0.0) {
                    SetRotatePosition(Main.motorControllerMap.get("targetRotateDegree"));
                }
                Main.switchMap.put("rotateStop", rotateStop);
                
                if(Main.motorControllerMap.get("targetLiftPos") != 0.0) {
                    setLiftPosition(Main.motorControllerMap.get("targetLiftPos"));
                }
                Main.switchMap.put("liftStop", liftStop);

                Main.motorControllerMap.put("updateTime", motorsUpdateTime);

                Thread.sleep(20);
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

    private final PID PID_RIGHT = new PID(0.051, 0.43, 0.0, -100, 100); 
    private final PID PID_LEFT = new PID(0.051, 0.43, 0.0, -100, 100); 
    private final PID PID_ROTATE = new PID(0.051, 0.43, 0.0, -100, 100); 
    private final PID PID_LIFT = new PID(0.051, 0.43, 0.0, -100, 100);

    private final Servo SERVO_GRAB = new Servo(Constants.SERVO_GRAB);
    private final Servo SERVO_GRIP_ROTATE = new Servo(Constants.SERVO_GRIP_ROTATE);
    private final ServoContinuous SERVO_GLIDE = new ServoContinuous(Constants.SERVO_GLIDE);

    double liftCurrentPosition = 0; // Первоначальная позиция лифта
    
    private void setLiftPosition(double pos) {

        double currentPos = -Main.motorControllerMap.get("encLift");

        double percentPos = Functions.TransitionFunction(currentPos, arrOfpercentForLift);
        double encPos = Functions.TransitionFunction(pos, arrOfPosForLift);

        double speed = Functions.TransitionFunction(currentPos - encPos, speedForLift);

        liftStop = Functions.BooleanInRange(currentPos - encPos, -5, 5);
        
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
    }
    
    private void SetRotatePosition(double degree) {
        
        double currentRotatePos = -Main.motorControllerMap.get("encRotate");
        
        
        double rotateDegree = Functions.TransitionFunction(currentRotatePos, arrOfPosForRotate);
        double speed = Functions.TransitionFunction(rotateDegree - degree, speedForRotate);
        
        rotateStop = Functions.BooleanInRange(degree - rotateDegree, -0.2, 0.2);

        Main.motorControllerMap.put("currentRotateDegree", rotateDegree);
        // SmartDashboard.putNumber("currentRotatePos", -Main.motorControllerMap.get("encRotate"));
        // SmartDashboard.putNumber("currentRotatePosition", rotateDegree);
        // SmartDashboard.putNumber("rotateDiff", rotateDegree - degree);
        // SmartDashboard.putNumber("rotateSpeedOut", speed);
        // SmartDashboard.putBoolean("rotateStop", rotateStop);

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
            PID_RIGHT.calculate(ENC_RIGHT.getSpeed(), speed);
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

    private void resetEncRight() {
        ENC_RIGHT.reset();
    }

    private void resetEncLeft() {
        ENC_LEFT.reset();
    }

    private void resetEncRotate() {
        ENC_ROTATE.reset();
    }

    private void resetEncLift() {
        ENC_LIFT.reset();
    }

    private void resetPIDRight() {
        PID_RIGHT.reset();
    }

    private void resetPIDLeft() {
        PID_LEFT.reset();
    }

    private void resetPIDRotate() {
        PID_ROTATE.reset();
    }

    private void resetPIDLift() {
        PID_LIFT.reset();
    }

    private void setServoGrab(double angle) {
        try {
            SERVO_GRAB.setAngle(angle);
        } catch (Exception e) {
            System.out.println("Error in setServoGrab");
            e.printStackTrace();
        } 
    }

    private void setServoGripRotate(double angle) {
        try {
            SERVO_GRIP_ROTATE.setAngle(angle);
        } catch (Exception e) {
            System.out.println("Error in setServoGripRotate");
            e.printStackTrace();
        }
    }

    private void setGlideServoSpeed(double speed) {
        try {
            SERVO_GLIDE.set(speed);
        } catch (Exception e) {
            System.out.println("Error in setGlideServoSpeed");
            e.printStackTrace();
        }  
    }
}