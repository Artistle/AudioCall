package com.example.audiocall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class CallsActivity extends AppCompatActivity {
    private Call call;
    private TextView callState;
    private SinchClient sinchClient;
    private Button button;
    private String callerId;
    private String recipientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        callerId = intent.getStringExtra("callerId");
        recipientId = intent.getStringExtra("recipientId");
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(callerId)
                .applicationKey("81ae61ee-b76f-430a-810c-a8341f8530f2")
                .applicationSecret("LXyZ/aPzTEm5T24xchLULw==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCall());

        button = (Button) findViewById(R.id.button);
        callState = (TextView) findViewById(R.id.callState);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make a call!
                if(call == null){
                    call = sinchClient.getCallClient().callUser(recipientId);
                    call.addCallListener(new SinchCallListener());
                    button.setText("Hang Up");
                }else{
                    call.hangup();
                    call = null;
                    button.setText("Call");
                }
            }
        });
    }


    class SinchCall implements CallClientListener{
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            call.answer();//метод принятия звонка
            call.addCallListener(new SinchCallListener());
            button.setText("Hang Up");
        }
    }

    public class SinchCallListener implements CallListener {
        @Override
        public void onCallProgressing(Call call) {
            //звонок звонит :)
            callState.setText("ringing");
            Toast.makeText(CallsActivity.this, "call progressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            //взодящий звонок принят
            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            Toast.makeText(CallsActivity.this, "call Established", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEnded(Call callEnded) {
            //любая сторона закончила вызов
            call = null;
            SinchError a = callEnded.getDetails().getError();
            button.setText("Call");
            callState.setText("");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            Toast.makeText(CallsActivity.this, "call ended", Toast.LENGTH_SHORT).show();
            Log.i("Error call ended", "error + " + a);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            //метод Push уведомлений
        }
    }
}