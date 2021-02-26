package jp.ac.jec.cm0113.mywordbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //学習ボタンを押したら、StudyModeSelectActivityへ遷移する
        Button btnStudy = (Button)findViewById(R.id.btnStudy);
        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudyModeSelectActivity.class);
                startActivity(intent);
            }
        });

        //追加ボタンを押したら、EditActivityの追加モードへ遷移する
        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("mode", "add"); //追加モード
                startActivity(intent);
            }
        });

        //単語帳ボタンを押したら、CardListActivityへ遷移する
        Button btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardListActivity.class);
                startActivity(intent);
            }
        });
    }
}