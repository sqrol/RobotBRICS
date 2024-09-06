package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CheckRotten implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 3.0);
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING);
        Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
    }

    @Override
    public void execute() {
        if(Main.stringMap.get("detectedFruit").equals(Constants.ROTTEN_PEAR) || Main.stringMap.get("detectedFruit").equals(Constants.SMALL_ROTTEN_APPLE) || Main.stringMap.get("detectedFruit").equals(Constants.BIG_ROTTEN_APPLE)) {
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