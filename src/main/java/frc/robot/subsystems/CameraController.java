package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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
    private static CvSource outStream, outHSV, outRect, outGlide, thresh, upperBranch, middleBranch, lowerBranch;

    private String currentColor = "";

    private double startTime = -1; // Время начала ожидания
    private double colorIndex = 0; 
    private double maxColorIndex = 3; 
    // private double colorIndex = 0.0;

    @Override
    public void run() {

        SmartDashboard.putNumber("RED1", 0.0);
        SmartDashboard.putNumber("RED2", 0.0);

        SmartDashboard.putNumber("GREEN1", 0.0);
        SmartDashboard.putNumber("GREEN2", 0.0);

        SmartDashboard.putNumber("BLUE1", 0.0);
        SmartDashboard.putNumber("BLUE2", 0.0);


        // SmartDashboard.putNumber("Hue1", 0.0);
        // SmartDashboard.putNumber("Hue2", 0.0);

        // SmartDashboard.putNumber("Saturation1", 0.0);
        // SmartDashboard.putNumber("Saturation2", 0.0);

        // SmartDashboard.putNumber("Value1", 0.0);
        // SmartDashboard.putNumber("Value2", 0.0);

        camera = CameraServer.getInstance().startAutomaticCapture(); 
        camera.setResolution(640, 480); 
        // camera.setResolution(320, 240); 
        // camera.setResolution(220, 140); 
        camera.setFPS(30); 

        cvSink = CameraServer.getInstance().getVideo(camera); 

        outStream = CameraServer.getInstance().putVideo("OutImage", 640, 480); 
        outHSV = CameraServer.getInstance().putVideo("outHSV", 640, 480); 
        outRect = CameraServer.getInstance().putVideo("outRect", 640, 480); 
        outGlide = CameraServer.getInstance().putVideo("outGlide", 640, 480); 
        thresh = CameraServer.getInstance().putVideo("thresh", 640, 480);

        upperBranch = CameraServer.getInstance().putVideo("upperBranch", 640, 480);
        middleBranch = CameraServer.getInstance().putVideo("middleBranch", 640, 480);
        lowerBranch = CameraServer.getInstance().putVideo("lowerBranch", 640, 480);

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

                if (Main.sensorsMap.get("camTask") == 1.0) {
                    groundModeAutoGrab(source, Main.camMap.get("currentColorIndex"));
                }

                if (Main.sensorsMap.get("camTask") == 2.0) {
                    searchForGrab(source, 80);
                }

                if(Main.sensorsMap.get("camTask") == 3.0) {
                    checkRotten(source);
                }

                if(Main.sensorsMap.get("camTask") == 4.0) {
                    treeModeAutoGrab(source, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 5.0) {
                    searchForGrab(source, 70);
                }

                if(Main.sensorsMap.get("camTask") == 6.0) {
                    trackImageTrueArea(source, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 10.0) { // Перебор масок для фруктов
                    // thresholdSettings(source);
                    searchDominantColorMask(source);
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

    

    private void searchDominantColorMask(Mat source) {
        double scanningTime = 2;

        Mat resizedSource = Viscad.ReduceResolutionImage(source, 3);
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat imageOut = createMask(hsvImage, setPointsForColors(colorIndex));
        
        outHSV.putFrame(imageOut);
        int currentMaskArea = Viscad.ImageTrueArea(imageOut);
        releaseMats(resizedSource, blur, hsvImage, imageOut);

        if (currentMaskArea < 100 && Timer.getFPGATimestamp() - startTime > scanningTime) {
            colorIndex++;
            startTime = Timer.getFPGATimestamp(); 
            return;
        } else if (Timer.getFPGATimestamp() - startTime > scanningTime*1.5) {
            Main.camMap.put("currentColorIndex", colorIndex);
            Main.camMap.put("targetColorFound", 1.0);
            colorIndex=0;
        }

        if (colorIndex >= maxColorIndex) {
            Main.camMap.put("currentColorIndex", 0.0);
            Main.camMap.put("targetColorFound", 1.0);
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
    private static ColorRange setPointsForColors(double colorIndex) {
        ColorRange outRange = new ColorRange();

        // красный 
        if (colorIndex == 1.0) { outRange = new ColorRange(new Point(0, 15), new Point(120, 255), new Point(150, 255)); }
        // желтый
        if (colorIndex == 2.0) { outRange = new ColorRange(new Point(20, 255), new Point(120, 255), new Point(150, 255)); }
        // if (colorIndex == 3.0) { outRange = new ColorRange(new Point(0, 180), new Point(150, 255), new Point(80, 255)); }

        return outRange;
    }


    private static Mat createMask(Mat hsvImage, ColorRange colorRange) {
        Mat mask = Viscad.Threshold(hsvImage, colorRange.getHue(), colorRange.getSaturation(), colorRange.getValue());
        Mat eroded = Viscad.Erode(mask, 1);
        Mat dilated = Viscad.Dilate(eroded, 1);
        releaseMats(mask, eroded);
        return dilated;
    }

    private void searchForGrab(Mat source, int size) {
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

        if(Viscad.ImageTrueArea(square) >= 700) {
            Main.stringMap.put("detectedFruit", Constants.BIG_RED_APPLE);
            Main.camMap.put("targetFound", 1.0);
        } else if(Viscad.ImageTrueArea(square) < 700 && Viscad.ImageTrueArea(square) >= 100) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_RED_APPLE);
            Main.camMap.put("targetFound", 1.0);
        } else {
            Main.stringMap.put("detectedFruit", "none");
        }
    
        SmartDashboard.putNumber("ImageAreaGlideSquare", Viscad.ImageTrueArea(square));

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

    private void groundModeAutoGrab(Mat source, Double colorIndex) {

        // double Hue1 = SmartDashboard.getNumber("Hue1", 0);
        // double Hue2 = SmartDashboard.getNumber("Hue2", 0);

        // double Saturation1 = SmartDashboard.getNumber("Saturation1", 0);
        // double Saturation2 = SmartDashboard.getNumber("Saturation2", 0);

        // double Value1 = SmartDashboard.getNumber("Value1", 0);
        // double Value2 = SmartDashboard.getNumber("Value2", 0);

        ColorRange currentColor = setPointsForColors(colorIndex);

        List<Rect> currentCordinate = new ArrayList<>();
        Point lowestObjectCordinate = new Point();
        Point centreSearch = new Point();

        Point HueBound = new Point(0, 255);
        Point SaturationBound = new Point(100, 255);
        Point ValueBound = new Point(100, 255);

        // Снижение разрешения изображения
        Mat resizedSource = Viscad.ReduceResolutionImage(source, 3);
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);

        Mat outPA = new Mat();
        currentCordinate = Viscad.ParticleAnalysis(mask, outPA);

        int maskArea = Viscad.ImageTrueArea(mask);

        centreSearch = Viscad.detectCenter(mask);

        outHSV.putFrame(mask);

        if (centreSearch.x == 0 && centreSearch.y == 0) {
            // Освобождение памяти и опять по новой
            releaseMats(resizedSource, blur, hsvImage, mask, outPA);
            return;
        }

        lowestObjectCordinate = findLowestObject(mask, currentCordinate);

        if (lowestObjectCordinate.x != 0 && lowestObjectCordinate.y != 0 && maskArea > 100) {
            Main.camMap.put("targetFound", 1.0);

            Main.camMap.put("currentCenterX", lowestObjectCordinate.x);
            // Main.camMap.put("currentCenterY", lowestObjectCordinate.y);
        } else {
            Main.camMap.put("targetFound", 0.0);

            Main.camMap.put("currentCenterX", 0.0);
            Main.camMap.put("currentCenterY", 0.0);
        }

        // Освобождение памяти
        releaseMats(resizedSource, blur, hsvImage, mask, outPA);
    }

    private static void trackImageTrueArea(Mat orig, double colorIndex) {
        boolean keepTrack = false;

        double stop = 0;

        ColorRange currentColor = setPointsForColors(colorIndex);

        Mat resizedImage = Viscad.ReduceResolutionImage(orig, 3);

        Mat blur = Viscad.Blur(resizedImage, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);

        Mat square = cropSquareFromCenter(mask, 57);

        if(Main.stringMap.get("detectedFruit").equals(Constants.BIG_RED_APPLE)) {
            stop = 120;
        } 

        if(Main.stringMap.get("detectedFruit").equals(Constants.SMALL_RED_APPLE)) {
            stop = 100;
        } 

        if(Viscad.ImageTrueArea(square) > stop) {
            keepTrack = true;
            
        } else {
            keepTrack = false;
        }
        SmartDashboard.putNumber("trackedImageArea", Viscad.ImageTrueArea(square));
        outRect.putFrame(square);

        releaseMats(resizedImage, blur, hsvImage, mask, square);

        Main.switchMap.put("trackImageArea", keepTrack);
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
    
            return center;
        }
        return null;
    }

    private static Point findHighestObject(Mat orig, List<Rect> currentCoordinate) {
        Rect highestObject = null;
        int maxY = Integer.MAX_VALUE;
    
        for (Rect rect : currentCoordinate) {
            int x = rect.x;
            int y = rect.y;
            int width = rect.width;
            int height = rect.height;
            int topY = y;
    
            Imgproc.rectangle(orig, new Point(x, y), new Point(x + width, y + height), new Scalar(0, 255, 0), 2);
            
            thresh.putFrame(orig);
            if (topY < maxY) {
                maxY = topY;
                highestObject = rect;
            }
        }
    
        if (highestObject != null) {
            int highestX = highestObject.x + highestObject.width / 2;
            int highestY = highestObject.y + highestObject.height / 2;

            Point center = new Point(highestX, highestY);
    
            Imgproc.circle(orig, center, 5, new Scalar(255, 0, 0), -1);

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

    private void treeModeAutoGrab(Mat orig, double colorIndex) {

        ColorRange currentColor = setPointsForColors(colorIndex);
        
        List<Rect> currentCoordinate = new ArrayList<>();

        Point highestFruit = new Point();
        Point centreSearch = new Point();

        Mat resizedOrig = Viscad.ReduceResolutionImage(orig, 3);

        Mat blur = Viscad.Blur(resizedOrig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);
        
        Mat outPA = new Mat();
        currentCoordinate = Viscad.ParticleAnalysis(mask, outPA);
        
        centreSearch = Viscad.detectCenter(mask);

        outHSV.putFrame(mask);
        if(centreSearch != null) {
            if(centreSearch.x == 0 && centreSearch.y == 0) {
                releaseMats(blur, hsvImage, mask, outPA, resizedOrig);
                return;
            }
        }
        
        highestFruit = findHighestObject(mask, currentCoordinate);

        if(highestFruit.x != 0 && highestFruit.y != 0 && Viscad.ImageTrueArea(mask) > 100) {
            Main.camMap.put("targetFound", 1.0);
            Main.camMap.put("currentCenterX", highestFruit.x);
        } else {
            Main.camMap.put("targetFound", 0.0);
            Main.camMap.put("currentCenterX", 0.0);
            Main.camMap.put("currentCenterY", 0.0);
        }
        releaseMats(blur, hsvImage, mask, outPA, resizedOrig);
    }

    private static void thresholdSettings(Mat orig) {
        
        double red1 = SmartDashboard.getNumber("RED1", 0.0);
        double red2 = SmartDashboard.getNumber("RED2", 0.0);

        double green1 = SmartDashboard.getNumber("GREEN1", 0.0);
        double green2 = SmartDashboard.getNumber("GREEN2", 0.0);

        double blue1 = SmartDashboard.getNumber("BLUE1", 0.0);
        double blue2 = SmartDashboard.getNumber("BLUE2", 0.0);

        Point redPoint = new Point(red1, red2);
        Point greenPoint = new Point(green1, green2);
        Point bluePoint = new Point(blue1, blue2);

        Mat blur = Viscad.Blur(orig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat threshold = Viscad.Threshold(hsvImage, redPoint, greenPoint, bluePoint);
        Mat dilate = Viscad.Dilate(threshold, 4);
        thresh.putFrame(dilate);
        SmartDashboard.putNumber("imgAreaRotten", Viscad.ImageTrueArea(dilate));
        releaseMats(threshold, blur, hsvImage, dilate);
    }

    private static void checkFruit(Mat orig) {
        
    }

    private static void checkRotten(Mat orig) {
        double red1 = SmartDashboard.getNumber("RED1", 0.0);
        double red2 = SmartDashboard.getNumber("RED2", 0.0);

        double green1 = SmartDashboard.getNumber("GREEN1", 0.0);
        double green2 = SmartDashboard.getNumber("GREEN2", 0.0);

        double blue1 = SmartDashboard.getNumber("BLUE1", 0.0);
        double blue2 = SmartDashboard.getNumber("BLUE2", 0.0);

        Point redPoint = new Point(85, 255);
        Point greenPoint = new Point(0, 255);
        Point bluePoint = new Point(0, 255);

        Mat blur = Viscad.Blur(orig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat threshold = Viscad.Threshold(hsvImage, redPoint, greenPoint, bluePoint);
        Mat dilate = Viscad.Dilate(threshold, 4);

        SmartDashboard.putNumber("imgAreaRotten", Viscad.ImageTrueArea(dilate));
        thresh.putFrame(dilate);

        if(Viscad.ImageTrueArea(dilate) > 3000) {
            Main.stringMap.put("detectedFruit", "RottenPear"); // пока не получается определять конкретный гнилой фрукт
        } else {                                                   // для базового модуля пока так
            Main.stringMap.put("detectedFruit", "none");
        }

        releaseMats(threshold, blur, hsvImage, dilate);
    }

    private static void releaseMats(Mat... mats) {
        for (Mat mat : mats) {
            mat.release();
        }
    }
}