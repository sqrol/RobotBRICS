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
        GRIP_ROTATE = Constants.GRIP_ROTATE_DROP;
        CAM_TASK = 0.0;
    }

    @Override
    public void initialize() {
        
        Main.sensorsMap.put("camTask", CAM_TASK);
        Main.camMap.put("currentColorIndex", 0.0);
        
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

        if(treeMode) {
            if (branchNumber == 1 && !treeEnd) {
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_BRANCH);
                Main.motorControllerMap.put("targetRotateDegree", 26.0);
                SmartDashboard.putNumber("branchNumberCheck", 1);
                treeEnd = Main.switchMap.get("rotateStop") && StateMachine.iterationTime > 2; 
            }

            if (branchNumber == 2 && !treeEnd) {
                SmartDashboard.putNumber("branchNumberCheck", 2);
                Main.motorControllerMap.put("targetLiftPos", 39.0);
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_BRANCH);
                Main.motorControllerMap.put("targetRotateDegree", -24.0);
                treeEnd = Main.switchMap.get("rotateStop") && Main.switchMap.get("liftStop") && StateMachine.iterationTime > 2; 
            }

            if (branchNumber == 3 && !treeEnd) {
                SmartDashboard.putNumber("branchNumberCheck", 3);
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_BRANCH);
                Main.motorControllerMap.put("targetLiftPos", 50.0);
                Main.motorControllerMap.put("targetRotateDegree", 0.0);
                treeEnd = Main.switchMap.get("rotateStop") && Main.switchMap.get("liftStop") && StateMachine.iterationTime > 3; 
            }

            if (treeEnd) {
                Main.sensorsMap.put("camTask", 10.0); 
                if(Main.switchMap.get("targetColorFound")) {
                    Main.sensorsMap.put("camTask", 1.0);
                    if(Main.camMap.get("currentCenterX") != 0.0) {
                        flag = true;
                        newStates.add(new AutoRotate(true, branchNumber));
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;
                    }
                } else if(!Main.switchMap.get("targetColorFound") && branchNumber < 4 && StateMachine.iterationTime > 6) {
                    newStates.add(new AutoStart(true, branchNumber + 1));
                    StateMachine.states.addAll(StateMachine.index + 1, newStates);
                    stateEnd = true;
                }
            }

            if (!flag && StateMachine.iterationTime > 10) {
                newStates.add(new AutoEnd()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
        } else {
            Main.sensorsMap.put("camTask", 10.0);
            if(Main.switchMap.get("targetColorFound")) {
                Main.sensorsMap.put("camTask", 1.0);
                flag = true;
                if(Main.camMap.get("targetFound") == 1.0) {
                    
                    if(treeMode) {
                        newStates.add(new AutoRotate());
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;  
                    } else {
                        Main.sensorsMap.put("camTask", 2.0);
                        newStates.add(new AutoRotate());
                        StateMachine.states.addAll(StateMachine.index + 1, newStates);
                        stateEnd = true;
                    }
                }
            }
            
            if(StateMachine.iterationTime > 15) {
                newStates.add(new AutoEnd());
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }

            if (!flag && StateMachine.iterationTime > 5) {
                newStates.add(new AutoEnd()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                stateEnd = true;
            }
        }
    }
    @Override
    public void finilize() {
    }

    @Override
    public boolean isFinished() {
        return stateEnd;
    }
}