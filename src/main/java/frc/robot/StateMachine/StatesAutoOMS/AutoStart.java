package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.Transition;
import frc.robot.StateMachine.StatesOMS.SetLiftPosition;
import frc.robot.StateMachine.StatesOMS.SetRotatePosition;
import frc.robot.Maths.Common.Functions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;

public class AutoStart implements IState {

    private boolean flag, oneTimeFlag;
    private boolean treeMode = false;
    private boolean stateEnd = false;

    private boolean treeEnd = false;

    private int branchNumber = 0;
    
    private double GRIP_ROTATE, CAM_TASK = 0.0;

    private static double startTime = 0;

    private static double realStartTime = 0.0;

    

    private ArrayList<IState> newStates = new ArrayList<>();
    
    public AutoStart() {
        this.treeMode = false;
        GRIP_ROTATE = Constants.GRIP_ROTATE_CHECK_ZONE;
        CAM_TASK = 10.0;
    }

    public AutoStart(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        oneTimeFlag = true;
        GRIP_ROTATE = 85.0;
        CAM_TASK = 4.0;
    }

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", CAM_TASK);
        Main.camMap.put("currentColorIndex", 0.0);
        // Main.stringMap.put("detectedFruit", "none");
        Main.switchMap.put("targetColorFound", false);

        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);
        Main.motorControllerMap.put("servoGrab", Constants.GRAB_OPEN);

        realStartTime = Timer.getFPGATimestamp();

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        flag = false;
    }

    @Override
    public void execute() {

        if(Main.switchMap.get("targetColorFound")) {
            if(treeMode) {
        
                if (branchNumber == 1 && !treeEnd ) {
                    Main.motorControllerMap.put("servoGripRotate", 85.0);
                    Main.motorControllerMap.put("targetRotateDegree", 26.0);
                    treeEnd = Main.switchMap.get("rotateStop") && StateMachine.iterationTime > 2; 
                }
    
                if (branchNumber == 2 && !treeEnd) {
                    Main.motorControllerMap.put("targetLiftPos", 39.0);
                    Main.motorControllerMap.put("servoGripRotate", 85.0);
                    Main.motorControllerMap.put("targetRotateDegree", -24.0);
                    treeEnd = Main.switchMap.get("rotateStop") && Main.switchMap.get("liftStop") && StateMachine.iterationTime > 2; 
                }
    
                if (branchNumber == 3 && !treeEnd) {
                    Main.motorControllerMap.put("servoGripRotate", 85.0);
                    Main.motorControllerMap.put("targetLiftPos", 72.0);
                    Main.motorControllerMap.put("targetRotateDegree", 0.0);
                    treeEnd = Main.switchMap.get("rotateStop") && Main.switchMap.get("liftStop") && StateMachine.iterationTime > 3; 
                }
    
                if (treeEnd) {
                    SmartDashboard.putNumber("estimatedTime", Timer.getFPGATimestamp() - realStartTime);
                    Main.sensorsMap.put("camTask", 4.0); 
                    if(Main.camMap.get("targetFound") == 1.0 && Timer.getFPGATimestamp() - realStartTime > 1) {
                        newStates.add(new AutoGlide(true, branchNumber));
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;
                    }
                }
            } else {
                Main.sensorsMap.put("camTask", 1.0);

                if(Main.camMap.get("targetFound") == 1.0) {
                    SmartDashboard.putNumber("perperper", 1); 
                    if(treeMode) {
                        newStates.add(new AutoRotate());
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;  
                    } else {
                        SmartDashboard.putNumber("perperper", 2); 
                        Main.sensorsMap.put("camTask", 2.0);
                        newStates.add(new AutoRotate());
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;
                    }
                }
    
                if (!flag && StateMachine.iterationTime > 10) {
                    newStates.add(new AutoEnd()); 
                    StateMachine.states.addAll(StateMachine.index + 1, newStates);
                    stateEnd = true;
                }
            }
            SmartDashboard.putBoolean("flagCheck", flag);
        } else {
            if(StateMachine.iterationTime > 5) {
                newStates.add(new AutoEnd());
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
        }

        // SmartDashboard.putNumber("currentColorIndex", currentColorIndex);
        // Main.camMap.put("currentColorIndex", currentColorIndex);
        
        
    }
    
    @Override
    public void finilize() {
        // if (!flag) 
        
        // Main.sensorsMap.put("camTask", 0.0);
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        return stateEnd;
    }
}