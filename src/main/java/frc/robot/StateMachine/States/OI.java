package frc.robot.StateMachine.States;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.GamepadConstants;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class OI implements IState {
    // flags for paths changing
    private boolean IR = false;
    private boolean US = false;
    private boolean drive = false;
    
    // // SonicDrive
    private boolean isFirstIterSonic = true;
    private double sonicX = 0;
    private double sonicDiffX, sonicDiffZ = 0;
    private double sonicLastGyro = 0;

    private boolean finishXSonic, finishZSonic = false;

    private double[][] sonicArray = { { 0, 1, 2.5, 5, 10, 14, 18, 35, 50, 100 },
            { 0, 3, 10, 12, 18, 35, 50, 65, 78, 80 } };

    private static double[][] sonicDegFunction = { { 0.1, 2, 4, 8, 12, 15, 20, 25 }, { 5, 9, 11, 18, 24, 26, 33, 35 } };

    // // SharpAlign
    private boolean isFirstIterSharp = true;
    private boolean sharpFinishX, sharpFinishZ = false;

    private double sharpX = 0;
    private double sharpSpeedX, sharpDiffX = 0;
    private double sharpSpeedZ = 0;
    private double sharpDiff, sharpLastGyro = 0;
    private double sharpCoefForTime = 0;

    private static double[][] SharpXArray = { { 0, 0.1, 2.5, 5, 10, 15, 25, 30 }, { 0, 0.4, 5, 12, 20, 36, 60, 80 } };

    private static double[][] sharpDegFunction = { { 0.1, 2, 4, 8, 12, 15, 20, 25 }, { 3, 9, 11, 18, 24, 26, 33, 35 } };

    private static double[][] sharpArrayForTime = { { 0, 0.33, 0.66, 1 }, { 0, 0.33, 0.66, 1 } };

    // // for simple drive

    private boolean firstIterSimpleDrive = true;

    private double XPosition, ZPosition = 0;
    private double posX, posZ = 0;
    private double speedX, speedZ = 0;

    private boolean finishX, finishZ = false;

    private double[][] speedXArray = { { 0, 1, 10, 12, 14, 18, 35, 50, 100 }, { 0, 10, 15, 30, 45, 50, 65, 75, 95 } };

    private double[][] speedZArray = { { 0.1, 0.5, 1.5, 2, 3, 6, 12, 26, 32, 50 },
            { 3, 8, 12, 18, 25, 30, 40, 53, 60, 70 } };

    private double[][] speedZArrayJustTurn = { { 0, 1, 5, 10, 26, 60, 75, 90 }, { 0, 2, 4, 10, 17, 35, 50, 70 } };

    private double[][] startKoefSpeedForX = { { 0.33, 0.66, 1 }, { 0.33, 0.66, 1 } };

    @Override
    public void initialize() {

        SmartDashboard.putNumber("sonicDistance", 0.0);
        SmartDashboard.putNumber("sharpDistance", 0.0);
        SmartDashboard.putNumber("targetZ", 0.0);
        SmartDashboard.putNumber("distX", 0.0);

        try {
            System.setErr(new PrintStream(new File("/home/pi/Desktop/PATHS.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() {

        if(getDriveYButton()) {
            drive = true;
            IR = false;
            US = false;
        } else if(getDriveXButton()) {
            IR = true;
            US = false;
            drive = false;
        } else if(getDriveBButton()) {
            US = true;
            IR = false;
            drive = false;
        }

        if(getDriveAButton()) {
            isFirstIterSharp = true;
            isFirstIterSonic = true;
            Main.motorControllerMap.put("resetDriveEncs", 1.0);
            drive = false;
            IR = false;
            US = false;
            posX = 0.0;
            posZ = 0.0;
            SmartDashboard.putNumber("sonicDistance", 0.0);
            SmartDashboard.putNumber("sharpDistance", 0.0);
            SmartDashboard.putNumber("targetZ", 0.0);
            SmartDashboard.putNumber("distX", 0.0);
            Main.motorControllerMap.put("speedX", 0.0);
            Main.motorControllerMap.put("speedZ", 0.0);
            Main.sensorsMap.put("resetGyro", 1.0);
            Main.sensorsMap.put("resetGyro", 0.0);
        }

        if(drive) {
            posX = ((Main.motorControllerMap.get("encRight") - Main.motorControllerMap.get("encLeft")) / 2) / 48;
            
            Main.motorControllerMap.put("speedX", -getRightDriveY() * 100);
            Main.motorControllerMap.put("speedZ", -getLeftDriveX() * 100);

            if(getDriveLeftBumper()) posZ = -90;

            if(getDriveDPadX()) posZ = 90;
            
            if (getDriveRightBumper()) {
                try {
                    if(posX != 0 && posZ == 0.0) {
                        System.err.println("newStates.add(new SimpleDrive(" + Math.round(posX) + ", " + 0 + "));");
                    } else {
                        System.err.println("newStates.add(new SimpleDrive(" + 0 + ", " + posZ + "));");
                    }
                    System.err.flush();
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    
                }
            }
            SmartDashboard.putNumber("posZJOY", posZ);
            SmartDashboard.putNumber("posXJOY", posX);
        }

        if(IR) {
            double distToWall = SmartDashboard.getNumber("sharpDistance", 0.0);
            sharpAlign(distToWall);
            SmartDashboard.putBoolean("isFirstSHARP!!!!", isFirstIterSharp);
            if (getDriveRightBumper()) {
                try {
                    System.err.println("newStates.add(new AlignSharp(" + distToWall + "));");
                    System.err.flush();
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                
                }
            }
        }

        if(US) {
            double distToWall = SmartDashboard.getNumber("sonicDistance", 0.0);
            SmartDashboard.putBoolean("isFirstSONIC!!!!", isFirstIterSonic);
            driveSonic(distToWall);
            if (getDriveRightBumper()) {
                try {
                    System.err.println("newStates.add(new DriveSonic(" + distToWall + "));");
                    System.err.flush();
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                
                }
            }
        }
        SmartDashboard.putBoolean("US", US);
        SmartDashboard.putBoolean("drive", drive);
        SmartDashboard.putBoolean("IR", IR);
    }
    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
    // только вот это починить и добавить поворот и будет работать
    private boolean driveSonic(double distToWall) {
        
        if(distToWall != 0.0) {
            if(isFirstIterSonic) {
                sonicLastGyro = Main.sensorsMap.get("srcGyro");
                Main.motorControllerMap.put("resetDriveEncs", 1.0);
                isFirstIterSonic = false;
            } else {
                SmartDashboard.putNumber("sonicLastGyro!!!!!", sonicLastGyro);
                double backSonicDist = Main.sensorsMap.get("sonicRight");
    
                sonicDiffX = distToWall - backSonicDist;
                sonicDiffZ = sonicLastGyro - Main.sensorsMap.get("srcGyro");
        
                double speedX = Functions.TransitionFunction(sonicDiffX, sonicArray);
                double speedZ = Functions.TransitionFunction(sonicDiffZ, sonicDegFunction);
        
                Main.motorControllerMap.put("speedX", speedX);
                Main.motorControllerMap.put("speedZ", -speedZ);
                
                finishXSonic = Functions.BooleanInRange(sonicX - backSonicDist, -0.5, 0.5);
                finishZSonic = Functions.BooleanInRange(sonicLastGyro - Main.sensorsMap.get("srcGyro"), -0.2, 0.2);
            }
            if(finishX && finishZ) {
                Main.motorControllerMap.put("resetEncRight", 1.0);
                Main.motorControllerMap.put("resetEncLeft", 1.0);
                Main.sensorsMap.put("resetGyro", 1.0);
                isFirstIterSonic = true;
                Main.motorControllerMap.put("speedX", 0.0);
                Main.motorControllerMap.put("speedZ", 0.0);
                Main.sensorsMap.put("resetGyro", 0.0);
            }
        }
        return finishX && finishZ;
        
    }

    private boolean sharpAlign(double distToWall) {
        
        if(distToWall != 0.0) {

            if(isFirstIterSharp) {
                sharpLastGyro = Main.sensorsMap.get("srcGyro");
                Main.motorControllerMap.put("resetDriveEncs", 1.0);
                isFirstIterSharp = false;
            } else {
                sharpCoefForTime = Functions.TransitionFunction(StateMachine.iterationTime, sharpArrayForTime);
            
                double leftSharp = Main.sensorsMap.get("sharpLeft");
                double rightSharp = Main.sensorsMap.get("sharpRight");
        
                sharpDiffX = distToWall - Math.min(leftSharp, rightSharp);
                sharpSpeedX = Functions.TransitionFunction(sharpDiffX, SharpXArray);
        
                if (Math.min(leftSharp, rightSharp) < 30) {
                    sharpDiff = leftSharp - rightSharp;
                    sharpSpeedZ = Functions.TransitionFunction(sharpDiff, sharpDegFunction);
                } else {
                    sharpSpeedZ = (sharpLastGyro - Main.sensorsMap.get("srcGyro"));
                }
        
                Main.motorControllerMap.put("speedX", -sharpSpeedX * sharpCoefForTime);
                Main.motorControllerMap.put("speedZ", -sharpSpeedZ);
        
                sharpFinishX = Functions.BooleanInRange(sharpSpeedX, -0.5, 0.5);
                sharpFinishZ = Functions.BooleanInRange(sharpSpeedZ, -0.2, 0.2);

                if(sharpFinishZ && sharpFinishX) {
                    Main.motorControllerMap.put("resetEncRight", 1.0);
                    Main.motorControllerMap.put("resetEncLeft", 1.0);
                    Main.sensorsMap.put("resetGyro", 1.0);
                    isFirstIterSharp = true;
                    Main.motorControllerMap.put("speedX", 0.0);
                    Main.motorControllerMap.put("speedZ", 0.0);
                }
            }
            
        }  
        return sharpFinishX && sharpFinishZ;
    }

    private boolean simpleDrive(double XPosition, double targetAngle) {
        
        if(firstIterSimpleDrive) {
            Main.motorControllerMap.put("resetDriveEncs", 1.0);
            Main.sensorsMap.put("resetGyro", 1.0);
            firstIterSimpleDrive = false;
        }

        double startKoef = Functions.TransitionFunction(StateMachine.iterationTime, startKoefSpeedForX);

        if (XPosition != 0) {
            
            double currentRight = Main.motorControllerMap.get("encRight");
            double currentLeft = Main.motorControllerMap.get("encLeft");

            double gyro = Main.sensorsMap.get("srcGyro");
    
            posX = ((currentRight - currentLeft) / 2) / 48;
    
            double[] polar = Functions.ReImToPolar(posX, 0);
            double[] decard = Functions.PolarToReIm(polar[0], (polar[1] + Math.toRadians(gyro))); 
    
            double diffX = XPosition - decard[0];
            
            speedX = Functions.TransitionFunction(diffX, speedXArray);
            speedZ = Functions.TransitionFunction(gyro, speedZArray);

            finishX = Functions.BooleanInRange(diffX, -0.5, 0.5); 
            finishZ = Functions.BooleanInRange(speedZ, -0.2, 0.2); 

            Main.motorControllerMap.put("posX", posX);
            Main.motorControllerMap.put("speedX", speedX * startKoef);
            Main.motorControllerMap.put("speedZ", speedZ);
    
        } else {
            speedX = 0;
            speedZ = -Functions.TransitionFunction(ZPosition - Main.sensorsMap.get("posZ"), speedZArrayJustTurn);
            
            finishX = true;
            finishZ = Functions.BooleanInRange(speedZ, -0.3, 0.3);

            Main.motorControllerMap.put("posX", posX);
            Main.motorControllerMap.put("speedX", speedX * startKoef);
            Main.motorControllerMap.put("speedZ", speedZ);
        }

        

        if(finishXSonic && finishZ) {
            firstIterSimpleDrive = true;
        }

        return finishX && finishZ;
    }

    /**
     * @return the y-axis value from the drivePad right joystick
     */
    public double getRightDriveY()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else  
            return joy;
    }

    /**
     * @return the x-axis value from the drivePad right Joystick
     */
    public double getRightDriveX()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.RIGHT_ANALOG_X);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    /**
     * @return the y-axis value from the drivePad left joystick
     */
    public double getLeftDriveY()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else  
        SmartDashboard.putNumber("triggetCheck", 12);
            return joy;
    }

    /**
     * @return the x-axis value from the drivePad left Joystick
     */
    public double getLeftDriveX()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.LEFT_ANALOG_X);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveRightTrigger()
    {
        // double joy = Main.joystick.getRawAxis(GamepadConstants.RIGHT_TRIGGER);
        // if(Math.abs(joy) < 0.05)
        //     return 0.0;
        // else
        //     return joy;
        return Main.joystick.getRawButton(GamepadConstants.RIGHT_TRIGGER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveRightBumper()
    {
        return Main.joystick.getRawButton(GamepadConstants.RIGHT_BUMPER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveLeftTrigger()
    {
        // double joy = Main.joystick.getRawAxis(GamepadConstants.LEFT_TRIGGER);
        // if(Math.abs(joy) < 0.05)
        //     return 0.0;
        // else
        //     return joy;
        return Main.joystick.getRawButton(GamepadConstants.LEFT_TRIGGER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveLeftBumper()
    {
        return Main.joystick.getRawButton(GamepadConstants.LEFT_BUMPER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveDPadX()
    {
        return Main.joystick.getRawButton(GamepadConstants.DPAD_X);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveDPadY()
    {
        return Main.joystick.getRawButton(GamepadConstants.DPAD_Y);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveXButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.SQUARE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveYButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.TRIANGLE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveBButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.CIRCLE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveAButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.X_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveBackButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.SHARE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveStartButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.OPTIONS_BUTTON);
    }    
}