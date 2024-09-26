package frc.robot.StateMachine.States;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class CheckRotten implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.sensorsMap.put("indicationMode", Constants.INDICATION_FINISHED);
        Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
        Main.sensorsMap.put("camTask", -1.0);
    }

    @Override
    public void execute() {
        if(Main.stringMap.get("detectedFruit").equals(Constants.ROTTEN_PEAR) || Main.stringMap.get("detectedFruit").equals(Constants.SMALL_ROTTEN_APPLE) || Main.stringMap.get("detectedFruit").equals(Constants.BIG_ROTTEN_APPLE)) {
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_REACTION);
            SmartDashboard.putString("RottenDetection", "BIG RED ROTTEN APPLE");
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        } else if (Main.stringMap.get("detectedFruit").equals(Constants.BIG_RED_APPLE)){
            SmartDashboard.putString("RottenDetection", "BIG RED APPLE");
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_WAITING);
        } else {
            SmartDashboard.putString("RottenDetection", "none");
            Main.sensorsMap.put("indicationMode", Constants.INDICATION_FINISHED);
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