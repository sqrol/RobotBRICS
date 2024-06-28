package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
// import com.studica.frc.Cobra;

import frc.robot.Constants;
import frc.robot.Main;
import frc.robot.Filters.*;
import frc.robot.Maths.Common.Functions;

public class SensorController implements Runnable{

    public static double sensorsUpdateTime;
    public double resetGyroValue = 0;
    private double lastGyro = 0;
    public double newGyroThread = 0;

    public boolean resetGyroThread = false;
    public boolean resetGyroThreadOnce = false;
    private boolean plus360once = false;
    private boolean minus360once = false;

    private static final AHRS GYRO = new AHRS(SPI.Port.kMXP);

    private static final AnalogInput COBRA = new AnalogInput(Constants.COBRA);

    private static final AnalogInput SHARP_RIGHT = new AnalogInput(Constants.SHARP_RIGHT);
    private static final AnalogInput SHARP_LEFT = new AnalogInput(Constants.SHARP_LEFT);

    private static final DigitalInput LIMIT_SWITCH = new DigitalInput(Constants.LIMIT_SWITCH);
    private static final DigitalInput START_BUTTON = new DigitalInput(Constants.START_BUTTON);
    private static final DigitalInput EMS_BUTTON = new DigitalInput(Constants.EMS_BUTTON);

    private static DigitalOutput GREEN_LED = new DigitalOutput(Constants.GREEN_LED);
    private static DigitalOutput RED_LED = new DigitalOutput(Constants.RED_LED);
    
    private static final Ultrasonic SONIC_RIGHT = new Ultrasonic(Constants.SONIC_PING_RIGHT, Constants.SONIC_ECHO_RIGHT);
    private static final Ultrasonic SONIC_LEFT = new Ultrasonic(Constants.SONIC_PING_LEFT, Constants.SONIC_ECHO_LEFT);

    // // Новый вызов датчика Cobra (Опять же если Софа начнет не считать меня пустым местом) 
    // // Данный датчик подключается в порт I2C контроллера VMX
    // private static final Cobra COBRA = new Cobra();

    private static final MedianFilter RIGHT_SHARP_FILTER = new MedianFilter(5);
    private static final MedianFilter LEFT_SHARP_FILTER = new MedianFilter(5);
 
    // private static final MeanFilter RIGHT_SONIC_FILTER = new MeanFilter(6);

    @Override
    public void run() {

        try {
            // GREEN_LED = new DigitalOutput(Constants.GREEN_LED);
            // RED_LED = new DigitalOutput(Constants.RED_LED);
            
        } catch (Exception e) {
            //TODO: handle exception
        }

        while (!Thread.interrupted()) {
            double startTime = Timer.getFPGATimestamp();
            try {

                setIndicationMode(Main.sensorsMap.get("indicationMode"));

                if (Main.sensorsMap.get("resetGyro") == 1.0) {
                    resetGyroValue = 0;
                    resetGyroThreadOnce = true;
                    Main.sensorsMap.put("resetGyro", 0.0);
                }

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

                Main.sensorsMap.put("cobraVoltage", getCobraVoltage());

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

                // // Обработка значений с нового датчика черной линии
                // Main.sensorsMap.put("cobraSignal0", (double)getCobraSignal0());
                // Main.sensorsMap.put("cobraSignal1", (double)getCobraSignal1());

                Thread.sleep(20);
            } catch (Exception e) {
                System.err.println("!!!An error occurred in SensorController: " + e.getMessage());
                e.printStackTrace();
            }
            sensorsUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
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

    private double getCobraVoltage() {
        return COBRA.getAverageVoltage();
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

    

    // public float getCobraSignal0()
    // {
    //     try 
    //     {
    //         return COBRA.getRawValue(0);
    //     } 
    //     catch (Exception e) 
    //     {
    //         return 0;
    //     }
    // }

    // public float getCobraSignal1()
    // {
    //     try 
    //     {
    //         return cobra.getRawValue(1);
    //     } 
    //     catch (Exception e) 
    //     {
    //         return 0;
    //     }
    // }
}