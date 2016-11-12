package com.cs442.dsuraj.paymentprocessing;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

/**
 * Created by sushma on 10/23/2016.
 */

public class MovieConfirmation extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;
    NotificationManager notificationManager;
    String speech;
    Notification.Builder Builder;
    Notification notification;
    private Bundle bundle;
    public static final int ID = 45612;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieconfirmation);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Builder = new Notification.Builder(MovieConfirmation.this);
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        speech = "Movie name" + "Booking date" + "  Hello";
        Log.i("inside screen","ok");

        final TextView MovieName=(TextView)findViewById(R.id.MovieName);
        final TextView theatre=(TextView)findViewById(R.id.theatrescreen);
        final TextView bookingid=(TextView)findViewById(R.id.bookingid);
        final TextView noseats=(TextView)findViewById(R.id.quantity);
        final TextView totalamount=(TextView)findViewById(R.id.totalamount);
        final TextView datetime=(TextView)findViewById(R.id.datetime);
        Log.i("done select","ok");


        String BookingId;
        BookingId="1";
        DatabaseHelper   d = new DatabaseHelper(getApplicationContext());
        boolean val= d.insertdatamovie("Flywire","adad", "6", "adad",true,5 );
        System.out.print(val);

        val= d.insertdatatimimgs(1,"Imax","10.30",50);
        System.out.print(val);
        val=d.insertmoviebooked(1,"1","imax","10.20", 50,"cs442project@gmail.com", "3125362060");
        System.out.print(val);
        Log.i("done inserting","ok");
        SQLiteDatabase db =d.getReadableDatabase();
        Cursor cursor =d.getData(db,BookingId);
        Log.i("done selecting","ok");
        bookingid.setText(BookingId);

        while (cursor.moveToNext()) {
            String email=  cursor.getString(3);
            String phoneno=  cursor.getString(4);

            System.out.print("email");
            try {
                new SendEmailAsyncTask().execute(email, bookingid.getText().toString(), MovieName.getText().toString(), theatre.getText().toString(), datetime.getText().toString(), noseats.getText().toString(), totalamount.getText().toString());
                Toast.makeText(getApplicationContext(), "Email Sent Succesfully",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
                Toast.makeText(getApplicationContext(), "Error in Sending Email",
                        Toast.LENGTH_SHORT).show();
            }

            sendSMS(phoneno, "MovieName: " + MovieName.getText().toString() + " Theatre:" + theatre.getText().toString() +
                    " No Of Seats:" + noseats.getText().toString() + " Total Amount:" + totalamount.getText().toString() + " Date/Time:" + datetime.getText().toString());
        }

    }
    private void sendSMS( String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, "null", message.trim(), sentPI, deliveredPI);
    }

    class SendEmailAsyncTask extends AsyncTask<String, Void, Boolean> {
        GMailSender m = new GMailSender("cs442project@gmail.com", "cs442mobile");

        public SendEmailAsyncTask() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                m.sendMail("Movie Booking",
                        "Booking Id : "+params[1]+" MovieName: "+params[2]+" Theatre: "+params[3]+" Date/Time: "+params[4]+" No Of Seats: "+params[5]+" TotalAmount:" +params[6],
                        "cs442project@gmail.com",
                        params[0]);
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    private void speakWords(String speech) {
        //speak straight away
        Log.d("Speak Words Called"," ");
        myTTS.speak(speech,TextToSpeech.QUEUE_FLUSH,null,null);
    }
    public void notification(String message)           //Notification tab to print the notification
    {
        Log.d("Notification started", " ");
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.images);
        notification = Builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Notification")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setLargeIcon(bm)
                .setContentTitle("Movie Details " + message)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).build();

        notificationManager.notify(ID, notification);
        speakWords(speech);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS

                myTTS = new TextToSpeech(MovieConfirmation.this, this);
                Log.d("Initialised TTS", " ");

            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
            Log.d("Success" ," ");
            notification(speech);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(MovieConfirmation.this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            Log.d("Failed" , " ");
        }
    }
}
