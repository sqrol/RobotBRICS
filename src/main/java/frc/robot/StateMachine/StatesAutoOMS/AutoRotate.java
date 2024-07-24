package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.Transition;
import frc.robot.Maths.Common.Functions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

public class AutoRotate implements IState {

    private Boolean flag;
    private ArrayList<IState> newStates = new ArrayList<>();

    private double lastRotateDegree = 0;
    private double currentTargetDegree = 0;
    private double fruitPosX = 0;
    private Boolean statesEnd = false;
    private boolean rotateStop = false; 
    private double targetAngle = 0;
    
    private static final double[][] arrForLift = { { 1, 106, 213} , { -45, 0, 45} }; // Тут в первом массиве мы закладываем параметры исходной картинки

    public AutoRotate() {

    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", 70.0);

        rotateStop = false; 
        flag = false;
        statesEnd = false;
    }

    @Override
    public void execute() {
        if (!flag) {
            fruitPosX = Main.camMap.get("currentCenterX"); // Мы в AutoStart уже смотрели потому берем только координаты
            if (fruitPosX != 0.0 && StateMachine.iterationTime > 1) {
                SmartDashboard.putNumber("currentCenterX123", fruitPosX);
                lastRotateDegree = Main.motorControllerMap.get("currentRotatePosition"); // Запоминаем текущую позицию поворота
                SmartDashboard.putNumber("lastRotateDegree", lastRotateDegree);
                flag = true;
            }
        } else {
            currentTargetDegree = Functions.TransitionFunction(fruitPosX, arrForLift);
            SmartDashboard.putNumber("currentTargetDegree", currentTargetDegree);
            SmartDashboard.putNumber("lastRotateDegree + currentTargetDegree", currentTargetDegree - lastRotateDegree);
            Main.motorControllerMap.put("targetRotateDegree", currentTargetDegree - lastRotateDegree); // Прибавляем к текущему нужный градус
            rotateStop = Main.switchMap.get("rotateStop");    
        }

        // Если мы долго выравниваемся по объекту то выходим
        if (StateMachine.iterationTime > 10) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }

        // Если мы выровнились по объекту то переходим к следующему этапу
        if (rotateStop) {
            newStates.add(new AutoGlide()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public boolean isFinished() {
        return statesEnd;
    }
}