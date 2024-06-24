package frc.robot.StateMachine.States;

import frc.robot.Main;

import frc.robot.StateMachine.CoreEngine.IState;

public class MoveWhenFound implements IState {

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute() {
        if (Main.sensorsMap.get("objectFind") == 1.0)
        {
            Main.motorControllerMap.put("speedX", 30.0);
        }
        else 
        {
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