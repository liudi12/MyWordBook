package jp.ac.jec.cm0113.mywordbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private int id = -1; //表示しているカードのID
    private EditText edtJapanese;
    private EditText edtChinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //EditActivityのモードを取得する
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        TextView txtMsg = (TextView)findViewById(R.id.txtMessage);
        edtJapanese = (EditText)findViewById(R.id.edtJapanese);
        edtChinese = (EditText)findViewById(R.id.edtChinese);

        //追加or編集ボタン
        Button btn = (Button)findViewById(R.id.btnAdd);
        if (mode.equals("update")) { //編集モード
            //idによって編集したい単語の情報を取得
            id = intent.getIntExtra("id", -1);
            CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
            Card tmp = helper.findCardById(id);

            txtMsg.setText("単語を編集しましょう！");
            edtJapanese.setText(tmp.getJapanese());
            edtChinese.setText(tmp.getChinese());

            //更新ボタンをセット
            btn.setOnClickListener(new UpdateOnClickAction());
            btn.setText("更　新");

            //動的削除ボタンを生成
            LinearLayout lay = (LinearLayout)findViewById(R.id.layEditHome);
            Button btnDelete = new Button(this);
            btnDelete.setText("削　除");
            //ボタンの見た目
            btnDelete.setTextColor(Color.WHITE);
            //btn高さ
            float scale = getResources().getDisplayMetrics().density;
            int btnHeight = (int)(60 * scale);
            btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, //第一引数：幅
                    btnHeight)); //第二引数：高さ
            //btn背景
            Drawable btnColor = ResourcesCompat.getDrawable(getResources(), R.drawable.press_effect, null);
            btnDelete.setBackground(btnColor);
            btnDelete.setTextSize(TypedValue.COMPLEX_UNIT_DIP,24);
            lay.addView(btnDelete);
            btnDelete.setOnClickListener(new DeleteOnClickAction());

        } else if (mode.equals("add")) { //追加モード
            edtJapanese.setText("");
            edtChinese.setText("");
            btn.setOnClickListener(new AddOnClickAction());
            btn.setText("追　加");
        }

        //editを閉める
        Button btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //追加ボタンのイベントハンドラクラス
    private class AddOnClickAction implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String japanese = edtJapanese.getText().toString();
            String chinese = edtChinese.getText().toString();

            //入力チェック
            if(japanese.equals("") || chinese.equals("")) {
                Toast.makeText(EditActivity.this, "単語を入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            //データベースに登録する
            CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(EditActivity.this);
            //Cardオブジェクトにする
            Card newCard = new Card(chinese, japanese);
            //重複チェック
            boolean isExist = helper.isExistWord(japanese);
            if(isExist) {
                Toast.makeText(EditActivity.this, "すでに登録済みです", Toast.LENGTH_SHORT).show();
                return;
            }
            //登録実行
            boolean result = helper.insertCard(newCard);
            if (result) {
                Toast.makeText(EditActivity.this, "登録成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditActivity.this, "登録失敗", Toast.LENGTH_SHORT).show();
            }

            //　EditTextをクリア
            edtJapanese.setText("");
            edtChinese.setText("");
        }
    }

    //更新ボタンのイベントハンドラクラス
    private class UpdateOnClickAction implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String japanese = edtJapanese.getText().toString();
            String chinese = edtChinese.getText().toString();

            Card newCard = new Card(chinese, japanese, id);
            CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(EditActivity.this);
            boolean result = helper.updateCard(newCard);
            if (result) {
                Toast.makeText(EditActivity.this, "編集成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditActivity.this, "編集失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //deleteボタンのイベントハンドラ
    private class DeleteOnClickAction implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            //アラートダイアログで削除を確認する
            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setTitle("確認");
            builder.setMessage("本当に削除しますか？");
            //はい➡️
            builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(EditActivity.this);
                    //データを削除する
                    helper.deleteCardById(id);
                    finish();
                    Toast.makeText(EditActivity.this, "削除成功", Toast.LENGTH_SHORT).show();
                }
            });
            //いいえ➡️
            builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(EditActivity.this, "削除取消", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
    }
}