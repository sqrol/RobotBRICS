package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;

public class AutoGlide implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    private double fruitPosY = 0;
    
    private int branchNumber = 0;

    private double glideServoSpeed, GRIP_ROTATE = 0; 
    
    private boolean treeMode = false;

    private boolean flag, stateEnd = false; 

    private boolean treeEnd, glideStop = false;
    
    private int camMiddleForGrab = 27;
    
    private static final double[][] speedForGlideServo = { { 0, 1, 4, 10, 20, 40, 60, 80, 100 }, { 0, 0.1, 0.1, 0.1, 0.1, 0.25, 0.3, 0.4, 0.4} };

    public AutoGlide() {
        flag = false; 
        GRIP_ROTATE = 75.0;
    }

    public AutoGlide(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        if(branchNumber == 1) {
            GRIP_ROTATE = 22.0;
        }
        if(branchNumber == 2) {
            GRIP_ROTATE = 30.0;
        }
        if(branchNumber == 3) {
            GRIP_ROTATE = 28.0;
        }
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("glideMode", 1.0);
        if(!treeMode)
            Main.sensorsMap.put("camTask", 2.0); 
        // else Main.sensorsMap.put("camTask", 0.0);
        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);
        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.switchMap.put("glideStop", false);
    }

    @Override
    public void execute() {

        if(treeMode) {
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
                Main.sensorsMap.put("targetGlidePos", 11.0);
                treeEnd = Main.switchMap.get("glideStop") && StateMachine.iterationTime > 4;
            }

            if(treeEnd) {
                newStates.add(new AutoGrab(true));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
            
        } else {
            fruitPosY = Main.camMap.get("currentCenterY");

            if (fruitPosY == 0 && !flag) {
                glideServoSpeed = 0.3;
            } else {
                flag = true; 
                glideServoSpeed = Functions.TransitionFunction(camMiddleForGrab - fruitPosY, speedForGlideServo); 
            }

            Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
            glideStop = Functions.BooleanInRange(camMiddleForGrab - fruitPosY, -2, 2);

            SmartDashboard.putBoolean("glideStop", glideStop);

            if (Main.sensorsMap.get("currentGlidePos") >= 23 || StateMachine.iterationTime > 25) {
                newStates.add(new AutoEnd()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }

            if (glideStop && StateMachine.iterationTime > 2) {
                newStates.add(new AutoGrab()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
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