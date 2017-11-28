package com.j_hawk.whattoplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.j_hawk.whattoplay.data.Game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class filterResult extends AppCompatActivity {
    private HomePageActivity.ItemAdapter myAdapter;
    private ArrayList<Game> resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       final Bundle it = this.getIntent().getExtras();
        resultList = (ArrayList<Game>) it.getSerializable("resultList");
        setContentView(R.layout.activity_filter_result);
        LayoutInflater myinflater = getLayoutInflater();
        myAdapter = new HomePageActivity.ItemAdapter(myinflater, (ArrayList<Game>) resultList);
        ListView resultlv = (ListView) findViewById(R.id.filterResult);
        resultlv.setAdapter(myAdapter);






            resultlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Game currentItem = myAdapter.getItem(position);
                    // (...)

                    Intent intent = new Intent(getApplicationContext(), ViewGame.class);
                    intent.putExtra("Id", currentItem.getId());

                    startActivity(intent);
                }
            });
        }
    public class ItemAdapter extends BaseAdapter {
        private ArrayList<Game> mitem;
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

        public ItemAdapter(LayoutInflater inflater, ArrayList<Game> items) {
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
}
