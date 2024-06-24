package frc.robot.StateMachine.States;

import frc.robot.Main;

import frc.robot.StateMachine.CoreEngine.IState;

public class MoveWhenCloseSonic implements IState {

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute() {
        if (Main.sensorsMap.get("sonarLeft") < 100) {
            Main.motorControllerMap.put("speedX", 30.0);
        }
        else if (Main.sensorsMap.get("sonarRight") < 100)
        {
            Main.motorControllerMap.put("speedX", -30.0);
        }
        else {
            Main.motorControllerMap.put("speedX", 0.0);
        }
    }

    @Override
    public void finilize() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }
}