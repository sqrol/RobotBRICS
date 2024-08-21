package frc.robot.StateMachine.States;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CheckRotten implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 3.0);
        Main.sensorsMap.put("indicationMode", 1.0);
        Main.motorControllerMap.put("servoGripRotate", 70.0);
    }

    @Override
    public void execute() {
        if(Main.stringDutyMap.get("detectedFruit").equals(Constants.ROTTEN_PEAR) || Main.stringDutyMap.get("detectedFruit").equals(Constants.SMALL_ROTTEN_APPLE) || Main.stringDutyMap.get("detectedFruit").equals(Constants.BIG_ROTTEN_APPLE)) {
            Main.sensorsMap.put("indicationMode", 2.0);
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