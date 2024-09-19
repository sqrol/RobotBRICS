package frc.robot.StateMachine.States;
//Import the joystick class
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.GamepadConstants;
import frc.robot.Main;
import frc.robot.StateMachine.CoreEngine.IState;

public class OI implements IState{

    @Override
    public void initialize() {
        Main.sensorsMap.put("camTask", 1.0);
        Main.camMap.put("currentColorIndex", 3.0);
    }

    @Override
    public void execute() {
        Main.motorControllerMap.put("servoGripRotate", Constants.GRIP_ROTATE_CHECK_ZONE);
        Main.motorControllerMap.put("rotateSpeed", getRightDriveX() * 100);
        // Main.motorControllerMap.put("speedX", -getRightDriveX() * 100);
        
    }

    @Override
    public void finilize() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * @return the y-axis value from the drivePad right joystick
     */
    public double getRightDriveY()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.RIGHT_ANALOG_Y);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else  
            return joy;
    }

    /**
     * @return the x-axis value from the drivePad right Joystick
     */
    public double getRightDriveX()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.RIGHT_ANALOG_X);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    /**
     * @return the y-axis value from the drivePad left joystick
     */
    public double getLeftDriveY()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.LEFT_ANALOG_Y);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else  
        SmartDashboard.putNumber("triggetCheck", 12);
            return joy;
    }

    /**
     * @return the x-axis value from the drivePad left Joystick
     */
    public double getLeftDriveX()
    {
        double joy = Main.joystick.getRawAxis(GamepadConstants.LEFT_ANALOG_X);
        if(Math.abs(joy) < 0.05)
            return 0.0;
        else
            return joy;
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveRightTrigger()
    {
        return Main.joystick.getRawButton(GamepadConstants.RIGHT_TRIGGER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveRightBumper()
    {
        return Main.joystick.getRawButton(GamepadConstants.RIGHT_BUMPER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveLeftTrigger()
    {
        return Main.joystick.getRawButton(GamepadConstants.LEFT_TRIGGER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveLeftBumper()
    {
        return Main.joystick.getRawButton(GamepadConstants.LEFT_BUMPER);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveDPadX()
    {
        return Main.joystick.getRawButton(GamepadConstants.DPAD_X);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveDPadY()
    {
        return Main.joystick.getRawButton(GamepadConstants.DPAD_Y);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveXButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.SHARE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveYButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.TRIANGLE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveBButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.CIRCLE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveAButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.X_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveBackButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.SHARE_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveStartButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.OPTIONS_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveRightAnalogButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.RIGHT_ANALOG_BUTTON);
    }

    /**
     * @return a true or false depending on the input
     */
    public boolean getDriveLeftAnalogButton()
    {
        return Main.joystick.getRawButton(GamepadConstants.LEFT_ANALOG_BUTTON);
    }
}