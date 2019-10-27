package com.dfrobot.angelo.blunobasicdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity  extends BlunoLibrary implements RemoReceiver.RemoListener, SeekBar.OnSeekBarChangeListener {
	private Button buttonScan;
	private Button buttonSerialSend;
    private SeekBar sliderTop;
    private SeekBar sliderBottom;
    private RemoReceiver remoReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        remoReceiver = new RemoReceiver(this, this);

		setContentView(R.layout.activity_main);
        onCreateProcess();														//onCreate Process by BlunoLibrary

        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);		//initial the button for sending the data
        buttonSerialSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				serialSend(getDriveArray(false));				//send the data to the BLUNO
			}
		});
        buttonSerialSend.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sliderTop.setProgress(256);
                sliderBottom.setProgress(256);
                return true;
            }
        });

        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
			}
		});

        sliderTop = findViewById(R.id.seekBarLeft);
        sliderBottom = findViewById(R.id.seekBarRight);
        sliderTop.setOnSeekBarChangeListener(this);
        sliderBottom.setOnSeekBarChangeListener(this);
        getDriveArray(true);
    }

    /**
     * Get the motor drive array for either USB or BLE
     * @param  updateUI if we should update the UI
     * @return a bytearray packet that is readable by the Sphero RVR
     */
	private byte[] getDriveArray(boolean updateUI) {
	    int[] left = parseMotor(sliderTop.getProgress());
	    int leftMode = left[1];
	    int leftSpeed = left[0];

        int[] right = parseMotor(sliderBottom.getProgress());
        int rightMode = right[1];
        int rightSpeed = right[0];
        byte[] command = SpheroMotors.drive(leftMode, leftSpeed, rightMode, rightSpeed);
        if(updateUI){
            updateUI(leftMode, leftSpeed, rightMode, rightSpeed, command);
        }
        return command;
    }

    private void updateUI(int leftMode, int leftSpeed, int rightMode, int rightSpeed, byte[] command) {
        StringBuilder builder = new StringBuilder();
        builder.append(getReadableDirection(leftMode)).append(",").append(leftSpeed).append("\n");
        builder.append(getReadableDirection(rightMode)).append(",").append(rightSpeed).append("\n");

        builder.append("[");
        for (int i = 0; i < command.length; i++) {
            builder.append(command[i]);
            if(i+1 < command.length)
                builder.append(",");
        }
        builder.append("]");

        TextView textView = findViewById(R.id.textViewDebug);
        textView.setText(builder);
    }

    private String getReadableDirection(int mode) {
	    switch (mode){
            case 0x01:
                return ">";
            case 0x02:
                return "<";
            default:
                return "X";
        }
    }

    private int[] parseMotor(int speed){
	    int[] result = new int[]{speed, 0x0};
        int direction;
        if(speed > 256) {
            //forward
            speed = speed - 257; //negate value some
            direction = 0x1;
        }
        else if (speed < 256){
            //reverse
            speed = 256-speed; //inverse the speed
            direction = 0x2;
        }
        else{
            //stop
			speed = 0;
            direction = 0x0;
        }

        if(speed > 255 || speed <= 0){
            speed = 0;
            direction = 0x0;
        }

        result[0] = speed;
        result[1] = direction;
        return result;
    }

    protected void onResume(){
		super.onResume();
        remoReceiver.register();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        remoReceiver.unregister();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }
	
	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			buttonScan.setText("Connected");
			serialSend(wakeUp);
			break;
		case isConnecting:
			buttonScan.setText("Connecting");
			break;
		case isToScan:
			buttonScan.setText("Scan");
			break;
		case isScanning:
			buttonScan.setText("Scanning");
			break;
		case isDisconnecting:
			buttonScan.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}

    @Override
    public void onCommand(String command) {
        int[] speed = parseMotor(sliderTop.getProgress());
        int leftMode = 0x0;
        int rightMode = 0x0;
        int leftSpeed = speed[0];
        int rightSpeed = speed[0];
        switch (command.replace("\r\n", "")){
            case "f":
                leftMode = 0x1;
                rightMode = 0x1;
                break;
            case "b":
                leftMode = 0x2;
                rightMode = 0x2;
                break;
            case "r":
                leftMode = 0x1;
                rightMode = 0x2;
                break;
            case "l":
                leftMode = 0x2;
                rightMode = 0x1;
                break;
        }
        byte[] commandByteArray = SpheroMotors.drive(leftMode, leftSpeed, rightMode, rightSpeed);

        updateUI(leftMode, leftSpeed, rightMode, rightSpeed, commandByteArray);

        serialSend(commandByteArray);
    }

	@Override
	public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
		// TODO Auto-generated method stub

	}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        getDriveArray(true);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //unused
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //unused
    }
}