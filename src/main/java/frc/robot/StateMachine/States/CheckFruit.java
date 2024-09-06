package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CheckFruit implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING);
        Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
        Main.sensorsMap.put("camTask", -1.0);
    }

    @Override
    public void execute() {
        if(Main.stringMap.get("detectedFruit").equals(Constants.BIG_RED_APPLE)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.SMALL_RED_APPLE)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.YELLOW_PEAR)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.SMALL_GREEN_APPLE)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.BIG_GREEN_APPLE)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if(Main.stringMap.get("detectedFruit").equals(Constants.GREEN_PEAR)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else {
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
