package com.campandroid.listviewtest;

import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	// 미디어플레이어 선언
	private MediaPlayer mPlayer;
    public  TextView sMusicName, duration;
		
	// 시간계산용
	private double timeElapsed = 0,   finalTime = 0;
	private int    forwardTime = 2000, backwardTime = 2000;
		
	// 핸들러
	private Handler durationHandler = new Handler();
		
	// seekbar 선언
	private SeekBar seekbar;
	
	// ListView관련 선언
	ListView mListView = null;
	mediaListAdapater mAdapter = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			
		setContentView(R.layout.activity_main);
	    
		SetUpUI();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity가 종료되면 플레이를 종료시킨다.
		if(mPlayer != null){
			mPlayer.stop();
		}
	}
		
	// 화면과 각종 VIew와 대입시킨다.
	public void SetUpUI(){
		sMusicName = (TextView) findViewById(R.id.txtMusic);
		
		sMusicName.setText("바이크로 떠다는 여행..");
		
		// mp3 파일을 선택한다.
		setMP3File(0);
		
		//미디어 정보들을 추가한다.
		AddMediaInfo();
	}
	
	public void setMP3File(int nIndx){
		int FILE_LIST[] = {R.raw.cafe, R.raw.dog, R.raw.cafe2};
		
		if(mPlayer != null){
			mPlayer.stop();
			mPlayer = null;
		}
		
		mPlayer = MediaPlayer.create(this, FILE_LIST[nIndx]);
		finalTime = mPlayer.getDuration();
		duration = (TextView) findViewById(R.id.txtDuration);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		
		seekbar.setMax((int) finalTime);
		seekbar.setClickable(false);
		
	}
	
	public void AddMediaInfo(){
        mListView = (ListView) findViewById(R.id.lstMain);
        
        mAdapter = new mediaListAdapater(getApplicationContext());
        mListView.setAdapter(mAdapter);
        
        mAdapter.addItem(getResources().getDrawable(R.drawable.biker),
                "바이크로 떠다는 여행..",
                "바이크로 떠나는 여행에 대한 XX님의 경험을 토대로 이야기가 전개됩니다.");
        mAdapter.addItem(getResources().getDrawable(R.drawable.dog),
                "애완동물이 아닌 반려동물",
                "애완동물에서 반려동물로 변해가는 우리의 친구들 이야기를 소탈하게 이야기 합니다.");
        mAdapter.addItem(getResources().getDrawable(R.drawable.macbook),
                "나도 맥북유저",
                "맥북 사용자가 되기 위한 기본 강좌입니다.");
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                ListData mData = mAdapter.mListData.get(position);
                sMusicName.setText(mData.mTitle);
                setMP3File(position);
            }
        });
	}

	// play
	public void play(View view) {
		mPlayer.start();
		timeElapsed = mPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
	}

	// Play 진행상황을 Thread로 채크한다.
	private Runnable updateSeekBarTime = new Runnable() {
		public void run() {
			// 현재위치 가져오기
		    timeElapsed = mPlayer.getCurrentPosition();
				
			//바로 진행상태를 보여준다.
			seekbar.setProgress((int) timeElapsed);
				
			// 지나간 시간을 보여준다.
			DislpayPassedTime();
				
			//repeat yourself that again in 100 miliseconds
			durationHandler.postDelayed(this, 100);
		}
	};
		
	// 시간계산이 지저분해서 짤라버렸다.
	@SuppressLint("NewApi")
	private void DislpayPassedTime(){
		double timeRemaining = finalTime - timeElapsed;
		long nMin = TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining);
		long nSec = 
			TimeUnit.MILLISECONDS.toSeconds( (long) timeRemaining )  
		    - 
		    TimeUnit.MINUTES.toSeconds(nMin);
			
		duration.setText(
			String.format("%dm %ds", 
		    nMin,
		    nSec)
		);
	}

	// 멈춘다.
	public void pause(View view) {
		mPlayer.pause();
	}

	// 앞으로(현재위치가 끝까지 갈 수 있는 위치라면)
	public void forward(View view) {
		if ((timeElapsed + forwardTime) <= finalTime) {
			timeElapsed = timeElapsed + forwardTime;
			mPlayer.seekTo((int) timeElapsed);
		}
	}

	// 뒤로(현재위치가 뒤로갔을 떄 0보다 크다면)
	public void rewind(View view) {
		if ((timeElapsed - backwardTime) > 0) {
			timeElapsed = timeElapsed - backwardTime;
			mPlayer.seekTo((int) timeElapsed);
		}
	}
}
