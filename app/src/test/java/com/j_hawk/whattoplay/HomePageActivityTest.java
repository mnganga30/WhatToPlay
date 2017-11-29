package com.j_hawk.whattoplay;


import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.Toolbar;

import org.junit.After;
import org.junit.Before;
import com.j_hawk.whattoplay.HomePageActivity;
import static org.junit.Assert.*;

/**
 * Created by simonyang on 11/19/17.
 */
public class HomePageActivityTest extends HomePageActivity {
    private HomePageActivity mActivity;
    private Button button0;
    private Toolbar toolbar;
    private ViewPager viewPager;


    @Before
    public void setUp() throws Exception {
       // button0 = mActivity.findViewById(R.id.);
        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar2);
        viewPager = (ViewPager) mActivity.findViewById(R.id.pager);
    }

    @After
    public void tearDown() throws Exception {

    }

}