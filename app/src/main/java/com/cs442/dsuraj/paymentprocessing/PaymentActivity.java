package com.cs442.dsuraj.paymentprocessing;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class PaymentActivity extends AppCompatActivity {
    ArrayList<String> arr= new ArrayList<String>();
    final Context context =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        Bundle seat_info = intent.getBundleExtra("seat_information");
        ArrayList<String> seating = seat_info.getStringArrayList("seats");
        ArrayList<String> total = seat_info.getStringArrayList("total");
        arr.add(0,"Flypaper");
        int len = seating.size();
        arr.add(1,"Regal Cinemas");
        arr.add(2, "25/25-");
        arr.add(3,"12:25");
        arr.add(4,"$25");
        TextView movie_name = (TextView) findViewById(R.id.movie);
        final TextView seats = (TextView) findViewById(R.id.noofseats);
        final TextView theatre = (TextView) findViewById(R.id.theascre);
        TextView time = (TextView) findViewById(R.id.datetime);
        TextView price = (TextView) findViewById(R.id.prices);
        EditText email = (EditText) findViewById(R.id.emailid);
        EditText phone = (EditText) findViewById(R.id.phoneid);
        movie_name.setText(arr.get(0));
        seats.setText("");
        for(int i=0;i<len-1;i++)
        {
            seats.setText(seats.getText() + seating.get(i));
            if(i!=len-2)
            {
                seats.setText(seats.getText() + ",");
            }
        }
        seats.setText(seats.getText() + " " + total.get(0));
        theatre.setText(arr.get(1));
        time.setText(arr.get(2) + arr.get(3) );
        price.setText(arr.get(4));
        Button bktkt = (Button) findViewById(R.id.payment);
        bktkt.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(PaymentActivity.this, DialogBox.class);
                intent.putExtra("amount",arr.get(4));
                startActivity(intent);

            }
        });

    }
}
