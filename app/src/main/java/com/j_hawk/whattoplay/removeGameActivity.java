package com.j_hawk.whattoplay;



        import android.media.Image;
        import android.os.AsyncTask;
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

        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.ExecutionException;

        import com.j_hawk.whattoplay.data.Game;
        import com.j_hawk.whattoplay.services.ParserGame;
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
        ArrayList<Game> allGames = dbHelper.getAllGames();
        viewGameList=(ListView) findViewById(R.id.gameList);
        LayoutInflater inflater = getLayoutInflater();
        gameAdapter mgameAdapter=new gameAdapter(inflater,allGames);
        viewGameList.setAdapter(mgameAdapter);
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
            final Game Item = mitem.get(i);
            TextView name = viewInfromation.findViewById(R.id.gameToRemove);
            ImageButton delete = viewInfromation.findViewById(R.id.deletebutton);
            name.setText(Item.getName());
            delete.setTag(i);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View view) {
                    int position = (int) view.getTag();
                    dbHelper.removeGame(Item.getId());
                    mitem.remove(position);
                    notifyDataSetChanged();
                    }
                });
            return viewInfromation;
        }

    }
}


