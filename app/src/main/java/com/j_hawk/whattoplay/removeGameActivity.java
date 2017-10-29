package com.j_hawk.whattoplay;



        import android.media.Image;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.app.Activity;
        import android.widget.ListView;


        import com.j_hawk.whattoplay.data.DBHelper;

        import java.util.ArrayList;
        import java.util.List;

        import com.j_hawk.whattoplay.data.Game;
/**
 * Created by Jian on 10/25/17.
 */




/**
 * This function setup up the adapeter and the ListView of Games
 */
public class removeGameActivity extends Activity{
    private DBHelper dbHelper;
    private Toast statusMessage;
    private ListView viewGameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_game);
        dbHelper = new DBHelper(getApplicationContext());
        statusMessage = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        List<Game> allGames;
        //setupGameView();
      //  allGames=dbHelper.getAllGames();
        allGames = new ArrayList<Game>();
        Game first = new Game(12,"jijiboom",1,5,2011,50);
        Game second = new Game(213,"yang yiju",1,2,3,4);
        allGames.add(first);
        allGames.add(second);
        viewGameList=(ListView) findViewById(R.id.gameList);
        LayoutInflater inflater = getLayoutInflater();
        gameAdapter mgameAdapter=new gameAdapter(inflater,allGames);
        viewGameList.setAdapter(mgameAdapter);
    }
    private void setupGameView()
    {
//        Game first = new Game(12,"jijiboom",1,5,2011,50);
//        allGames.add(first);
//        viewGameList=(ListView) findViewById(R.id.gameList);
//        LayoutInflater inflater = getLayoutInflater();
//        gameAdapter mgameAdapter=new gameAdapter(inflater,allGames);
//        viewGameList.setAdapter(mgameAdapter);
//
//        viewGameList.setOnItemClickListener(new AdapterView.OnClickListener()) {
//
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
//        {
//
//
//
//        }
    //}

    }

    class gameAdapter extends BaseAdapter {
        private List<Game> mitem;
        private LayoutInflater mInflater;
        /**
         *This is a Constructor of an Adapter of game ListView
         * @param  'inflater' is the LayoutInflater of ViewAcitvity, ArrayList<Game> is a list to store all the games
         */
        public gameAdapter(LayoutInflater inflater, List<Game> items) {
            mitem = items;
            mInflater = inflater;
        }

        @Override
        public int getCount() {
            return mitem.size();
        }

        @Override
        public Object getItem(int i) {
            return mitem.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        /**
         *
         * @param  i is the position of ListView
         * @return what view looks like
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInfromation = mInflater.inflate(R.layout.remove_game_item, null);
            Game Item = mitem.get(i);
            TextView name = viewInfromation.findViewById(R.id.gameToRemove);
            ImageButton delete = viewInfromation.findViewById(R.id.deletebutton);
            name.setText(Item.getName());
            delete.setTag(i);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View view) {
                    int position = (int) view.getTag();
                    mitem.remove(position);
                    notifyDataSetChanged();
                    }
                });
            return viewInfromation;
        }
    }
}


