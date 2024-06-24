package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import frc.robot.MachineVision.Viscad;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.CvSource;

public class CameraController implements Runnable {

    public static double cameraUpdateTime;
    private CvSink cvSink;
    private static CvSource outStream;
    private static UsbCamera camera;
    private static CameraServer cameraServer;

    @Override
    public void run() {
        // SmartDashboard.putNumber("RED1", 0.0);
        // SmartDashboard.putNumber("GREEN1", 0.0);
        // SmartDashboard.putNumber("BLUE1", 0.0);
        // SmartDashboard.putNumber("RED2", 0.0);
        // SmartDashboard.putNumber("GREEN2", 0.0);
        // SmartDashboard.putNumber("BLUE2", 0.0);

        // cameraServer = CameraServer.getInstance(); // Этот метод автоматически обнаруживает камеру и начинает захватывать видеоизображение с нее
        // camera = cameraServer.startAutomaticCapture();
        // camera.setResolution(640, 480); // Устанавливаем разрешение изображения камеры на 640x480 пикселей
        // camera.setFPS(30); // Устанавливаем частоту кадров на 30 fps
        // settingCameraParameters();

        // cvSink = CameraServer.getInstance().getVideo(); // Этот объект позволяет получить видеопоток с камеры для дальнейшей обработки.
        // outStream = CameraServer.getInstance().putVideo("OutImage", 640, 480); // Этот объект будет использоваться для вывода обработанного видеопотока

        // while (true) {
        //     double startTime = Timer.getFPGATimestamp();
        //     try {
        //         Mat source = new Mat();

        //         if (cvSink.grabFrame(source) == 0) {
        //             continue;
        //         }
        //         if (Main.sensorsMap.get("camTask") == 0) {
        //             Main.sensorsMap.put("objectFind", CheckApple(source));
        //         }

        //         Main.sensorsMap.put("updateTimeCamera", cameraUpdateTime);
        //         // outStream.putFrame(source);
        //     } catch (Exception e) {
        //         System.err.println("!!!An error occurred in CameraController: " + e.getMessage());
        //         e.printStackTrace();
        //     }
        //     cameraUpdateTime = Timer.getFPGATimestamp() - startTime;
        // }
    }

    public static void settingCameraParameters() {
        camera.setWhiteBalanceAuto(); // Устанавливается баланс белого
        camera.setExposureManual(100);; // Устанавливается яркость камеры
        camera.setBrightness(1);
    }

    // public static double CheckFruit(Mat orig) {
        
    // }
}