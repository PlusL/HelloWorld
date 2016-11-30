package com.plus.ff.helloworld.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import com.plus.ff.helloworld.R;

import java.util.Calendar;

public class PopupSettingActivity extends Activity implements View.OnClickListener{

    private LinearLayout layout;
    private TextView label_5_text;
    private Calendar cal;
    private int year, month, day;
    //private List<EditText> editList = new ArrayList<EditText>();
    //private static String[] info = {"county","street","company","sponser","date"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_setting);
        layout = (LinearLayout)findViewById(R.id.layout_popup_setting);
        try {
            SharedPreferences sharedpre = getSharedPreferences("setinfo",Context.MODE_PRIVATE);
            getdate();
            //label_1_value
            String value1 = sharedpre.getString("county","defaulvalue");
            EditText edit1 = (EditText)findViewById(R.id.label_1_text);
            edit1.setText(value1);
            //label_2_value
            String value2 = sharedpre.getString("street","defaulvalue");
            EditText edit2 = (EditText)findViewById(R.id.label_2_text);
            edit2.setText(value2);
            //label_3_value
            String value3 = sharedpre.getString("company","defaulvalue");
            EditText edit3 = (EditText)findViewById(R.id.label_3_text);
            edit3.setText(value3);
            //label_4_value
            String value4 = sharedpre.getString("sponser","defaulvalue");
            EditText edit4 = (EditText)findViewById(R.id.label_4_text);
            edit4.setText(value4);
            //label_5_value
            String value5 = sharedpre.getString("date","defaulvalue");
            label_5_text = (TextView)findViewById(R.id.label_5_text);
            label_5_text.setText(value5);
        }
        catch (Exception ex){
            throw ex;
        }
        layout.setOnClickListener(this);
        label_5_text.setOnClickListener(this);
        findViewById(R.id.button_regist).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.label_5_text:
                OnDateSetListener listener=new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        label_5_text.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(PopupSettingActivity.this,0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月

                dialog.show();
                break;
            case R.id.button_regist:
                SharedPreferences sharedpre = getSharedPreferences("setinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpre.edit();
                //regist basic settings
                EditText edit1 = (EditText)findViewById(R.id.label_1_text);
                String value1 = edit1.getText().toString();
                EditText edit2 = (EditText)findViewById(R.id.label_2_text);
                String value2 = edit2.getText().toString();
                EditText edit3 = (EditText)findViewById(R.id.label_3_text);
                String value3 = edit3.getText().toString();
                EditText edit4 = (EditText)findViewById(R.id.label_4_text);
                String value4 = edit4.getText().toString();
                TextView edit5 = (TextView)findViewById(R.id.label_5_text);
                String value5 = edit5.getText().toString();
                editor.putString("county",value1);
                editor.putString("street",value2);
                editor.putString("company",value3);
                editor.putString("sponser",value4);
                editor.putString("date",value5);
                editor.commit();
                break;
            case R.id.button_cancel:
                this.finish();
                break;
            case R.id.layout_popup_setting:
                //// TODO: 2016/11/16  Auto-generated method stub
                Toast.makeText(getApplicationContext(),"提示：点击窗口外部关闭窗口",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    public void getdate()
    {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        Log.i("wxy","year"+year);
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }
}
