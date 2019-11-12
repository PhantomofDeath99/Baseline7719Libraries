package org.firstinspires.ftc.teamcode.SeansSpace.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeansSpace.Subsystems.RobotMedia;
import org.firstinspires.ftc.teamcode.Subsystems.Driving.RobotComponents;
import org.firstinspires.ftc.teamcode.SeansSpace.Subsystems.EasyTeleOpFunctions;

import com.qualcomm.robotcore.hardware.Servo;
/**
 * @Author Sean Cardosi
 * @Date 11/11/19
 */
@Disabled
@TeleOp(name = "SeansTeleOp", group = "SeansTeleOp")
public class SeansTeleOp extends OpMode {

    Servo lgrab , rgrab;
    private EasyTeleOpFunctions robot;
    private RobotComponents component;
    private RobotMedia media;//:D

    private boolean isReady = false;
    boolean isCornered = false;

    double turn = 0;

    @Override
    public void init() {

        lgrab = hardwareMap.servo.get("lgrab");
        rgrab = hardwareMap.servo.get("rgrab");
        rgrab.setDirection(Servo.Direction.REVERSE);

        //Initialize robot
        robot = new EasyTeleOpFunctions(telemetry, hardwareMap);
        robot.runUsingEncoders();

        component = new RobotComponents(hardwareMap, telemetry);

        media = new RobotMedia(hardwareMap);//:D

        isReady = true;

        lgrab.setPosition(0.25);
        rgrab.setPosition(0.25);
    }

    @Override
    public void init_loop() {
        if(isReady==true) {
            telemetry.addData(">", "Robot Ready!");
            telemetry.update();
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {

        //----------------------------------------------=+(Drivetrain)+=----------------------------------------------\\
        robot.FieldOrientedDrive(gamepad1, telemetry);

        if (gamepad1.x) {
            robot.resetHeading();
        }
        //----------------------------------------------=+(Drivetrain)+=----------------------------------------------\\


        //----------------------------------------------=+(Intake)+=----------------------------------------------\\
        if ((gamepad1.left_trigger > 0.1)&&(Math.abs(gamepad1.right_trigger ) < 0.1)) {//This is a precaution
            component.intakeStone();//Intake stone
        } else if ((gamepad1.right_trigger > 0.1)&&(Math.abs(gamepad1.left_trigger ) < 0.1)) {//This is a precaution
            component.ejectStone();//Eject stone
        } else {
            component.stopStone();//Stop power to the stone intake
        }
        //----------------------------------------------=+(Intake)+=----------------------------------------------\\


        //----------------------------------------------=+(Lift)+=----------------------------------------------\\
        component.liftControlUp(gamepad2);
        //----------------------------------------------=+(Lift)+=----------------------------------------------\\


        //----------------------------------------------=+(Grabber)+=----------------------------------------------\\
        if (gamepad1.y) {
            lgrab.setPosition(1);
            rgrab.setPosition(1);
        }
        if (gamepad1.a) {
            lgrab.setPosition(0.25);
            rgrab.setPosition(0.25);
        }
        //----------------------------------------------=+(Grabber)+=----------------------------------------------\\


        //----------------------------------------------=+(Media)+=----------------------------------------------\\
        media.playSounds(gamepad1, hardwareMap);//:D
        //----------------------------------------------=+(Media)+=----------------------------------------------\\

        //----------------------------------------------=+(EZ Drive)+=----------------------------------------------\\

        //----------------------------------------------=+(EZ Drive)+=----------------------------------------------\\




    }
}