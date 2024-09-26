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
import frc.robot.Maths.Common.Functions;
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
    private static double colorIndex = 0; 
    private double maxColorIndex = 6; 
    // private double colorIndex = 0.0;

    private static final double MAX_Y = 0;

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

        settingCameraParameters(true);

        while (true) {
            double startTime = Timer.getFPGATimestamp();
            try {
                Mat source = new Mat();

                if (cvSink.grabFrame(source) == 0) {
                    continue;
                }
                
                // очистка значений в хешмапах, связанных с фруктами, координатами, флагами при нахождении фруктов и тд.
                if(Main.sensorsMap.get("camTask") == 25.0) {
                    // Main.stringMap.put("detectedFruit", "none");
                    Main.switchMap.put("targetColorFound", false);
                    // Main.camMap.put("currentColorIndex", 0.0);
                    Main.camMap.put("targetFound", 0.0);
                    Main.camMap.put("currentCenterX", 0.0);
                    Main.camMap.put("currentCenterY", 0.0); settingCameraParameters(true);
                }

                // лайтовая очистка: координаты, флаги нахождения фруктов и прочего.
                if (Main.sensorsMap.get("camTask") == 0.0) {
                    colorIndex = 0; settingCameraParameters(true);
                    Main.camMap.put("targetFound", 0.0);
                    Main.camMap.put("currentCenterX", 0.0);
                    Main.camMap.put("currentCenterY", 0.0);
                }

                if (Main.sensorsMap.get("camTask") == 1.0) {
                    groundModeAutoGrab(source, Main.camMap.get("currentColorIndex"));
                }

                if (Main.sensorsMap.get("camTask") == 2.0) {
                    searchForGrab(source, 90, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 3.0) {
                    checkRotten(source);
                }

                if(Main.sensorsMap.get("camTask") == 4.0) {
                    treeModeAutoGrab(source, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 5.0) {
                    searchForGrab(source, 70, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 6.0) {
                    trackImageTrueArea(source, Main.camMap.get("currentColorIndex"));
                }

                if(Main.sensorsMap.get("camTask") == 7.0) {
                    thresholdSettings(source);
                }

                if(Main.sensorsMap.get("camTask") == 10.0) { // Перебор масок для фруктов
                    // thresholdSettings(source);
                    searchDominantColorMask(source);
                }

                if(Main.sensorsMap.get("camTask") == 11.0) {
                    checkGrippedFruit(source, Main.camMap.get("currentColorIndex"), 260);
                }

                if(Main.sensorsMap.get("camTask") == -1) {
                    checkFruit(source);
                }

                if(Main.sensorsMap.get("camTask") == -2) {
                    trackLowestPointBGR(source, Main.camMap.get("currentColorIndex"));
                }
                if(Main.sensorsMap.get("camTask") == -3.0) {
                    trackLowestPointGRAY(source);
                }

                Main.sensorsMap.put("updateTimeCamera", cameraUpdateTime);
                outStream.putFrame(source);
                source.release();
                Thread.sleep(10);
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
        double scanningTime = 1;
        
        Mat resizedSource = Viscad.ReduceResolutionImage(source, 3);
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat imageOut = createMask(hsvImage, setPointsForColors(colorIndex));
        
        outHSV.putFrame(imageOut);

        int currentMaskArea = Viscad.ImageTrueArea(imageOut);
        SmartDashboard.putNumber("imageOutAREA", currentMaskArea);
        releaseMats(resizedSource, blur, hsvImage, imageOut);

        if (currentMaskArea < 50 && Timer.getFPGATimestamp() - startTime > scanningTime) {
            colorIndex++;
            startTime = Timer.getFPGATimestamp(); 
            
            return;
        } else if (Timer.getFPGATimestamp() - startTime > scanningTime * 1.5) {
            Main.camMap.put("currentColorIndex", colorIndex);
            Main.switchMap.put("targetColorFound", true);
            colorIndex = 0;
        }

        if (colorIndex >= maxColorIndex) {
            colorIndex = 0;
            Main.camMap.put("currentColorIndex", 0.0);
            Main.switchMap.put("targetColorFound", false);
        }
    }

    private static void settingCameraParameters(boolean mode) {
        // camera.setWhiteBalanceAuto(); // Устанавливается баланс белого
        // camera.setExposureManual(100);; // Устанавливается яркость камеры
        // camera.setBrightness(1);
        if(mode) {
            camera.getProperty("focus_auto").set(1);
            camera.getProperty("white_balance_temperature_auto").set(0);
            camera.getProperty("brightness").set(15);
            camera.getProperty("focus_auto").set(0);
        } else {
            camera.getProperty("brightness").set(41);
        }
        
    }

    private static ColorRange setPointsForColors(double colorIndex) {
        ColorRange outRange = new ColorRange();

        // красный 
        if (colorIndex == 3.0) { outRange = new ColorRange(new Point(0, 10), new Point(200, 255), new Point(156, 255)); }
        // желтый
        if (colorIndex == 2.0) { outRange = new ColorRange(new Point(23, 30), new Point(196, 255), new Point(78, 254)); }
        // фиолетовый
        if (colorIndex == 1.0) { outRange = new ColorRange(new Point(123, 200), new Point(67, 255), new Point(12, 255)); }
        // зеленый для груш
        if (colorIndex == 5.0) { outRange = new ColorRange(new Point(32, 36), new Point(231, 255), new Point(98, 255)); }
        // зеленый для яблок
        if(colorIndex == 4.0) { outRange = new ColorRange(new Point(35, 45), new Point(123, 255), new Point(125, 255)); }
        return outRange;
    }

    private static Mat createMask(Mat hsvImage, ColorRange colorRange) {
        Mat mask = Viscad.Threshold(hsvImage, colorRange.getHue(), colorRange.getSaturation(), colorRange.getValue());
        Mat eroded = Viscad.Erode(mask, 1);
        Mat dilated = Viscad.Dilate(eroded, 1);
        releaseMats(mask, eroded);
        return dilated;
    }

    private void searchForGrab(Mat source, int size, double colorIndex) {
        List<Rect> currentCordinate = new ArrayList<>();

        ColorRange currentColor = setPointsForColors(colorIndex);
        // Point HueBound = new Point(0, 180);
        // Point SaturationBound = new Point(150, 255);
        // Point ValueBound = new Point(80, 255);

        // Снижение разрешения изображения
        Mat resizedSource = new Mat();
        Imgproc.resize(source, resizedSource, new Size(source.cols() / 3, source.rows() / 3));
    
        Mat blur = Viscad.Blur(resizedSource, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);

        Mat square = cropSquareFromCenter(mask, size);
    
        SmartDashboard.putNumber("ImageAreaGlideSquare", Viscad.ImageTrueArea(square));

        Mat outPA = new Mat();
        currentCordinate = Viscad.ParticleAnalysis(square, outPA);

        if (!currentCordinate.isEmpty()) {
            Rect firstRect = currentCordinate.get(0);
            Main.camMap.put("currentCenterY", (double) firstRect.y);
        } else {
            Main.camMap.put("currentCenterY", 0.0);
        }

        if(colorIndex == 1.0) {
            if(Viscad.ImageTrueArea(square) > 300) {
                Main.stringMap.put("detectedFruit", Constants.SMALL_ROTTEN_APPLE);
            }
        }

        if(colorIndex == 4.0) {
            if(Viscad.ImageTrueArea(square) > 100) {
                Main.stringMap.put("detectedFruit", Constants.SMALL_GREEN_APPLE);
            }
        }

        if(Viscad.ImageTrueArea(square) > Constants.STOP_AUTO_GLIDE_THRESHOLD) {
            Main.switchMap.put("stopAutoGlide", true);
        } else {
            Main.switchMap.put("stopAutoGlide", false);
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
            releaseMats(resizedSource, blur, hsvImage, mask, outPA);
            return;
        }

        lowestObjectCordinate = findLowestObject(mask, currentCordinate);

        if (lowestObjectCordinate.x != 0 && lowestObjectCordinate.y != 0 && maskArea > 50) {
            Main.camMap.put("targetFound", 1.0);

            Main.camMap.put("currentCenterX", lowestObjectCordinate.x);
            Main.camMap.put("currentCenterY", lowestObjectCordinate.y);

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

        ColorRange currentColor = setPointsForColors(3);

        Mat resizedImage = Viscad.ReduceResolutionImage(orig, 3);

        Mat blur = Viscad.Blur(resizedImage, 4);
        Mat hsvImage = Viscad.ConvertBGR2GRAY(blur);
        Mat mask = createMask(hsvImage, currentColor);

        // Mat thrGRAY = Viscad.ThresholdGray(hsvImage, new Point(0, 255));
        Imgproc.Canny(hsvImage, hsvImage, 300, 200, 5, true);
        Mat square = cropSquareFromCenter(mask, 65);

        keepTrack = !(Viscad.ImageTrueArea(square) >= stop); // не менять

        Main.switchMap.put("trackImageArea", keepTrack);
        SmartDashboard.putNumber("trackedImageArea", Viscad.ImageTrueArea(square));

        outRect.putFrame(hsvImage);

        releaseMats(resizedImage, blur, hsvImage, mask, square);        
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
            
            thresh.putFrame(inImage);
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
        outGlide.putFrame(croppedImage);
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
            Main.camMap.put("currentCenterY", highestFruit.y);
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

    private static void checkGrippedFruit(Mat orig, double colorIndex, int size) {

        ColorRange currentColor = setPointsForColors(colorIndex);

        Mat blur = Viscad.Blur(orig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        Mat mask = createMask(hsvImage, currentColor);

        Mat square = cropSquareFromCenter(mask, size);
        
        thresh.putFrame(square);
        if(Viscad.ImageTrueArea(square) < 14000 && Viscad.ImageTrueArea(square) >= 100 && colorIndex == 3.0) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_RED_APPLE);
            Main.camMap.put("targetFound", 1.0);
        } else if(Viscad.ImageTrueArea(square) >= 20000 && colorIndex == 3.0) {
            Main.stringMap.put("detectedFruit", Constants.BIG_RED_APPLE);
            Main.camMap.put("targetFound", 1.0);
        } else if(colorIndex == 2.0 && Viscad.ImageTrueArea(square) > 1000) {
            Main.stringMap.put("detectedFruit", Constants.YELLOW_PEAR);
            Main.camMap.put("targetFound", 1.0);
        } else if(colorIndex == 1.0 && Viscad.ImageTrueArea(square) > 100){
            Main.stringMap.put("detectedFruit", Constants.SMALL_ROTTEN_APPLE);
            Main.camMap.put("targetFound", 1.0);
        } else if(colorIndex == 5.0 && Viscad.ImageTrueArea(square) > 100) {
            Main.stringMap.put("detectedFruit", Constants.GREEN_PEAR);
            Main.camMap.put("targetFound", 1.0);
        } else if(colorIndex == 4.0 && Viscad.ImageTrueArea(square) > 100) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_GREEN_APPLE);
            Main.camMap.put("targetFound", 1.0); 
        } else {
            Main.camMap.put("targetFound", 0.0);
            Main.stringMap.put("detectedFruit", "none");
        }
        releaseMats(blur, hsvImage, mask, square);
        SmartDashboard.putNumber("grippedFruitImageArea", Viscad.ImageTrueArea(square));
    }

    private static void checkFruit(Mat orig) {

        double red1 = SmartDashboard.getNumber("RED1", 0.0);
        double red2 = SmartDashboard.getNumber("RED2", 0.0);

        double green1 = SmartDashboard.getNumber("GREEN1", 0.0);
        double green2 = SmartDashboard.getNumber("GREEN2", 0.0);

        double blue1 = SmartDashboard.getNumber("BLUE1", 0.0);
        double blue2 = SmartDashboard.getNumber("BLUE2", 0.0);
        
        Mat blur = Viscad.Blur(orig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        
        Mat redApple = Viscad.Threshold(hsvImage, new Point(0, 11), new Point(200, 255), new Point(156, 255));
        int areaRedApple = Viscad.ImageTrueArea(redApple);

        Mat yellowPear = Viscad.Threshold(hsvImage, new Point(19, 26), new Point(196, 255), new Point(139, 254));
        int areaYellowPear = Viscad.ImageTrueArea(yellowPear);

        Mat greenPear = Viscad.Threshold(hsvImage, new Point(32, 36), new Point(231, 255), new Point(98, 255));
        int areaGreenPear = Viscad.ImageTrueArea(greenPear);

        Mat greenApple = Viscad.Threshold(hsvImage, new Point(39, 45), new Point(156, 255), new Point(125, 255));
        int areaGreenApple = Viscad.ImageTrueArea(greenApple);

        Mat rotten = Viscad.Threshold(hsvImage, new Point(123, 200), new Point(67, 255), new Point(12, 255));
        int imgArearotten = Viscad.ImageTrueArea(rotten);

        
        if(areaRedApple > 15000) {
            Main.stringMap.put("detectedFruit", Constants.BIG_RED_APPLE);
        } else if(areaRedApple > 1000 && areaRedApple < 12000) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_RED_APPLE);
        } else if(areaYellowPear > 9000) {
            Main.stringMap.put("detectedFruit", Constants.YELLOW_PEAR);  
        } else if(areaGreenPear > 12000){
            Main.stringMap.put("detectedFruit", Constants.GREEN_PEAR);  
        } else if (areaGreenApple > 10000) {
            Main.stringMap.put("detectedFruit", Constants.BIG_GREEN_APPLE);
        } else if (areaGreenApple > 1900) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_GREEN_APPLE);
        }
         else if(imgArearotten > 1000) {
            Main.stringMap.put("detectedFruit", Constants.BIG_ROTTEN_APPLE);
        }
        else {
            Main.stringMap.put("detectedFruit", "none");
        }
        thresh.putFrame(redApple);
        SmartDashboard.putNumber("areaRedAPPLEEE!!", areaRedApple);
        releaseMats(blur, hsvImage, redApple, yellowPear, greenPear, greenApple);
    }
    // работает, но можно лучше и надежнее
    private static void trackLowestPointBGR(Mat orig, double colorIndex) {
        double trackStop = 470;

        ColorRange currentColor = setPointsForColors(colorIndex);
        settingCameraParameters(false);

        Mat blur = Viscad.Blur(orig, 4);
        Mat hsvImage = Viscad.ConvertBGR2HSV(blur);
        boolean trackAutoGlide = false;
        Mat mask = createMask(hsvImage, currentColor);
        
        Point lowestCoord = Viscad.getLowestObjectPoint(mask);
        outHSV.putFrame(mask);


        if(lowestCoord != null) {
            SmartDashboard.putNumber("lowestCoord check", 222);
            if(lowestCoord.y > trackStop) {
                trackAutoGlide = true;
            } else {
                trackAutoGlide = false;
            }
        } else {
            SmartDashboard.putNumber("lowestCoord check", 111);
        }
        releaseMats(blur, hsvImage, mask);
        Main.switchMap.put("trackAutoGlide", trackAutoGlide);
        SmartDashboard.putNumber("trackStopLOWEST", trackStop);
        SmartDashboard.putNumber("lowestCoord.y", lowestCoord.y);
    }

    private static void trackLowestPointGRAY(Mat orig) {

        double red1 = SmartDashboard.getNumber("RED1", 0.0);
        double red2 = SmartDashboard.getNumber("RED2", 0.0);
        settingCameraParameters(false);
        Mat blur = Viscad.Blur(orig, 4);
        Mat gray = Viscad.ConvertBGR2GRAY(blur);
        Mat thrGRAY = Viscad.ThresholdGray(gray, new Point(red1, red2));
        thresh.putFrame(thrGRAY);


        releaseMats(blur, gray, thrGRAY);
    }

    private static void checkRotten(Mat orig) {
        ColorRange rottenColor = setPointsForColors(1.0);
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
        Mat mask = createMask(hsvImage, rottenColor);

        SmartDashboard.putNumber("imgAreaRotten", Viscad.ImageTrueArea(mask));
        thresh.putFrame(mask);

        if(Viscad.ImageTrueArea(mask) > 3000) {
            Main.stringMap.put("detectedFruit", Constants.SMALL_ROTTEN_APPLE); // пока не получается определять конкретный гнилой фрукт
        } else {                                                   // для базового модуля пока так
            Main.stringMap.put("detectedFruit", "none");
        }

        releaseMats(mask, blur, hsvImage);
    }

    private static void releaseMats(Mat... mats) {
        for (Mat mat : mats) {
            mat.release();
        }
    }
}