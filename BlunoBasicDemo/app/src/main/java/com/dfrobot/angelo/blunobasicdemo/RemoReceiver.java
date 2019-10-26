package com.dfrobot.angelo.blunobasicdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

class RemoReceiver extends BroadcastReceiver {

    public final static String REMO_SDK_CONTROL_SOCKET = "tv.remo.android.controller.sdk.socket.controls";
    public final static String REMO_SDK_CONTROL_SOCKET_PERMISSION = "tv.remo.android.controller.sdk.socket.controls";
    private final Context context;
    private final RemoListener listener;

    public RemoReceiver(Context context, RemoListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void register() {
        IntentFilter filter = new IntentFilter(REMO_SDK_CONTROL_SOCKET);
        context.registerReceiver(this, filter);
        listener.onCommand("stop");
    }

    public void unregister() {
        context.unregisterReceiver(this);
        listener.onCommand("stop");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REMO_SDK_CONTROL_SOCKET.equals(intent.getAction())) {
            listener.onCommand(intent.getStringExtra("command"));
        }
    }

    public interface RemoListener {
        void onCommand(String command);
    }
}