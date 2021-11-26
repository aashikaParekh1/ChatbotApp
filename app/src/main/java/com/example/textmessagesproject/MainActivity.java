package com.example.textmessagesproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class MainActivity extends AppCompatActivity {

    int REQUEST_KEY = 1234;
    SmsManager smsManager = SmsManager.getDefault();
    public static String phone;
    Handler handler = new Handler();
    int counter = 0;
    TextView states;


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)){

        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE}, REQUEST_KEY);

        }

        states = findViewById(R.id.id_state);

    }



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle myBundle = intent.getExtras();
            SmsMessage [] messages = null;

            if (myBundle != null)
            {
                Object [] pdus = (Object[]) myBundle.get("pdus");

                messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = myBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    }
                    else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    String mess = messages[i].getMessageBody();
                    phone = messages[i].getOriginatingAddress();
                    String strMessage = "Sender2: " + phone + "\nMessage: " + mess;
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                    //textView.setText(Message);
                    bot(mess);
                }
            }
        }
    };

    public void autoMessage(final String message, int time){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    smsManager.sendTextMessage(phone,null,message,null,null);
                    Toast.makeText(getApplicationContext(), "SMS Sent Successfully!",
                            Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "SMS FAILED!",
                            Toast.LENGTH_LONG).show();
                    Log.d("OUTPUT ", "_______________________________________________");
                    e.printStackTrace();
                    Log.d("OUTPUT ", phone);
                    Log.d("OUTPUT ", message);
                }

            }
        }, time);
    }

    public void bot(String message)
    {

        if(counter == 0) {
            states.setText("State 0: Greeting State");
            if(message.contains("hi") || message.contains("hey") || message.contains("hello") ||  message.contains("what's up") ||  message.contains("sup") || message.contains("morning") ||  message.contains("afternoon")){
                autoMessage("Hey there! Welcome to Dunder Mifflin Paper Company! How can we help?", 1000);
                counter = 1;

            }
            else{
                autoMessage("Sorry dear human, I don't respond to rudeness.", 1000);
                counter = 10;

            }
        }

        else if(counter == 10) {
            states.setText("State 0: Greeting State");
            if(message.contains("hi") || message.contains("hey") || message.contains("hello") ||  message.contains("what's up") ||  message.contains("sup") || message.contains("morning") ||  message.contains("afternoon")){
                autoMessage("Much better, hey there! Welcome to Dunder Mifflin Paper Company! How can we help?", 1000);
                counter = 1;

            }
            else{
                autoMessage("Sorry dear human, I'll say it again I don't respond to rudeness.", 1000);
                counter = 10;

            }
        }

        else if(counter == 1)
        {
            states.setText("State 1: Catalog Viewing/Ordering");
            if(message.contains("paper") || message.contains("order") || message.contains("reams") ||  message.contains("ship") || message.contains("printer") || message.contains("cardstock") || message.contains("clips") || message.contains("pens") || message.contains("medium")){
                autoMessage("What address should the order be sent to?", 2000);
                counter = 2;
            }

            else if(message.contains("no thank") || message.contains("thank you") || message.contains("thanks") ||  message.contains("that's") ||  message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else if(message.contains("menu") || message.contains("catalog") || message.contains("list") ||  message.contains("sheet") || message.contains("choices") || message.contains("options") || message.contains("types") || message.contains("kinds")){
                autoMessage("Top Sellers:\nPrinter Paper\nCardstock\nLinerboard\nFile Folders\nToilet Paper", 2000);
                counter = 2;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 1;

            }
        }

        else if(counter == 2)
        {
            states.setText("State 2: Extra Questions/Ordering");
            if(message.contains("road") || message.contains("court") || message.contains("drive") ||  message.contains("lane") || message.contains("street")){
                autoMessage("Order has been filled! Can we help you with anything else?", 2000);
                counter = 3;
            }

            else if(message.contains("good") || message.contains("thank you") || message.contains("thanks") ||  message.contains("that's") ||  message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else if(message.contains("paper") || message.contains("order") || message.contains("reams") ||  message.contains("ship") || message.contains("printer") || message.contains("cardstock") || message.contains("clips") || message.contains("pens") || message.contains("medium") || message.contains("folder") || message.contains("linerboard")){
                autoMessage("What address should the order be sent to?", 2000);
                counter = 3;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 2;
            }
        }

        else if(counter == 3)
        {
            states.setText("State 3: Address/Prices");
            int rand = (int)(Math.random()*80)+30;

            if(message.contains("price") || message.contains("cost") || message.contains("amount") ||  message.contains("money")){
                autoMessage("The total comes to $" + rand + ". Anything else I can help with?", 3000);
                counter = 4;
            }
            else if(message.contains("time") || message.contains("come") || message.contains("arrive") ||  message.contains("date") ||  message.contains("deliver")){
                autoMessage("Your order will arrive in 5 - 10 businsess days", 2000);
                counter = 4;
            }
            else if(message.contains("good") || message.contains("thank you") || message.contains("thanks") ||  message.contains("that's")){
                autoMessage("Your order confirmation should be sent to you shortly! Thanks for shopping at DM! Limitless paper, in a paperless world :)", 2000);
                counter = 0;
            }

            else if(message.contains("road") || message.contains("court") || message.contains("drive") ||  message.contains("lane") || message.contains("street")){
                autoMessage("Order has been filled! Can we help you with anything else?", 2000);
                counter = 4;
            }

            else if(message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 3;

            }

        }

        else if(counter == 4)
        {
            states.setText("State 4: Delivery/Pricing/Discounts");
            int rand = (int)(Math.random()*80)+30;

            if(message.contains("thank you") || message.contains("thanks") || message.contains("bye") ||  message.contains("see you")){
                autoMessage("Your order confirmation should be sent to you shortly! Thanks for shopping at DM! Limitless paper, in a paperless world :)", 2000);
                counter = 0;
            }

            else if(message.contains("discount") || message.contains("sale") || message.contains("free") ||  message.contains("markdown")){
                autoMessage("At this time we have no sales on paper, but we are offering free shipping with orders more than $125. Would you like to purchase any other products?", 2000);
                counter = 5;
            }

            else if(message.contains("price") || message.contains("cost") || message.contains("amount") ||  message.contains("money")){
                autoMessage("The total comes to $" + rand + ". Anything else I can help with?", 2000);
                counter = 5;
            }
            else if(message.contains("time") || message.contains("come") || message.contains("arrive") ||  message.contains("date") ||  message.contains("deliver")){
                autoMessage("Your order will arrive in 5 - 10 businsess days", 2000);
                counter = 5;
            }

            else if(message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 5;

            }
        }

        else if(counter == 5)
        {
            states.setText("State 5: Delivery/Pricing/Discounts");
            int rand2 = (int)(Math.random()*25)+125;
            int rand = (int)(Math.random()*80)+30;

            if(message.contains("thank you") || message.contains("thanks") || message.contains("bye") ||  message.contains("see you") || message.contains("no thanks") || message.contains("good") || message.contains("nevermind") ||  message.contains("fine") || message.contains("no") || message.contains("sorry")){
                autoMessage("No problem, your order confirmation should be sent to you shortly! Thanks for shopping at DM! Limitless paper, in a paperless world :)", 2000);
                counter = 0;
            }

            else if(message.contains("yes") || message.contains("paper") || message.contains("cardstock") ||  message.contains("folders") || message.contains("printer")){
                autoMessage("Perfect! Your new total is " + rand2 + ". You are all set for free shipping!", 3000);
                counter = 6;
            }

            else if(message.contains("price") || message.contains("cost") || message.contains("amount") ||  message.contains("money")){
                autoMessage("Your current total is " + rand + ". So you are not yet eligible for free shipping. Would you like to purchase any other products?", 3000);
                counter = 6;
            }

            else if(message.contains("time") || message.contains("come") || message.contains("arrive") ||  message.contains("date") ||  message.contains("deliver")){
                autoMessage("Your order will arrive in 5 - 10 businsess days", 2000);
                counter = 6;
            }

            else if(message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 5;

            }
        }

        else if(counter == 6)
        {
            states.setText("State 6: Ordering");
            int rand2 = (int)(Math.random()*25)+125;

            if(message.contains("thank you") || message.contains("thanks") || message.contains("bye") ||  message.contains("see you") || message.contains("no thanks") || message.contains("good") || message.contains("nevermind") ||  message.contains("fine") || message.contains("no")){
                autoMessage("Your updated order confirmation should be sent to you shortly! Thanks for shopping at DM! Limitless paper, in a paperless world :)", 2000);
                counter = 0;
            }

            else if(message.contains("yes") || message.contains("paper") || message.contains("cardstock") ||  message.contains("folders") || message.contains("printer")){
                autoMessage("Perfect! Your new total is " + rand2 + ". You are all set for free shipping!", 3000);
                counter = 7;
            }

            else if(message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 6;

            }
        }

        else if(counter == 7)
        {

            states.setText("State 7: Ending");
            if(message.contains("thank you") || message.contains("thanks") || message.contains("bye") ||  message.contains("see you")){
                autoMessage("Your updated order confirmation should be sent to you shortly! Thanks for shopping at DM! Limitless paper, in a paperless world :)", 3000);
                counter = 0;
            }

            else if(message.contains("cancel")){
                autoMessage("Maybe next time! Thanks for coming to DM Virtual Paper Store! Limitless paper, in a paperless world :)", 1000);
                counter = 0;
            }

            else{
                autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
                counter = 7;

            }
        }

        else{
            states.setText("Invalid Response State");
            autoMessage("Sorry I can't help you with that at this moment. Feel free to ask me something else!", 1000);
        }


    }



}


