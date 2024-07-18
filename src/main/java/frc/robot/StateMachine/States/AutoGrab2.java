package frc.robot.StateMachine.States;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.Main;

public class AutoGrab2 implements IState {

    public AutoGrab2() {

    }

    @Override
    public void initialize() {
        // Начинаем работу
        Main.sensorsMap.put("camTask", 1.0);

        Main.motorControllerMap.put("servoGrab", 15.0);
        Main.motorControllerMap.put("servoGripRotate", 70.0);
    }

    @Override
    public void execute() {
        // Main.motorControllerMap.put("targetLiftPos", 50.0);
        // Main.sensorsMap.put("targetGlidePos", 15.0);
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return Main.camMap.get("targetFound") == 0 && StateMachine.iterationTime > 10;
    }

}