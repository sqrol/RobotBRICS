package frc.robot.Logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Main;

public class TreeTraverse {

    private static final String[] TREE_ZONE_NAMES = {"LZ", "TZ", "RZ"};
    private static final String[] TREE_NUMBER = {"FIRST", "SECOND", "THIRD"};

    private final HashMap<String, String> containersForFruits = new HashMap<String, String>() {
        {
            put("rotten", "CON2");
            put("AppleSmallRed", "CON4");
            put("AppleBigRed", "CON1");
            put("PearYellow", "CON3");
        }
    };

    private static final HashMap<String, String> grabTypeForZones = new HashMap<String, String>() {
        {
            put("LZ", "AUTO_GRAB_UPPER");
            put("RZ", "AUTO_GRAB_UPPER");
            put("TZ", "AUTO_GRAB_UPPER");
        }
    };

    private static boolean fruitFind = true; 
    private static boolean checkFruit = false; 

    // Для setter и getter для чекпойнта
    private static String lastCheckpoint = "";
    // Первый вызов
    private static boolean firstCall = true;
    // Для setter и getter для зоны
    private static String treeZone = "";

    private static boolean treeNumberChange = true;
    private static boolean resetSteps = false;
    private static int stepsForEnd = 0;
    private static String findFruitName = "";
    private static int currentTreeNumber, currentTreeZoneNumber, currentTreeZoneSteps = 0;


    public String execute() {
        String outCommand = "";
        if (firstCall){
            outCommand = initializeFirstCommand();
            firstCall = false;
        } else {
            findFruitName = getGrippedFruit();
            outCommand = commandBuildProcess();
        }
        // System.out.println(outCommand);
        return outCommand; 
    }

    private String initializeFirstCommand() {
        final String outIndex = "MOV_IN_START_CH1";
        setLastCheckpoint("CH1");
        return outIndex;
    }

    private String processEnd(final Integer step) {
        String outIndex = "none";
        if (step == 0) {
            final String[] replaceZone = getLastTreeZone().split("_");
            outIndex = "MOV_IN_" + getLastTreeZone() + "_TO_"
                    + choosingBestZoneForCheck(replaceZone[1], replaceZone[0]);
            setLastCheckpoint(choosingBestZoneForCheck(replaceZone[1], replaceZone[0]));
        } else if (step == 1) {
            outIndex = "MOV_IN_" + getLastCheckpoint() + "_TO_" + "FINISH";
        } else if (step == 2) {
            outIndex = "LogicEnd";
        }
        return outIndex;
    }

