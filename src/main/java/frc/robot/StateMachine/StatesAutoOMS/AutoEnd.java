package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

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
        Main.switchMap.get("liftStop");
    }

    @Override
    public void finilize() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}