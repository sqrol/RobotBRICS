package frc.robot.Logic;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Main;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;

public class TreeTraverse {

    private static final String[] TREE_ZONE_NAMES = { "LZ", "LOZ", "TZ", "RZ" };
    private static final String[] TREE_NUMBER = { "FIRST", "SECOND", "THIRD" };

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
            put("LOZ", "AUTO_GRAB_UPPER");
        }
    };

    private static boolean fruitFind = false;

    // Для setter и getter для чекпойнта
    private static String lastCheckpoint = "";
    // Первый вызов
    private static boolean firstCall = true;
    // Для setter и getter для зоны
    private static String treeZone = "";

    private static boolean treeNumberChange = false;

    private static boolean removal = false; // Переменная которая не дает завершиться доставке
    private static boolean resetSteps = false;

    private static boolean autoGrabCheck = false;

    private static int stepsForEnd = 0;
    private static String findFruitName = "";
    private static int currentTreeNumber, currentTreeZoneNumber, currentTreeZoneSteps = 0;
    private static int lastCurrentTreeNumber, lastCurrentTreeZoneNumber = 0;

    private static boolean cycleWork = true;

    private static boolean testForDebug = false;
    private static boolean lastStepDel = false;

    private static String outCommand;

    private static Logger LOGGER;
    private static boolean createLogger = true;

    private static FileHandler handler;

    public String execute() {
        
        
        outCommand = "";
        if (firstCall){
            outCommand = initializeFirstCommand();
            firstCall = false;
        } else {
            outCommand = commandBuildProcess();
        }

        if(createLogger) {
            try(FileInputStream ins = new FileInputStream("C:/Users/asdfc/OneDrive/Рабочий стол/RobotBRICS/src/main/resources/log.txt")){ 
                LogManager.getLogManager().readConfiguration(ins);
                LOGGER = Logger.getLogger(Main.class.getName());
                LOGGER.log(Level.INFO, outCommand);
            }catch (Exception ignore){
                ignore.printStackTrace();
            }
            createLogger = false;
        }
        
        
        return outCommand;
    }

    private String initializeFirstCommand() {
        final String outIndex = "MOVE_FROM_START_TO_CH1";
        setLastCheckpoint("CH1");
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
        return outIndex;
    }

    private String commandBuildProcess() {
        String outCommand = "none";
        if (currentTreeNumber < TREE_NUMBER.length) {
            if (currentTreeZoneNumber < TREE_ZONE_NAMES.length) {
                if (currentTreeZoneSteps <= 7) {
                   findFruitName = Main.stringMap.get("detectedFruit");
                   fruitFind = !Main.stringMap.get("detectedFruit").equals("none");

                    outCommand = createPathForScanning(currentTreeNumber, currentTreeZoneNumber, currentTreeZoneSteps);

                    currentTreeZoneSteps++;

                    // Сброс задач этого блока
                    if (currentTreeZoneSteps >= 7 || resetSteps) {
                        resetSteps = false;
                        resetStepsZone();
                    }
                }

                // Сброс задач этого блока
                if (currentTreeZoneNumber >= TREE_ZONE_NAMES.length) {
                    treeNumberChange = true;
                    resetTreeZone();
                }
            }

        } else {
            outCommand = processEnd(stepsForEnd);
            stepsForEnd++;
        }

        // // Debug
        // if (outCommand.equals("MOVE_FROM_SECOND_LOZ_TO_SECOND_RZ") || outCommand.equals("MOVE_FROM_CH2_TO_SECOND_RZ")) {
        //     testForDebug = true;
        // }
//
        // if (outCommand.equals("none") || outCommand.equals("")) {
        //     System.out.println("currentTreeNumber: " + currentTreeNumber);
        //     System.out.println("currentTreeZoneNumber: " + currentTreeZoneNumber);
        //     System.out.println("currentTreeZoneSteps: " + currentTreeZoneSteps);
        // }


        return outCommand;
    }

    private String createPathForScanning(int currentTreeNumber2, int currentTreeZoneNumber2, int currentTreeZoneSteps2) {
        String outCommand = "none1";

        String currentTreeName = TREE_NUMBER[currentTreeNumber2]; // Текущее дерево
        String currentTreeZoneName = TREE_ZONE_NAMES[currentTreeZoneNumber2]; // Текущая зона дерева

        if (autoGrabCheck && fruitFind || removal) {
            if (currentTreeZoneSteps2 == 0) {
                outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + getConForFruit(findFruitName);
                removal = true;
            }
            if (currentTreeZoneSteps2 == 1) { // Теперь можно сбрасывать все переменные тут
                outCommand = "RESET_FRUIT";
            }
            if (currentTreeZoneSteps2 == 2) {
                outCommand = "MOVE_FROM_" + getConForFruit(findFruitName) + "_TO_" + getLastCheckpoint();
            }
            if (currentTreeZoneSteps2 == 3) {
                outCommand = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + getLastTreeZone();
                // В этом условии мы ссылаемся на AutoGrab который ниже!
                currentTreeZoneNumber = lastCurrentTreeZoneNumber;
                currentTreeNumber = lastCurrentTreeNumber;

                // Тут нужно сбросить переменные с нахождением и название фрукта
                Main.stringMap.put("detectedFruit", "none");
                lastStepDel = true;
                findFruitName = "none";
                fruitFind = false;
                removal = false;
                testForDebug = false;
                autoGrabCheck = false;
                currentTreeZoneSteps = 0;
            }

        } else {
            if (treeNumberChange) {
                if (currentTreeZoneSteps2 == 0) { // Получаем текущую зону и от него возвращаемся на чекпоинт
                    outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + choosingBestZoneForCheck(currentTreeZoneName, currentTreeName);
                    setLastCheckpoint(choosingBestZoneForCheck(currentTreeZoneName, currentTreeName));
                }
                if (currentTreeZoneSteps2 == 1) { // Получаем текущую зону и от него возвращаемся на чекпоинт
                    if (lastStepDel) { // Тут тоже вызывается AutoGrab так что имей ввиду
                        outCommand = getGrabModeInArray(currentTreeZoneName);

                        // Запоминаем номера на котором мы ушли отсюда
                        lastCurrentTreeNumber = currentTreeNumber2;
                        lastCurrentTreeZoneNumber = currentTreeZoneNumber2;

                        // Узнаем следующий шаг будет следующее дерево
                        treeNumberChange = currentTreeZoneNumber >= TREE_ZONE_NAMES.length - 1;
                        autoGrabCheck = true;
                        resetSteps = true;
                        lastStepDel = false;

                        if (testForDebug) {
                            findFruitName = "SmallRedApple";
                            fruitFind = true;
                        }
                    } else {
                        outCommand = "MOVE_FROM_" + getLastCheckpoint() + "_TO_" + currentTreeName + "_" + currentTreeZoneName;
                        setLastTreeZone(currentTreeName + "_" + currentTreeZoneName);
                        currentTreeZoneSteps = 0;
                        treeNumberChange = false;
                    }
                    // !!!
                }
            } else {
                if (currentTreeZoneSteps2 == 0) {
                    if (cycleWork) { // Переход с первого старта к работе
                        outCommand = "MOVE_FROM_" + getLastCheckpoint()+ "_TO_" + currentTreeName + "_" + currentTreeZoneName;
                        setLastTreeZone(currentTreeName + "_" + currentTreeZoneName);
                        cycleWork = false;
                    } else {
                        outCommand = "MOVE_FROM_" + getLastTreeZone() + "_TO_" + currentTreeName + "_" + currentTreeZoneName;
                        setLastTreeZone(currentTreeName + "_" + currentTreeZoneName);
                    }
                }
                if (currentTreeZoneSteps2 == 1) { // Ищем фрукты для захвата
                    outCommand = getGrabModeInArray(currentTreeZoneName);

                    // Запоминаем номера на котором мы ушли отсюда
                    lastCurrentTreeNumber = currentTreeNumber2;
                    lastCurrentTreeZoneNumber = currentTreeZoneNumber2;

                    // Узнаем следующий шаг будет следующее дерево
                    treeNumberChange = currentTreeZoneNumber >= TREE_ZONE_NAMES.length - 1;
                    autoGrabCheck = true;
                    resetSteps = true;

//                    if (treeNumberChange) {
//                      if (currentTreeNumber2 == TREE_NUMBER.length - 1) {
//                          currentTreeZoneNumber = 0;
//                      }
//                    }

                    if (testForDebug) {
                        findFruitName = "SmallRedApple";
                        fruitFind = true;
                    }
                }
            }
        }

        return outCommand;
    }

    private static void resetStepsZone() {
        currentTreeZoneSteps = 0;
        currentTreeZoneNumber++;
    }

    private static void resetTreeZone() {
        currentTreeZoneNumber = 0;
        currentTreeNumber++;
    }

    private static String choosingBestZoneForCheck(final String currentZoneName, final String zoneName) {
        String out = "";
        if (zoneName.equals("FIRST")) {
            switch(currentZoneName) {
                case "LZ":
                    out = "CH1";
                    break;
                case "RZ":
                    out = "CH1";
                    break;
                case "LOZ":
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
            switch(currentZoneName) {
                case "LZ":
                    out = "CH2";
                    break;
                case "RZ":
                    out = "CH2";
                    break;
                case "LOZ":
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
            switch(currentZoneName) {
                case "LZ":
                    out = "CH2";
                    break;
                case "RZ":
                    out = "CH3";
                    break;
                case "LOZ":
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

    private  void setLastCheckpoint(String lastCheckpoint) {
        TreeTraverse.lastCheckpoint = lastCheckpoint;
    }

    private void setLastTreeZone(final String treeZone) {
        TreeTraverse.treeZone = treeZone;
    }

    private String getLastTreeZone() {
        return TreeTraverse.treeZone;
    }

    private String getLastCheckpoint() {
        return TreeTraverse.lastCheckpoint;
    }
}