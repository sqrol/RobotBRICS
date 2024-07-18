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

    private static UsbCamera camera;
    private CvSink cvSink;
    private static CvSource outStream, outHSV, outRect;

    private String currentColor = "";

    @Override
    public void run() {

        // SmartDashboard.putNumber("Hue1", 0.0);
        // SmartDashboard.putNumber("Hue2", 0.0);

        // SmartDashboard.putNumber("Saturation1", 0.0);
        // SmartDashboard.putNumber("Saturation2", 0.0);

        // SmartDashboard.putNumber("Value1", 0.0);
        // SmartDashboard.putNumber("Value2", 0.0);

        camera = CameraServer.getInstance().startAutomaticCapture(); 
        camera.setResolution(640, 480); 
        camera.setFPS(30); 

        cvSink = CameraServer.getInstance().getVideo(camera); 

        outStream = CameraServer.getInstance().putVideo("OutImage", 640, 480); 
        outHSV = CameraServer.getInstance().putVideo("outHSV", 640, 480); 
        outRect = CameraServer.getInstance().putVideo("outRect", 640, 480); 

        while (true) {
            double startTime = Timer.getFPGATimestamp();
            try {
                Mat source = new Mat();

                if (cvSink.grabFrame(source) == 0) {
                    continue;
                }

                if (Main.sensorsMap.get("camTask") == 0) {
                    Main.camMap.put("targetFound", 0.0);
                    Main.camMap.put("currentCenterX", 0.0);
                    Main.camMap.put("currentCenterY", 0.0);
                }

                if (Main.sensorsMap.get("camTask") == 1) {
                    testForAutoGrab(source);
                }

                if (Main.sensorsMap.get("camTask") == 2) {
                    // testForAutoGrab(source);
                }

                Main.sensorsMap.put("updateTimeCamera", cameraUpdateTime);
                outStream.putFrame(source);
                source.release();
            } catch (Exception e) {
                System.err.println("!!!An error occurred in CameraController: " + e.getMessage());
                e.printStackTrace();
            }
            cameraUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
    }

    private static void settingCameraParameters() {
        camera.setWhiteBalanceAuto(); // Устанавливается баланс белого
        camera.setExposureManual(100);; // Устанавливается яркость камеры
        camera.setBrightness(1);
    }

    private void testForAutoGrab(Mat source) {

        // double Hue1 = SmartDashboard.getNumber("Hue1", 0);
        // double Hue2 = SmartDashboard.getNumber("Hue2", 0);

        // double Saturation1 = SmartDashboard.getNumber("Saturation1", 0);
        // double Saturation2 = SmartDashboard.getNumber("Saturation2", 0);

        // double Value1 = SmartDashboard.getNumber("Value1", 0);
        // double Value2 = SmartDashboard.getNumber("Value2", 0);

        List<Rect> currentCordinate = new ArrayList<>();
        Point lowestObjectCordinate = new Point();

        Point HueBound = new Point(0, 255);
        Point SaturationBound = new Point(0, 255);
        Point ValueBound = new Point(100, 255);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = Viscad.Threshold(hsvImage, HueBound, SaturationBound, ValueBound);

        Mat outPA = new Mat();
        currentCordinate = Viscad.ParticleAnalysis(mask, outPA);

        // Mat square = cropSquareFromCenter(mask, 80);
        // outRect.putFrame(square);

        if (currentCordinate.isEmpty()) {
            return;
        }

        lowestObjectCordinate = findLowestObject(mask, currentCordinate);

        if (lowestObjectCordinate.x != 0 && lowestObjectCordinate.y != 0) {
            Main.camMap.put("targetFound", 1.0);

            Main.camMap.put("currentCenterX", lowestObjectCordinate.x);
            Main.camMap.put("currentCenterY", lowestObjectCordinate.y);
        }

        outHSV.putFrame(mask);
    
        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask, outPA);
    }

    private static Point findLowestObject(Mat inImage, List<Rect> currentCoordinate) {
        Rect lowestObject = null;
        int maxY = Integer.MIN_VALUE;
    
        for (Rect rect : currentCoordinate) {
            int x = rect.x;
            int y = rect.y;
            int width = rect.width;
            int height = rect.height;
            int bottomY = y + height;
    
            Imgproc.rectangle(inImage, new Point(x, y), new Point(x + width, y + height), new Scalar(0, 255, 0), 2);
    
            if (bottomY > maxY) {
                maxY = bottomY;
                lowestObject = rect;
            }
        }
    
        if (lowestObject != null) {
            int lowestX = lowestObject.x + lowestObject.width / 2;
            int lowestY = lowestObject.y + lowestObject.height / 2;
            Point center = new Point(lowestX, lowestY);
    
            Imgproc.circle(inImage, center, 5, new Scalar(255, 0, 0), -1);
            SmartDashboard.putNumber("Lowest Object Center X", lowestX);
            SmartDashboard.putNumber("Lowest Object Center Y", lowestY);
    
            return center;
        }
    
        return null;
    }

    public static Mat cropSquareFromCenter(Mat source, int sideLength) { // Используется при выдвижении Glide к объекту
        // Проверяем, что сторона квадрата не превышает размеры исходного изображения
        int width = source.cols();
        int height = source.rows();
        
        if (sideLength > width || sideLength > height) {
            throw new IllegalArgumentException("Side length is larger than the dimensions of the source image.");
        }

        int centerX = width / 2;
        int centerY = height / 2;

        int xStart = centerX - (sideLength / 2);
        int yStart = centerY - (sideLength / 2);

        Rect rectCrop = new Rect(xStart, yStart, sideLength, sideLength);

        Mat croppedImage = new Mat(source, rectCrop);
        
        return croppedImage;
    }
    
    private static void releaseMats(Mat... mats) {
        for (Mat mat : mats) {
            mat.release();
        }
    }
}