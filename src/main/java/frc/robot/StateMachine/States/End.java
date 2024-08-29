package frc.robot.StateMachine.States;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class End implements IState {

    @Override
    public void initialize() {
       Main.sensorsMap.put("indicationMode", 3.0);
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
        Main.motorControllerMap.put("liftSpeed", 0.0);
        Main.motorControllerMap.put("rotateSpeed", 0.0);
    }

    @Override
    public void finilize() {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
