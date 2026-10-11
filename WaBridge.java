package com.salesdashboard.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

/** Sale Completed: page ka screenshot WhatsApp me bhejta hai. */
public class WaBridge {
    private final Activity activity;

    public WaBridge(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public String send(String number, String b64, String caption) {
        try {
            byte[] bytes = Base64.decode(b64, Base64.DEFAULT);
            File dir = new File(activity.getCacheDir(), "shots");
            dir.mkdirs();
            File file = new File(dir, "sale.jpg");
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.close();

            final Uri uri = FileProvider.getUriForFile(
                    activity, activity.getPackageName() + ".fileprovider", file);
            final String digits = number == null ? "" : number.replaceAll("[^0-9]", "");
            final String text = caption == null ? "" : caption;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] packages = {"com.whatsapp", "com.whatsapp.w4b"};
                    for (String pkg : packages) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/jpeg");
                        i.putExtra(Intent.EXTRA_STREAM, uri);
                        i.putExtra(Intent.EXTRA_TEXT, text);
                        if (digits.length() > 0) {
                            i.putExtra("jid", digits + "@s.whatsapp.net");
                        }
                        i.setPackage(pkg);
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            activity.startActivity(i);
                            return;
                        } catch (ActivityNotFoundException ignored) {
                        }
                    }
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.putExtra(Intent.EXTRA_TEXT, text);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(Intent.createChooser(i, "Share"));
                }
            });
            return "ok";
        } catch (Exception e) {
            return "ERR:" + e.getMessage();
        }
    }
}
