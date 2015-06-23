package com.shpark.androidnativesample.billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

/**
 * Created by sh on 2015-06-19.
 */

public class BillingHelper {

    private static final String TAG = "BillingHelper";

    private Context _context;
    private IInAppBillingService _iInAppBillingServiceService;
    private ServiceConnection _serviceConnection;

    public BillingHelper(Context context, String base64PublicKey) {

        _context = context;

        _serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "Service disconnected");
                _iInAppBillingServiceService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "Service connected");
                _iInAppBillingServiceService = IInAppBillingService.Stub.asInterface(service);
            }
        };
    }

    public void Dispose() {
        if (_serviceConnection != null) {
            if (_context != null) _context.unbindService(_serviceConnection);
        }
        _serviceConnection = null;
        _iInAppBillingServiceService = null;
        _context = null;
    }

    public void BuyItem(String sku) {

    }
}