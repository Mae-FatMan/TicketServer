/**
 * Created by ${doufals} on 1/21/16.
 */

package com.douglas.ticketmanager;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
//import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dewav.ticket.ITicketManager;
import com.douglas.ticketmanager.ClientQueue;
import com.douglas.ticketmanager.DouglasService;


public class BuyTicketTestView extends Activity {

    private static String TAG = "com.douglas.ticketmanager.DouglasService";
    private static ImageView imageView = null;
    private static ImageView imageView2 = null;
    private static ImageView imageView3 = null;
    private static EditText editText = null;
    private static EditText editText2 = null;
    private static EditText editText3 = null;
    private static Button button = null;
    private static Button button2 = null;
    private static TextView textView = null;
    String num = null;
    String staA = null;
    String staB = null;

    private ITicketManager iTicketManager;
    boolean isBinded = false;


    private ServiceConnection serConn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iTicketManager = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iTicketManager = ITicketManager.Stub.asInterface((IBinder)service);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        textView = (TextView) findViewById(R.id.textView);

        imageView.setImageResource(R.drawable.people);
        imageView2.setImageResource(R.drawable.station1);
        imageView3.setImageResource(R.drawable.station2);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String num = editText.getText().toString();
                String staA = editText2.getText().toString();
                String staB = editText3.getText().toString();
                int myNum = 0;

                try {
                    myNum = Integer.parseInt(num);
                }catch (Exception e){
                    Log.d(TAG, "myNum is error, myNum: " + myNum + "staA: " + staA + "staB:"+staB);
                    System.out.println(e.getMessage());
                }

                Log.d(TAG, isBinded + num + staA + staB + "\n" + ".................");
                if (isBinded && (staA != null || staB != null) && BuyTicket(myNum, staA, staB)) {
                    Log.d(TAG, staA + " --- " + staB + "\n" + "Buy tickets Success!");
                    textView.setText(staA + " --- " + staB + "\n" + "Buy tickets Success!");
                } else {
                    Log.d(TAG, "Failed to buy tickets!");
                    textView.setText("Failed to buy tickets!");
                }
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                //intent.setAction("com.dewav.ticket.buy");
                //intent.setPackage("com.douglas.ticketmanager");
                //isBinded = bindService(intent, serConn, Service.BIND_AUTO_CREATE);

            }
        });
    }

    public boolean BuyTicket(int number, String departureStation, String terminus) {
        Log.d(TAG, "BuyTicket");

        try{
            if (iTicketManager != null){
                if (iTicketManager.buy(number, departureStation, terminus)) {
                    textView.setText("Buy ticket successfull!");
                    return true;
                } else {
                    textView.setText("Failed to buy ticket!");
                    return false;
                }
            }
        } catch(RemoteException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
