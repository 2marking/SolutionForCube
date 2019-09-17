package com.example.leegyowon.solutionforcube;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import static android.R.attr.inputType;
import static java.util.ResourceBundle.getBundle;

public class MainActivity extends AppCompatActivity {
    public static final String INITIAL_INPUT_TYPE = "initial input type";
    public static final String MANUAL_COLOR_INPUT = "manual color input";
    public static final String CAMERA_INPUT = "camera input";
    public static final String ALL_COLORS_INPUTTED = "all colors inputted";
    public static final String COLORS_INPUTTED_LEFT = "colors inputted left";
    public static final String COLORS_INPUTTED_UP = "colors inputted up";
    public static final String COLORS_INPUTTED_FRONT = "colors inputted front";
    public static final String COLORS_INPUTTED_BACK = "colors inputted back";
    public static final String COLORS_INPUTTED_RIGHT = "colors inputted right";
    public static final String COLORS_INPUTTED_DOWN = "colors inputted down";

    private final String TEXT_SCRAMBLE = "text scramble";
    private final String COLOR_INPUT = "color input";
    private String currentMode = TEXT_SCRAMBLE;
    private TextSolutionFragment solutionFragment;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView drawerList;
    private String[] navDrawerTitles;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView icon = new ImageView(this); // Create an icon

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        actionButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.menu_button));
        actionButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.menu_image));
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.menu_button));

        FloatingActionButton.LayoutParams params1=new FloatingActionButton.LayoutParams(140,140);
        itemBuilder.setLayoutParams(params1);

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.camera));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureCubeActivity.class);
                //Intent intent = new Intent(getApplicationContext(), CaptureCubeActivity.class);
                startActivity(intent);
            }
        });

        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.paintbrush));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ColorInputFragment(), "Color Input Fragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackfomula));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon).build();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FomulaActivity.class);
                startActivity(intent);
            }
        });

        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.setting2));
        SubActionButton button4 = itemBuilder.setContentView(itemIcon).build();
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/sf_A8bok8-A"));
                startActivity(intent);
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(actionButton)
                .build();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            Bundle args = (extras != null) ? extras.getBundle(ALL_COLORS_INPUTTED) : null;
            String inputType = (args != null) ? args.getString(INITIAL_INPUT_TYPE) : " ";

            if (inputType != null && inputType.equals(CAMERA_INPUT)) {
                ColorInputFragment colorInputFragment = new ColorInputFragment();
                if(args != null) colorInputFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, colorInputFragment, "색상 입력")
                        .addToBackStack(null)
                        .commit();
            } else {
                solutionFragment = new TextSolutionFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, solutionFragment, "솔루션")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
