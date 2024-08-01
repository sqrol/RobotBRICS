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
import frc.robot.Main;

public class AutoStart implements IState {

    private boolean flag, oneTimeFlag;
    private boolean treeMode = false;

    private int branchNumber = 0;
    
    private double GRIP_ROTATE = 0.0;
    private double currentColorIndex = 0; 
    private static double startTime = 0;

    private ArrayList<IState> newStates = new ArrayList<>();
    
    public AutoStart() {
        this.treeMode = false;
        GRIP_ROTATE = 70.0;
    }

    public AutoStart(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        oneTimeFlag = true;
        GRIP_ROTATE = 30.0; // 30
    }

    @Override
    public void initialize() {
        Main.camMap.put("currentColorIndex", 0.0);
        
        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        flag = false;
    }

    @Override
    public void execute() {
        
        // Перебераем цвета 
        double currentTime = Timer.getFPGATimestamp();

        if (currentTime - startTime >= 1) {
            startTime = currentTime;
            if (Main.camMap.get("targetFound") == 0) {
                currentColorIndex++; 
            }   
        }

        SmartDashboard.putNumber("currentColorIndex", currentColorIndex);
        Main.camMap.put("currentColorIndex", currentColorIndex);
        // я не знаю почему, но это не работает. оно просто спавнит 150к стейтов в основной массив
        if(treeMode) {
            Main.sensorsMap.put("camTask", 5.0);
            if(oneTimeFlag && branchNumber < 4) {
                addStatesDependingOnBranchNumber(branchNumber);
                oneTimeFlag = false;
            } else flag = true;

            if(Main.camMap.get("targetFound") == 1.0) {
                newStates.add(new AutoGlide(true));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                flag = true;
            } else {
                newStates.add(new AutoStart(true, branchNumber + 1));
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                flag = true;
            }
            SmartDashboard.putNumber("branchNumber", branchNumber);
            SmartDashboard.putNumber("newStates.size()", newStates.size()); // здесь всё хорошо, в массиве только нужные элементы
        } else {
            Main.sensorsMap.put("camTask", 1.0);
            if (Main.camMap.get("targetFound") != 0 && StateMachine.iterationTime > 1) {
                newStates.add(new AutoRotate()); 
                StateMachine.states.addAll(StateMachine.index + 1, newStates);
                flag = true;
            }
        }
    }
    
    @Override
    public void finilize() {
        if (!flag) 
            Main.sensorsMap.put("camTask", 0.0);

        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 10 || flag;
        // return false;
    }

    private void addStatesDependingOnBranchNumber(int branchNumber) {
        if(branchNumber == 1) {
            newStates.add(new SetRotatePosition(30));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            flag = true;
        }

        if(branchNumber == 2) {
            newStates.add(new SetLiftPosition(41));  
            newStates.add(new SetRotatePosition(-30));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            flag = true;
        }

        if(branchNumber == 3) {
            newStates.add(new SetRotatePosition(0));
            newStates.add(new SetLiftPosition(70));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            flag = true;
        } 
        else if(branchNumber > 3) {
            newStates.add(new AutoEnd());
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            newStates.clear();
            flag = true;
        }
    }

}