package io.github.teccheck.gear360app.bluetooth;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import io.github.teccheck.gear360app.MainActivity;
import io.github.teccheck.gear360app.Utils;

public class BTMessageLogger {

    public static class LoggerMessage{
        public LoggerMessage(boolean outgoing, JSONObject message, long time){
            this.outgoing = outgoing;
            this.message = message;
            this.time = time;
        }
        public boolean outgoing;
        public JSONObject message;
        public long time;
    }

    private static ArrayList<LoggerMessage> messages = new ArrayList<>();

    public static void addMessage(boolean outgoing, JSONObject message, long time){
        messages.add(new LoggerMessage(outgoing, message, time));
    }

    public static void logIncommingMessage(JSONObject message){
        addMessage(false, message, System.currentTimeMillis());
        Log.i(MainActivity.TAG, "Incomming:\n" + Utils.getPrettyJsonStringFromByteData(message.toString()));
    }

    public static void logOutgoingMessage(JSONObject message){
        addMessage(true, message, System.currentTimeMillis());
        Log.i(MainActivity.TAG, "Outgoing:\n" + Utils.getPrettyJsonStringFromByteData(message.toString()));

    }

    public static ArrayList<LoggerMessage> getMessages(){
        return messages;
    }
}
