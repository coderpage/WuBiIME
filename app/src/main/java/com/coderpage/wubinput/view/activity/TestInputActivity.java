package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.model.Content;
import com.coderpage.wubinput.tools.MLog;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author abner-l
 * @since 2015-08-31
 */
public class TestInputActivity extends BaseInputActivity {
    private final String tag = TestInputActivity.class.getSimpleName();
    private final boolean debug = true;

    private final int TIME_INCREASE = 0x1;
    private final int UPDATE_ACCURACY = 0x2;
    private final int UPDATE_SPEED = 0x3;

    private TextView typingAccuracy;
    private TextView typingTime;
    private TextView typingSpeed;


    // 当前行打错的字数
    private int currentWrongWordSize = 0;
    // 不连当前行所有打错字数
    private int wrongWordTotalSize = 0;

    // 不连当前行多有打的字数，不连标点符号
    private int preLineTypedWordsSize;
    // 当前行打的字数，不连标点符号
    private int currentTypedWordsSize;

    private Stack<Integer> preLineWrongWordSize = new Stack<>();
    private Content content;

    // 计时器
    private Timer timer;
    // 用时 秒为单位
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
                case UPDATE_ACCURACY:
                    updateAccuracy(msg.obj.toString());
                    break;
                case UPDATE_SPEED:
                    updateSpeed(msg.obj.toString());
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

//    @Override
//    protected void initContent() {
//        dbHelper = DictionaryDBHelper.getInstance(TestInputActivity.this);
//        String data = dbHelper.queryArticle(1);
//
//        if (inputSourceTV == null) {
//            inputSourceTV = (TextView) View.inflate(TestInputActivity.this, R.layout.input_src_view, null);
//        }
//
//        this.content = new Content(data, inputSourceTV);
//        this.lines = content.getLines();
//    }

    @Override
    protected void onCurChanged(String data) {
        CharSequence currentLineText = currentInputView.getInputSrcView().getText();

        int currentLineMaxLen = currentLineText.length();
        if (data.length() > currentLineMaxLen) {
            currentTypedWordsSize = countHanZi(data, 0, currentLineMaxLen);
        } else {
            currentTypedWordsSize = countHanZi(data, 0, data.length());
        }
        super.onCurChanged(data);

        Message msg = new Message();
        msg.what = UPDATE_ACCURACY;
        msg.obj = "正确率:" + calculateAccuracy();
        handler.sendMessage(msg);

        msg = new Message();
        msg.what = UPDATE_SPEED;
        msg.obj = calculateSpeed() + "字/分钟";
        handler.sendMessage(msg);

        if (debug) {
            MLog.debug(tag, "onCurChanged.");
        }
    }

    @Override
    protected void nextLine(String overData) {
        preLineWrongWordSize.add(currentWrongWordSize);
        wrongWordTotalSize += currentWrongWordSize;

        CharSequence currentLineText = currentInputView.getInputSrcView().getText();
        preLineTypedWordsSize += countHanZi(currentLineText, 0, currentLineText.length());
        super.nextLine(overData);

    }

    @Override
    protected void preLine() {
        super.preLine();
        int preWrongWordSize = preLineWrongWordSize.pop();
        wrongWordTotalSize -= preWrongWordSize;
        currentWrongWordSize = preWrongWordSize;

        CharSequence currentLineText = currentInputView.getInputSrcView().getText();
        preLineTypedWordsSize -= countHanZi(currentLineText, 0, currentLineText.length());
    }

    @Override
    protected void markWrongWord(int position) {
        currentWrongWordSize = position;
    }

    @Override
    protected void onDelText() {
        super.onDelText();
    }

    /**
     * 计算正确率
     */
    private String calculateAccuracy() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setRoundingMode(RoundingMode.HALF_UP);

        int haveTypedWordSize = calculateHaveTypedWords();
        if (haveTypedWordSize == 0){
            return "100%";
        }
        int correctWordSize = haveTypedWordSize - calculateWrongWords();
        float accuracyNum = (float) (correctWordSize) / (float) haveTypedWordSize * 100;

        return df.format(accuracyNum) + "%";
    }

    /**
     * 计算速度
     */
    private int calculateSpeed() {
        return calculateHaveTypedWords() * 60 / time;
    }

    /**
     * 计算已经打的字数，不连标点符号
     */
    private int calculateHaveTypedWords() {
        return currentTypedWordsSize + preLineTypedWordsSize;
    }

    /**
     * 计算所有打错的字数，包含标点符号
     */
    private int calculateWrongWords() {
        return currentWrongWordSize + wrongWordTotalSize;
    }

    /**
     * 更新用时时间
     */
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

    /**
     * 更新正确率
     */
    private void updateAccuracy(String accuracy) {
        typingAccuracy.setText(accuracy);
    }

    /**
     * 更新打字速度
     */
    private void updateSpeed(String speed) {
        typingSpeed.setText(speed);
    }

    /**
     * 统计汉字个数
     */
    private int countHanZi(CharSequence cs, int offset, int length) {
        int counter = 0;
        // 由于本方法自己使用，为了减少比较次数，此处没有进行边界检查，使用时请注意
        for (int i = offset; i < length; i++) {
            if (isHanZi(cs.charAt(i))) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * 判断是否是汉字
     *
     * @param c 需判断的字符
     * @return 若是汉字，返回 true，反之，返回 false
     */
    private boolean isHanZi(char c) {

        return c >= 0x4e00 && c < 0x9fff;
    }

}
