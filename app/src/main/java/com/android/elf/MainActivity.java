package com.android.elf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.elf.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'elf' library on application startup.
    static {
        System.loadLibrary("elf");
    }

    private ActivityMainBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = MainActivity.this;

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        String libPath = mContext.getApplicationInfo().nativeLibraryDir + "/libelf.so";
        parsingElf(libPath);

        // java 解析
        byte[] byteArray = ElfUtils.fileToBytes(libPath);
        if (byteArray != null) {
            ElfParse.parseElfHeader(byteArray, 0);
            ElfParse.parseElfProgramHeader(byteArray);
            ElfParse.parseElfSectionHeader(byteArray);
        }
    }

    /**
     * A native method that is implemented by the 'elf' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void parsingElf(String path);
}