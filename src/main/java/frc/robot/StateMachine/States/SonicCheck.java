package frc.robot.StateMachine.States;

import frc.robot.Main;

import frc.robot.StateMachine.CoreEngine.IState;

public class SonicCheck implements IState {

    private boolean exit = false;

    @Override
    public void initialize() {
        Main.motorControllerMap.put("speedX", 0.0);
        Main.motorControllerMap.put("speedZ", 0.0);
    }

    @Override
    public void execute() {
        if (Main.sensorsMap.get("sonicLeft") < 20 || Main.sensorsMap.get("sonicRight") < 20) {
            Main.sensorsMap.put("indicationMode", 4.0);
            if(Main.switchMap.get("startButton")) {
                exit = true;
            }
        }
        else {
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