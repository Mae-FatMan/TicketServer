package com.douglas.ticketmanager;
import android.os.Messenger;
import android.widget.Button;
import android.widget.TextView;

public class TicketStructure {

    private int[][][] startArray = new int[500][12][2];

    public void setSameStartArray(int seat, int i, int j, int myDefault) {
        startArray[seat][i][j] = myDefault;
    }

    public int getSameStartArray(int seat, int i, int j) {
        return startArray[seat][i][j];
    }
}