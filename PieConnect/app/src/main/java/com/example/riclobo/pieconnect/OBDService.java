package com.example.riclobo.pieconnect;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.intel.telematics.comm.BaseCommManager;
import com.intel.telematics.comm.IHardwareStatusListener;
import com.intel.telematics.comm.IOBDDataChangeListener;
import com.intel.telematics.comm.OBDCommManager;
import com.intel.telematics.config.ObdConfig;
import java.util.Arrays;

/**
 * Created by Riclobo on 3/15/2016.
 */
public class OBDService extends Service implements IOBDDataChangeListener,IHardwareStatusListener {

    private OBDCommManager mnger;
    private int regId;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mnger = new OBDCommManager(this);
        mnger.initServiceConnection(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mnger.deInitServiceConnection();
    }

    @Override
    public void onObdDataChange(int[] data, int pid, char mode, int registrationId, long timeStamp){
        // Change in data is notified in this method.
        Log.d("OnChange Data: ", Arrays.toString(data));
    }

    @Override
    public void onCyclicObdData(int[] data, int pid, char mode, int registrationId, long timeOfDataInMillis){

    }

    @Override
    public void onHardwareOpenStatus(boolean status) {
        if (status) {
            int [] pids = { ObdConfig.Mode1Mode2Pid.vehicleSpeed,
                            ObdConfig.Mode1Mode2Pid.engineRPM };
            regId = mnger.registerObdPids( (char)ObdConfig.MODE_REQUEST_LIVEDATA, ObdConfig.SamplingRate5, pids, this);
        }
    }

    @Override
    public void onHardwareStateChanged(BaseCommManager.HwState hwState) {

    }
}
