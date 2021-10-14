package org.firstinspires.ftc.teamcode.RISE2021.Tests;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * Created by Jordan Nuthalapaty on 10/14/2021.
 */
public class Components {

    private Servo prototypeR;
    private Servo prototypeL;




    public Components(final HardwareMap hardwareMap) {

        prototypeR = hardwareMap.servo.get("prototype");
        prototypeL = hardwareMap.servo.get("prototypeL");
        prototypeL.setDirection(Servo.Direction.REVERSE);

    }
    public void init() {

        prototypeL.setPosition(0);
        prototypeL.setPosition(0);
    }


    //----------------------------------------------=+(Intake)+=----------------------------------------------\\
    public void intake() {
        prototypeR.setPosition(1);
        prototypeL.setPosition(-1);
    }
    public void outtake() {
        prototypeL.setPosition(-1);
        prototypeR.setPosition(1);
    }


}

