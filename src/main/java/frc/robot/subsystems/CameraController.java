package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import frc.robot.MachineVision.ColorRange;
import frc.robot.MachineVision.Viscad;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.CvSource;

public class CameraController implements Runnable {

    public static double cameraUpdateTime;

    private static UsbCamera camera;
    private CvSink cvSink;
    private static CvSource outStream, outHSV, outRect, outGlide;

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
        outGlide = CameraServer.getInstance().putVideo("outGlide", 640, 480); 

        settingCameraParameters(); // На пробу)

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
                    glideSearchForGrab(source, 80); // Тут мы обозначаем параметры обрезаемого квадрата по середине картинки
                }

                Main.sensorsMap.put("updateTimeCamera", cameraUpdateTime);
                outStream.putFrame(source);
                source.release();
            } catch (Exception e) {
                System.err.println("!!!An error occurred in CameraController: " + e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(50); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); 
                }
            }
            cameraUpdateTime = Timer.getFPGATimestamp() - startTime;
        }
    }

    private static void settingCameraParameters() {
        // camera.setWhiteBalanceAuto(); // Устанавливается баланс белого
        // camera.setExposureManual(100);; // Устанавливается яркость камеры
        // camera.setBrightness(1);
        camera.getProperty("focus_auto").set(1);
        camera.getProperty("focus_auto").set(0);
    }

    // Бля я хуй знает как это правильно написать(
    private static ColorRange setPointsForColors(int colorIndex) {
        ColorRange outRange = new ColorRange();

        switch (colorIndex) 
        {
            case 1: // Фиолетовый (Гнилые фрукты)
                outRange = new ColorRange(new Point(0, 180), new Point(150, 255), new Point(80, 255));
                break;
            case 2: // Красный (Больше красное и маленькое яблоко)
                outRange = new ColorRange(new Point(0, 180), new Point(150, 255), new Point(80, 255));
                break;
            case 3: // Желтый (Желтая груша)
                outRange = new ColorRange(new Point(0, 180), new Point(150, 255), new Point(80, 255));
                break;
            default:
                break;
        }

        return outRange;
    }

    // Это нужно удалить
    private void testing(Mat source, int colorIndex) {

        ColorRange currentColor = setPointsForColors(colorIndex);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);

        outGlide.putFrame(mask);

        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask);
    }

    private void colorSearching(Mat source, int colorIndex) {

        ColorRange currentColor = setPointsForColors(colorIndex);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);
        
        outGlide.putFrame(mask);

        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask);
    }

    private static Mat createMask(Mat hsvImage, ColorRange colorRange) {
        Mat mask = new Mat();
        mask = Viscad.Threshold(hsvImage, colorRange.getHue(), colorRange.getSaturation(), colorRange.getValue());
        return mask;
    }

    private void glideSearchForGrab(Mat source, int size) {
        List<Rect> currentCordinate = new ArrayList<>();

        Point HueBound = new Point(0, 180);
        Point SaturationBound = new Point(150, 255);
        Point ValueBound = new Point(80, 255);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = Viscad.Threshold(hsvImage, HueBound, SaturationBound, ValueBound);

        Mat square = cropSquareFromCenter(mask, size);

        Mat outPA = new Mat();
        currentCordinate = Viscad.ParticleAnalysis(square, outPA);

        if (!currentCordinate.isEmpty()) {
            Rect firstRect = currentCordinate.get(0);
            Main.camMap.put("currentCenterY", (double) firstRect.y);
        } else {
            Main.camMap.put("currentCenterY", 0.0);
        }

        outRect.putFrame(square);

        outGlide.putFrame(mask);

        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask, outPA, square);
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
        Point centreSearch = new Point();

        Point HueBound = new Point(0, 255);
        Point SaturationBound = new Point(100, 255);
        Point ValueBound = new Point(100, 255);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = Viscad.Threshold(hsvImage, HueBound, SaturationBound, ValueBound);

        Mat eroded = Viscad.Erode(mask, 1);
        Mat dilated = Viscad.Dilate(eroded, 1);

        Mat outPA = new Mat();
        currentCordinate = Viscad.ParticleAnalysis(mask, outPA);

        centreSearch = Viscad.detectCenter(mask);

        outHSV.putFrame(dilated);

        SmartDashboard.putNumber("imageWidth/2", mask.cols() / 2);

        if (centreSearch.x == 0 && centreSearch.y == 0) {
            releaseMats(resizedSource, blur, hsvImage, mask, outPA, eroded, dilated);
            return;
        }

        SmartDashboard.putNumber("centreSearch.y", centreSearch.y);

        lowestObjectCordinate = findLowestObject(mask, currentCordinate);

        rotateToObject(lowestObjectCordinate  , mask.cols(), mask.rows());

        if (lowestObjectCordinate.x != 0 && lowestObjectCordinate.y != 0) {
            Main.camMap.put("targetFound", 1.0);

            Main.camMap.put("currentCenterX", lowestObjectCordinate.x);
            // Main.camMap.put("currentCenterY", lowestObjectCordinate.y);
        } else {
            Main.camMap.put("targetFound", 0.0);

            Main.camMap.put("currentCenterX", 0.0);
            Main.camMap.put("currentCenterY", 0.0);
        }

        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask, outPA, eroded, dilated);
    }

    private static void rotateToObject(Point target, int imageWidth, int imageHeight) {

    }

    // private static void rotateToObject(Point target, int imageWidth, int imageHeight) {
    //     if (target.y != 0) {
    //         Point center = new Point(imageWidth / 2, imageHeight / 2); 
    //         double dx = target.x - center.x;
        
    //         double angle = Math.atan2(0, dx) * 180 / Math.PI;
        
    //         SmartDashboard.putNumber("angle123", angle);
    //         Main.camMap.put("targetAngle", angle);
    //     } else {
    //         SmartDashboard.putNumber("angle123", 0);
    //         Main.camMap.put("targetAngle", 0.0);
    //     }
    // }

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