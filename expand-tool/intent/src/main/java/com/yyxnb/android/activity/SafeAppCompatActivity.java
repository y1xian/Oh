package com.yyxnb.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yyxnb.android.intent.IntentUtils;
import com.yyxnb.android.intent.SafeIntent;

/**
 * SafeAppCompatActivity
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/1
 */
public class SafeAppCompatActivity extends AppCompatActivity {
    private static final String TAG = SafeAppCompatActivity.class.getSimpleName();

    @Override
    public Intent getIntent() {
        try {
            return new SafeIntent(super.getIntent());
        } catch (Exception e) {
            LogUtil.e(TAG, "getIntent: " + e.getMessage(), true);
        }
        return new SafeIntent(new Intent());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivity: " + e.getMessage(), true);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        try {
            super.startActivityForResult(intent, requestCode, options);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivity: " + e.getMessage(), true);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivity Exception ");
        }
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        try {
            super.startActivity(intent, options);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivity: " + e.getMessage(), true);
        }
    }

    @Override
    public void startActivities(Intent[] intents) {
        try {
            super.startActivities(intents);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivities: " + e.getMessage(), true);
        }
    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        try {
            super.startActivities(intents, options);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivities: " + e.getMessage(), true);
        }
    }

    @Override
    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        try {
            return super.startActivityIfNeeded(intent, requestCode);
        } catch (Exception e) {
            LogUtil.e(TAG, "startActivityIfNeeded: " + e.getMessage(), true);
        }
        return false;
    }

    @Override
    public void finishAffinity() {
        try {
            super.finishAffinity();
        } catch (Exception e) {
            LogUtil.e(TAG, "finishAffinity: " + e.getMessage(), true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 检测到有攻击时，直接finish，防止后面再finish时抛异常（finish->hasExtra->containsKey->unparcel->initializeFromParcelLocked->readArrayMapSafelyInternal）,
        if (IntentUtils.hasIntentBomb(super.getIntent())) {
            LogUtil.e(TAG, "onCreate : hasIntentBomb");
        }
    }


    @Override
    protected void onStart() {
        if (IntentUtils.hasIntentBomb(super.getIntent())) {
            LogUtil.e(TAG, "onStart : hasIntentBomb");
        }
        super.onStart();
    }

    @Override
    protected void onRestart() {
        if (IntentUtils.hasIntentBomb(super.getIntent())) {
            LogUtil.e(TAG, "onRestart : hasIntentBomb");
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (IntentUtils.hasIntentBomb(super.getIntent())) {
            LogUtil.e(TAG, "onResume : hasIntentBomb");
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (IntentUtils.hasIntentBomb(super.getIntent())) {
            LogUtil.e(TAG, "onStop : hasIntentBomb");
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            LogUtil.e(TAG, "onDestroy exception : " + e.getMessage(), true);
        }
    }

    @Override
    public void finish() {
        try {
            super.finish();
        } catch (Exception e) {
            LogUtil.e(TAG, "finish exception : " + e.getMessage(), true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, new SafeIntent(data));
        } catch (Exception e) {
            LogUtil.e(TAG, "onActivityResult exception : " + e.getMessage(), true);
        }
    }

    @Override
    public Uri getReferrer() {
        try {
            return super.getReferrer();
        } catch (Exception e) {
            LogUtil.e(TAG, "getReferrer: " + e.getMessage(), true);
        }
        return null;
    }
}