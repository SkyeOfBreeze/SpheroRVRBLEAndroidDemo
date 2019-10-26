package com.dfrobot.angelo.blunobasicdemo;

/**
 * Created by Brendon on 10/23/2019.
 */
public class SpheroMotors {
    public static byte[] driveCommand = new byte[]{
            (byte)-115,
            (byte)24,
            (byte)2,
            (byte)22,
            (byte)1,
            (byte)0,
            (byte)2,
            (byte)-119,
            (byte)1,
            (byte)-119,
            (byte)-87,
            (byte)-40
    };

    public static byte[] drive(int leftMode, int leftSpeed, int rightMode, int rightSpeed){
        driveCommand[5]++;
        driveCommand[6] = (byte) leftMode;
        driveCommand[7] = (byte) leftSpeed;
        driveCommand[8] = (byte) rightMode;
        driveCommand[9] = (byte) rightSpeed;
        driveCommand[10] = checksum(driveCommand, 10);
        return driveCommand;
    }

    private static byte checksum(byte[] driveCommand, int checksumIndex) {
        byte sum = 0;
        //skip index 0
        for(int i = 1; i < checksumIndex; i++){
            sum += driveCommand[i] ;
        }
        //sum += 1;
        return (byte) ((sum & 0xFF)^0xFF);
    }
}
