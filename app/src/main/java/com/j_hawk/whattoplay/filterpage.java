package com.j_hawk.whattoplay;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;


import com.j_hawk.whattoplay.data.DBHelper;

import java.util.List;


public class filterpage extends AppCompatActivity {

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localsearching);
    }
    public static class ItemAdapter extends BaseAdapter {
        private List<String> mitem;
        private LayoutInflater minflater;

        /**
         * Constructor for ItemAdapter
         * @param inflater LayoutInflater
         * @param items List<Game>
         */
        public ItemAdapter(LayoutInflater inflater, List<String> items) {
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
            View viewInformation = minflater.inflate(R.layout.checkbox_item, null);
            String item = mitem.get(i);
            CheckBox cb = viewInformation.findViewById(R.id.type);
            cb.setText(item);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return viewInformation;
        }
    }
}
