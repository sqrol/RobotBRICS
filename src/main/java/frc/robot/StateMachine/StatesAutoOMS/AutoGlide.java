package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;

public class AutoGlide implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    private double fruitPosY = 0;
    
    private int branchNumber = 0;
    private final int MAX_GLIDE_POS = 29;

    private double glideServoSpeed, GRIP_ROTATE = 0; 
    private double timestamp = 0;
    
    private boolean treeMode = false;

    private boolean flag, stateEnd = false; 

    private boolean treeEnd, glideStop = false;
    private boolean keepTrack = true;

    private double camMiddleForGlide = 10;

    private double MAX_FRUIT_POS_Y = 90.0;
    private boolean endFlag = false;
    
    private static final double[][] speedForGlideServo = { { 0, 1, 4, 10, 20, 40, 60, 80, 100 }, { 0, 0.1, 0.1, 0.1, 0.1, 0.18, 0.3, 0.4, 0.4} };

    public AutoGlide() {
        flag = false; 
        GRIP_ROTATE = 131.0;
    }

    public AutoGlide(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        GRIP_ROTATE = 85.0;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("glideMode", 1.0);
        if(!treeMode)
            Main.sensorsMap.put("camTask", 2.0); 
        else Main.sensorsMap.put("camTask", 6.0);

        // Main.switchMap.put("glideStop", false);
        // glideStop = false;
    }

    @Override
    public void execute() {
        
        if(treeMode) {
            Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);
            SmartDashboard.putBoolean("GLIDE STOP AUTO", glideStop);
            Main.sensorsMap.put("camTask", 2.0);
            Main.motorControllerMap.put("glideMode", 0.0);
            if(branchNumber == 1 && !treeEnd) {
                Main.sensorsMap.put("targetGlidePos", 21.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(branchNumber == 2 && !treeEnd) {
                Main.sensorsMap.put("targetGlidePos", 21.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(branchNumber == 3 && !treeEnd) {
                Main.sensorsMap.put("targetGlidePos", 13.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(treeEnd) {
                newStates.add(new AutoGrab(true));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }

        } else {
            boolean oneTimeFlag = false;

            Main.motorControllerMap.put("targetLiftPos", 80.0);
            
            if(!oneTimeFlag && StateMachine.iterationTime < 2) {
                Main.motorControllerMap.put("servoGripRotate", 105.0);
                Main.sensorsMap.put("camTask", 2.0);
                if(!Main.stringMap.get("detectedFruit").equals("none")) {
                    oneTimeFlag = true;
                } 
                
            } else {
                Main.sensorsMap.put("camTask", 6.0);
                Main.motorControllerMap.put("servoGripRotate", 118.0);

                if(Main.switchMap.get("trackImageArea") && !Main.stringMap.get("detectedFruit").equals("none")) {
                    glideServoSpeed = 0.15;
                    SmartDashboard.putNumber("autoglide check", 1111);
                } else if(!Main.switchMap.get("trackImageArea") && Main.sensorsMap.get("camTask") == 6.0 && StateMachine.iterationTime > 5){                    
                    SmartDashboard.putNumber("autoglide check", 4444);
                    glideServoSpeed = 0.0;
                    glideStop = true;
                    
                }
            }
            SmartDashboard.putNumber("glideServoSpeedAutoGLIDE", glideServoSpeed);
            Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
            SmartDashboard.putBoolean("glideStopAutoGlide", glideStop);
        }

        if (Main.sensorsMap.get("currentGlidePos") > MAX_GLIDE_POS || StateMachine.iterationTime > 25) {
            SmartDashboard.putNumber("AUTOGLIDE CHECK", 4444);
            newStates.add(new AutoEnd(true)); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if (glideStop && !treeMode) {
            SmartDashboard.putNumber("AUTOGLIDE CHECK", 333);
            newStates.add(new AutoGrab(true)); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        // if (glideStop && !treeMode && StateMachine.iterationTime > 2) {
        //     SmartDashboard.putNumber("AUTOGLIDE CHECK", 222);
        //     newStates.add(new AutoGrab()); 
        //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
        //     stateEnd = true;
        // }

        if (glideStop && treeMode && StateMachine.iterationTime > 2) {
            SmartDashboard.putNumber("AUTOGLIDE CHECK", 222);
            newStates.add(new AutoGrab(true)); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        } 
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("setGlideSpeed", 0.0);
        // Main.motorControllerMap.put("glideMode", 0.0);
        Main.sensorsMap.put("camTask", 0.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        // return false;
        return stateEnd;
    }
}