    private String commandBuildProcess() {
        String outCommand = "";
        if (currentTreeNumber < TREE_NUMBER.length) {
            if (currentTreeZoneNumber < TREE_ZONE_NAMES.length) {
                if (currentTreeZoneSteps <= 7) {
                    final String currentTreeZone = TREE_NUMBER[currentTreeNumber] + "_"
                            + TREE_ZONE_NAMES[currentTreeZoneNumber];

                    if (treeNumberChange) {
                        if (currentTreeNumber == 0 && currentTreeZoneNumber == 0) {
                            outCommand = "MOV_IN_" + getLastCheckpoint() + "_TO_" + currentTreeZone;
                            treeNumberChange = false;
                        } else {
                            if (currentTreeZoneSteps == 0) {
                                final String[] replaceZone = currentTreeZone.split("_");
                                outCommand = "MOV_IN_" + getLastTreeZone() + "_TO_"
                                        + choosingBestZoneForCheck(replaceZone[1], replaceZone[0]);
                                setLastCheckpoint(choosingBestZoneForCheck(replaceZone[1], replaceZone[0]));
                            }
                            if (currentTreeZoneSteps == 1) {
                                outCommand = "MOV_IN_" + getLastCheckpoint() + "_TO_" + currentTreeZone;
                            }
                            if (currentTreeZoneSteps == 2) {
                                outCommand = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);
                                treeNumberChange = false;
                                checkFruit = true;
                                resetSteps = true;
                            }
                        }

                        setLastTreeZone(currentTreeZone);

                    } else {

                        if (fruitFind && checkFruit) {
                            if (currentTreeZoneSteps == 0) {
                                outCommand = "MOV_IN_" + getLastTreeZone() + "_TO_" + getLastCheckpoint();
                                currentTreeZoneNumber = currentTreeZoneNumber - 1;
                            } else if (currentTreeZoneSteps == 1) {
                                outCommand = "MOV_IN_" + getLastCheckpoint() + "_TO_" + getConForFruit(findFruitName);
                            } else if (currentTreeZoneSteps == 2) {
                                outCommand = "RESET_FRUIT";
                            } else if (currentTreeZoneSteps == 3) {
                                outCommand = "MOV_IN_" + getConForFruit(findFruitName) + "_TO_" + getLastCheckpoint();
                            } else if (currentTreeZoneSteps == 4) {
                                outCommand = "MOV_IN_" + getLastCheckpoint() + "_TO_" + getLastTreeZone();
                            } else if (currentTreeZoneSteps == 5) {
                                outCommand = getGrabModeInArray(TREE_ZONE_NAMES[currentTreeZoneNumber]);

                                // Сбрасываем переменные для захвата
                                findFruitName = "";
                                resetSteps = true;
                                checkFruit = false;
                                fruitFind = false;
                            }
                        } else {

                            if (currentTreeZoneSteps == 0) {
                                outCommand = "MOV_IN_" + getLastTreeZone() + "_TO_" + currentTreeZone;
                                checkFruit = false;
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
                            }

                            setLastTreeZone(currentTreeZone);
                        }
                    }

                    currentTreeZoneSteps++;

                    // Сброс задач этого блока
                    if (currentTreeZoneSteps >= 7 || resetSteps) {
                        resetSteps = false;
                        resetStepsZone();
                    }
                }

                // Сброс задач этого блока
                if (currentTreeZoneNumber >= TREE_ZONE_NAMES.length) {
                    resetTreeZone();
                }
            }
        } else {
            outCommand = processEnd(stepsForEnd);
            stepsForEnd++;
        }

        // System.out.println("currentTreeNumber: " + currentTreeNumber);
        // System.out.println("currentTreeZoneNumber: " + currentTreeZoneNumber);
        // System.out.println("currentTreeZoneSteps: " + currentTreeZoneSteps);

        return outCommand;
    }

    private void resetStepsZone() {
        currentTreeZoneSteps = 0;
        currentTreeZoneNumber++;
    }

    private void resetTreeZone() {
        currentTreeZoneNumber = 0;
        currentTreeNumber++;
    }

    private String getGrippedFruit() {
        String detectionResult = "";
        switch (Main.camMap.get("grippedFruit").intValue()) {
        case 1:
            detectionResult = "AppleBigRed";
            fruitFind = true;
            break;
        case 2:
            detectionResult = "AppleSmallRed";
            fruitFind = true;
            break;
        case 3:
            detectionResult = "PearYellow";
            fruitFind = true;
            break;
        default:
            detectionResult = "";
            fruitFind = false;
            break;
        }
        return detectionResult;
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
        return out;
    }

    private String getGrabModeInArray(final String zoneName) {
        return grabTypeForZones.getOrDefault(zoneName, "none");
    }

    private String getConForFruit(final String fruitName) {
        return containersForFruits.getOrDefault(fruitName, "none");
    }

    private void setLastCheckpoint(final String lastCheckpoint) {
        TreeTraverse.lastCheckpoint = lastCheckpoint;
    }

    private void setLastTreeZone(final String treeZone) {
        TreeTraverse.treeZone = treeZone;
    }

    private String getLastTreeZone() {
        return TreeTraverse.treeZone;
    }

    private String getLastCheckpoint() {
        return lastCheckpoint;
    }
}