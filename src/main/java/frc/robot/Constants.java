/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public final class Constants
{
    public static final int TITAN_ID                                  = 42;

    public static final int MOTOR_RIGHT                               = 3;
    public static final int MOTOR_LEFT                                = 1;
    public static final int MOTOR_ROTATE                              = 0;
    public static final int MOTOR_LIFT                                = 2;

    public static final double DIST_PER_TICK                          = 1;

    public static final int ENC_RIGHT                                 = 3;
    public static final int ENC_LEFT                                  = 1;
    public static final int ENC_ROTATE                                = 0;
    public static final int ENC_LIFT                                  = 2;

    public static final int SERVO_GRAB                                = 0;
    public static final int SERVO_GLIDE                               = 1;
    public static final int SERVO_GRIP_ROTATE                         = 2;

    ///////////////////////

    public static final int COBRA                                     = 3; 

    public static final int SHARP_RIGHT                               = 0;
    public static final int SHARP_LEFT                                = 1;


    public static final int LIMIT_SWITCH_LIFT                         = 5;
    public static final int LIMIT_SWITCH_GLIDE                        = 3;
    public static final int START_BUTTON                              = 0;
    public static final int EMS_BUTTON                                = 1;

    public static final int RED_LED                                   = 20;
    public static final int GREEN_LED                                 = 21;

    public static final int SONIC_ECHO_RIGHT                          = 10;
    public static final int SONIC_PING_RIGHT                          = 11;
    
    public static final int SONIC_ECHO_LEFT                           = 6;
    public static final int SONIC_PING_LEFT                           = 7;

    ///////////////////////

    public static final double GRAB_OPEN                              = 55.0;
    public static final double GRAB_CLOSE                             = 79.0;
    public static final double GRAB_BIG_APPLE                         = 69.0;
    public static final double GRAB_SMALL_APPLE                       = 79.0;
    public static final double GRAB_PEAR                              = 69.0;

    public static final double GRIP_ROTATE_FLOOR                      = 139.0;
    public static final double GRIP_ROTATE_DROP                       = 82.0;
    public static final double GRIP_ROTATE_CHECK_ZONE                 = 131.0;
    public static final double GRIP_ROTATE_CHECK_BRANCH               = 85.0;

    ///////////////////////
    public static final double INDICATION_WAITING                     = 1.0;
    public static final double INDICATION_IN_PROGRESS                 = 2.0;
    public static final double INDICATION_FINISHED                    = 3.0;
    public static final double INDICATION_REACTION                    = 4.0;

    ///////////////////////

    public static final String BIG_RED_APPLE                          = "BigRedApple";
    public static final String SMALL_RED_APPLE                        = "SmallRedApple";
    public static final String BIG_GREEN_APPLE                        = "BigGreenApple";
    public static final String SMALL_GREEN_APPLE                      = "SmallGreenApple";
    public static final String GREEN_PEAR                             = "PearGreen";
    public static final String YELLOW_PEAR                            = "YellowPear"; 
    public static final String BIG_ROTTEN_APPLE                       = "BigRottenApple";
    public static final String SMALL_ROTTEN_APPLE                     = "SmallRottenApple";
    public static final String ROTTEN_PEAR                            = "RottenPear";

    ///////////////////////
    public static final int STOP_AUTO_GLIDE_THRESHOLD                 = 7000;
}



