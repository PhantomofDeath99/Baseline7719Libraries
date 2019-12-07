package org.firstinspires.ftc.teamcode.SeansSpace.SeansSubsystems.Driving;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Sean Cardosi on 2019-11-11
 */

public class EasyTeleOpFunctions {

    Telemetry telemetry;
    HardwareMap hardwareMap;

    DcMotor lf, lr, rf, rr;

    BNO055IMU imu;

    double headingOffset = 0.0;
    Orientation angles;
    boolean isCornered = false;


    public EasyTeleOpFunctions(Telemetry telemetry_, HardwareMap hardwareMap_) {
        hardwareMap = hardwareMap_;
        telemetry = telemetry_;

        //configuring the components
        lr = hardwareMap.dcMotor.get("leftB");
        lf = hardwareMap.dcMotor.get("leftF");
        lr.setDirection(DcMotor.Direction.FORWARD);
        lf.setDirection(DcMotor.Direction.FORWARD);
        lr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rr = hardwareMap.dcMotor.get("rightB");
        rf = hardwareMap.dcMotor.get("rightF");
        rr.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.REVERSE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled       = true;
        parameters.useExternalCrystal   = true;
        parameters.mode                 = BNO055IMU.SensorMode.IMU;
        parameters.loggingTag           = "IMU";
        imu                             = hardwareMap.get(BNO055IMU.class, "imuINT");

        imu.initialize(parameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    private void setMotorMode(DcMotor.RunMode mode, DcMotor... motors) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    public void runUsingEncoders() {
        setMotorMode(DcMotor.RunMode.RUN_USING_ENCODER, lf, lr, rf, rr);
    }

    /**
     * Fetch all once-per-time-slice values.
     * <p>
     * Call this either in your OpMode::loop function or in your while(opModeIsActive())
     * loops in your autonomous. It refresh gyro and other values that are computationally
     * expensive.
     */
    public void loop() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    /**
     * @return the raw heading along the desired axis
     */
    private double getRawHeading() {
        return angles.firstAngle;
    }

    /**
     * @return the robot's current heading in radians
     */
    public double getHeading() {
        return (getRawHeading() - headingOffset) % (2.0 * Math.PI);
    }

    /**
     * Set the current heading to zero.
     */
    public void resetHeading() {
        headingOffset = getRawHeading();
    }

    /**
     * Find the maximum absolute value of a set of numbers.
     *
     * @param xs Some number of double arguments
     * @return double maximum absolute value of all arguments
     */
    private static double maxAbs(double... xs) {
        double ret = Double.MIN_VALUE;
        for (double x : xs) {
            if (Math.abs(x) > ret) {
                ret = Math.abs(x);
            }
        }
        return ret;
    }

    /**
     * Set motor powers
     * <p>
     * All powers will be scaled by the greater of 1.0 or the largest absolute
     * value of any motor power.
     *
     * @param _lf Left front motor
     * @param _lr Left rear motor
     * @param _rf Right front motor
     * @param _rr Right rear motor
     */
    public void setMotors(double _lf, double _lr, double _rf, double _rr) {
        final double scale = maxAbs(1.0, _lf, _lr, _rf, _rr);
        lf.setPower(_lf / scale);
        lr.setPower(_lr / scale);
        rf.setPower(_rf / scale);
        rr.setPower(_rr / scale);
    }

    /**
     * Allows the robot driver to drive in a field centric manner.
     * @param gamepad This is gamepad1 or gamepad2
     * @param telemetry This is just telemetry
     */
    public void FieldOrientedDrive(Gamepad gamepad, Telemetry telemetry) {
        loop();

        final double x = gamepad.left_stick_x;
        final double y = -gamepad.left_stick_y;

        final double rotation = (gamepad.right_stick_x);
        final double direction = Math.atan2(x, y) + getHeading();
        final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

        final double lf = speed * Math.sin(direction + Math.PI / 4.0) + rotation;
        final double rf = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
        final double lr = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
        final double rr = speed * Math.sin(direction + Math.PI / 4.0) - rotation;

        if((gamepad.right_trigger>0.1)) {
            setMotors(lf / 2, lr / 2, rf / 2, rr / 2);
        } else {
            setMotors(lf, lr, rf, rr);
        }

        telemetry.addData("Speeds","%f,%f,%f,%f", lf,rf,lr,rr);
        telemetry.addData("RAW Gyro: ",getRawHeading());
        telemetry.addData("Heading: ",getHeading());
        telemetry.addData("Offset: ",headingOffset);

        telemetry.update();
    }

    /**
     * Function to move the foundation into the corner of the field
     * @param isCornered
     */
    public void CornerFoundation(boolean isCornered) {
        isCornered = true;

    }

    /**
     * Function to automatically place the stone into the previously cornered foundation.
     */
    public void PlaceStone() {
        if (isCornered == true) {

        }
    }

    /**
     * Function to allign with a nearby stone
     */
    public void StoneAllign() {

    }

    /**
     * Function to check if the intake is jammed and to automatically unjam it. May need ot be converted into
     * a boolean.
     */
    public void isJammed() {

    }

    /**
     * Function to automatically drive to the stone depot and collect a stone.
     * Pure Pursuit should be implemented.
     * @param speed The speed to give Pure Pursuit as a scale factor.
     */
    public void driveToStoneDepot(double speed) {

    }

    /**
     * More functions should be added as anyone comes up with any ideas.
     * All of the previous functions need to be finished when the new robot is finished.
     */
}
