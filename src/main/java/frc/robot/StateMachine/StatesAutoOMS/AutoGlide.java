package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;

public class AutoGlide implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    private double fruitPosY = 0;
    
    private int branchNumber = 0;
    private final int MAX_GLIDE_POS = 30;

    private double glideServoSpeed, GRIP_ROTATE = 0; 
    
    private boolean treeMode = false;

    private boolean flag, stateEnd = false; 

    private boolean treeEnd = false;
    private boolean glideStop = false;
    
    private int camMiddleForGrab = 25;
    
    private static final double[][] speedForGlideServo = { { 0, 1, 4, 10, 20, 40, 60, 80, 100 }, { 0, 0.15, 0.15, 0.15, 0.15, 0.15, 0.15, 0.15, 0.15} };

    public AutoGlide() {
        flag = false; 
        GRIP_ROTATE = Constants.GRIP_ROTATE_FLOOR;
    }

    public AutoGlide(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        if(branchNumber == 2 || branchNumber == 3) {
            GRIP_ROTATE = Constants.GRIP_ROTATE_FLOOR;
        } else {
            GRIP_ROTATE = Constants.GRIP_ROTATE_DROP;
        }
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("glideMode", 1.0);
        if(!treeMode)
            Main.sensorsMap.put("camTask", 2.0); 
        else Main.sensorsMap.put("camTask", 6.0);

        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);
        // Main.switchMap.put("glideStop", false);
        glideStop = false;
    }

    @Override
    public void execute() {
        
        if(treeMode) {

            Main.sensorsMap.put("camTask", 2.0);
            
            fruitPosY = Main.camMap.get("currentCenterY");

            if(branchNumber == 1 && !treeEnd) {
                Main.motorControllerMap.put("glideMode", 0.0);
                Main.motorControllerMap.put("targetLiftPos", 3.0);
                Main.sensorsMap.put("targetGlidePos", 15.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(branchNumber == 2 && !treeEnd) {
                
                Main.motorControllerMap.put("targetLiftPos", 20.0);
                
                if (fruitPosY == 0 && !flag) {
                    glideServoSpeed = 0.15;
                } else {
                    flag = true; 
                    glideServoSpeed = Functions.TransitionFunction(camMiddleForGrab - fruitPosY, speedForGlideServo); 
                }
    
                Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
                glideStop = Functions.BooleanInRange(camMiddleForGrab - fruitPosY, -2, 2) || Main.switchMap.get("stopAutoGlide");
    
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(branchNumber == 3 && !treeEnd) {
                Main.sensorsMap.put("targetGlidePos", 13.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(treeEnd) {
                newStates.add(new AutoGrab(true, branchNumber));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
            
        } else {
            fruitPosY = Main.camMap.get("currentCenterY");

            if (fruitPosY == 0 && !flag) {
                glideServoSpeed = 0.15;
            } else {
                flag = true; 
                glideServoSpeed = Functions.TransitionFunction(camMiddleForGrab - fruitPosY, speedForGlideServo); 
            }

            Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
            glideStop = Functions.BooleanInRange(camMiddleForGrab - fruitPosY, -2, 2) || Main.switchMap.get("stopAutoGlide");

            SmartDashboard.putBoolean("glideStop", glideStop);
        }

        if (Main.sensorsMap.get("currentGlidePos") > MAX_GLIDE_POS || StateMachine.iterationTime > 25) {
            SmartDashboard.putNumber("AUTOGLIDE CHECK", 111);
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        // if (glideStop && treeMode && StateMachine.iterationTime > 2) {
        //     SmartDashboard.putNumber("AUTOGLIDE CHECK", 222);
        //     newStates.add(new AutoGrab(true)); 
        //     StateMachine.states.addAll(StateMachine.index + 1, newStates);
        //     stateEnd = true;
        // }

        if (glideStop && !treeMode && StateMachine.iterationTime > 2) {
            SmartDashboard.putNumber("AUTOGLIDE CHECK", 333);
            newStates.add(new AutoGrab()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("setGlideSpeed", 0.0);
        
        Main.sensorsMap.put("camTask", 1.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        // return false;
        return stateEnd;
    }
}