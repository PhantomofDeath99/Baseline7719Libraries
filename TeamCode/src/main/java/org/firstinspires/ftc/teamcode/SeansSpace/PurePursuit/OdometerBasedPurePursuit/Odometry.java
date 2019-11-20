package org.firstinspires.ftc.teamcode.SeansSpace.PurePursuit.OdometerBasedPurePursuit;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/*
 * Created by Sean Cardosi on 10/17/2019.
 * Class used for finding the robots current angle and location on the field.
 */
public class Odometry {

    DcMotor leftOdometer;
    DcMotor rightOdometer;

    public BNO055IMU gyro;
    public Orientation angles;

    Telemetry telemetry;

    double xLocation = 0.0;
    double yLocation = 0.0;
    double distance = 0.0;
    double changeRight = 0.0;
    double changeLeft = 0.0;
    double previousRightValue = 0.0;
    double previousLeftValue = 0.0;
    double COUNTS_PER_REV = 537.6;
    double EXTERNAL_GEAR_RATIO = 0.78125;     // This is < 1.0 if geared UP
    double WHEEL_DIAMETER_INCHES = 3.937;     // For figuring circumference
    double COUNTS_PER_INCH = ((COUNTS_PER_REV * EXTERNAL_GEAR_RATIO) / (WHEEL_DIAMETER_INCHES * 3.1415));


    public Odometry(HardwareMap hardwareMap, Telemetry tel) {
        //GYRO IS IN RADIANS FOR PURE PURSUIT

        gyro = hardwareMap.get(BNO055IMU.class, "imuINT");

        BNO055IMU.Parameters param = new BNO055IMU.Parameters();
        param.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        param.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        param.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        gyro.initialize(param);
        angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

        leftOdometer.setDirection(DcMotor.Direction.REVERSE);
        leftOdometer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightOdometer.setDirection(DcMotor.Direction.FORWARD);
        rightOdometer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        leftOdometer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightOdometer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftOdometer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightOdometer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftOdometer = hardwareMap.dcMotor.get("encL");
        rightOdometer = hardwareMap.dcMotor.get("encR");

        telemetry = tel;
    }

    public void init () {

        xLocation = 0.0;
        yLocation = 0.0;
        distance = 0.0;
        changeRight = 0.0;
        changeLeft = 0.0;
        previousRightValue = 0.0;
        previousLeftValue = 0.0;
    }

    /*
     * Updates the previous encoder values..
     */
    public void previousValues() {

        previousRightValue = rightOdometer.getCurrentPosition();
        previousLeftValue = leftOdometer.getCurrentPosition();
    }

    /*
     * Finds the robots (x,y) location using the previous encoder values and the robot heading.
     */
    public void updateLocation() {

        loop();

        changeRight = rightOdometer.getCurrentPosition() - previousRightValue;
        changeLeft = leftOdometer.getCurrentPosition() - previousLeftValue;

        distance = (changeRight + changeLeft) / 2;
        xLocation += (distance * Math.cos(getRawHeading())) / COUNTS_PER_INCH;
        yLocation += (distance * Math.sin(getRawHeading())) / COUNTS_PER_INCH;

        telemetry.addData("xLocation", xLocation);
        telemetry.addData("yLocation", yLocation);

        previousValues();
    }
    public double getRawHeading() {
        return angles.firstAngle;
    }
    public void loop() {
        angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }
}
