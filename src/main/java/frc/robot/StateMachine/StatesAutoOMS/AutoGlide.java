package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Maths.Common.Functions;

public class AutoGlide implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    private double fruitPosY = 0;

    private Boolean statesEnd = false;
    private boolean glideStop = false;
    
    private static final double[][] speedForGlideServo = { { 0, 4, 10, 20, 40, 60, 80, 100 }, { 0, 0.8, 0.15, 0.2, 0.25, 0.3, 0.4, 0.4} };

    public AutoGlide() {

    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("glideMode", 1.0);
        Main.sensorsMap.put("camTask", 2.0);
    }

    @Override
    public void execute() {
        // Нужно по другому как будто это не правильно
        fruitPosY = Main.camMap.get("currentCenterY");

        double glideServoSpeed = Functions.TransitionFunction(210 - fruitPosY, speedForGlideServo); // 210 не корректно нужно брать актуальную с текущего изображения
        Main.motorControllerMap.put("setGlideSpeed", glideServoSpeed);
        glideStop = Functions.BooleanInRange(210 - fruitPosY, -10, 10);

        // Если переехали лимит выдвижного механизма
        if (Main.motorControllerMap.get("lastGlidePosition") >= 25 || StateMachine.iterationTime > 15) {
            newStates.add(new AutoEnd()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }

        // Если мы выровнились по объекту то переходим к следующему этапу
        if (glideStop) {
            newStates.add(new AutoGrab()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            statesEnd = true;
        }
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("glideMode", 0.0);
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public boolean isFinished() {
        return statesEnd;
    }
}