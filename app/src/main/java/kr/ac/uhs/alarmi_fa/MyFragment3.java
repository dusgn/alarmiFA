package kr.ac.uhs.alarmi_fa;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class MyFragment3 extends Fragment {


    TextView tv31;
    TextView tv32;
    TextView tv33;
    TextView tv34;

    int [] classStart ={900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300};
    int [] classfinish={940,1040,1140,1240,1340,1440,1540,1640,1740,1840,1940,2040,2140,2240,2340};

    int timelockerstart = 0;
    int timelockerfinish = 0;

    int extraStartTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment3, container, false);

        Common.whatFragment = 3;

        tv31 = (TextView) v.findViewById(R.id.tv31);
        tv32 = (TextView) v.findViewById(R.id.tv32);
        tv33 = (TextView) v.findViewById(R.id.tv33);
        tv34 = (TextView) v.findViewById(R.id.tv34);


        // 화면 업데이트 동작
        final Runnable runnableUI = new Runnable() {
            @Override
            public void run() {
                checkNearBeacon2();
            }
        };

        // 실제 쓰레드 동작
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 선택된 프래그먼트가 3이면 무한 반복
                while( Common.whatFragment == 3) {
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

    private void prepairCheckNearBeacon2(){

    }
    public void CompareClassTime () {//Start배열 Finish배열크기를 각각의timelocker넣어줌
        Calendar today = Calendar.getInstance();
        int currentHour = today.get(Calendar.HOUR_OF_DAY);
        int currentMin = today.get(Calendar.MINUTE);
        int currentSec = today.get(Calendar.SECOND);

        int mcurrenttime = ((currentHour * 60) + currentMin) * 60 + currentSec;

        for (int i = 0; i < classStart.length; i++) {
            int Smin = (int) (classStart[i] / 100) * 60 + (int) (classStart[i] % 100);
            int Ssec = Smin * 60;
            if (mcurrenttime < Ssec) {
                timelockerstart = Ssec;
                break;
            }
        }
        for (int i = 0; i < classfinish.length; i++) {
            int Fmin = (int) (classfinish[i] / 100) * 60 + (int) (classfinish[i] % 100);
            int Fsec = Fmin * 60;
            if (mcurrenttime < Fsec) {
                timelockerfinish = Fsec;
                break;
            }
        }
        if (timelockerstart < timelockerfinish) {
            extraStartTime = timelockerstart - mcurrenttime;
            tv31.setText("수업시간까지" ) ;
            tv32.setText(extraStartTime / 60 + ":" + extraStartTime % 60);
            tv33.setText("출석되었습니다");
        } else {
            extraStartTime = timelockerfinish - mcurrenttime;
            tv31.setText("다음 수업시간은");
            tv32.setText(extraStartTime / 60 +":" + extraStartTime % 60);
            tv33.setText("지각입니다");
        }
    }



    public void checkNearBeacon2() {
        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (Common.rssiBeacon[1] < 50) {
            CompareClassTime();
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            tv34.setText("이공관\n503호");
        } else if(Common.rssiBeacon[0] < 50) {
            CompareClassTime();
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            tv34.setText("이공관\n504호");
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            tv31.setText("강의실이 아닙니다");
            tv32.setText("");
            tv33.setText("");
            tv34.setText("");
        }
    }

}
