package jp.ac.jec.cm0113.mywordbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StudyModeSelectActivity extends AppCompatActivity {

    private int pattern = R.id.rbtPatternA;
    private int order = R.id.rbtOrderA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_mode_select);

        //学習方法の選択
        RadioGroup rgPattern = (RadioGroup)findViewById(R.id.rgPattern);
        rgPattern.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                pattern = id;
            }
        });

        //単語順序の選択
        RadioGroup rgOrder = (RadioGroup)findViewById(R.id.rgOrder);
        rgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                order = id;
            }
        });

        //学習ボタンを押したら、CardActivityへ遷移する
        Button btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyModeSelectActivity.this, CardActivity.class);
                intent.putExtra("pattern", pattern);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });
    }
}