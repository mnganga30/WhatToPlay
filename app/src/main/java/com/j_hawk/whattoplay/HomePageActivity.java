package com.j_hawk.whattoplay;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.j_hawk.whattoplay.data.Game;

import java.util.ArrayList;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;

public class HomePageActivity extends AppCompatActivity {


    private PersonalInfoPagerAdapter myPersonalInfoPagerAdapter;
    private SearchPagerAdapter mySearchPagerAdapter;
    private HomePagerAdapter myHomePagerAdapter;
    ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(myHomePagerAdapter);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mySearchPagerAdapter);
                     return true;
                case R.id.navigation_notifications:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(myPersonalInfoPagerAdapter);
                    return true;
            }
            return false;
        }
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            myHomePagerAdapter =
                    new HomePagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(myHomePagerAdapter);
            mySearchPagerAdapter =
                            new SearchPagerAdapter(getSupportFragmentManager());
            myPersonalInfoPagerAdapter =
                    new PersonalInfoPagerAdapter(getSupportFragmentManager());
        }


    public class PersonalInfoPagerAdapter extends FragmentStatePagerAdapter {
        public PersonalInfoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new PersonalInfoFragment();
            Bundle args = new Bundle();
            args.putInt(PersonalInfoFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class PersonalInfoFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.personalinfopage, container, false);
            ImageView background = (ImageView) rootView.findViewById(R.id.backgroundimg);
            background.setImageResource(R.drawable.background);
            Button removeMode = (Button) rootView.findViewById(R.id.removemode);
            Button checkinglist = (Button) rootView.findViewById(R.id.checklist);
            removeMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getContext(),removeGameActivity.class);
                    startActivity(mIntent);
                }
            });

            checkinglist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            Bundle args = getArguments();
            return rootView;
        }

    }
    public class SearchPagerAdapter extends FragmentStatePagerAdapter {
        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new SearchFragment();
            Bundle args = new Bundle();
            args.putInt(SearchFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class SearchFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private final List<Game> list = new ArrayList<>();
        private ItemAdapter msItemAdapter;
        private ListView lySearch = null;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.searchpage, container, false);
            ImageButton search = (ImageButton) rootView.findViewById(R.id.searchbutton);
            final EditText input = (EditText) rootView.findViewById(R.id.editText);

            Spinner spinner = (Spinner) rootView.findViewById(R.id.searchmode);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mode, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Two different modes

                    //search by name
                    if(position == 0){
                        input.setText("");
                        input.setInputType(TYPE_CLASS_TEXT);
                    }
                    //search by number of players
                    if(position == 1){
                        input.setText("");
                        input.setInputType(TYPE_CLASS_NUMBER);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //can be delete, cause they just used for testing...........................
            Game first = new Game(10,"Demo1",1,5,2010,5);
            Game second = new Game(10,"Demo2",1,5,2010,5);
            Game third = new Game(10,"used for testing",1,5,2010,5);
            Game forth = new Game(10,"used for testing",1,5,2010,5);
            Game fifth = new Game(10,"used for testing",1,5,2010,5);
            Game sixth = new Game(10,"used for testing",1,5,2010,5);
            Game seventh = new Game(10,"used for testing",1,5,2010,5);
            Game eighth = new Game(10,"used for testing",1,5,2010,5);
            Game ninth = new Game(10,"used for testing",1,5,2010,5);
            Game tenth = new Game(10,"used for testing",1,5,2010,5);
            list.add(first);
            list.add(second);
            list.add(third);
            list.add(forth);
            list.add(fifth);
            list.add(sixth);
            list.add(seventh);
            list.add(eighth);
            list.add(ninth);
            list.add(tenth);
            //............................................................................
            msItemAdapter = new ItemAdapter(inflater,list);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lySearch.setAdapter(msItemAdapter);
                }
            });
            lySearch = (ListView) rootView.findViewById(R.id.searchingresult);
            Bundle args = getArguments();
            return rootView;
        }
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            lySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Item currentItem = adapter.getItem(position);
                    // (...)
                }
            });
        }

    }

    public class HomePagerAdapter extends FragmentStatePagerAdapter {
        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new HomepageFragment();
            Bundle args = new Bundle();
            args.putInt(HomepageFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class HomepageFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private final List<Game> list = new ArrayList<>();
        private ItemAdapter mItemAdapter;
        private ListView lyHome = null;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            //            int id, String name, int minPlayers, int maxPlayers, int year, int playTime
            View rootView = inflater.inflate(
                    R.layout.homepage_dailyrecommand, container, false);
            Game first = new Game(10,"Game Name",1,5,2010,5);
            Game second = new Game(10,"used for testing",1,5,2010,5);
            Game third = new Game(10,"used for testing",1,5,2010,5);
            Game forth = new Game(10,"used for testing",1,5,2010,5);
            Game fifth = new Game(10,"used for testing",1,5,2010,5);
            Game sixth = new Game(10,"used for testing",1,5,2010,5);
            Game seventh = new Game(10,"used for testing",1,5,2010,5);
            Game eighth = new Game(10,"used for testing",1,5,2010,5);
            Game ninth = new Game(10,"used for testing",1,5,2010,5);
            Game tenth = new Game(10,"used for testing",1,5,2010,5);
            list.add(first);
            list.add(second);
            list.add(third);
            list.add(forth);
            list.add(fifth);
            list.add(sixth);
            list.add(seventh);
            list.add(eighth);
            list.add(ninth);
            list.add(tenth);
            mItemAdapter = new ItemAdapter(inflater,list);
            lyHome = (ListView) rootView.findViewById(R.id.recommlist);
            Bundle args = getArguments();
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            lyHome.setAdapter(mItemAdapter);
            lyHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // Item currentItem = adapter.getItem(position);
                    // (...)
                }
            });
        }

    }

    public static class ItemAdapter extends BaseAdapter {
        private List<Game> mitem;
        private LayoutInflater minflater;
        public ItemAdapter(LayoutInflater inflater, List<Game> items){
            mitem = items;
            minflater = inflater;
        }

        @Override
        public int getCount() {
            return mitem.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInformation = minflater.inflate(R.layout.listview_item,null);
            Game item = mitem.get(i);
            TextView title = viewInformation.findViewById(R.id.itemTitle);
            TextView max = viewInformation.findViewById(R.id.atenddessnummax);
            TextView min = viewInformation.findViewById(R.id.atenddessnummin);
            TextView time = viewInformation.findViewById(R.id.time);
            TextView year = viewInformation.findViewById(R.id.year);
            ImageView image = viewInformation.findViewById(R.id.gamepic);
            title.setText(item.getName());
            min.setText("Min Play: "+Integer.toString(item.getMinPlayers()));
            max.setText("Max Play: "+Integer.toString(item.getMaxPlayers()));
            year.setText("["+Integer.toString(item.getYear())+"]");
            time.setText(Integer.toString(item.getPlayTime())+" mins");
            //image.setImageResource(item.getImageId());
            return viewInformation;
        }
    }
}

