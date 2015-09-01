package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.model.Content;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author abner-l
 * @since 2015-08-31
 */
public class TestInputActivity extends BaseInputActivity {
    private final int TIME_INCREASE = 0x1;

    private TextView typingAccuracy;
    private TextView typingTime;
    private TextView typingSpeed;

    private Timer timer;
    private int time;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            time++;
            handler.sendEmptyMessageAtTime(TIME_INCREASE, 0);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case TIME_INCREASE:
                    updateTime();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);
        super.onCreate(savedInstanceState);
        initView();
        timer = new Timer(true);
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_test_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        typingAccuracy = (TextView) findViewById(R.id.typing_accuracy);
        typingTime = (TextView) findViewById(R.id.typing_time);
        typingSpeed = (TextView) findViewById(R.id.typing_speed);

        typingAccuracy.setText("正确率:100%");
        typingTime.setText("00:00");
        typingSpeed.setText("速度:0字/分");
    }

    @Override
    protected void initContent() {
        dbHelper = DictionaryDBHelper.getInstance(TestInputActivity.this);
        String data = dbHelper.queryArticle(1);

        if (inputSourceTV == null) {
            inputSourceTV = (TextView) View.inflate(TestInputActivity.this, R.layout.input_src_view, null);
        }

        Content content = new Content(data, inputSourceTV);
        this.lines = content.getLines();
    }

    @Override
    protected void onCurChanged(String data) {
        super.onCurChanged(data);


    }

    private void updateTime() {

        StringBuilder builder = new StringBuilder(5);
        int t = time % 3600;

        int min = t / 60;

        if (min > 9) {
            builder.append(min).append(':');
        } else {
            builder.append(0).append(min).append(':');
        }

        int sec = t % 60;
        if (sec > 9) {
            builder.append(sec);
        } else {
            builder.append(0).append(sec);
        }

        typingTime.setText(builder.toString());
    }

    private String getMsec() {
        long msecL = (time % 1000) / 10;
        String msecS = String.valueOf(msecL);

        if (msecL > 9) {
            return msecS;
        } else {
            return "0" + msecS;
        }
    }

    private String getSec() {
        int sec = time % 60;
        String secStr = String.valueOf(sec);

        if (sec > 9) {
            return secStr;
        } else {
            return "0" + secStr;
        }
    }

    private String getMin() {
        int min = time / 60;
        String minS = String.valueOf(min);

        if (min > 9) {
            return minS;
        } else {
            return "0" + minS;
        }
    }


}
