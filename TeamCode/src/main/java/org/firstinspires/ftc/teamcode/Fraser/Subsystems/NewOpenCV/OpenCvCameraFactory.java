package org.firstinspires.ftc.teamcode.Fraser.Subsystems.NewOpenCV;

import android.content.Context;
import android.support.annotation.IdRes;

import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeManager;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvInternalCamera;

/**
 * Created by Sean Cardosi on 2020-01-17.
 */

public abstract class OpenCvCameraFactory {
    static OpenCvCameraFactory theInstance;

public static OpenCvCameraFactory getInstance()
{
    return theInstance;
}

    /*
     * Internal
     */
    public abstract OpenCvInternalCamera createInternalCamera(OpenCvInternalCamera.CameraDirection direction);
    public abstract OpenCvInternalCamera createInternalCamera(OpenCvInternalCamera.CameraDirection direction, @IdRes int viewportContainerId);

    /*
     * Webcam
     */
    public abstract OpenCvCamera createWebcam(WebcamName cameraName);
    public abstract OpenCvCamera createWebcam(WebcamName cameraName, @IdRes int viewportContainerId);

    public enum ViewportSplitMethod
    {
        VERTICALLY,
        HORIZONTALLY
    }

    /*
     * Viewport containers
     */
    public abstract @IdRes int[] splitLayoutForMultipleViewports(@IdRes int containerId, int numViewports, ViewportSplitMethod viewportSplitMethod);
}