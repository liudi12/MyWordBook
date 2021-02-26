package jp.ac.jec.cm0113.mywordbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class CardActivity extends AppCompatActivity {

    private ArrayList<Card> cards = new ArrayList<Card>(); //複数枚のカード情報
    private int pos; //現在表示しているカードの位置
    private int index[]; //単語の順序
    private int pattern;
    private int order;
//    Button btnPrev;
//    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //選んだ学習モードを取得する
        Intent intent = getIntent();
        pattern = intent.getIntExtra("pattern", R.id.rbtPatternA);
        order = intent.getIntExtra("order", R.id.rbtOrderA);
        Log.i("myWordCard", pattern + "pattern");
        Log.i("myWordCard", order + "order");

        //データベースから単語データを取得
        CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
        cards = helper.getAllCard();

        Button btnNext = (Button)findViewById(R.id.btnNext);
        Button btnPrev = (Button)findViewById(R.id.btnPrev);
        //次の単語へ
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                displayOneCard(index[pos]);
                if (pos == (cards.size() - 1)){
                    btnNext.setEnabled(false);
                }
                btnPrev.setEnabled(true);
            }
        });
        //前の単語へ
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                displayOneCard(index[pos]);
                if (pos == 0){
                    btnPrev.setEnabled(false);
                }
                btnNext.setEnabled(true);
            }
        });

        // 初期設定
        getIndexAry();
        pos = 0;
        displayOneCard(index[pos]); //１問目の表示
        btnPrev.setEnabled(false);

        //答えを表示or非表示
        Button btnAnswer = (Button)findViewById(R.id.btnAnswer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtAnswer = (TextView)findViewById(R.id.txtAnswer);
                if (txtAnswer.getVisibility() == View.INVISIBLE){
                    txtAnswer.setVisibility(View.VISIBLE);
                } else {
                    txtAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });

        //閉じるボタンを押したら、MainActivityへ遷移する
        Button btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //カードの中身を表示する
    private void displayOneCard(int index){
        Card temp = cards.get(index); //現在位置のカード情報を取得する

        TextView txtNo = (TextView)findViewById(R.id.txtNo);
        txtNo.setText((pos + 1) + "問目/全" + cards.size() + "問中");

        TextView txtQuestion = (TextView)findViewById(R.id.txtQuestion);
        TextView txtAnswer = (TextView)findViewById(R.id.txtAnswer);

        switch (pattern) {
            case R.id.rbtPatternA:
                //日本語 -> 中国語
                txtQuestion.setText(temp.getJapanese());
                txtAnswer.setText(temp.getChinese());
                break;
            case R.id.rbtPatternB:
                //中国語 -> 日本語
                txtQuestion.setText(temp.getChinese());
                txtAnswer.setText(temp.getJapanese());
                break;
        }
        //初期状態では、答えを非表示に
        txtAnswer.setVisibility(View.INVISIBLE);
    }

    //単語の順序を取得
    private void getIndexAry() {
        index = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            index[i] = i;
        }
        switch (order) {
            case R.id.rbtOrderA:
                Random random = new Random();
                for (int i = index.length; i > 0; i--) {
                    int randomInd = random.nextInt(i);
                    int temp = index[randomInd];
                    index[randomInd] = index[i - 1];
                    index[i - 1] = temp;
                }
                break;
            case R.id.rbtOrderB:
                break;
            case R.id.rbtOrderC:
                for (int i = 0; i < cards.size(); i++) {
                    index[i] = cards.size() - 1 - i;
                }
                break;
        }
    }
}