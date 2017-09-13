package com.androidatc.tapthenumber;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class MainActivity extends Activity {
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
    private long time;
    private int count;
    private GridView gridView;
    private NumberAdapter gridAdapter;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        count = 0;
        shuffleArray(numbers);

        adView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        gridView = (GridView) findViewById(R.id.playground);
        gridAdapter = new NumberAdapter(this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (count == 0)
                    time = System.currentTimeMillis();
                    count++;

                    if ((Integer) parent.getItemAtPosition(position) == count) {
                        view.findViewById(R.id.number).setBackgroundColor(Color.GREEN);
                    } else {
                        view.findViewById(R.id.number).setBackgroundColor(Color.RED);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Result:").setMessage("You Lost...!!").setCancelable(false).setNegativeButton("Try Again!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                shuffleArray(numbers);
                                gridAdapter.notifyDataSetChanged();
                                count = 0;

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    if ((Integer) parent.getItemAtPosition(position) == count && count == 25) {
                        view.findViewById(R.id.number).setBackgroundColor(Color.GREEN);
                        time = System.currentTimeMillis() - time;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Result:").setMessage("You Won, Time Elapsed:" + time / 1000 + "sec").setCancelable(false).setNeutralButton("Can do Better...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                shuffleArray(numbers);
                                gridAdapter.notifyDataSetChanged();
                                count = 0;
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

        });
    }
    public class NumberAdapter extends BaseAdapter {
        private Context mContext;
        public NumberAdapter (Context c){
            mContext = c;
        }
        @Override
        public int getCount(){
            return numbers.length;
        }
        @Override
        public Object getItem (int position){
            return numbers[position];
        }
        @Override
        public long getItemId (int position) {
            return  0;
        }
        @Override
        public View getView (int position, View convertView, ViewGroup parent){
            final LinearLayout BoxView;
            BoxView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_box, parent, false);
            TextView v = (TextView) BoxView.findViewById(R.id.number);
            v.setText("" + numbers[position]);
            return BoxView;
        }
    }
    static void shuffleArray (int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length -1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] =ar[i];
            ar[i] = a;
        }
    }
}
