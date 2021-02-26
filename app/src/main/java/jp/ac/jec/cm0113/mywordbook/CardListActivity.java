package jp.ac.jec.cm0113.mywordbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CardListActivity extends AppCompatActivity {

    private RowModelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        adapter = new RowModelAdapter(this);
        //データベースからデータをもらう
        CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
        adapter.addAll(helper.getAllCard());

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        //行をクリックしたらEditActivityの編集モードへ遷移する
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //押された行の内容を取得する
                Card tmp = (Card)listView.getItemAtPosition(position);

                //行が押されたら、編集画面へ遷移する
                Intent intent = new Intent(CardListActivity.this, EditActivity.class);
                intent.putExtra("mode", "update"); //編集モード
                intent.putExtra("id", tmp.getId());
                startActivity(intent);
            }
        });
    }

    // 編集モードの◀︎ボタンが押されたら、Listも更新
    @Override
    protected void onResume() {
        super.onResume();
        CardSQLiteOpenHelper helper = new CardSQLiteOpenHelper(this);
        adapter.clear();
        adapter.addAll(helper.getAllCard());
        adapter.notifyDataSetChanged();
    }

    //１行のアダプター
    public class RowModelAdapter extends ArrayAdapter<Card> {
        public RowModelAdapter(Context context) {
            super(context, R.layout.row_item);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //アダプターにセットされている該当のデータを取り出す
            Card item = getItem(position);

            //ListViewが初めて表示される場合は引数のconvertView(行のView)は
            //nullなので行のレイアウトをinflate(適用)させる必要がある
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_item, null);
            }
            //データを入れる
            if (item != null) {
                //日本語
                TextView txtJapanese = (TextView)convertView.findViewById(R.id.txtJapanese);
                if (txtJapanese != null) {
                    txtJapanese.setText(item.getJapanese());
                }
                //中国語
                TextView txtChinese = (TextView)convertView.findViewById(R.id.txtChinese);
                if (txtChinese != null) {
                    txtChinese.setText(item.getChinese());
                }
                //icon画像
                ImageView img = (ImageView)convertView.findViewById(R.id.img);
                img.setImageResource(R.drawable.icon);
            }
            return convertView;
        }
    }
}