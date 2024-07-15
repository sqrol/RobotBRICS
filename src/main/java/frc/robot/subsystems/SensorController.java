package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.studica.frc.Cobra;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.Filters.*;
import frc.robot.Maths.Common.Functions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorController implements Runnable{

    private static AHRS GYRO;

    // Новый вызов датчика Cobra (Опять же если Софа начнет не считать меня пустым местом) 
    // Данный датчик подключается в порт I2C контроллера VMX
    private static Cobra COBRA;

    private static AnalogInput SHARP_RIGHT;
    private static AnalogInput SHARP_LEFT;

    private static DigitalInput LIMIT_SWITCH;
    private static DigitalInput START_BUTTON;
    private static DigitalInput EMS_BUTTON;

    private static DigitalOutput GREEN_LED; 
    private static DigitalOutput RED_LED;

    private static Ultrasonic SONIC_RIGHT;
    private static Ultrasonic SONIC_LEFT;

    public static double sensorsUpdateTime;
    private double resetGyroValue = 0;
    private double lastGyro = 0;
    private double newGyroThread = 0;

    private static boolean resetGyroThread = false;
    private static boolean resetGyroThreadOnce = false;
    private static boolean plus360once = false;
    private static boolean minus360once = false;

    private static MedianFilter RIGHT_SHARP_FILTER;
    private static MedianFilter LEFT_SHARP_FILTER;

    public SensorController() {
        try {
            GYRO = new AHRS(SPI.Port.kMXP);

            COBRA = new Cobra();
        
            SHARP_RIGHT = new AnalogInput(Constants.SHARP_RIGHT);
            SHARP_LEFT = new AnalogInput(Constants.SHARP_LEFT);
        
            LIMIT_SWITCH = new DigitalInput(Constants.LIMIT_SWITCH);
            START_BUTTON = new DigitalInput(Constants.START_BUTTON);
            EMS_BUTTON = new DigitalInput(Constants.EMS_BUTTON);
        
            GREEN_LED = new DigitalOutput(Constants.GREEN_LED);
            RED_LED = new DigitalOutput(Constants.RED_LED);
            
            SONIC_RIGHT = new Ultrasonic(Constants.SONIC_PING_RIGHT, Constants.SONIC_ECHO_RIGHT);
            SONIC_LEFT = new Ultrasonic(Constants.SONIC_PING_LEFT, Constants.SONIC_ECHO_LEFT);

            RIGHT_SHARP_FILTER = new MedianFilter(5);
            LEFT_SHARP_FILTER = new MedianFilter(5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (!Thread.interrupted()) {
            double startTime = Timer.getFPGATimestamp();
            try {

                if (Main.sensorsMap.get("resetGyro") == 1.0) {
                    resetGyroValue = 0;
                    resetGyroThreadOnce = true;
                    Main.sensorsMap.put("resetGyro", 0.0);
                }

                setIndicationMode(Main.sensorsMap.get("indicationMode"));
                gyroFunc();

                Main.sensorsMap.put("sharpLeft", getLeftSharp());
                Main.sensorsMap.put("sharpRight", getRightSharp());

                Main.sensorsMap.put("sonicLeft", getLeftSonic());
                Main.sensorsMap.put("sonicRight", getRightSonic());

                Main.sensorsMap.put("srcGyro", newGyroThread);
                Main.sensorsMap.put("posZ", newGyroThread);

                Main.switchMap.put("startButton", getStartButton());
                Main.switchMap.put("EMSButton", getEMSButton());
                Main.switchMap.put("limitSwitch", getLimitSwitch());

                Main.sensorsMap.put("updateTimeSensors", sensorsUpdateTime);

                // Обработка значений с нового датчика черной линии
                Main.sensorsMap.put("cobraSignal0", getCobraSignal0());
                Main.sensorsMap.put("cobraSignal1", getCobraSignal1());
                Main.sensorsMap.put("cobraSignal2", getCobraSignal2());
                Main.sensorsMap.put("cobraSignal3", getCobraSignal3());

                Thread.sleep(20);
            } catch (Exception e) {
                System.err.println("!!!An error occurred in SensorController: " + e.getMessage());
                e.printStackTrace();
            }
            sensorsUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
    }

    private void gyroFunc() {
        double gyro = getLongYaw();
        double dGyro = gyro - lastGyro;
        double outGyro = 0;
        
        if (!resetGyroThread && !resetGyroThreadOnce) {
            outGyro = dGyro + newGyroThread;
        }

        if (resetGyroThread) {
            outGyro = resetGyroValue;
        }

        if (resetGyroThreadOnce) {
            outGyro = resetGyroValue;
            resetGyroThreadOnce = false;
        }

        if (plus360once) {
            outGyro += 360;
            plus360once = false;
        }

        if (minus360once) {
            outGyro -= 360;
            minus360once = false;
        }

        newGyroThread = outGyro;
        lastGyro = gyro;
    }
    
    private final double getLongYaw() {
        return GYRO.getAngle();
    }

    private double getLeftSonic() {
        SONIC_LEFT.ping();
        Timer.delay(0.005);
        return SONIC_LEFT.getRangeMM() / 10;
    }

    private double getRightSonic() {
        SONIC_RIGHT.ping();
        Timer.delay(0.005);
        // return RIGHT_SONIC_FILTER.Filter(RIGHT_SONIC_FILTER.Filter(SONIC_RIGHT.getRangeMM() / 10));
        return SONIC_RIGHT.getRangeMM() / 10;
    }

    private double getLeftSharp() {
        return (LEFT_SHARP_FILTER.Filter((Math.pow(SHARP_LEFT.getAverageVoltage(), -1.2045) * 27.726)));
    }

    private double getRightSharp() {
        return (RIGHT_SHARP_FILTER.Filter((Math.pow(SHARP_RIGHT.getAverageVoltage(), -1.2045) * 27.726)));
    }

    private boolean getLimitSwitch() {
        return LIMIT_SWITCH.get();
    }

    private boolean getStartButton() {
        return START_BUTTON.get();
    }

    private boolean getEMSButton() {
        return EMS_BUTTON.get();
    }

    private void setGreenLED(boolean state) {
        try {
            GREEN_LED.disablePWM();
            GREEN_LED.set(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRedLED(boolean state) {
        try {
            RED_LED.disablePWM();
            RED_LED.set(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Устанавливает индикацию в соответствии с выбранным режимом
     * @param mode 1.0 - WAITING, 2.0 - IN PROCESS, 3.0 - FINISHED, 4.0 - FOR CHECK
     */
    private void setIndicationMode(double mode) {
        if(mode == 1.0) {
            setRedLED(true);
            setGreenLED(false);
        }
        if(mode == 2.0) {
            setRedLED(true);
            setGreenLED(true);
        }
        if(mode == 3.0) {
            setRedLED(false);
            setGreenLED(false);
        }
        if(mode == 4.0) {
            setRedLED(false);
            setGreenLED(true);
        }
    }

    private double getCobraSignal0() {
        try {
            return COBRA.getRawValue(0);
        } 
        catch (Exception e) {
            return 0;
        }
    }

    private double getCobraSignal1() {
        try {
            return COBRA.getRawValue(1);
        } 
        catch (Exception e) {
            return 0;
        }
    }

    private double getCobraSignal2() {
        try {
            return COBRA.getRawValue(2);
        } 
        catch (Exception e) {
            return 0;
        }
    }

    private double getCobraSignal3() {
        try {
            return COBRA.getRawValue(3);
        } 
        catch (Exception e) {
            return 0;
        }
    }
}