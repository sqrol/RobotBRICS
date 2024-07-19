package frc.robot.StateMachine.States;

import java.util.ArrayList;

import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.*;
import frc.robot.StateMachine.StatesOMS.*;

// Данный класс не завершен надеюсь связка классов для параллельного управления ОМС не сломал проект

public class OMSMode implements IState {

    private int currentCommandIndex;
    private ArrayList<IState> newOMSStates = new ArrayList<>();
    private boolean endAddState = false;

    public OMSMode(int currentCommandIndex) {
        this.currentCommandIndex = currentCommandIndex;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);

        switch (currentCommandIndex) 
        {
            case 1:
                newOMSStates.add(new SetRotatePosition(45));
                StateMachineOMS.statesOMS.addAll(StateMachineOMS.index + 1, newOMSStates);
                endAddState = true;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
    }

    @Override
    public void finilize() {
        
    }

    @Override
    public boolean isFinished() {
        return StateMachineOMS.endOMSWork && StateMachine.iterationTime > 0.5 && endAddState;
    }
}
