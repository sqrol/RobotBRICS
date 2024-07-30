package frc.robot.StateMachine.StatesAutoOMS;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;
import frc.robot.Maths.Common.Functions;
import frc.robot.StateMachine.CoreEngine.IState;
import frc.robot.StateMachine.CoreEngine.StateMachine;

public class AutoLift implements IState {

    private ArrayList<IState> newStates = new ArrayList<>();
    
    private boolean flag = true;
    private boolean stateEnd = false;

    private double fruitPosY = 0;
    private double lastLiftPos, currentLiftPos = 0;

    private double[][] arrForLift = { { 30, 250, 460 }, { 80, 40, 20 } };

    public AutoLift() {

    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if(flag) {
            fruitPosY = Main.camMap.get("currentCenterY");
            
            if(fruitPosY != 0 && StateMachine.iterationTime > 1) {
                lastLiftPos = Main.motorControllerMap.get("currentLiftPos");
                flag = false;
            }
        } else {
            currentLiftPos = Functions.TransitionFunction(fruitPosY, arrForLift);
            SmartDashboard.putNumber("currentLiftPos + lastLiftPos", currentLiftPos + lastLiftPos);
            Main.motorControllerMap.put("targetLiftPos", currentLiftPos + lastLiftPos);
        }

        if(StateMachine.iterationTime > 10) {
            newStates.add(new AutoEnd());
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }

        if(Main.switchMap.get("liftStop")) {
            newStates.add(new AutoGlide(true));
            StateMachine.states.addAll(StateMachine.index + 1, newStates);
            stateEnd = true;
        }
    }

    @Override
    public void finilize() {
        
    }

    @Override
    public boolean isFinished() {
        return stateEnd;
    }
    
}