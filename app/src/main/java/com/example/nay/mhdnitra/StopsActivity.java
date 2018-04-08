package com.example.nay.mhdnitra;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class StopsActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);
        lv = (ListView) findViewById(R.id.stops_list_view);
        connectAdapter();
        addOnItemClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stop_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_menu_linky:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.stop_list_layout, dbh.getCursor(MyContract.Stop.TABLE_NAME),
                new String[]{MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);
    }

    public void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ListView lv = (ListView) findViewById(R.id.stops_list_view);
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
            }
        });
    }
}
