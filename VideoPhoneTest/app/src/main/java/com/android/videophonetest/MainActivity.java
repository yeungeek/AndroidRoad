package com.android.videophonetest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.util.Log;

import com.android.avPipe.AVTypes;
import com.android.avPipe.VideoCapture;
import com.android.avPipe.AVTypes;

public class MainActivity extends Activity {

    private static final String TAG = "SettingsActivity";

    private Spinner spSettingsEncoderType;
    //private EditText etSettingsEncoderRate;
    private Spinner etSettingsEncoderRate;
    private Spinner spSettingsResolution;
    private Spinner spSettingsFrameRate;
    private Spinner spSettingsRecordLength;
    private EditText etSettingsIFrameInterval;
    private Button btnSettingsStart;
    private AVTypes.EncoderOptimizations opts = AVTypes.getEncoderOptsInstance();
	
	private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (checkPermission(permission, android.os.Process.myPid(), Process.myUid())
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                int i = 0;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, VIDEO_PERMISSIONS[i]+" premisson not granted");
                    }
                    i++;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingResource();
        spSettingsEncoderType.requestFocus();
        //Encoder Type
        spSettingsEncoderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opts.setEncodertype((String) spSettingsEncoderType.getSelectedItem());
                opts.encoderTypePos = i;
                if (i == 0) {
                    opts.encoderBitRatePos = 2;
                    opts.setEncoder_bitrate(2048);
                    etSettingsEncoderRate.setSelection(opts.encoderBitRatePos);
                } else {
                    opts.encoderBitRatePos = 1;
                    opts.setEncoder_bitrate(4096);
                    etSettingsEncoderRate.setSelection(opts.encoderBitRatePos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Encoder Rate
        etSettingsEncoderRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opts.setEncoder_bitrate(Integer.parseInt((String) etSettingsEncoderRate.getSelectedItem()));
                opts.encoderBitRatePos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*etSettingsEncoderRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //String debugString = String.valueOf(opts.getEncoder_bitrate());
                //Log.e(TAG, "debug" + debugString);
                //etSettingsEncoderRate.setText(debugString);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etSettingsEncoderRate.getText().toString().equals(""))
                    opts.setEncoder_bitrate(Integer.parseInt(etSettingsEncoderRate.getText().toString()));
                //opts.encoder_bitrate_enable = true;
            }
        });*/

        //Resolution
        spSettingsResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] strArrayResolution = ((String)spSettingsResolution.getSelectedItem()).split("x");
                for (int n = 0;n<strArrayResolution.length;n++) {
                    Integer.parseInt(strArrayResolution[n].trim());
                    opts.resolution[n] = Integer.parseInt(strArrayResolution[n].trim());
                }
                opts.setResolution(opts.resolution);
                opts.encoderResolutionPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Frame Rate
        spSettingsFrameRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opts.setEncoder_framerate(Integer.parseInt((String)spSettingsFrameRate.getSelectedItem()));
                opts.encodeFrameRatePos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Record Length
        spSettingsRecordLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opts.setEncoder_timeLimit(Integer.parseInt((String) spSettingsRecordLength.getSelectedItem()));
                opts.encoderRecoderPos = i;
                // opts.encoder_timeLimit_enable = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //I Frame Interval

        etSettingsIFrameInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //etSettingsIFrameInterval.setText(String.valueOf(opts.getEncoder_iframe()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etSettingsIFrameInterval.getText().toString().equals(""))
                    opts.setEncoder_iframe(Integer.parseInt(etSettingsIFrameInterval.getText().toString()));
                //opts.encoder_iframe_interval_enable = true;
            }
        });

        //start
        btnSettingsStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,VideoPlayerActivity.class);
                startActivity(intent);
                finish();
            }
        });
		
		if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            Log.d(TAG, "no permission granted!!!");
            requestPermissions(VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        } else {
            Log.d(TAG, "permission has been granted");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 弹出确定退出对话框
            new AlertDialog.Builder(this)
                    .setTitle("退出")
                    .setMessage("确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // TODO Auto-generated method stub
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                }
                            }).show();
            // 这里不需要执行父类的点击事件，所以直接return
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void loadingResource() {
        spSettingsEncoderType = (Spinner) findViewById(R.id.settings_encoder_type_spinner);
        //etSettingsEncoderRate = (EditText) findViewById(R.id.settings_encoder_rate_et);
        etSettingsEncoderRate = (Spinner) findViewById(R.id.settings_encoder_rate_spinner);
        spSettingsResolution = (Spinner) findViewById(R.id.settings_resolution_spinner);
        spSettingsFrameRate = (Spinner) findViewById(R.id.settings_frame_rate_spinner);
        spSettingsRecordLength = (Spinner) findViewById(R.id.settings_record_length_spinner);
        etSettingsIFrameInterval = (EditText) findViewById(R.id.settings_i_frame_interval_et);
        btnSettingsStart = (Button) findViewById(R.id.settings_start_btn);

        spSettingsEncoderType.setSelection(opts.encoderTypePos);
        spSettingsResolution.setSelection(opts.encoderResolutionPos);
        spSettingsFrameRate.setSelection(opts.encodeFrameRatePos);
        spSettingsRecordLength.setSelection(opts.encoderRecoderPos);
        if (opts.encoderTypePos == 0) {
            opts.encoderBitRatePos = 2;
            opts.setEncoder_bitrate(2048);
        } else {
            opts.encoderBitRatePos = 1;
            opts.setEncoder_bitrate(4096);
        }
        //etSettingsEncoderRate.setText(String.valueOf(opts.getEncoder_bitrate()));
        etSettingsEncoderRate.setSelection(opts.encoderBitRatePos);
        etSettingsIFrameInterval.setText(String.valueOf(opts.getEncoder_iframe()));
    }

}
