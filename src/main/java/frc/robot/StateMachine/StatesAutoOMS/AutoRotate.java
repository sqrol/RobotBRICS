package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.*;
import frc.robot.StateMachine.States.Transition;
import frc.robot.Maths.Common.Functions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

public class AutoRotate implements IState {

    private boolean flag;

    private boolean stateEnd, rotateStop = false;

    private boolean treeMode = false;

    private ArrayList<IState> newStates = new ArrayList<>();

    private double GRIP_ROTATE = 0.0;

    private double LIFT_POS = 60.0;

    private double lastRotateDegree, currentTargetDegree = 0;
    private double treeModeMultiplier = 0;
    private double fruitPosX = 0;
    private double targetAngle = 0;
    
    private int branchNumber = 0;

    // private static final double[][] arrForRotate = { { 1, 106, 213} , { -45, 0, 45} }; // Тут в первом массиве мы закладываем параметры исходной картинки
    // private static final double[][] arrForRotate = { { 1, 50, 106, 260, 340} , { -60, -55, -45, 0, 45} }; // Тут в первом массиве мы закладываем параметры исходной картинки
    private static final double[][] arrForRotate = { { 1, 50, 106, 213, 300} , { -45, -25, 0, 45, 60} };
    public AutoRotate() {
        GRIP_ROTATE = 131.0; // 70
    }

    public AutoRotate(boolean treeMode, int branchNumber) {
        this.treeMode = treeMode;
        this.branchNumber = branchNumber;
        if(branchNumber == 1) {
            GRIP_ROTATE = 85.0;
        }
        if(branchNumber == 2) {
            GRIP_ROTATE = 90.0;
        }
        if(branchNumber == 3) {
            GRIP_ROTATE = 86.0;
        }
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);
        Main.switchMap.put("rotateStop", false); 
        flag = false;
        stateEnd = false;
    }

    @Override
    public void execute() {
        
        if (!flag) {
            fruitPosX = Main.camMap.get("currentCenterX"); 
            if (fruitPosX != 0.0 && StateMachine.iterationTime > 1) {
                lastRotateDegree = Main.motorControllerMap.get("currentRotateDegree");
                SmartDashboard.putNumber("lastRotateDegree", lastRotateDegree);
                flag = true;
            }
        } else {
            currentTargetDegree = Functions.TransitionFunction(fruitPosX, arrForRotate);
            SmartDashboard.putNumber("currentTargetDegree", currentTargetDegree);
            SmartDashboard.putNumber("lastRotateDegree + currentTargetDegree", currentTargetDegree + lastRotateDegree);
            Main.motorControllerMap.put("targetRotateDegree", currentTargetDegree + lastRotateDegree); // Прибавляем к текущему нужный градус
            rotateStop = Main.switchMap.get("rotateStop");    
        }

        if (StateMachine.iterationTime > 10) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if (rotateStop && treeMode) {
            newStates.add(new AutoGlide(true, branchNumber)); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        } else if(rotateStop && !treeMode) {
            Main.motorControllerMap.put("targetLiftPos", LIFT_POS);
            if(StateMachine.iterationTime > 3) {
                newStates.add(new AutoGlide()); 
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