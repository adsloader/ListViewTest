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

	// �̵���÷��̾� ����
	private MediaPlayer mPlayer;
    public  TextView sMusicName, duration;
		
	// �ð�����
	private double timeElapsed = 0,   finalTime = 0;
	private int    forwardTime = 2000, backwardTime = 2000;
		
	// �ڵ鷯
	private Handler durationHandler = new Handler();
		
	// seekbar ����
	private SeekBar seekbar;
	
	// ListView���� ����
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
		// activity�� ����Ǹ� �÷��̸� �����Ų��.
		if(mPlayer != null){
			mPlayer.stop();
		}
	}
		
	// ȭ��� ���� VIew�� ���Խ�Ų��.
	public void SetUpUI(){
		sMusicName = (TextView) findViewById(R.id.txtMusic);
		
		sMusicName.setText("����ũ�� ���ٴ� ����..");
		
		// mp3 ������ �����Ѵ�.
		setMP3File(0);
		
		//�̵�� �������� �߰��Ѵ�.
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
                "����ũ�� ���ٴ� ����..",
                "����ũ�� ������ ���࿡ ���� XX���� ������ ���� �̾߱Ⱑ �����˴ϴ�.");
        mAdapter.addItem(getResources().getDrawable(R.drawable.dog),
                "�ֿϵ����� �ƴ� �ݷ�����",
                "�ֿϵ������� �ݷ������� ���ذ��� �츮�� ģ���� �̾߱⸦ ��Ż�ϰ� �̾߱� �մϴ�.");
        mAdapter.addItem(getResources().getDrawable(R.drawable.macbook),
                "���� �ƺ�����",
                "�ƺ� ����ڰ� �Ǳ� ���� �⺻ �����Դϴ�.");
        
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

	// Play �����Ȳ�� Thread�� äũ�Ѵ�.
	private Runnable updateSeekBarTime = new Runnable() {
		public void run() {
			// ������ġ ��������
		    timeElapsed = mPlayer.getCurrentPosition();
				
			//�ٷ� ������¸� �����ش�.
			seekbar.setProgress((int) timeElapsed);
				
			// ������ �ð��� �����ش�.
			DislpayPassedTime();
				
			//repeat yourself that again in 100 miliseconds
			durationHandler.postDelayed(this, 100);
		}
	};
		
	// �ð������ �������ؼ� ©����ȴ�.
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

	// �����.
	public void pause(View view) {
		mPlayer.pause();
	}

	// ������(������ġ�� ������ �� �� �ִ� ��ġ���)
	public void forward(View view) {
		if ((timeElapsed + forwardTime) <= finalTime) {
			timeElapsed = timeElapsed + forwardTime;
			mPlayer.seekTo((int) timeElapsed);
		}
	}

	// �ڷ�(������ġ�� �ڷΰ��� �� 0���� ũ�ٸ�)
	public void rewind(View view) {
		if ((timeElapsed - backwardTime) > 0) {
			timeElapsed = timeElapsed - backwardTime;
			mPlayer.seekTo((int) timeElapsed);
		}
	}
}
