package kr.ac.uhs.alarmi_fa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MinewBeaconManager mMinewBeaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBluetooth();
        initListener();
/*
        LinearLayout button_bus = (LinearLayout)findViewById(R.id.button_bus);
        LinearLayout button_time = (LinearLayout)findViewById(R.id.button_time);
        LinearLayout button_non_sound = (LinearLayout)findViewById(R.id.button_non_sound);


        button_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainframe,new MyFragment()).commit();
            }
        });
        button_non_sound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainframe,new MyFragment2()).commit();
            }
        });
        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainframe,new MyFragment3()).commit();
            }
        });*/
    }
    // 블루투스 상태 체크
    private void checkBluetooth() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported: // 블루투스가 지원되지 않는 스마트폰임
                Toast.makeText(this, "블루투스가 지원되지 않는 스마트폰...?", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff: // 블루투스 상태가 꺼져있으면 켜달라고 요청하기
                Toast.makeText(this, "블루투스가 꺼져있음...!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOn: // 켜져있으면 아무것도 안함
                break;
        }
    }

    // 비콘 매니저 초기화하고 스캔 시작
    private void initListener() {
        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {

            }

            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ㅎㅈ", "탐지된 비콘 수: " + minewBeacons.size());

                        String temp = "";
                        for( int i=0; i<minewBeacons.size(); i++ ){
                            MinewBeacon target = minewBeacons.get(i);

                            String id = target.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                            String mac = target.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_MAC).getStringValue();
                            float rssi = target.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getFloatValue();
                            rssi = Math.abs(rssi); // 양수로 저장

                            temp = temp + mac + " " + rssi + "\n";

                            setRssiData(mac, (int)rssi);
                            //tv[i].setText(id + " " + mac + " " + rssi + " " + distance);
                        }
                        checkNearBeacon();
                    }
                });
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {

            }
        });

        mMinewBeaconManager.startScan();
    }

    public void mOnClick(View view) {
    }

    private void setRssiData(String mac, int rssi) {
        for( int i=0; i<4; i++ )
            if( Common.macBeacon[i].equals(mac) ) {
                Common.rssiBeacon[i] = rssi;
            }
    }

    private void checkNearBeacon(){
        if( Common.rssiBeacon[3] < 50 ) {
            if( Common.whatFragment != 1 )
                getFragmentManager().beginTransaction().replace(R.id.mainframe, new MyFragment()).commit();
        } else if( Common.rssiBeacon[2] < 50 ) {
            if( Common.whatFragment != 2 )
                getFragmentManager().beginTransaction().replace(R.id.mainframe, new MyFragment2()).commit();
        } else if( Common.rssiBeacon[1] < 50 ) {
            if( Common.whatFragment != 3 )
                getFragmentManager().beginTransaction().replace(R.id.mainframe, new MyFragment3()).commit();
        } else if(Common.rssiBeacon[0]<50){
            getFragmentManager().beginTransaction().replace(R.id.mainframe, new MyFragment3()).commit();
        }

    }
}
