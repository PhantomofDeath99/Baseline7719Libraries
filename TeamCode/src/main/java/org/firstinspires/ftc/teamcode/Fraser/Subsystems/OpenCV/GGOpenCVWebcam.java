package org.firstinspires.ftc.teamcode.Fraser.Subsystems.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Rect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

/**
 * Created by Sean Cardosi on 2020-01-15.
 */
public class GGOpenCVWebcam implements VisionSystem {

    public static final Rect CAMERA_RECT = new Rect(0, 0, 320, 240);

    public OpenCvCamera camera;
    public GGSkystoneDetector detector;
    HardwareMap hardwareMap;
    WebcamName webcam;
    LinearOpMode linearOpMode;
    Telemetry telemetry;

    @Override
    public void startLook(VisionSystem.TargetType targetType) {
        switch (targetType) {
            case SKYSTONE: {
                detector.useDefaults();
                break;
            }
        }
        startCamera();
    }

    @Override
    public void stopLook() {
        camera.stopStreaming();
        camera.closeCameraDevice();
    }

    @Override
    public boolean found() {
        return detector.isDetected();
    }

    public GGOpenCVWebcam(Telemetry tel, HardwareMap hardwareMap, LinearOpMode opMode) {
        detector = new GGSkystoneDetector();
        webcam = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = new OpenCvWebcam(webcam, cameraMonitorViewId);
        camera.openCameraDevice();
        linearOpMode = opMode;
        telemetry = tel;
    }

    public void startCamera() {
        camera.setPipeline(detector);
        camera.startStreaming(CAMERA_RECT.width, CAMERA_RECT.height, OpenCvCameraRotation.SIDEWAYS_LEFT);
    }

    /**
     * Scans for a Skystone using the webcam.
     * Should be called in the main method of a LinearOpMode.
     * Will usually be used for testing purposes.
     */
    public void scanMain() {
        startLook(TargetType.SKYSTONE);
        while (linearOpMode.opModeIsActive()) {
            if (found()) {
                telemetry.addLine("Skystone Found!");
                double x = detector.foundRectangle().x;
                double y = detector.foundRectangle().y;
                telemetry.addData("(x,y)", "%f,%f", x, y);
                telemetry.addData("Position (x): ", detector.foundRectangle().x);
            } else {
                telemetry.addLine("Skystone Not Found.");
            }
            telemetry.update();
        }
        stopLook();
    }

    /**
     * Scans for a Skystone using the webcam.
     * Should be called in the init method of a LinearOpMode.
     * Will usually be used in autonomous.
     */
    public void scanInit() {
        startLook(TargetType.SKYSTONE);
        while (!linearOpMode.isStarted()) {
            if (found()) {
                telemetry.addLine("Skystone Found!");
                double x = detector.foundRectangle().x;
                double y = detector.foundRectangle().y;
                telemetry.addData("(x,y)", "%f,%f", x, y);
                telemetry.addData("Position (x): ", detector.foundRectangle().x);
            } else {
                telemetry.addLine("Skystone Not Found.");
            }
            telemetry.update();
        }
        stopLook();
    }
}
