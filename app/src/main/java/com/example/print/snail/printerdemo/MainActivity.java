package com.example.print.snail.printerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.print.snail.printerdemo.view.PrinterTextView;

public class MainActivity extends AppCompatActivity {
    private PrinterTextView printerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printerTextView = (PrinterTextView) findViewById(R.id.print_text_view);
        Button bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    public void start() {
        printerTextView.setTextString("自定义view实现字符串逐字显示")
                .startPrintAnimation()
                .setTextAnimationListener(new PrinterTextView.TextAnimationListener() {
                    @Override
                    public void animationFinish() {
                        Toast.makeText(MainActivity.this, "展示结束", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
