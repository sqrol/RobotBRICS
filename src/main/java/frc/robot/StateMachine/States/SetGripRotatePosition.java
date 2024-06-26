package frc.robot.StateMachine.States;

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

    }

    @Override
    public void execute() {
        switch(position) {
            case "FLOOR":
                Main.motorControllerMap.put("servoGripRotate", 75.0);
                break;
            case "BRANCH":
                Main.motorControllerMap.put("servoGripRotate", 20.0);
                break;
            case "ANGLE":
                Main.motorControllerMap.put("servoGripRotate", 70.0);
                break;
            case "FOR DROP":
                Main.motorControllerMap.put("servoGripRotate", 13.0);
                break;
            case "SMALL ANGLE":
                Main.motorControllerMap.put("servoGripRotate", 52.0);
                break;
        }
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 1;
    }
}
