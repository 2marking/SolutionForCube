package com.example.leegyowon.solutionforcube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CubeView extends View {

    public final static int DELAY = 1500;
    private final String DEFAULT_SCRAMBLE = getResources().getString(R.string.default_scramble);
    TextView toPerformView, performedView;
    private Timer frameTimer;
    private TimerTask animationTask;
    private boolean animationStopped;
    private int speedMultiplier;
    private Cube cube = new Cube();
    private boolean hasColorInput;
    private char[][][] colorsInputted;
    private String scramble = DEFAULT_SCRAMBLE,
            sunflower = "",
            whiteCross = "",
            whiteCorners = "",
            secondLayer = "",
            yellowCross = "",
            OLL = "",
            PLL = "";
    private String[] moveSet;
    private String movesToPerform = "",
            movesPerformed = "";
    private int phase = 0;
    private String phaseString;
    //Helps keep track of moves to perform, and allows for painting of moves
    private int movesIndex = 0;
    private Paint bluePaint;
    private Paint greenPaint;
    private Paint redPaint;
    private Paint orangePaint;
    private Paint whitePaint;
    private Paint yellowPaint;
    private Paint strokePaint;
    private int cubieSize;
    private int gap;

    public CubeView(Context context) {
        super(context);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        cube = new Cube();
        moveSet = new String[8];
        resetScramble(scramble);

        Context context = getContext();
        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(ContextCompat.getColor(context, R.color.cubeBlue));

        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(ContextCompat.getColor(context, R.color.cubeGreen));

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(ContextCompat.getColor(context, R.color.cubeRed));

        orangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setColor(ContextCompat.getColor(context, R.color.cubeOrange));

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(ContextCompat.getColor(context, R.color.cubeWhite));

        yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(ContextCompat.getColor(context, R.color.cubeYellow));

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(6);

        speedMultiplier = 1;
        cubieSize = (int) dpToPx(22);
        gap = (int) dpToPx(4);
        animationStopped = true;

        setOnTouchListener(new OnTouchListener() {

            private final int MIN_DRAG_DISTANCE = 15;
            private final int MIN_DRAG_TIME = 1000;
            private long initTime;
            private float initX;
            private float initY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        initTime = System.currentTimeMillis();
                        initX = motionEvent.getX();
                        initY = motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float finalX = motionEvent.getX(), finalY = motionEvent.getY();
                        long eventTime = System.currentTimeMillis() - initTime;
                        int distance = (int) pxToDp(distance(initX, initY, finalX, finalY));
                        if (eventTime >= MIN_DRAG_TIME || distance >= MIN_DRAG_DISTANCE) {
                            if (finalX > initX + dpToPx(MIN_DRAG_DISTANCE)) {
                                if (phase < 7) {
                                    performNextMove();
                                    invalidate();

                                    UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
                                    updateMoves.execute(getContext());
                                }
                            } else if (initX > finalX + dpToPx(MIN_DRAG_DISTANCE)) {
                                boolean flag = false;
                                int prevIndex = movesIndex;
                                while (movesIndex > 1 && !flag) {
                                    movesIndex--;
                                    if (movesToPerform.substring(movesIndex - 1, movesIndex).equals(" ")) {
                                        flag = !flag;
                                    }
                                }
                                if (movesIndex == 1) {
                                    movesIndex = 0;
                                }
                                movesPerformed = movesToPerform.substring(0, movesIndex);
                                if (movesPerformed.length() >= 35) {
                                    movesPerformed = movesPerformed.substring(movesPerformed.length() - 33);
                                }
                                cube.reverseMoves(movesToPerform.substring(movesIndex, prevIndex));
                                invalidate();


                                UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
                                updateMoves.execute(getContext());
                            }
                        } else {
                            if (animationStopped && phase < 7) {
                                startAnimation(speedMultiplier);
                            } else if (!animationStopped) {
                                stopAnimation();
                            }
                        }

                        break;
                }
                return true;
            }

            private float distance(float initX, float initY, float finalX, float finalY) {
                float rise = finalY - initY;
                float run = finalX - initX;
                return (float) Math.sqrt(run * run + rise * rise);
            }

            private float pxToDp(float px) {
                return px / getResources().getDisplayMetrics().density;
            }

        });

        UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
        updateMoves.execute(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xVal = gap;
        int yVal = (cubieSize + gap) * 3 + gap * 3;
        for (int y = 2; y >= 0; y--) {
            for (int z = 2; z >= 0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z - 2),
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * Math.abs(z - 2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(0, y, z, 'L')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z - 2),
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * Math.abs(z - 2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        xVal += (cubieSize + gap) * 3 + gap;
        for (int x = 0; x <= 2; x++) {
            for (int y = 2; y >= 0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, y, 0, 'U')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        yVal -= (cubieSize + gap) * 3 + gap;
        for (int x = 0; x <= 2; x++) {
            for (int z = 2; z >= 0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z - 2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, 2, z, 'B')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z - 2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        yVal += ((cubieSize + gap) * 3 + gap) * 2;
        for (int z = 0; z <= 2; z++) {
            for (int x = 0; x <= 2; x++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, 0, z, 'F')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        xVal += (cubieSize + gap) * 3 + gap;
        yVal -= (cubieSize + gap) * 3 + gap;
        for (int y = 2; y >= 0; y--) {
            for (int z = 0; z <= 2; z++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(2, y, z, 'R')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        xVal += (cubieSize + gap) * 3 + gap;
        for (int x = 2; x >= 0; x--) {
            for (int y = 2; y >= 0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x - 2),
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * Math.abs(x - 2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, y, 2, 'D')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x - 2),
                        yVal + (cubieSize + gap) * Math.abs(y - 2),
                        xVal + (cubieSize + gap) * Math.abs(x - 2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y - 2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

    }

    private Paint colorToPaint(char color) {
        switch (color) {
            case 'W':
                return whitePaint;
            case 'Y':
                return yellowPaint;
            case 'R':
                return redPaint;
            case 'O':
                return orangePaint;
            case 'B':
                return bluePaint;
            case 'G':
                return greenPaint;
        }
        return strokePaint;
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void resetScramble(String s) {
        scramble = s;
        cube = new Cube();
        cube.scramble(scramble);
        sunflower = cube.makeSunflower();
        whiteCross = cube.makeWhiteCross();
        whiteCorners = cube.finishWhiteLayer();
        secondLayer = cube.insertAllEdges();
        yellowCross = cube.makeYellowCross();
        OLL = cube.orientLastLayer();
        PLL = cube.permuteLastLayer();

        movesToPerform = sunflower;
        movesPerformed = "";

        cube = new Cube();
        cube.scramble(scramble);
        movesIndex = 0;
        phase = 0;
        phaseString = "Sunflower";
        invalidate();
        hasColorInput = false;

        moveSet[0] = sunflower;
        moveSet[1] = whiteCross;
        moveSet[2] = whiteCorners;
        moveSet[3] = secondLayer;
        moveSet[4] = yellowCross;
        moveSet[5] = OLL;
        moveSet[6] = PLL;
        moveSet[7] = " ";


        UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
        updateMoves.execute(getContext());
    }

    public void resetScrambleByColorInputs(char[][][] colorsInputted) {
        this.colorsInputted = colorsInputted;
        cube = new Cube();
        cube.setAllColors(colorsInputted);

        sunflower = cube.makeSunflower();
        whiteCross = cube.makeWhiteCross();
        whiteCorners = cube.finishWhiteLayer();
        secondLayer = cube.insertAllEdges();
        yellowCross = cube.makeYellowCross();
        OLL = cube.orientLastLayer();
        PLL = cube.permuteLastLayer();

        cube = new Cube();
        cube.setAllColors(colorsInputted);
        movesIndex = 0;
        phase = 0;
        phaseString = "Sunflower";
        hasColorInput = true;
        scramble = "";

        moveSet[0] = sunflower;
        moveSet[1] = whiteCross;
        moveSet[2] = whiteCorners;
        moveSet[3] = secondLayer;
        moveSet[4] = yellowCross;
        moveSet[5] = OLL;
        moveSet[6] = PLL;
        moveSet[7] = " ";

        resetCurrentScramble();
    }

    public void resetCurrentScramble() {
        if (!hasColorInput) {
            cube = new Cube();
            cube.scramble(scramble);

        } else {
            cube = new Cube();
            cube.setAllColors(colorsInputted);
        }

        movesToPerform = sunflower;
        movesPerformed = "";

        movesIndex = 0;
        phase = 0;
        phaseString = "Sunflower";

        invalidate();
        UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
        updateMoves.execute(getContext());
    }

    public String randScramble() {
        scramble = Cube.generateRandScramble();
        resetScramble(scramble);

        return scramble;
    }

    public void performNextMove() {
        updatePhase();

        while (movesIndex < movesToPerform.length() - 1 && movesToPerform.substring(movesIndex, movesIndex + 1).compareTo(" ") == 0) {
            movesIndex++;
        }
        if (movesToPerform.length() > 0 && movesToPerform.substring(movesIndex, movesIndex + 1) != " ") {
            if (movesIndex != movesToPerform.length() - 1) {
                if (movesToPerform.substring(movesIndex + 1, movesIndex + 2).compareTo("2") == 0) {
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex + 1));
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex + 1));
                    movesIndex++;
                } else if (movesToPerform.substring(movesIndex + 1, movesIndex + 2).compareTo("'") == 0) {
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex + 2));
                    movesIndex++;
                } else {
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex + 1));
                }
            } else {
                cube.turn(movesToPerform.substring(movesIndex, movesIndex + 1));
            }
        }
        movesIndex++;
        if (movesToPerform.length() > 0) {
            movesPerformed = movesToPerform.substring(0, movesIndex);
        }
        if (movesPerformed.length() >= 35) {
            movesPerformed = movesPerformed.substring(movesPerformed.length() - 30);
        }
    }

    public void updatePhase() {
        if (movesIndex >= movesToPerform.length()) {
            switch (phase) {
                case 0:
                    phaseString = "시작";
                    break;
                case 1:
                    phaseString = "밑면 십자가";
                    break;
                case 2:
                    phaseString = "2층 맞추기";
                    break;
                case 3:
                    phaseString = "윗면 십자가";
                    break;
                case 4:
                    phaseString = "윗면 모서리";
                    break;
                case 5:
                    phaseString = "완료";
                    break;
                case 6:
                    if (!animationStopped) {
                        phaseString = "Solved";
                        stopAnimation();
                    }
                    break;
            }
            if (phase < 7) {
                movesPerformed = "";
                phase++;
                movesIndex = 0;
                movesToPerform = moveSet[phase];
            }
        }
    }

    public int getPhase() {
        return phase;
    }

    public void skipToPhase(int skipTo) {
        UpdateUI skipMoves = new UpdateUI(UpdateUI.SKIP_PHASES, phase, skipTo);
        skipMoves.execute(getContext());

        UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
        updateMoves.execute(getContext());
    }

    public void stopAnimation() {
        if (!animationStopped) {
            frameTimer.cancel();
            animationStopped = true;
        }
    }

    public void setSpeedMultiplier(int speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public boolean getAnimationStopped() {
        return animationStopped;
    }

    public void startAnimation(int multiplySpeed) {
        speedMultiplier = multiplySpeed;

        if (animationStopped) {
            animationTask = new TimerTask() {
                synchronized public void run() {
                    performNextMove();
                    postInvalidate();

                    UpdateUI updateMoves = new UpdateUI(UpdateUI.UPDATE_MOVES_ON_UI);
                    updateMoves.execute(getContext());

                }
            };

            frameTimer = new Timer();
            frameTimer.scheduleAtFixedRate(animationTask,
                    TimeUnit.MILLISECONDS.toMillis(DELAY / speedMultiplier),
                    TimeUnit.MILLISECONDS.toMillis(DELAY / speedMultiplier));
            animationStopped = false;
        }
    }

    private class UpdateUI extends AsyncTask<Context, Void, MainActivity> {
        final static int UPDATE_MOVES_ON_UI = 1;
        final static int SKIP_PHASES = 2;
        private int taskType;
        private int currentPhase, skipTo;

        public UpdateUI(int task) {
            taskType = task;
        }

        public UpdateUI(int task, int curPhase, int skipToPhase) {
            taskType = task;
            currentPhase = curPhase;
            skipTo = skipToPhase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (taskType == SKIP_PHASES) {
                stopAnimation();
            }
        }

        @Override
        protected MainActivity doInBackground(Context... contexts) {
            MainActivity activity = (MainActivity) contexts[0];

            if (taskType == SKIP_PHASES) {
                if (skipTo > currentPhase && currentPhase < 7) {
                    cube.performMoves(movesToPerform.substring(movesIndex));
                    currentPhase++;
                    movesToPerform = moveSet[phase];

                    for (; currentPhase < skipTo; currentPhase++) {
                        cube.performMoves(moveSet[currentPhase]);
                    }

                    movesPerformed = "";
                    movesIndex = 0;
                    movesToPerform = moveSet[currentPhase];
                } else if (skipTo < currentPhase && currentPhase > 0) {
                    cube = new Cube();
                    cube.scramble(scramble);

                    for (currentPhase = 0; currentPhase < skipTo; currentPhase++) {
                        cube.performMoves(moveSet[currentPhase]);
                    }

                    movesPerformed = "";
                    movesIndex = 0;
                    movesToPerform = moveSet[currentPhase];
                }
                phase = currentPhase;
            }

            return activity;
        }

        @Override
        protected void onPostExecute(MainActivity activity) {

            if (taskType == SKIP_PHASES) {
                postInvalidate();
            } else if (taskType == UPDATE_MOVES_ON_UI) {
                switch (phase) {
                    case 0:
                        phaseString = "시작";
                        break;
                    case 1:
                        phaseString = "밑면 십자가";
                        break;
                    case 2:
                        phaseString = "밑면 맞추기";
                        break;
                    case 3:
                        phaseString = "2층 맞추기";
                        break;
                    case 4:
                        phaseString = "윗면 십자가";
                        break;
                    case 5:
                        phaseString = "윗면 모서리";
                        break;
                    case 6:
                        phaseString = "윗면 귀퉁이";
                        break;
                    case 7:
                        phaseString = "완료";
                        break;
                }

                TextSolutionFragment fragment = (TextSolutionFragment) ((AppCompatActivity) getContext())
                        .getSupportFragmentManager().findFragmentById(R.id.container);

                if (fragment != null && movesToPerform.length() > 0) {
                    fragment.updateMoves(movesToPerform.substring(movesIndex).trim(),
                            movesPerformed.trim(), phaseString);
                }

                super.onPostExecute(activity);
            }
        }
    }
}


