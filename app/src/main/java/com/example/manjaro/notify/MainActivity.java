package com.example.manjaro.notify;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> alList=new ArrayList();
    ListView lvLista;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        id=createID();
        lvLista=findViewById(R.id.lv);
        lvLista.setLongClickable(true);


        try{
            alList=FileIO.readArrayListFromFile("data",this);
            if(alList.isEmpty()){
                alList.add("No reminders.");
            }
        }catch(FileNotFoundException e){
            alList.add("No reminders.");
        }

        lvLista.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,alList));


        lvLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                alList.remove(pos);
                FileIO.writeArrayListToFile(alList,"data",getApplicationContext());
                if(alList.isEmpty())alList.add("No reminders.");
                lvLista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,alList));
                return true;
            }
        });
    }

    public void button(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        alert.setTitle("Add note");
        final EditText note = new EditText(MainActivity.this);
        note.setBackgroundResource(android.R.color.transparent);
        note.setOnFocusChangeListener(new View.OnFocusChangeListener()  {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager inputMgr = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });
        note.setHint("");
        layout.addView(note);
        alert.setView(layout);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(alList.contains("No reminders."))alList.remove("No reminders.");
                alList.add(note.getText().toString());
                lvLista.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,alList));
                FileIO.writeArrayListToFile(alList,"data",MainActivity.this);
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
                } catch (Exception e) {

                }
                id=createID();
                showNotification();
            }
        });
        alert.show();
    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notify";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.enableVibration(false);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "Notify")
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Notify")
                .setContentText(alList.get(alList.size()-1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(alList.get(alList.size()-1)))
                .setAutoCancel(true)
                .setVibrate(null)
                .setOngoing(true)
                ;


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());



        notificationManager.notify(id, mBuilder.build());
    }
    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }
}
