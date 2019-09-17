package com.example.leegyowon.solutionforcube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static android.R.attr.x;
import static android.content.ContentValues.TAG;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    protected Camera camera;
    Size previewSize;
    List<Size> supportedPreviewSizes;
    int camImageWidth, camImageHeight;
    private SurfaceHolder surfaceHolder;
    private byte[] data;
    private float[][][][] pixelHSVs;
    private Bitmap[] previewBitmaps;
    private int side;
    private int centerX, centerY, startX, startY, cubieSideLength;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        previewBitmaps = new Bitmap[6];
        pixelHSVs = new float[6][3][3][3];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (supportedPreviewSizes != null) {
            previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, height);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        previewBitmaps = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        camImageWidth = params.getPreviewSize().width;
        camImageHeight = params.getPreviewSize().height;

        this.data = data;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (surfaceHolder.getSurface() == null) {
            Log.d("Surface Changed", "FALSE");
            return;
        } else {
            Log.d("Surface Changed", "TRUE");
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        List<String> focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        Log.d(TAG, "width: " + previewSize.width);
        Log.d(TAG, "height: " + previewSize.height);

        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            camera.setPreviewCallback(this);


            invalidate();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void setSide(int side) {
        this.side = side;
    }

    public void setGridPositions(int... values) {
        if(values.length == 5) {
            centerX = values[0];
            centerY = values[1];
            startX = values[3];
            startY = values[2];
            cubieSideLength = values[4];
        }
    }

    public void saveCurrentBitmap(int side) {
        Bitmap bitmap = Bitmap.createBitmap(camImageWidth, camImageHeight, Bitmap.Config.ARGB_8888);
        Allocation bitmapData = renderScriptNV21ToRGBA888(
                getContext(),
                camImageWidth,
                camImageHeight,
                data);

        Log.d("Data null", "" + (data == null));
        Log.d("Bitmap null", "" + (bitmap == null));
        Log.d("Side", "" + side);

        bitmapData.copyTo(bitmap);

        Log.d("Width", "" + bitmap.getWidth());
        Log.d("Height", "" + bitmap.getHeight());

        int y = startY;
        int x = startX;

        for (int j = 0; j < 3; j++, startX += cubieSideLength) {
            for (int k = 0; k < 3; k++, startY += cubieSideLength) {
                float[] colorHSV = new float[3];
                Color.colorToHSV(bitmap
                                .getPixel((int) (startX + 0.5 * cubieSideLength),
                                        (int) (startY + 0.5 * cubieSideLength)),
                        colorHSV);
                pixelHSVs[side][j][k][0] = colorHSV[0];
                pixelHSVs[side][j][k][1] = colorHSV[1];
                pixelHSVs[side][j][k][2] = colorHSV[2];
            }
            startY = y;
        }

        startY = y;
        startX = x;

        Toast.makeText(getContext(), "캡처 성공! 다른 면을 촬영해주세요",
                Toast.LENGTH_SHORT).show();
    }

    public char[][][] resolveColors() {
        char[] indexColors = {'R', 'Y', 'G', 'B', 'O', 'W'};
        float[][] centerColors = new float[6][];

        for (int i = 0; i < centerColors.length; i++) {
            centerColors[i] = pixelHSVs[i][1][1];

            Log.d("Center Hue " + indexColors[i], "" + centerColors[i][0]);
            Log.d("Center Saturation " + indexColors[i], "" + centerColors[i][1]);
            Log.d("Center Value " + indexColors[i], "" + centerColors[i][2]);

        }

        char[][][] colors = new char[6][3][3];
        for (int i = 0; i < pixelHSVs.length; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    float[] colorHSV = pixelHSVs[i][j][k];

                    float hue = colorHSV[0];
                    char color = indexColors[i];
                    if (j == 1 && k == 1) {
                        colors[i][k][j] = color;
                        Log.d("" + i + ", " + j + ", " + k, " " + color);
                    } else if (colorHSV[1] < 0.3) {
                        colors[i][k][j] = 'W';
                        Log.d("" + i + ", " + j + ", " + k, " " + 'W');
                    } else {
                        int minDiff = (int) (Math.abs(hue - centerColors[i][0]));

                        for (int l = 0; l < centerColors.length; l++) {
                            int diff = (int) (Math.abs(hue - centerColors[l][0]));
                            if (diff < minDiff) {
                                minDiff = diff;
                                color = indexColors[l];
                            }
                        }

                        colors[i][k][j] = color;
                        Log.d("" + i + ", " + j + ", " + k, " " + color);
                    }
                }
            }
        }

        return colors;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private Allocation renderScriptNV21ToRGBA888(Context context, int width, int height, byte[] nv21) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));

        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

        in.copyFrom(nv21);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        return out;
    }
}
