package frc.robot.StateMachine.States;

import frc.robot.Maths.Sensors.SensorsSimple;
import frc.robot.StateMachine.CoreEngine.IState;

public class SensorDrive implements IState {

    private double sideWall;
    private double backWall;  
    private boolean startAcceleration;  
    private boolean endAcceleration;  

    public SensorDrive(double sideWall, double backWall, boolean startAcceleration, boolean endAcceleration){
        this.sideWall = sideWall; 
        this.backWall = backWall; 
        this.startAcceleration = startAcceleration; 
        this.endAcceleration = endAcceleration; 
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        SensorsSimple.correct(sideWall, backWall, startAcceleration, endAcceleration); 
        
    }

    @Override
    public void finilize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        // return SensorsSimple.isFinished();
        return false;
    }
    
}
