package com.lge.lai.test;

import com.lge.lai.test.stub.ContextStub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class ActionName extends Activity {
    private static final String ACTION = "com.lge.lai.test.ACTION.TEST";
    
    public void actionNameDeclaration() {
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        Intent intent2 = new Intent(Intent.ACTION_PICK, Uri.parse("http://www.google.com"));
        Intent intent3 = new Intent(Intent.ACTION_PICK,
                Uri.parse("content://media/internal/images"), new ContextStub(),
                ActionName.class);
        
        Intent intent4 = new Intent();
        intent4.setAction(Intent.ACTION_PICK);
        intent4.setAction("com.lge.lai.test.ACTION.TEST");
        intent4.setAction(ACTION);
    }
}
