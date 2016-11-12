package com.cs442.dsuraj.paymentprocessing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ar1 on 11/6/2016.
 */
public class Confirmation extends Activity {
    ArrayList<String> arr= new ArrayList<String>();
    final Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Toast toast= Toast.makeText(getApplicationContext(),"Confirmation Page", Toast.LENGTH_LONG);
        Intent intent = getIntent();
        Bundle seat_info = intent.getBundleExtra("seat_information");
        ArrayList<String> seating = seat_info.getStringArrayList("seats");
        ArrayList<String> total = seat_info.getStringArrayList("total");
        arr.add(0,"Flypaper");
        int len = seating.size();
        arr.add(1,"Regal Cinemas");
        arr.add(2, "25/25");
        arr.add(3,"12:25");
        arr.add(4,"$25");
        final TextView seats = (TextView) findViewById(R.id.noofseats);
        final TextView theatre = (TextView) findViewById(R.id.theascr);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time= (TextView) findViewById(R.id.time);
        TextView price = (TextView) findViewById(R.id.price);
        EditText email = (EditText) findViewById(R.id.emailid);
        EditText phone = (EditText) findViewById(R.id.phone);
        SpannableString styledString = new SpannableString(arr.get(0));
        styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, arr.get(0).length(), 0);
        //styledString.setSpan(new StyleSpan(Typeface.ITALIC), 8, 14, 0);

        TextView tv = (TextView)findViewById(R.id.textView16);
        tv.setText(styledString);
        seats.setText("");
        for(int i=0;i<len;i++)
        {
            seats.setText(seats.getText() + seating.get(i));
            if(i!=len-1)
            {
                seats.setText(seats.getText() + ",");
            }
        }
        seats.setText(seats.getText() + " " + total.get(0));
        theatre.setText(arr.get(1));
        time.setText(arr.get(3));
        date.setText(arr.get(2));
        price.setText(arr.get(4));
        Button bktkt = (Button) findViewById(R.id.pay);
        bktkt.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_user_information);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                // params.height = WindowManager.LayoutParams.FILL_PARENT;
                params.width= WindowManager.LayoutParams.FILL_PARENT;
                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                final EditText namecard = (EditText) dialog.findViewById(R.id.nameoncard);
                final EditText creditcard = (EditText) dialog.findViewById(R.id.creditcard);
                final EditText cvvno = (EditText) dialog.findViewById(R.id.cvv);
                final EditText valid = (EditText) dialog.findViewById(R.id.vt1);
                final EditText valid1 = (EditText) dialog.findViewById(R.id.vt2);

                Button order = (Button) dialog.findViewById(R.id.completeorder);
                order.setText("Complete Order ( Total " + arr.get(4) + ")");
                // if button is clicked, close the custom dialog
                order.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if ((namecard.getText().toString().isEmpty()) || (creditcard.getText().toString().isEmpty()) || (cvvno.getText().toString().isEmpty()) || (valid.getText().toString().isEmpty())) {
                            Toast.makeText(getApplicationContext(), "Please insert the data", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent;
                            intent = new Intent(context, MovieConfirmation.class);
                            context.startActivity(intent);
                        }
                    }
                });
                dialog.show();

            }
        });
    }
}
