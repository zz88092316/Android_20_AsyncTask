package com.example.android_20_asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    Button download;
    ProgressBar pb;
    TextView tv;
    private ImageView img;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb=(ProgressBar)findViewById(R.id.pb);
        tv=(TextView)findViewById(R.id.tv);
        img=(ImageView)findViewById(R.id.imageView);

        download = (Button)findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/LetterA.svg/1200px-LetterA.svg.png");
            }
        });
    }

    class DownloadTask extends AsyncTask<String, Integer, Bitmap>{
        //後面尖括號內分別是參數（例子裏是線程休息時間），進度(publishProgress用到)，返回值 類型
        HttpURLConnection connection;
        @Override
        protected void onPreExecute() {
            //第一個執行方法
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //第二個執行方法,onPreExecute()執行完後執行
            for(int i=0;i<=100;i++){
                pb.setProgress(i);
                publishProgress(i);//調用onProgressUpdate(Integer... progress)方法
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream input = new BufferedInputStream((connection.getInputStream()));
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                return bitmap;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            finally {
                connection.disconnect();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            tv.setText(progress[0]+"%");
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                img.setImageBitmap(result);
                Toast.makeText(getApplicationContext(), "Download Successful!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Download Error!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}