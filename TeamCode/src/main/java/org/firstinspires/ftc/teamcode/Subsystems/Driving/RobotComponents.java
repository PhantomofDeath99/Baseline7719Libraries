package org.firstinspires.ftc.teamcode.Subsystems.Driving;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
 * Created by Sean Cardosi on 10/15/2019.
 */
public class RobotComponents {

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final DcMotor intake;
    private final DcMotor liftL;
    private final DcMotor liftR;

    private int liftMax = 2000;
    private int liftMin = 50;
    Servo lgrab , rgrab, shoulder,wrist,claw;


    private double posit = 0.14;






    public RobotComponents(final HardwareMap _hardwareMap, final Telemetry _telemetry) {
        hardwareMap = _hardwareMap;
        telemetry = _telemetry;

        intake = hardwareMap.dcMotor.get("intake");
        intake.setDirection(DcMotor.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        liftL = hardwareMap.dcMotor.get("liftL");
        liftL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftL.setDirection(DcMotor.Direction.FORWARD);
        liftL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftR = hardwareMap.dcMotor.get("liftR");
        liftR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftR.setDirection(DcMotor.Direction.REVERSE);
        liftR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lgrab = hardwareMap.servo.get("lgrab");
        rgrab = hardwareMap.servo.get("rgrab");
        rgrab.setDirection(Servo.Direction.REVERSE);

        shoulder = hardwareMap.servo.get("shoulder");
        wrist = hardwareMap.servo.get("wrist");
        claw = hardwareMap.servo.get("claw");
    }
    public void init() {
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    //----------------------------------------------=+(Intake)+=----------------------------------------------\\
    public void intakeStone() {
        intake.setPower(-0.75);
    }
    public void ejectStone() {
        intake.setPower(1);
    }
    public void stopStone() {
        intake.setPower(0.0);
    }
    //----------------------------------------------=+(Intake)+=----------------------------------------------\\



    //TODO: Adjust lift encoder values to properly stop to avoid breaking the lift.
    //----------------------------------------------=+(Lift)+=----------------------------------------------\\

    /**
     * Uses a gamepad input to assign the upward movement of the lift to
     * @param gamepad The gamepad to be used to control the upward movement of the lift
     */
    public void liftControlUp(Gamepad gamepad) {//For driver control
        liftL.setPower(gamepad.left_stick_y/2);
        liftR.setPower(gamepad.left_stick_y/2);
    }

    /**
     * Uses a gamepad number to assign the downward movement of the lift to
     * @param gamepad The gamepad to be used to control the downward movement of the lift
     */


    /**
     * Makes the lift run to a certain height.
     * @param height The height for the lift to raise to. Currently in encoder ticks.
     *               Needs to be converted into inches.
     * @param velocity The speed and direction for the lift to run at. -1 goes down and 1 goes up
     */
    public void liftPosition(int height, double velocity) {//TODO: Convert the position to inches.

        liftL.setTargetPosition(height);
        liftR.setTargetPosition(height);

        liftL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        liftL.setPower(velocity);
        liftR.setPower(velocity);
    }

    /**
     * Stops the lifts movement
     */
    public void  liftStop() {
        liftL.setPower(0.0);
        liftR.setPower(0.0);
    }
    //----------------------------------------------=+(Lift)+=----------------------------------------------\\




    //----------------------------------------------=+(Block build)+=----------------------------------------------\\


    public void clawGrab(){
        claw.setPosition(0.6);
    }

    public void clawRelease(){
        claw.setPosition(0);
    }

    public void wrist(Gamepad gamepad){
        wrist.setPosition(0.47 + gamepad.left_trigger/5 - gamepad.right_trigger/5);
    }
//
//    public void wristLeft(Gamepad gamepad){
//        wrist.setPosition(gamepad.right_trigger/5);
//    }

    public void shoulder(Gamepad gamepad){
//        double down = 0.13;
//        double up = 0.9;
//        if (gamepad.a){
//            posit = down;
//        } else if (gamepad.y){
//            posit = up;
//        }

//        shoulder.setPosition(posit + gamepad.right_stick_y/10);
        shoulder.setPosition(gamepad.right_stick_y);
    }
//
//    public void shoulderDown(Gamepad gamepad){
//        shoulder.setPosition(0.140);
//    }



    //----------------------------------------------=+(Block build)+=----------------------------------------------\\


//    //----------------------------------------------=+(Grabber)+=----------------------------------------------\\
    public void returnPosition() {
        lgrab.setPosition(1);
        rgrab.setPosition(1);

    }
    public void outPosition() {
        lgrab.setPosition(0.25);
        rgrab.setPosition(0.25);

    }
//    //----------------------------------------------=+(Grabber)+=----------------------------------------------\\




}
