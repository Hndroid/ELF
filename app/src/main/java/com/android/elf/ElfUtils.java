package com.android.elf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class ElfUtils {

    public static byte[] copyBytes(byte[] fileBuff, int start, int count) {
        if (fileBuff == null || fileBuff.length == 0) {
            return null;
        }

        byte[] item = new byte[count];
        for (int i = 0; i < count; ++i) {
            item[i] = fileBuff[start + i];
        }
        return item;
    }

    public static String bytesToHexString(byte[] bytes){
        final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] buf = new char[bytes.length * 3];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
            buf[index++] = ' ';
        }

        return new String(buf);
    }

    public static byte[] fileToBytes(String path) {
        File lFile = new File(path);
        if (lFile.exists()) {
            byte[] tempBuff = new byte[1024];
            byte[] result = null;
            FileInputStream inputStream = null;
            ByteArrayOutputStream arrayOutputStream = null;
            try {
                inputStream = new FileInputStream(lFile);
                arrayOutputStream = new ByteArrayOutputStream(1024);
                int length = 0;
                while ((length = inputStream.read(tempBuff)) != -1) {
                    arrayOutputStream.write(tempBuff, 0, length);
                }
                result = arrayOutputStream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (arrayOutputStream != null) {
                    try {
                        arrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
        return null;
    }

    public static int byteToInt(byte[] bytes) {
        int targets = 0;
        if (bytes.length == 2) {
            targets = (bytes[0] & 0xff)
                    | ((bytes[1] << 8) & 0xff00);
        }
        if (bytes.length == 4) {
            targets = (bytes[0] & 0xff)
                    | ((bytes[1] << 8) & 0xff00)
                    | ((bytes[2] << 24) >>> 8)
                    | (bytes[3] << 24);
        }
        return targets;
    }

}
