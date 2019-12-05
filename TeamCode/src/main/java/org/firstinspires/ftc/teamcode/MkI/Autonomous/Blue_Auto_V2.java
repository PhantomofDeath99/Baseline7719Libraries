package org.firstinspires.ftc.teamcode.MkI.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.SeansSpace.SeansSubsystems.Driving.AutonomousPathing;
import org.firstinspires.ftc.teamcode.MkI.Subsystems.Driving.RobotComponents;
import org.firstinspires.ftc.teamcode.MkI.Subsystems.Driving.SeansEncLibrary;
import org.firstinspires.ftc.teamcode.MkI.Subsystems.Driving.Drivetrain;
import org.firstinspires.ftc.teamcode.MkI.Subsystems.Transitioning.AutoTransitioner;
import org.firstinspires.ftc.teamcode.MkI.Subsystems.VisionTargeting.SkystoneDetectionPhone;

@Autonomous (name = "Blue Auto V2", group = "MkI Blue Auto")
public class Blue_Auto_V2 extends LinearOpMode {


    private Drivetrain robot;
    boolean isSkystone;
    private RobotComponents mech;
    private AutonomousPathing path;
    //int position;
    SeansEncLibrary enc;
    SkystoneDetectionPhone phone;


    @Override
    public void runOpMode() throws InterruptedException {

        enc = new SeansEncLibrary(hardwareMap, telemetry, this);
        enc.init();
        mech = new RobotComponents(hardwareMap, telemetry);

        waitForStart();


         enc.steeringDrive(64, true,false);
         enc.gyroTurn(enc.TURN_SPEED, 90);
         enc.steeringDrive(32, true,false);
         enc.steeringDrive(56,true,false);
//        enc.gyroTurn(enc.TURN_SPEED, -90);
//        enc.steeringDrive(40, false);
//        enc.steeringDrive(-24, false);
//        //strafing here
//        enc.gyroTurn(enc.TURN_SPEED, -90);
//        mech.intakeStone();
//        enc.gyroTurn(enc.TURN_SPEED, 180);
//        enc.steeringDrive(80, false);
//
//        //sample
//        //vision code goes here
//        mech.intakeStone();
//        enc.steeringDrive(-12, false);
//        enc.gyroTurn(enc.TURN_SPEED, 90);
//        enc.steeringDrive(80, false);

        AutoTransitioner.transitionOnStop(this,"MkITeleOp");
    }
}

