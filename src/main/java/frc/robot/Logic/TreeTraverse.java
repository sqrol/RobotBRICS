package frc.robot.Logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;

public class TreeTraverse {

    private static final String[] TREE_ZONE_NAMES = {"LZ", "TZ", "RZ"};
    private static final String[] TREE_NUMBER = {"FIRST", "SECOND", "THIRD"};

    private final HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put(Constants.ROTTEN_PEAR, "CON2");
            put(Constants.BIG_ROTTEN_APPLE, "CON2");
            put(Constants.SMALL_ROTTEN_APPLE, "CON2");

            put(Constants.BIG_RED_APPLE, "CON4");
            put(Constants.SMALL_RED_APPLE, "CON1");
            put(Constants.YELLOW_PEAR, "CON3");
        }
    };

    private static final HashMap<String, String> grabTypeForZones = new HashMap<String, String>() {
        {
            put("LZ", "AUTO_GRAB_UPPER");
            put("RZ", "AUTO_GRAB_UPPER");
            put("TZ", "AUTO_GRAB_UPPER");
        }
    };

    public static boolean fruitFind = false; 
    public static boolean checkFruit = false; 

    // Для setter и getter для чекпойнта
    public static String lastCheckpoint = "";
    // Первый вызов
    public static boolean firstCall = true;
    // Для setter и getter для зоны
    public static String treeZone = "";

    public static String conName = ""; 

    public static boolean treeNumberChange = true;
    public static boolean resetSteps = false;
    public static int stepsForEnd = 0;
    public static String findFruitName = "";
    public static int currentTreeNumber, currentTreeZoneNumber, currentTreeZoneSteps = 0;

    public String execute() {
        String outCommand = "";
        if (firstCall){
            outCommand = initializeFirstCommand();
            firstCall = false;
        } else {
            findFruitName = Main.stringMap.get("detectedFruit");
            // findFruitName = "SmallRedApple";
            fruitFind = !Main.stringMap.get("detectedFruit").equals("none");
            outCommand = commandBuildProcess();
        }
        // System.out.println(outCommand);
        return outCommand; 
    }

    private String initializeFirstCommand() {
        final String outIndex = "MOVE_FROM_START_TO_CH1";
        setLastCheckpoint("CH1");
        SmartDashboard.putBoolean("initializeFirstCommand()", !lastCheckpoint.equals(""));

        return outIndex;
    }

    private String processEnd(final Integer step) {
        String outIndex = "none";
        if (step == 0) {
            final String[] replaceZone = getLastTreeZone().split("_");
            outIndex = "MOVE_FROM_" + getLastTreeZone() + "_TO_"
                    + choosingBestZoneForCheck(replaceZone[1], replaceZone[0]);
            setLastCheckpoint(choosingBestZoneForCheck(replaceZone[1], replaceZone[0]));
        } else if (step == 1) {
            outIndex = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + "FINISH";
        } else if (step == 2) {
            outIndex = "END";
        }
        SmartDashboard.putNumber("processEnd.step", step);
        SmartDashboard.putBoolean("precessEnd()", !outIndex.equals("none"));
        return outIndex;
    }

    private String commandBuildProcess() {
        String outCommand = "none";
        if (currentTreeNumber < TREE_NUMBER.length) {
            SmartDashboard.putNumber("commandCheck", 1);
            if (currentTreeZoneNumber < TREE_ZONE_NAMES.length) {
                SmartDashboard.putNumber("commandCheck", 2);
                if (currentTreeZoneSteps <= 7) {
                    SmartDashboard.putNumber("commandCheck", 3);
                    final String currentTreeZone = TREE_NUMBER[currentTreeNumber] + "_"
                            + TREE_ZONE_NAMES[currentTreeZoneNumber];

                    if (treeNumberChange) {
                        SmartDashboard.putNumber("commandCheck", 4);
                        if (currentTreeNumber == 0 && currentTreeZoneNumber == 0) {
                            outCommand = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + currentTreeZone;
                            treeNumberChange = false;
                            SmartDashboard.putNumber("commandCheck", 5);
                        } else {
                            SmartDashboard.putNumber("commandCheck", 6);
                            if (currentTreeZoneSteps == 0) {
                                final String[] replaceZone = currentTreeZone.split("_");
                                outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_"
                                        + choosingBestZoneForCheck(replaceZone[1], replaceZone[0]);
                                setLastCheckpoint(choosingBestZoneForCheck(replaceZone[1], replaceZone[0]));
                                SmartDashboard.putNumber("commandCheck", 7);
                            }
                            if (currentTreeZoneSteps == 1) {
                                
                                outCommand = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + currentTreeZone;
                                SmartDashboard.putNumber("commandCheck", 8);
                            }
                            if (currentTreeZoneSteps == 2) {
                                outCommand = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);
                                treeNumberChange = false;
                                checkFruit = true;
                                resetSteps = true;
                                SmartDashboard.putNumber("commandCheck", 9);
                            }
                        }

                        setLastTreeZone(currentTreeZone);

                    } else {
                        SmartDashboard.putNumber("commandCheck", 10);
                        if (fruitFind && checkFruit) {
                            SmartDashboard.putNumber("commandCheck", 11);
                            if (currentTreeZoneSteps == 0) {
                                setLastCon(getConForFruit(findFruitName)); 
                                outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getLastCon();
                                SmartDashboard.putNumber("commandCheck", 12);
                            } else if (currentTreeZoneSteps == 1) {
                                outCommand = "RESET_FRUIT";
                                SmartDashboard.putNumber("commandCheck", 13);
                            } else if (currentTreeZoneSteps == 2) {
                                SmartDashboard.putNumber("check currentTreeZoneSteps", 111);
                                outCommand = "MOVE_FROM_" + getLastCon() + "_TO_" + getLastCheckpoint();
                                SmartDashboard.putNumber("commandCheck", 14);
                            } else if (currentTreeZoneSteps == 3) {
                                outCommand = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + getLastTreeZone();
                                SmartDashboard.putNumber("commandCheck", 15);
                            } else if (currentTreeZoneSteps == 4) {
                                outCommand = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);

                                // Сбрасываем переменные для захвата
                                resetSteps = true;
                                checkFruit = false;
                                fruitFind = false;
                                
                                currentTreeZoneNumber = currentTreeZoneNumber - 1; 
                                
                                SmartDashboard.putNumber("commandCheck", 16);
                            }
                        } else {
                            SmartDashboard.putNumber("commandCheck", 17);
                            if (currentTreeZoneSteps == 0) {
                                outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + currentTreeZone;
                                checkFruit = false;
                                SmartDashboard.putNumber("commandCheck", 18);
                            }

                            if (currentTreeZoneSteps == 1) {
                                outCommand = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);
                                treeNumberChange = currentTreeZoneNumber >= TREE_ZONE_NAMES.length - 1; // Смотрим это
                                                                                                        // последняя
                                                                                                        // зона для
                                                                                                        // текущего
                                                                                                        // дерева
                                checkFruit = true;
                                resetSteps = true;
                                SmartDashboard.putNumber("commandCheck", 19);
                            }

                            setLastTreeZone(currentTreeZone);
                        }
                    }

                    currentTreeZoneSteps++;

                    // Сброс задач этого блока
                    if (currentTreeZoneSteps >= 7 || resetSteps) {
                        resetSteps = false;
                        resetStepsZone();
                        SmartDashboard.putNumber("commandCheck", 20);
                    }
                }

                // Сброс задач этого блока
                if (currentTreeZoneNumber >= TREE_ZONE_NAMES.length) {
                    resetTreeZone();
                    SmartDashboard.putNumber("commandCheck", 21);
                }
            }
        } else {
            outCommand = processEnd(stepsForEnd);
            stepsForEnd++;
            SmartDashboard.putNumber("commandCheck",22);
        }

        // System.out.println("currentTreeNumber: " + currentTreeNumber);
        // System.out.println("currentTreeZoneNumber: " + currentTreeZoneNumber);
        // System.out.println("currentTreeZoneSteps: " + currentTreeZoneSteps);
        SmartDashboard.putBoolean("commandBuildProcess()", !outCommand.equals("none"));
        return outCommand;
    }

    private void resetStepsZone() {
        currentTreeZoneSteps = 0;
        currentTreeZoneNumber++;
        SmartDashboard.putBoolean("resetStepsZone()", true);
    }

    private void resetTreeZone() {
        currentTreeZoneNumber = 0;
        currentTreeNumber++;
        SmartDashboard.putBoolean("resetTreeZone()", true);
    }

    private String choosingBestZoneForCheck(final String currentZoneName, final String zoneName) {
        String out = "";
        if (zoneName.equals("FIRST")) {
            switch (currentZoneName) {
            case "LZ":
                out = "CH1";
                break;
            case "RZ":
                out = "CH1";
                break;
            case "TZ":
                out = "CH1";
                break;
            case "START":
                out = "CH1";
                break;
            default:
                out = "null";
                break;
            }
        }
        if (zoneName.equals("SECOND")) {
            switch (currentZoneName) {
            case "LZ":
                out = "CH2";
                break;
            case "RZ":
                out = "CH2";
                break;
            case "TZ":
                out = "CH2";
                break;
            case "START":
                out = "CH2";
                break;
            default:
                out = "null";
                break;
            }
        }
        if (zoneName.equals("THIRD")) {
            switch (currentZoneName) {
            case "LZ":
                out = "CH2";
                break;
            case "RZ":
                out = "CH3";
                break;
            case "TZ":
                out = "CH3";
                break;
            case "START":
                out = "CH3";
                break;
            default:
                out = "null";
                break;
            }
        }
        SmartDashboard.putBoolean("choosingBestZoneForCheck()", !out.equals(""));
        return out;
    }

    private String getGrabModeInArray(final String zoneName) {
        SmartDashboard.putString("getGrabModeInArray()", grabTypeForZones.getOrDefault(zoneName, "none"));
        return grabTypeForZones.getOrDefault(zoneName, "none");
    }

    private String getConForFruit(final String fruitName) {
        SmartDashboard.putString("getConForFruit()", containersForFruits.getOrDefault(fruitName, "none"));
        return containersForFruits.getOrDefault(fruitName, "none");
    }

    private void setLastCheckpoint(String lastCheckpoint) {
        TreeTraverse.lastCheckpoint = lastCheckpoint;
    }

    private void setLastTreeZone(final String treeZone) {
        TreeTraverse.treeZone = treeZone;
    }

    private String getLastTreeZone() {
        return TreeTraverse.treeZone;
    }

    private  String getLastCheckpoint() {
        return TreeTraverse.lastCheckpoint;
    }

    private void setLastCon(final String conName) {
        TreeTraverse.conName = conName;
    }

    private String getLastCon() {
        return TreeTraverse.conName;
    }

    
}