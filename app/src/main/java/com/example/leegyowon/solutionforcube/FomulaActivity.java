package com.example.leegyowon.solutionforcube;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class FomulaActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fomula_activity);

        tabLayout = (TabLayout) findViewById(R.id.subtabs);
        viewPager = (ViewPager) findViewById(R.id.subcontainer);

        pagerAdapter adapter = new pagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), "Step1");
        adapter.addFragment(new FragmentTwo(), "Step2");
        adapter.addFragment(new FragmentThree(), "Step3");
        adapter.addFragment(new FragmentFour(), "Step4");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        //finish();
    }

}