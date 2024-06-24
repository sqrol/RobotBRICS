package frc.robot.StateMachine.States;

import frc.robot.Main;

import frc.robot.StateMachine.CoreEngine.IState;

public class MoveWhenCloseSharp implements IState {

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute() {
        if (Main.sensorsMap.get("sharpLeft") < 10 || Main.sensorsMap.get("sharpRight") < 10) {
            Main.motorControllerMap.put("speedX", -30.0);
        }
        else {
            Main.motorControllerMap.put("speedX", 0.0);
        }
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}