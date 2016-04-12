/**
 *
 * author douglas
 * Created by douglas on 1/21/16.
 */

package com.douglas.ticketmanager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.dewav.ticket.ITicketManager;

import java.util.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DouglasService extends Service {
    private final static String TAG = "com.douglas.ticketmanager.DouglasService";

    private int[][][] startArray = new int[500][12][2];

    private int currentSeat = 0;/*1-----500*/
    private int currentTicketNum = 0;/*1-----6000*/

    private static final int CLIENT_MAX = 3;
    private static final int SEATS_MAX = 50;
    private static final int TRAIN_MAX = 10;
    private static final int SEARS_MAX = (SEATS_MAX * TRAIN_MAX);
    private static final int STATION_MAX = 13;
    private static final int STATION_ONE_TRAIN_MAX = (STATION_MAX -1);
    private static final int TICKETS_MAX = (SEATS_MAX * TRAIN_MAX * (STATION_MAX - 1)) ;

    public void onCreate() {
        super.onCreate();
        initTicketStrure();
    }

    public void initTicketStrure(){
        Log.d(TAG, "initTicketStrure begin!");

        for (int seat = 0; seat < SEARS_MAX; seat++) {
            for (int i = 0; i < STATION_ONE_TRAIN_MAX; i++) {
                for (int j = 0; j < 2; j++) {
                    startArray[seat][i][j]= 0;
                }
            }
        }
        Log.d(TAG, "initTicketStrure end!");
    }

    public String checkStation1(String Station) {
        //Log.d(TAG, "checkStation1");

        String sub="S";
        int result = Station.indexOf(sub);
        Log.d(TAG, "result :" + result);
        String myStation = null;

        /*
        * Exclude illegal character
        * */
        if (result >= 0 ) {
            myStation = Station.substring(1);
            Log.d(TAG, "myStation" + myStation);
            return myStation;
        }else{
            Log.d(TAG, "return null;");
            return null;
        }
    }

    public int checkStation2(String Station) {
        //Log.d(TAG, "checkStation2");

        int myStation = 0;
        /*
        * Exception Handling
        * */
        try {

            myStation = Integer.parseInt(Station);
            Log.d(TAG, "myStation" + myStation);
            return myStation;
        } catch (Exception e) {
            Log.d(TAG, "return 0!");
            return 0;
        }
    }

    public boolean JudgeParameters(int number, String departureStation, String terminus) {
        //Log.d(TAG, "JudgeParameters");

        int startStation = 0;
        int endStation = 0;

        Message msg = new Message();
        msg.what = 0;

        if (number != 0) {
            if (number <= 0 || number > 500) {//1-500
                msg.obj = "The number must be Greater than zero or Less than 500!";
                myHandler.sendMessage(msg);

                //Log.d(TAG, "The number must be Greater than zero or Less than 500!");
                return false;
            }
            else if (number == 1234) {
                initTicketStrure();
                //Log.d(TAG, "Clean Up SationList!");
                return false;
            }
        } else {
            msg.obj = "The number must a valid number from 1 to 500!";
            myHandler.sendMessage(msg);

            //Log.d(TAG, "The number must a valid number from 1 to 500!");
        }        /*
        * Exclude illegal character
        * */
        departureStation = checkStation1(departureStation);
        terminus = checkStation1(terminus);
        if (departureStation == null || terminus == null) {
            msg.obj = "Please enter a valid station! Format S+digital!";
            myHandler.sendMessage(msg);

            //Log.d(TAG, "departureStation == null || terminus == null " + "\n" + " return false !");
            return false;
        }

        /*
        * Exception Handling
        * */
        startStation = checkStation2(departureStation);
        endStation = checkStation2(terminus);

        if (startStation == 0 || endStation == 0) {
            msg.obj = "\"Please enter a valid station! Format S+digital!\"";
            myHandler.sendMessage(msg);

            Log.d(TAG, "startStation == 0 || endStation == 0 ---- return false!");
            return false;
        }

        /* Check  */
        if (startStation <= 0 || startStation > 13 || endStation <= 0 || endStation > 13 ) {//1-13
            msg.obj = "The station must be Greater than S1 or Less than S13!";
            myHandler.sendMessage(msg);

            Log.d(TAG, "The station must be Greater than S1 or Less than S13!");
        } else if (startStation >= endStation) {
            msg.obj = "Please enter a valid station! startStation must be greater than endStation!";
            myHandler.sendMessage(msg);

            Log.d(TAG, "Please enter a valid station! startStation must be greater than endStation!");
        } else {
            return true;
        }

        return false;
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };

    public boolean BuyTicket(int number, String departureStation, String terminus) {
        //Log.d(TAG, "BuyTicket");

        int intPeoArr = number;
        int peoplenum = 0;
        int seat = 0;
        int myArray = 0;
        int startStation = 0;
        int endStation = 0;

        int[] peopleArray = new int[number];
        int[] seatArray = new int[number];
        int[] myArrayArray = new int[number];
        int[] strA = new int[number];
        int[] strB = new int[number];

        Message msg = new Message();
        msg.what = 0;

        departureStation = checkStation1(departureStation);
        terminus = checkStation1(terminus);
        if (departureStation == null || terminus == null) {
            return false;
        }

        startStation = checkStation2(departureStation);
        endStation = checkStation2(terminus);
        if (startStation == 0 || endStation == 0) {
            return false;
        }

        /* buy */
        if (currentTicketNum > TICKETS_MAX) {
            currentTicketNum = 0;
        }

        //Log.d(TAG, " currentTicketNum:" + currentTicketNum);

        while(intPeoArr > 0){
            peopleArray[intPeoArr-1] = 0;
            seatArray[intPeoArr-1] = 0;
            myArrayArray[intPeoArr-1] = 0;
            strA[intPeoArr-1] = 0;
            strB[intPeoArr-1] = 0;
            intPeoArr--;
        }

        for (peoplenum = 0; peoplenum < number; peoplenum++) {

            for(seat = 0; seat < SEARS_MAX; seat++) {

                for(myArray = 0; myArray < STATION_ONE_TRAIN_MAX; myArray++){

                    if ( startArray[seat][myArray][1] == 0) {
                        /*
                        startArray[seat][myArray][0]= startStation;
                        startArray[seat][myArray][1]= endStation;
                        */
                        currentTicketNum++;
                        peopleArray[peoplenum] = 1;

                        seatArray[intPeoArr] = seat;
                        myArrayArray[intPeoArr] = myArray;
                        strA[intPeoArr] = startStation;
                        strB[intPeoArr] = endStation;

                        break;
                    }

                    if ((startStation >= startArray[seat][myArray][1])
                            || (endStation <= startArray[seat][myArray][0])) {
                        //Do nothing
                    }else {
                        break;
                    }
                }

                if (peopleArray[peoplenum] == 1) {
                    break;
                }
            }

        }

        intPeoArr = number;
        int i = 0; int j = 0;
        for(i = 0; i < intPeoArr; i++) {
            if (peopleArray[i] == 1) {
                j++;
            }
        }


        if ((j > 0) && (j == intPeoArr)){
            //Log.d(TAG, "Tickets enough, " + j + " people successful complete!!!"+ "\n");

            msg.obj = "Tickets enough, " + j + " people successful complete!!!";
            myHandler.sendMessage(msg);

            for(i = 0; i < intPeoArr; i++) {
                startArray[seatArray[i]][myArrayArray[i]][0] = strA[i];
                startArray[seatArray[i]][myArrayArray[i]][1] = strB[i];
            }
            return true;
        }
        else if ((j > 0) && (j < intPeoArr)){
            //Log.d(TAG, "Tickets not enough, but " + j + " people successful complete!!!"+ "\n");

            msg.obj = "Tickets not enough, only" + j + " tickets!!!";
            myHandler.sendMessage(msg);
        }


        return false;
    }

    ITicketManager.Stub mBinder = new ITicketManager.Stub() {
        private Integer CilentMax = 0;
        private Boolean CilentSuccess = false;

        //ClientQueue stack = new ClientQueue<String>();
        BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(CLIENT_MAX);

        public int Random() {
            int intRd = 0; //存放随机数
            Random rdm = new Random(System.currentTimeMillis());
            intRd = Math.abs(rdm.nextInt()) % 32 + 1;
            //Log.i(TAG, "intRd" + intRd);
            return intRd;
        }

        @Override
        public synchronized boolean buy(int number, String departureStation, String terminus) throws RemoteException {
            //Log.i(TAG, "buy");

            try {
                if (!JudgeParameters(number, departureStation, terminus)) {
                    return false;
                }
                CilentSuccess = BuyTicket(number, departureStation, terminus);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(TAG, "11111CilentSuccess;");
            return CilentSuccess;

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "OnBind");
        //return new stub;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        //return true;
        return super.onUnbind(intent);
    }
}