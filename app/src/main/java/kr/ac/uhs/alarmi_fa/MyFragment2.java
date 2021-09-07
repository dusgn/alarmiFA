package kr.ac.uhs.alarmi_fa;

import android.app.Fragment;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment2 extends Fragment {


    TextView tv21;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment2, container, false);

        Common.whatFragment = 2;

        tv21 = (TextView) v.findViewById(R.id.tv21);

        // 화면 업데이트 동작
        final Runnable runnableUI = new Runnable() {
            @Override
            public void run() {
                checkNearBeacon3();
            }
        };

        // 실제 쓰레드 동작
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 선택된 프래그먼트가 2이면 무한 반복
                while( Common.whatFragment == 2) {
                    try{
                        getActivity().runOnUiThread(runnableUI); // 화면 업데이트 동작 실행
                        Thread.sleep(100);
                    }catch(Exception e){

                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return v;
    }
    public void checkNearBeacon3(){
        AudioManager  mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        // 3번 비콘
        if( Common.rssiBeacon[2] < 50 ) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            tv21.setText("도서관 입장\n 무음모드");
        } else{
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            tv21.setText("도서관 퇴장\n 무음모드 해제");}
    }

}
