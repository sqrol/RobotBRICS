package frc.robot.StateMachine.StatesOMS;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class SetGripRotatePosition implements IState{

    private String position = "";

    public SetGripRotatePosition(String position) {
        this.position = position;
    }

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        switch(position) {
            case "FLOOR":
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_FLOOR);
                break;
            case "BRANCH":
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_BRANCH);
                break;
            case "CHECK ZONE":
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
                break;
            case "FOR DROP":
                Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_DROP);
                break;
        }
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 0.5;
    }
}
