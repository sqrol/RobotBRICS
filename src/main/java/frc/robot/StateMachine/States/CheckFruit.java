package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CheckFruit implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.sensorsMap.put("indicationMode", 1.0);
    }

    @Override
    public void execute() {
        if(Main.stringMap.get("detectedFruit").equals(Constants.BIG_RED_APPLE)) {
            Main.sensorsMap.put("indicationMode", 4.0);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.SMALL_RED_APPLE)) {
            Main.sensorsMap.put("indicationMode", 4.0);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.YELLOW_PEAR)) {
            Main.sensorsMap.put("indicationMode", 4.0);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else {
            Main.sensorsMap.put("indicationMode", 1.0);
        }
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("indicationMode", 1.0);
    }

    @Override
    public boolean isFinished() {
        return exit;
    }
    
}
