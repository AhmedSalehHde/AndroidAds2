package ah1.com.advertisements_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Thread thread =new Thread(){

            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent inte=new Intent(getApplicationContext(),TabActivity.class);
                    startActivity(inte);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }
}
