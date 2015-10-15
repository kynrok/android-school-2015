package com.elegion.githubclient.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.elegion.githubclient.AppDelegate;
import com.elegion.githubclient.R;
import com.elegion.githubclient.utils.ActivityBuilder;

/**
 * @author Artem Mochalov.
 */
public class BaseActivity extends AppCompatActivity {

    private Toast mToast;

    protected void startActivity(Class<? extends BaseActivity> clazz, boolean isClearTask) {
        ActivityBuilder builder = new ActivityBuilder()
                .setClass(clazz)
                .setContext(this);

        if (isClearTask) {
            builder.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        builder.startActivity();
    }

    protected void showSingleToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    protected void logout() {
        removeCookies();
        AppDelegate.getSettings().edit().remove(AppDelegate.ACCESS_TOKEN).commit();
        startActivity(LoginActivity.class, true);
    }

    private void removeCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
        } else {
            CookieManager.getInstance().removeAllCookies(null);
        }
    }

    protected void connectionError() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.connection_error_title)
                .setMessage(R.string.connection_error_text)
                .setNeutralButton("OK", new  ErrorDialogListener())
                .create().show();
    }

    protected class ErrorDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            logout();
        }
    }
}
