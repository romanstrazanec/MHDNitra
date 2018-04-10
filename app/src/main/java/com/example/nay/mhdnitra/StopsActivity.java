package com.example.nay.mhdnitra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.nay.mhdnitra.Entities.FavouriteStop;

public class StopsActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        lv = findViewById(R.id.stops_list_view);
        TextView tv = findViewById(R.id.stops_text_view);
        tv.setBackgroundColor(Color.rgb(255, 230, 0));
        tv.setTextColor(Color.BLACK);
        tv.setText("Zastávka");

        connectAdapter();
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stop_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_menu_lines:
                finish();
                return true;
            case R.id.stop_menu_favourites:
                startActivity(new Intent(this, FavouriteStopsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.stop_list_layout,
                dbh.getCursor(null, MyContract.Stop.TABLE_NAME, null, null, null, null, null, null),
                new String[]{MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);
    }

    public void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ListView lv = findViewById(R.id.stops_list_view);
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(StopsActivity.this, LineStopsActivity.class);
                i.putExtra("stops_id", c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID)));
                i.putExtra("title", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)));
                startActivity(i);
            }
        });
    }

    private void addOnItemLongClickListener() {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());

                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                final long ID = c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID));

                c = dbh.getCursor(null, MyContract.FavouriteStop.TABLE_NAME, null, null, null,
                        MyContract.FavouriteStop.COLUMN_ID_STOP + " = " + ID, null, null);
                if (c.moveToFirst()) {
                    builder.setMessage("Odobrať z obľúbených?").setTitle("Mazanie");
                    builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.deleteFavouriteStop(ID);
                            dialogInterface.dismiss();
                        }
                    });
                } else {
                    builder.setMessage("Pridať medzi obľúbené?").setTitle("Pridanie");
                    builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.addFavouriteStop(new FavouriteStop(1, ID));
                            dialogInterface.dismiss();
                        }
                    });
                }
                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
                return false;
            }
        });
    }
}
