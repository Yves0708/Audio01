package net.cloud95.android.lession.audio01;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

public class Audio01Activity extends Activity {

    private SeekBar control;

    private MediaPlayer mediaPlayer;

    private ImageButton record_button;
    private boolean isRecording = false;
    private ProgressBar record_volumn;

    private MyRecoder myRecoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 需要在狀態列顯示處理中圖示，
        // 一定要在指定Activity元件畫面配置資源之前，
        // 使用這行敘述執行設定
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_audio01);
        
        processViews();
        processControllers();

        // 建立指定資源的MediaPlayer物件
        mediaPlayer = MediaPlayer.create(this, R.raw.sound01);
        // 註冊播放完畢監聽事件
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer player) {
				// TODO Auto-generated method stub
				// 切換按鈕為可播放
				clickStop(null);
			}
		});
                
        
        // 設定SeekBar元件的最大值為音樂的總時間（毫秒）
        control.setMax(mediaPlayer.getDuration());
    }

    private void processViews() {
        control = (SeekBar) findViewById(R.id.control);
        record_button = (ImageButton) findViewById(R.id.record_button);
        record_volumn = (ProgressBar) findViewById(R.id.record_volumn);
        
        // 隱藏狀態列ProgressBar
        setProgressBarIndeterminateVisibility(false);
    }

    private void processControllers() {
        // 註冊SeekBar元件進度改變事件
    	control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// 一定要判斷是使用者的操作，因為播放過程也會更改進度
				if(fromUser){
					 // 移動音樂到指定的進度
					mediaPlayer.seekTo(progress);
				}
			}
		});
                    
                       
    }
    
    public void clickButton01(View view) {
        // 取得外部儲存裝置路徑
    	File file = Environment.getExternalStorageDirectory();
        // 建立外部儲存裝置下的音樂檔案Uri物件
    	Uri uri = Uri.parse(file.getAbsolutePath()+"/sound02.mp3");
        // 建立播放Uri指定音樂用的MediaPlayer物件
    	MediaPlayer player = MediaPlayer.create(this, uri);
        // 如果檔案不存在
    	if(player == null){
    		Toast.makeText(this, "File not found: \n"+file.getAbsolutePath()+"/sound02.mp3", Toast.LENGTH_LONG).show();
    		return;
    	}
    	try{
            // 準備MediaPlayer物件
    		player.prepare();
    	}catch(IOException e){
    		Log.d("Audio01Activity",e.toString());
    		return;
    	}
        // 註冊播放完成事件
    	player.setOnCompletionListener(new MyCompletion());
        // 開始播放
    	player.start();
    }

    public void clickButton02(View view) {
        // 建立播放應用程式音樂資源用的MediaPlayer物件
    	MediaPlayer player = MediaPlayer.create(this, R.raw.sound02);
        // 註冊播放完成事件
    	player.setOnCompletionListener(new MyCompletion());
        // 開始播放
    	player.start();
    }

    public void clickButton03(View view) {
        // 建立並執行從網路載入音樂與播放的AsyncTask物件
    	new NetworkTask().execute("http://www.ntust20140311ai2.comuv.com/song01.mp3");
    }

    public void clickButton04(View view) {
        // 建立系統預設來電鈴聲的Uri物件
    	Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
        // 建立播放Uri指定音樂用的MediaPlayer物件
    	MediaPlayer player = MediaPlayer.create(this,uri);
        // 如果沒有系統預設來電鈴聲
    	if(player == null){
    		Toast.makeText(this, "DEFAULT_RINGTONE not found", Toast.LENGTH_LONG).show();
    		return;
    	}
        // 註冊播放完成事件
    	player.setOnCompletionListener(new MyCompletion());
        // 開始播放
    	player.start();
    }

    public void clickPlay(View view) {
        // 開始播放
    	mediaPlayer.start();
        // 建立並執行顯示播放進度的AsyncTask物件
    	new MyPlayTask().execute();
    }

    public void clickPause(View view) {
        // 暫停播放
    	mediaPlayer.pause();
    }

    public void clickStop(View view) {
        // 停止播放
    	mediaPlayer.stop();
    	
    	try{
            // 重新設定
    		mediaPlayer.prepare();
    	}catch(IOException e){
    		Log.d("Audio01Activity",e.toString());
    	}
    	// 回到開始的位置
    	mediaPlayer.seekTo(0);
    	control.setProgress(0);
    }

    public void clickRecord(View view) {
        // 錄音中
            // 設定按鈕圖示為錄音中
            // 取得外部儲存裝置路徑
            // 建立錄音物件，儲存檔名為外部儲存裝置下的Audio01Activity.mp3
            // 開始錄音
            // 建立並執行顯示麥克風音量的AsyncTask物件
        // 停止錄音
            // 設定按鈕圖示為停止錄音
            // 麥克風音量歸零
            // 停止錄音
    }

    public void clickRecordPlay(View view) {
        // 取得外部儲存裝置（SD Card）路徑
        
        // 如果錄音檔案存在
            // 建立外部儲存裝置下的音樂檔案Uri物件
            // 建立播放Uri指定音樂用的MediaPlayer物件
            // 註冊播放完成事件
            // 開始播放
    }

    // 播放完成監聽類別
    private class MyCompletion implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer player) {
			// TODO Auto-generated method stub
			// 清除MediaPlayer物件
			player.release();
		}
            
    }

    // 在錄音過程中顯示麥克風音量
    private class MicLevelTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			// TODO Auto-generated method stub
			while (isRecording){
				publishProgress();//會去呼叫執行下面的onProgressUpdate方法,這樣就不是在UI THREAD下去修改UI
				
				try{
					Thread.sleep(200);
				}catch(InterruptedException e){
					Log.d("Audio01Activity",e.toString());
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			record_volumn.setProgress((int)myRecoder.getAmplitudeEMA());
		}
		

    }

    // 從指定的網路資源載入音樂並開始播放
    private class NetworkTask extends AsyncTask<String, Void, MediaPlayer> {
    
            // 顯示下載中
            // 建立網路資源音樂檔案Uri物件
            // 取消顯示下載中
            // 開始播放
    }

    // 在播放過程中顯示播放進度
    private class MyPlayTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			// TODO Auto-generated method stub
			while(mediaPlayer.isPlaying()){
				// 設定播放進度
				control.setProgress(mediaPlayer.getCurrentPosition());
				
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					Log.d("Audio01Activity", e.toString());
				}
			}
			return null;
		}
                
    	
    }

    // 執行錄音並且可以取得麥克風音量的錄音物件
    private class MyRecoder {
    	private static final double EMA_FILTER = 0.6;
    	private MediaRecorder recorder = null;
    	private double mEMA = 0.0;
    	private String output;
        // 建立錄音物件，參數為錄音儲存的位置與檔名
    	MyRecoder(String output){
    		this.output = output;
    	}
        // 開始錄音
    	public void start(){
    		if (recorder == null){
    			// 建立錄音用的MediaRecorder物件
    			recorder = new MediaRecorder();
    			// 設定錄音來源為麥克風，必須在setOutputFormat方法之前呼叫
    			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    			// 設定輸出格式為3GP壓縮格式，必須在setAudioSource方法之後，
                // 在prepare方法之前呼叫
    			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    			// 設定錄音的編碼方式，必須在setOutputFormat方法之後，
                // 在prepare方法之前呼叫
    			recorder.setOutputFile(output);
    			
    			try{
    				// 準備執行錄音工作，必須在所有設定之後呼叫
    				recorder.prepare();
    			}catch(IOException e){
    				Log.d("Audio01Activity", e.toString());
    			}
    			// 開始錄音
    			recorder.start();
    			mEMA=0.0;
    		}     
    	}
        // 停止錄音
    	public void stop(){
    		if(recorder != null){
    			// 停止錄音
    			recorder.stop();
    			// 清除錄音資源
    			recorder.release();
    			recorder=null;
    		}
    	}
    	//得到一個新值
         public double getAmplitude(){
        	 if(recorder != null){
        		 return (recorder.getMaxAmplitude()/2700.0);
        	 }else {
        		return 0;	 
        	 }
         }       
        // 取得麥克風音量
         public double getAmplitudeEMA(){
        	 double amp = getAmplitude();
        	 mEMA = EMA_FILTER*amp+(1.0-EMA_FILTER)*mEMA;
        	 return mEMA;
         }
    }

}
