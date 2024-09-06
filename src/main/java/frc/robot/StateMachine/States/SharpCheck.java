package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;

import frc.robot.StateMachine.CoreEngine.IState;

public class SharpCheck implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING); 
    }

    @Override
    public void execute() {
        if (Main.sensorsMap.get("sharpLeft") < 15 || Main.sensorsMap.get("sharpRight") < 15) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        }
        else {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING); 
        }
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING);
    }

    @Override
    public boolean isFinished() {
        return exit;
    }
}