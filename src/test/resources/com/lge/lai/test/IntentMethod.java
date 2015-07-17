package com.lge.lai.test;

import com.lge.lai.test.stub.BroadcastReceiverStub;
import com.lge.lai.test.stub.ServiceConnectionStub;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;

public class IntentMethod extends Activity {
    public void toActivity() {
        startActivities(new Intent[] {}, new Bundle());
        startActivities(new Intent[] {});
        startActivity(new Intent(), new Bundle());
        startActivity(new Intent());
        startActivityForResult(new Intent(), 0);
        startActivityForResult(new Intent(), 0, new Bundle());
        startActivityFromChild(this, new Intent(), 0, new Bundle());
        startActivityFromChild(this, new Intent(), 0);
        startActivityFromFragment(new Fragment(), new Intent(), 0, new Bundle());
        startActivityFromFragment(new Fragment(), new Intent(), 0);
        startActivityIfNeeded(new Intent(), 0, new Bundle());
        startActivityIfNeeded(new Intent(), 0);
        startNextMatchingActivity(new Intent());
        startNextMatchingActivity(new Intent(), new Bundle());
    }

    public void toBroadcastReceiver() {
        sendBroadcast(new Intent());
        sendBroadcast(new Intent(), "permission");
        sendBroadcastAsUser(new Intent(), new UserHandle(null));
        sendBroadcastAsUser(new Intent(), new UserHandle(null), "permission");
        sendOrderedBroadcast(new Intent(), "permission", new BroadcastReceiverStub(), new Handler(), 0, "initialData", new Bundle());
        sendOrderedBroadcast(new Intent(), "permission");
        sendOrderedBroadcastAsUser(new Intent(), new UserHandle(null), "permission", new BroadcastReceiverStub(), new Handler(), 0, "initialData", new Bundle());
        sendStickyBroadcast(new Intent());
        sendStickyBroadcastAsUser(new Intent(), new UserHandle(null));
        sendStickyOrderedBroadcast(new Intent(), new BroadcastReceiverStub(), new Handler(), 0, "initialData", new Bundle());
        sendStickyOrderedBroadcastAsUser(new Intent(), new UserHandle(null), new BroadcastReceiverStub(), new Handler(), 0, "initialData", new Bundle());
    }

    public void toService() {
        bindService(new Intent(), new ServiceConnectionStub(), 0);
        unbindService(new ServiceConnectionStub());
        startService(new Intent());
        stopService(new Intent());
    }
}
