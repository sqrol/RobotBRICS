package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
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

    private double lastRotateDegree, currentTargetDegree = 0;
    private double treeModeMultiplier = 0;
    private double fruitPosX = 0;
    private double targetAngle = 0;
    
    private static final double[][] arrForRotate = { { 1, 106, 213, 300} , { -45, 0, 45, 60} }; // Тут в первом массиве мы закладываем параметры исходной картинки

    public AutoRotate() {
        GRIP_ROTATE = 70.0;
    }

    public AutoRotate(boolean treeMode) {
        this.treeMode = treeMode;
        GRIP_ROTATE = 70.0; // 30.0
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", GRIP_ROTATE);

        rotateStop = false; 
        flag = false;
        stateEnd = false;
    }

    @Override
    public void execute() {
        
        if (!flag) {
            fruitPosX = Main.camMap.get("currentCenterX"); // Мы в AutoStart уже смотрели потому берем только координаты
            if (fruitPosX != 0.0 && StateMachine.iterationTime > 1) {
                lastRotateDegree = Main.motorControllerMap.get("currentRotatePosition"); // Запоминаем текущую позицию поворота
                flag = true;
            }
        } else {
            currentTargetDegree = Functions.TransitionFunction(fruitPosX, arrForRotate);
            SmartDashboard.putNumber("currentTargetDegree", currentTargetDegree);
            SmartDashboard.putNumber("lastRotateDegree + currentTargetDegree", currentTargetDegree - lastRotateDegree);
            Main.motorControllerMap.put("targetRotateDegree", currentTargetDegree - lastRotateDegree); // Прибавляем к текущему нужный градус
            rotateStop = Main.switchMap.get("rotateStop");    
        }

        if (StateMachine.iterationTime > 10) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if(rotateStop && treeMode) {
            newStates.add(new AutoLift());
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if (rotateStop) {
            newStates.add(new AutoGlide()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
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