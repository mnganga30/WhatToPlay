package com.j_hawk.whattoplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.Toast;

import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;
import com.j_hawk.whattoplay.data.OnlineGame;
import com.j_hawk.whattoplay.services.FindGameByID;
import com.j_hawk.whattoplay.services.FindHotItems;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;

/**
 * This page is the activity for navigating the home screen. Includes main menu and Game Search screen.
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 */
public class HomePageActivity extends AppCompatActivity {


    private PersonalInfoPagerAdapter myPersonalInfoPagerAdapter;
    private static SearchPagerAdapter mySearchPagerAdapter;
    private HomePagerAdapter myHomePagerAdapter;
    private ViewPager mViewPager;
    private static String fragmentDisplayed;
    private static final String SEARCH_FRAG = "SearchPage";
    private static final String HOME_FRAG = "HomePage";
    private static final String PERSONAL_FRAG = "PersonalPage";
    private static BottomNavigationView navigation;
    private Toast statusMessage;



    /**
     * Overrides back button functionality. Navigates back to Home screen instead of closing app.
     */
    @Override
    public void onBackPressed() {
        if (fragmentDisplayed == null) {
            super.onBackPressed();
        } else if (fragmentDisplayed.equals(SEARCH_FRAG) || fragmentDisplayed.equals(HOME_FRAG)) {
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(myPersonalInfoPagerAdapter);
            fragmentDisplayed = PERSONAL_FRAG;
            navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else if (fragmentDisplayed.equals(PERSONAL_FRAG)) {
            super.onBackPressed();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recommendations:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(myHomePagerAdapter);
                    fragmentDisplayed = HOME_FRAG;
                    return true;
                case R.id.navigation_search:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mySearchPagerAdapter);
                    fragmentDisplayed = SEARCH_FRAG;
                    return true;
                case R.id.navigation_home:
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(myPersonalInfoPagerAdapter);
                    fragmentDisplayed = PERSONAL_FRAG;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mySearchPagerAdapter =
                new SearchPagerAdapter(getSupportFragmentManager());
        myPersonalInfoPagerAdapter =
                new PersonalInfoPagerAdapter(getSupportFragmentManager());
        myHomePagerAdapter =
                new HomePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(myPersonalInfoPagerAdapter);

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }
    /**
     * FragmentStatePagerAdapter for PersonalInfoPager
     */
    public static class PersonalInfoPagerAdapter extends FragmentStatePagerAdapter {

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

    /**
     * Fragment for collection management and searching for games
     */
    public static class PersonalInfoFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        private Button manageCollection;
        private Button searchCollection;
        private Button removeMode;
        private Button addMode;
        private Button cancel;
        private Button importCollection;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(
                    R.layout.personalinfopage, container, false);
            ImageView background = (ImageView) rootView.findViewById(R.id.backgroundimg);
            background.setImageResource(R.drawable.background);
            manageCollection = (Button) rootView.findViewById(R.id.manageCollection);
            searchCollection = (Button) rootView.findViewById(R.id.searchCollection);
            addMode = (Button) rootView.findViewById(R.id.addMode);
            removeMode = (Button) rootView.findViewById(R.id.removeMode);
            cancel = (Button) rootView.findViewById(R.id.cancelManageCollection);
            importCollection = (Button) rootView.findViewById(R.id.importCollectionBtn);
            removeMode.setVisibility(View.GONE);
            addMode.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            importCollection.setVisibility(View.GONE);

            manageCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manageCollection.setVisibility(View.GONE);
                    searchCollection.setVisibility(View.GONE);
                    addMode.setVisibility(View.VISIBLE);
                    removeMode.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    importCollection.setVisibility(View.VISIBLE);
                }
            });

            searchCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigation.getMenu().findItem(R.id.navigation_search).setChecked(true);
                    ViewPager vp = (ViewPager) getActivity().findViewById(R.id.pager);
                    vp.setAdapter(mySearchPagerAdapter);
                    fragmentDisplayed = SEARCH_FRAG;
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manageCollection.setVisibility(View.VISIBLE);
                    searchCollection.setVisibility(View.VISIBLE);
                    addMode.setVisibility(View.GONE);
                    removeMode.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    importCollection.setVisibility(View.GONE);
                }
            });

            addMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), AddToCollection.class);
                    startActivity(intent);
                }
            });

            removeMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getContext(), removeGameActivity.class);
                    startActivity(mIntent);
                }
            });

            importCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ImportCollection.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    getContext().startActivity(intent);
                }
            });

            Bundle args = getArguments();
            return rootView;
        }

    }

    /**
     * FragmentStatePagerAdapter for SearchPager
     */
    public static class SearchPagerAdapter extends FragmentStatePagerAdapter {
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

    /**
     * Fragment for search page
     */
    public static class SearchFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private final List<Game> list = new ArrayList<>();
        private ItemAdapter msItemAdapter;
        private ListView lySearch = null;
        private DBHelper dbHelper;
        @Override
        public View onCreateView(final LayoutInflater inflater,
                                 ViewGroup container, final Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.searchpage, container, false);
            dbHelper = new DBHelper(rootView.getContext());
            lySearch = (ListView) rootView.findViewById(R.id.searchingresult);
            ImageButton search = (ImageButton) rootView.findViewById(R.id.searchbutton);
            final EditText input = (EditText) rootView.findViewById(R.id.editText);
            Button filter = (Button) rootView.findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getContext(), filterpage.class);
                    startActivity(mIntent);
                }
            });
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // need to clear game list after all this be show or brfore next searching
                    ArrayList<Game> games = dbHelper.getAllGames();
                    list.clear();
                    if (input.getInputType() == TYPE_CLASS_TEXT) {
                        for (int i = 0; i < games.size(); i++) {
                            if (checkName(games.get(i).getName(), input.getText().toString())) {
                                Game result = games.get(i);
                                list.add(result);
                                msItemAdapter = new ItemAdapter(inflater, list);
                                lySearch.setAdapter(msItemAdapter);
                            }
                        }
                    } else {
                        if(!input.getText().toString().isEmpty()){
                        for (int i = 0; i < games.size(); i++) {
                            if (games.get(i).getMinPlayers() <= Integer.parseInt(input.getText().toString()) &&
                                    games.get(i).getMaxPlayers() >= Integer.parseInt(input.getText().toString())) {
                                Game result = games.get(i);
                                list.add(result);
                            }
                        }
                        msItemAdapter = new ItemAdapter(inflater, list);
                        lySearch.setAdapter(msItemAdapter);
                    }}
                }
            });
            Bundle args = getArguments();
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            lySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Game currentItem = msItemAdapter.getItem(position);
                    // (...)

                    Intent intent = new Intent(getContext(), ViewGame.class);
                    intent.putExtra("Id", currentItem.getId());

                    startActivity(intent);
                }
            });
        }

        public boolean checkName(String s1, String s2) {
            if (s1.length() != s2.length()) {
                return (false);
            } else {
                for(int i = 0; i < s1.length(); i++){
                    if(s1.charAt(i) != s2.charAt(i)){return false;}
                }return true;}}





        }

    /**
     * FragmentStatePagerAdapter for HomePager
     */
    public static class HomePagerAdapter extends FragmentStatePagerAdapter {
        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            try {
                fragment = new HomepageFragment();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    /**
     * Fragment for recommendations page. Not functional yet
     */
    public static class HomepageFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        private final List<OnlineGame> list = new FindHotItems().execute().get();
        private ItemAdapter2 mItemAdapter;
        private ListView lyHome = null;
        private DBHelper dbHelper;
        private Toast statusMessage;



        public HomepageFragment() throws ExecutionException, InterruptedException {
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            dbHelper = new DBHelper(getActivity());
            statusMessage = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

            //            int id, String name, int minPlayers, int maxPlayers, int year, int playTime
            View rootView = inflater.inflate(
                    R.layout.homepage_dailyrecommand, container, false);
            mItemAdapter = new ItemAdapter2(inflater, list);
            lyHome = (ListView) rootView.findViewById(R.id.recommlist);
            Bundle args = getArguments();
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            lyHome.setAdapter(mItemAdapter);
            lyHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  final OnlineGame currentItem = mItemAdapter.getItem(position);
                    // (...)

                    dialog.setTitle("Add "+ currentItem.getName()+" To Collection")
                            .setMessage("Would you like to add " + currentItem.getName() + " to your collection?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        addHotGameToCollection(currentItem.getId());
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    //.setIcon(android.R.drawable.ic_dialog_alert);
                    final AlertDialog alertDialog = dialog.show();
                    if (!alertDialog.isShowing()) alertDialog.dismiss();
                }
            });
        }

        /**
         * Takes a game from private ArrayList and adds it to collection
         * @param id Integer id for game that is to be added to collection
         */
        public  void addHotGameToCollection(int id) throws ExecutionException, InterruptedException {
            Game newGame = new FindGameByID().execute(id).get();
            Log.i("test", newGame.toString());
            long result = dbHelper.addGame(newGame);
            if (result != -1) {
                statusMessage.setText("Game Succesfully Added To Collection!");
            } else {
                statusMessage.setText("ERROR: Game is already in your collection");
            }
            statusMessage.show();
        }

    }

    /**
     * BaseAdapter for Item
     */
    public static class ItemAdapter extends BaseAdapter {
        private List<Game> mitem;
        private LayoutInflater minflater;
        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(String... URL) {
                String imageURL = URL[0];
                Bitmap bitmap = null;
                try {
                    InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                //image.setImageBitmap(result);
            }
        }

        /**
         * Constructor for ItemAdapter
         * @param inflater LayoutInflater
         * @param items List<Game>
         */
        public ItemAdapter(LayoutInflater inflater, List<Game> items) {
            mitem = items;
            minflater = inflater;
        }


        @Override
        public int getCount() {
            return mitem.size();
        }

        @Override
        public Game getItem(int i) {
            return mitem.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInformation = minflater.inflate(R.layout.listview_item, null);
            Game item = mitem.get(i);
            TextView title = viewInformation.findViewById(R.id.itemTitle);
            TextView max = viewInformation.findViewById(R.id.atenddessnummax);
            TextView min = viewInformation.findViewById(R.id.atenddessnummin);
            TextView time = viewInformation.findViewById(R.id.time);
            TextView year = viewInformation.findViewById(R.id.year);
            ImageView image = viewInformation.findViewById(R.id.gamepic);
            title.setText(item.getName());
            min.setText("Min Play: " + Integer.toString(item.getMinPlayers()));
            max.setText("Max Play: " + Integer.toString(item.getMaxPlayers()));
            year.setText("[" + Integer.toString(item.getYear()) + "]");
            if(item.getPlayTime() == 0){
            time.setText("Not Sure Game time");
            }else{time.setText(Integer.toString(item.getPlayTime()) + " mins");}
            try {
                image.setImageBitmap(new DownloadImage().execute(item.getThumbnail()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return viewInformation;
        }
    }



    public static class ItemAdapter2 extends BaseAdapter {
        private List<OnlineGame> mitem;
        private LayoutInflater minflater;
        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(String... URL) {
                String imageURL = URL[0];
                Bitmap bitmap = null;
                try {
                    InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                //image.setImageBitmap(result);
            }
        }

        /**
         * Constructor for ItemAdapter
         * @param inflater LayoutInflater
         * @param items List<OnlineGame>
         */
        public ItemAdapter2(LayoutInflater inflater, List<OnlineGame> items) {
            mitem = items;
            minflater = inflater;
        }


        @Override
        public int getCount() {
            return mitem.size();
        }


        public String getName(int i) {
            return mitem.get(i).getName();
        }

        @Override
        public OnlineGame getItem(int i) {
            return mitem.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInformation = minflater.inflate(R.layout.hotview_item, null);
            OnlineGame item = mitem.get(i);
            TextView title = viewInformation.findViewById(R.id.hotListTitle);
            TextView year = viewInformation.findViewById(R.id.hotListYear);
            ImageView image = viewInformation.findViewById(R.id.hotListImage);
            title.setText(item.getName());
            year.setText("[" + Integer.toString(item.getYear()) + "]");


            try {
                image.setImageBitmap(new DownloadImage().execute(item.getThumbnail()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return viewInformation;
        }
    }




}


