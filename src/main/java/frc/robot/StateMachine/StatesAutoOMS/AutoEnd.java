package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoEnd implements IState {

    public AutoEnd() {

    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // Возврат СМО в исходное положение с учетом вводных параметров
        Main.motorControllerMap.put("targetLiftPos", 0.0);
        Main.motorControllerMap.put("targetRotateDegree", 0.0);
        Main.switchMap.get("liftStop");
    }

    @Override
    public void finilize() {
        Main.motorControllerMap.put("servoGrab", 15.0);
    }

    @Override
    public boolean isFinished() {
        return Main.switchMap.get("liftStop") && Main.switchMap.get("rotateStop") && Main.switchMap.get("glideStop") && StateMachine.iterationTime > 2;
    }
}