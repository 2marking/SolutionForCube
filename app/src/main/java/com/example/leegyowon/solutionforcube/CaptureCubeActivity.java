package com.example.leegyowon.solutionforcube;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import static com.example.leegyowon.solutionforcube.MainActivity.ALL_COLORS_INPUTTED;
import static com.example.leegyowon.solutionforcube.MainActivity.CAMERA_INPUT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_BACK;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_DOWN;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_FRONT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_LEFT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_RIGHT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_UP;
import static com.example.leegyowon.solutionforcube.MainActivity.INITIAL_INPUT_TYPE;
import static com.example.leegyowon.solutionforcube.MainActivity.MANUAL_COLOR_INPUT;

public class CaptureCubeActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, AdapterView.OnItemSelectedListener {

    CameraPreview preview;
    int centerX, centerY, startX, startY,
            cubeSideLength, cubieSideLength;
    TextView instructions;
    ImageView toggleInstructions;
    private Camera camera;
    private boolean hasCamera;
    private SurfaceView transparentView;
    private SurfaceHolder focusHolder;
    private int side;
    private boolean showingInstructions;
    private int animTime;

    public static Camera getCameraInstance(Context context) {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture_cube);
        hasCamera = checkCameraHardware(this);

        Spinner sideOptions = (Spinner) findViewById(R.id.side_options);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cube_capture_sides, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sideOptions.setAdapter(spinnerAdapter);
        sideOptions.setOnItemSelectedListener(this);

        transparentView = (SurfaceView) findViewById(R.id.grid_view);
        transparentView.setZOrderOnTop(true);
        focusHolder = transparentView.getHolder();
        focusHolder.setFormat(PixelFormat.TRANSPARENT);
        focusHolder.addCallback(this);
        focusHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        instructions = (TextView) findViewById(R.id.instructions);
        toggleInstructions = (ImageView) findViewById(R.id.toggle_instructions);
        showingInstructions = false;
        animTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        instructions.setOnClickListener(this);
        toggleInstructions.setOnClickListener(this);
        findViewById(R.id.instructions_layout).setOnClickListener(this);
        findViewById(R.id.take_picture).setOnClickListener(this);
        findViewById(R.id.solve_cube).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasCamera && camera == null) {
            camera = getCameraInstance(this);
            if (camera != null) {
                preview = new CameraPreview(this, camera);
                FrameLayout container = (FrameLayout) findViewById(R.id.camera_preview_container);
                container.addView(preview, 0);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            preview.getHolder().removeCallback(preview);
            preview.camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.instructions_layout
                || id == R.id.instructions
                || id == R.id.toggle_instructions) {
            if (showingInstructions) {
                instructions.animate()
                        .alpha(0f)
                        .setDuration(animTime)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                instructions.setVisibility(View.INVISIBLE);
                                transparentView.setVisibility(View.VISIBLE);
                            }
                        });

                toggleInstructions.animate()
                        .rotation(0)
                        .setDuration(animTime)
                        .setListener(null);

                showingInstructions = false;
            } else {
                instructions.setAlpha(0f);
                instructions.setVisibility(View.VISIBLE);

                instructions.animate()
                        .alpha(1f)
                        .setDuration(animTime)
                        .setListener(null);

                toggleInstructions.animate()
                        .rotation(180)
                        .setDuration(animTime)
                        .setListener(null);

                transparentView.setVisibility(View.INVISIBLE);
                showingInstructions = true;
            }

            return;
        }

        switch (id) {
            case R.id.take_picture:
                preview.saveCurrentBitmap(side);
                break;
            case R.id.solve_cube:
                char[][][] colors = preview.resolveColors();

                if(colors != null) {
                    Intent colorInputIntent = new Intent(getApplicationContext(),
                            MainActivity.class);

                    Bundle args = new Bundle();
                    args.putString(INITIAL_INPUT_TYPE, CAMERA_INPUT);

                    args.putCharArray(COLORS_INPUTTED_LEFT, packageSide(colors[0]));
                    args.putCharArray(COLORS_INPUTTED_RIGHT, packageSide(colors[4]));
                    args.putCharArray(COLORS_INPUTTED_UP, packageSide(colors[1]));
                    args.putCharArray(COLORS_INPUTTED_DOWN, packageSide(colors[5]));
                    args.putCharArray(COLORS_INPUTTED_FRONT, packageSide(colors[2]));
                    args.putCharArray(COLORS_INPUTTED_BACK, packageSide(colors[3]));

                    colorInputIntent.putExtra(ALL_COLORS_INPUTTED, args);
                    colorInputIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(colorInputIntent);
                    finish();
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        char side = adapterView.getItemAtPosition(pos).toString()
                .trim().charAt(0);

        switch (side) {
            case ('R'):
                this.side = 0;
                break;
            case ('Y'):
                this.side = 1;
                break;
            case ('G'):
                this.side = 2;
                break;
            case ('B'):
                this.side = 3;
                break;
            case ('O'):
                this.side = 4;
                break;
            default:
                this.side = 5; //case 'W'
                break;
        }

        if(preview != null){
            preview.setSide(this.side);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        strokePaint.setStrokeWidth(3);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        centerX = (metrics.widthPixels / 2);
        centerY = (metrics.heightPixels / 2);

        cubeSideLength = (metrics.widthPixels * 7 / 10);
        cubieSideLength = cubeSideLength / 3;
        startX = centerX - cubeSideLength / 2;
        startY = centerY - cubeSideLength / 2;

        for (int x = startX; x < startX + cubeSideLength; x += cubieSideLength) {
            for (int y = startY; y < startY + cubeSideLength; y += cubieSideLength) {
                canvas.drawRect(x, y,
                        x + cubieSideLength,
                        y + cubieSideLength,
                        strokePaint);
            }
        }
        preview.setGridPositions(centerX, centerY, startX, startY, cubieSideLength);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private char[] packageSide(char[][] colors) {
        char[] packageArray = new char[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                packageArray[i * 3 + j] = colors[i][j];
            }
        }
        return packageArray;
    }
}
