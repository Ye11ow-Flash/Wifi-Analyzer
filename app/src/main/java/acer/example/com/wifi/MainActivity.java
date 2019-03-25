package acer.example.com.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
{
    Button btn1, btn2, btn3;
    TextView tv1;
    WifiManager wifiManager;
    WifiInfo connection;
    String output = "";
    String displayOutput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        tv1 = findViewById(R.id.tv1);
        tv1.setMovementMethod(new ScrollingMovementMethod());
        btn1.setOnClickListener(new View.OnClickListener() {
            int br = 0;
            @Override
            public void onClick(View view)
            {
                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        connection = wifiManager.getConnectionInfo();
                        output += "\nSSID = "+connection.getSSID()+"\nRSSI = +"+connection.getRssi()+"\nMacAddress = "+connection.getMacAddress();
                        output += "\nBSSID = "+connection.getBSSID()+"IpAddress = "+connection.getIpAddress()+"\nNetworkID = "+connection.getNetworkId();
                        tv1.setText(output);
                    }

                    public void onFinish() {
                        //tv1.setText("D O N E");
                    }
                }.start();

//                Timer t = new Timer();
//                t.scheduleAtFixedRate(new TimerTask() {
//
//                     @Override
//                     public void run() {
//                         //Called each time when 1000 milliseconds (1 second) (the period parameter)
//                         br = br + 1;
//                         wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                         connection = wifiManager.getConnectionInfo();
//                         output += "\nSSID = "+connection.getSSID()+"\nRSSI = +"+connection.getRssi()+"\nMacAddress = "+connection.getMacAddress();
//                         output += "\nBSSID = "+connection.getBSSID()+"IpAddress = "+connection.getIpAddress()+"\nNetworkID = "+connection.getNetworkId();
//                         tv1.setText(output+"BR = "+br);
//                         if(br == 10){
//                             return;
//                         }
//                     }
//                },
//                //Set how long before to start calling the TimerTask (in milliseconds)
//               0,
//                //Set the amount of time between each execution (in milliseconds)
//               1000);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                File file = new File(getApplicationContext().getFilesDir(),"WiFi");
                if(!file.exists())
                {
                    file.mkdir();
                }

                try
                {
                    File gpxfile = new File(file, "mcan.txt");
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(output);
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity.this, "Write successful", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Write failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    File file = new File(getApplicationContext().getFilesDir(), "WiFi");
                    File gpxfile = new File(file, "mcan.txt");
                    //FileInputStream fis = new FileInputStream(gpxfile);
                    //FileReader reader = new FileReader(gpxfile);
                    FileInputStream fis = new FileInputStream(gpxfile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    displayOutput = "-----String coming from File-----\n";
                    while((line = br.readLine())!=null)
                    {
                        displayOutput += line;
                        displayOutput += "\n";
                    }
                    tv1.setText(displayOutput);

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
