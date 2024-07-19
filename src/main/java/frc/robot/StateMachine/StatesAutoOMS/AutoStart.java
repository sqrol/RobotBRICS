package frc.robot.StateMachine.StatesAutoOMS;

import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;
import frc.robot.StateMachine.States.Transition;
import frc.robot.Maths.Common.Functions;

import java.util.ArrayList;

import frc.robot.Main;

public class AutoStart implements IState {

    private Boolean flag;
    private ArrayList<IState> newStates = new ArrayList<>();

    public AutoStart() {

    }

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 1.0);
        flag = false;
    }

    @Override
    public void execute() {
        if (Main.camMap.get("targetFound") != 0) {
            newStates.add(new AutoRotate()); 
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            flag = true;
        }
    }

    @Override
    public void finilize() {
        Main.sensorsMap.put("camTask", 0.0);
    }

    @Override
    public boolean isFinished() {
        return StateMachine.iterationTime > 10 || flag;
    }
}