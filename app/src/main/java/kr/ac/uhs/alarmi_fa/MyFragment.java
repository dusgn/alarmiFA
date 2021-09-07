package kr.ac.uhs.alarmi_fa;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class MyFragment extends Fragment {

    TextView tv11;
    TextView tv12;
    TextView tv13;

    int [] busTimeSche = {
            900,915,930,945,
            1000,1015,1030,1045,
            1100,1115,1130,1145,
            1200,1215,1230,1245,
            1300,1315,1330,1345,
            1400,1415,1430,1445,
            1500,1515,1530,1545,
            1600,1615,1630,1645,
            1700,1715,1730,1745,
            1800,1815,1830,1845
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment1, container, false);

        Common.whatFragment = 1;

        tv11 = (TextView) v.findViewById(R.id.tv11);
        tv12 = (TextView) v.findViewById(R.id.tv12);
        tv13 = (TextView) v.findViewById(R.id.tv13);

        // 화면 업데이트 동작
        final Runnable runnableUI = new Runnable() {
            @Override
            public void run() {
                checkNearBeacon4();
            }
        };

        // 실제 쓰레드 동작
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 선택된 프래그먼트가 1이면 무한 반복
                while( Common.whatFragment == 1) {
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

    private void prepairCheckNearBeacon4(){

    }

    public void checkNearBeacon4(){
        // 4번 비콘
        if( Common.rssiBeacon[3] < 50 ) {
            int nexttime = NextBusTime();
            if( nexttime < 0 )
                tv13.setText("버스 운행이 종료되었습니다");
            else{
                tv11.setText("다음 버스시간 까지");
                tv12.setText(nexttime/60+":" +nexttime%60);
                tv13.setText("");}
        } else {
            tv11.setText("");
            tv12.setText("");
            tv13.setText("신호가 잡히지 않아요\n비콘 범위를 넘어섰습니다");

        }

    }
    public int NextBusTime () {
        Calendar today = Calendar.getInstance();
        int currentHour = today.get(Calendar.HOUR_OF_DAY);
        int currentMin = today.get(Calendar.MINUTE);
        int currentSec = today.get(Calendar.SECOND);
        int mcurrenttime = ((currentHour * 60) + currentMin) * 60 + currentSec;
        int timelocker = 0; //4

        for (int i = 0; i < busTimeSche.length; i++) {
            int min = (int) (busTimeSche[i] / 100) * 60 + (int) (busTimeSche[i] % 100);
            int sec = min * 60;
            if (mcurrenttime < sec) {
                timelocker = sec;
                break;
            }
        }
        return timelocker - mcurrenttime;
    }
}
