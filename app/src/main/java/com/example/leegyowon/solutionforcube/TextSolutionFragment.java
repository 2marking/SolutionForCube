package com.example.leegyowon.solutionforcube;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_BACK;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_DOWN;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_FRONT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_LEFT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_RIGHT;
import static com.example.leegyowon.solutionforcube.MainActivity.COLORS_INPUTTED_UP;
import static com.example.leegyowon.solutionforcube.MainActivity.INITIAL_INPUT_TYPE;
import static com.example.leegyowon.solutionforcube.MainActivity.MANUAL_COLOR_INPUT;

public class TextSolutionFragment extends Fragment implements View.OnClickListener,
        EditScrambleDialog.EditScrambleDialogListener, SeekBar.OnSeekBarChangeListener {

    private View rootView;
    private CubeView cubeView;

    private String inputType;
    private char[][][] allColorsInputted = new char[6][3][3];

    public TextSolutionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_solution, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cubeView = rootView.findViewById(R.id.cube_view);
        rootView.findViewById(R.id.rewind).setOnClickListener(this);
        rootView.findViewById(R.id.skip_forward).setOnClickListener(this);
        SeekBar seekBar = rootView.findViewById(R.id.speed_adjuster);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(14);

        Bundle args = getArguments();
        inputType = (args != null) ? args.getString(INITIAL_INPUT_TYPE) : " ";
        if (inputType != null && inputType.equals(MANUAL_COLOR_INPUT)) {
            try {
                cubeView.resetScrambleByColorInputs(allColorsInputted);

                TextView scrambleView = rootView.findViewById(R.id.scramble_view);
                scrambleView.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "정확한 입력을 해주세요", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (inputType.equals(MANUAL_COLOR_INPUT)) {
            ((Activity) getContext()).getMenuInflater().inflate(R.menu.menu_color_solution, menu);
        } else {
            ((Activity) getContext()).getMenuInflater().inflate(R.menu.menu_text_solution, menu);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateMoves(String movesToPerform, String movesPerformed, String phase) {
        TextView toPerformView = rootView.findViewById(R.id.moves_to_perform);
        toPerformView.setText(movesToPerform);
        TextView performedView = rootView.findViewById(R.id.moves_performed);
        performedView.setText(movesPerformed);
        TextView phaseView = rootView.findViewById(R.id.phase_view);
        phaseView.setText(phase);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_forward:
                cubeView.skipToPhase(cubeView.getPhase() + 1);
                break;
            case R.id.rewind:
                cubeView.skipToPhase(cubeView.getPhase() - 1);
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String scramble) {
        TextView scrambleView = rootView.findViewById(R.id.scramble_view);
        scrambleView.setText(scramble);
        cubeView.resetScramble(scramble);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (cubeView.getAnimationStopped()) {
            cubeView.setSpeedMultiplier(1 + progress); //Avoid division by 0, min is 1
        } else {
            cubeView.stopAnimation();
            cubeView.startAnimation(1 + progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_random:
                TextView scrambleView = rootView.findViewById(R.id.scramble_view);
                scrambleView.setText(cubeView.randScramble());
                break;
            case R.id.action_reset:
                cubeView.resetCurrentScramble();
                break;
            case R.id.action_edit_scramble:
                if (inputType.equals(MANUAL_COLOR_INPUT)) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Bundle args = new Bundle();
                    args.putString(EditScrambleDialog.SCRAMBLE_TAG,
                            ((TextView) rootView.findViewById(R.id.scramble_view)).getText().toString());
                    EditScrambleDialog dialog = new EditScrambleDialog();
                    dialog.setArguments(args);
                    dialog.show(((AppCompatActivity) getContext())
                            .getSupportFragmentManager(), "edit scramble");
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null) {
            String initInputType = args.getString(INITIAL_INPUT_TYPE);
            if (initInputType != null && initInputType.equals(MANUAL_COLOR_INPUT)) {
                allColorsInputted[0] = unpackArrays(args.getCharArray(COLORS_INPUTTED_LEFT));
                allColorsInputted[1] = unpackArrays(args.getCharArray(COLORS_INPUTTED_UP));
                allColorsInputted[2] = unpackArrays(args.getCharArray(COLORS_INPUTTED_FRONT));
                allColorsInputted[3] = unpackArrays(args.getCharArray(COLORS_INPUTTED_BACK));
                allColorsInputted[4] = unpackArrays(args.getCharArray(COLORS_INPUTTED_RIGHT));
                allColorsInputted[5] = unpackArrays(args.getCharArray(COLORS_INPUTTED_DOWN));
            }
        }
    }

    private char[][] unpackArrays(char[] colorsArray) {
        char[][] unpackedArray = new char[3][3];
        for (int i = 0; i < colorsArray.length; i++) {
            unpackedArray[i / 3][i % 3] = colorsArray[i];
        }
        return unpackedArray;
    }
}
