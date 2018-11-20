/*
 * Apache 2.0 License
 *
 * Copyright (c) Sebastian Katzer 2017
 *
 * This file contains Original Code and/or Modifications of Original Code
 * as defined in and that are subject to the Apache License
 * Version 2.0 (the 'License'). You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at
 * http://opensource.org/licenses/Apache-2.0/ and read it before using this
 * file.
 *
 * The Original Code and all software distributed under the License are
 * distributed on an 'AS IS' basis, WITHOUT WARRANTY OF ANY KIND, EITHER
 * EXPRESS OR IMPLIED, AND APPLE HEREBY DISCLAIMS ALL SUCH WARRANTIES,
 * INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT.
 * Please see the License for the specific language governing rights and
 * limitations under the License.
 */

package de.appplant.cordova.plugin.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.appplant.cordova.plugin.notification.Builder;
import de.appplant.cordova.plugin.notification.Manager;
import de.appplant.cordova.plugin.notification.Notification;
import de.appplant.cordova.plugin.notification.Options;


import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Abstract broadcast receiver for local notifications. Creates the
 * notification options and calls the event functions for further proceeding.
 */
abstract public class AbstractTriggerReceiver extends BroadcastReceiver {

    /**
     * Called when an alarm was triggered.
     *
     * @param context Application context
     * @param intent  Received intent with content data
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle   = intent.getExtras();
        InputStream DataInputStream = null;
        try {
 
            //Post parameters
             String PostParam = "lat=android&amp;lon=pala";
            
            //Preparing
            URL url = new URL("http://shopno33.atspace.cc/writelat.php");
 
            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to POST
            cc.setRequestMethod("POST");
            //set it to true as we are connecting for input
            cc.setDoInput(true);
            //opens the communication link
            cc.connect();
            
            //Writing data (bytes) to the data output stream 
            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            //flushes data output stream.
            dos.flush();
            dos.close();
            
            //Getting HTTP response code
            int response = cc.getResponseCode();
 
            //if response code is 200 / OK then read Inputstream
            //HttpURLConnection.HTTP_OK is equal to 200 
            if(response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }
 
        } catch (Exception e) {
           // Log.e(LOG_TAG, "Error in GetData", e);
        }

        if (bundle == null)
            return;

        int toastId     = bundle.getInt(Notification.EXTRA_ID, 0);
        Options options = Manager.getInstance(context).getOptions(toastId);

        if (options == null)
            return;

        Builder builder    = new Builder(options);
        Notification toast = buildNotification(builder, bundle);

        if (toast == null)
            return;

        onTrigger(toast, bundle);
    }

    /**
     * Called when a local notification was triggered.
     *
     * @param notification Wrapper around the local notification.
     * @param bundle       The bundled extras.
     */
    abstract public void onTrigger (Notification notification, Bundle bundle);

    /**
     * Build notification specified by options.
     *
     * @param builder Notification builder.
     * @param bundle  The bundled extras.
     */
    abstract public Notification buildNotification (Builder builder,
                                                    Bundle bundle);

}
