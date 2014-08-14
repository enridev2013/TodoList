package com.example.enricoc.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, View.OnKeyListener, DialogInterface.OnClickListener, AdapterView.OnItemClickListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    EditText txtItem;
    Button btnAdd;
    ListView listItems;
    ArrayList<String> toDoItems;
    ArrayAdapter<String> aa;
    // MyArrayAdapter<String> ab;

    String[] hot = {"Coffee","Latte","Tea","Hot Choccolate"};
    String[] wine = {"Burgandy","Pinot Noir","Cannonau"};
    String[] beer = {"Guinnes","Ichnusa","Stella Artoies"};
    String[] cocktail = {"Bloody Mary","White Russian","Pina Colada"};
    String[] can = {"Coke","Sprite","Lemonade"};
    private GestureDetector gestureDetector;
    String currentMenu;
    private static final String TAG = "MyActivity";
    View.OnTouchListener gestureListener;
    public Boolean isSwipe;

    @Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);
    //}
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialise components of the application

        txtItem= (EditText) findViewById(R.id.txtItem);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        listItems = (ListView) findViewById(R.id.listItems);
        listItems.setFocusable(true);
        listItems.setFocusableInTouchMode(true);

        btnAdd.setOnClickListener(this);
        txtItem.setOnKeyListener(this);

        toDoItems = new ArrayList<String>();

        aa= new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,toDoItems);
        // experimentail list
        //ab = new MyArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,toDoItems);
        //ab = new MyArrayAdapter<String>(this, R.layout.row_layout, toDoItems);

        listItems.setAdapter(aa);
        //listItems.setAdapter(ab);

        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.w(TAG,view.toString());
                int lListViewPosition = listItems.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
                Log.w(TAG,"position: " + lListViewPosition);

                if (gestureDetector.onTouchEvent(motionEvent)) {
                    deleteItem(lListViewPosition);
                    return true;
                }
                return false;
            }
        };

        listItems.setOnItemClickListener(MainActivity.this);
        listItems.setOnTouchListener(gestureListener);

    }

    private void addItem(String item) {
        if (item.length() > 0) {
            Log.w(TAG, "Adding item " + item);
            Toast.makeText(getApplicationContext(),item + " added",Toast.LENGTH_SHORT).show();
            this.toDoItems.add(item);

            this.aa.notifyDataSetChanged();
            this.txtItem.setText("");
        }
    }

    private void deleteItem(int itemId) {
        if (itemId >= 0) {
            String itemName = (String)  listItems.getItemAtPosition(itemId);
            Toast.makeText(getApplicationContext(),itemName + " deleted",Toast.LENGTH_SHORT).show();
            this.toDoItems.remove(itemId);
            aa.notifyDataSetChanged();
        }
    }

    // event a item in the menu is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        this.currentMenu = item.getTitle().toString();
        if(item.hasSubMenu() == false) {
            if ("hot".equals(currentMenu)) {
                this.displayPopup("Hot Drinks",this.hot,R.drawable.hot);
            }
            if ("wine".equals(currentMenu)) {
                this.displayPopup("Wines",this.wine,R.drawable.wine);
            }
            if ("cocktail".equals(currentMenu)) {
                this.displayPopup("Cocktails",this.cocktail,R.drawable.cocktail);
            }
            if ("beer".equals(currentMenu)) {
                this.displayPopup("Beers",this.beer,R.drawable.beer);
            }
            if ("can".equals(currentMenu)) {
                this.displayPopup("Cans",this.can,R.drawable.can);
            }
        }
        return true;
    }

    private void displayPopup(String title, String[] items, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items,this);
        builder.setIcon(icon);

        builder.show();
    }

    @Override
    public void onClick(View view) {
        if (view == this.btnAdd) {
            this.addItem(this.txtItem.getText().toString());
        }
        Log.w(TAG, view.toString());

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_DPAD_CENTER) {
            this.addItem(this.txtItem.getText().toString());
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuItem item;
        item = menu.add("beer");
        item.setIcon(R.drawable.beer);
        item = menu.add("wine");
        item.setIcon(R.drawable.wine);
        item = menu.add("hot");
        item.setIcon(R.drawable.hot);
        item = menu.add("cocktail");
        item.setIcon(R.drawable.cocktail);
        item = menu.add("can");
        item.setIcon(R.drawable.can);

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void onClick(DialogInterface dialogInterface, int item) {

        Log.w(TAG, "currentMenu value" + currentMenu);
        if (currentMenu == "hot") {
            Log.w(TAG, "Adding item hot: " + this.hot[item]);
            this.addItem(hot[item]);
        }
        if (currentMenu == "wine") {
            Log.w(TAG, "Adding item wine: " + this.wine[item]);
            this.addItem(wine[item]);
        }
        if (currentMenu == "can") {
            Log.w(TAG, "Adding item can: " + this.can[item]);
            this.addItem(can[item]);
        }
        if (currentMenu == "cocktail") {
            Log.w(TAG, "Adding item cocktail: " + this.cocktail[item]);
            this.addItem(cocktail[item]);
        }
        if (currentMenu == "beer") {
            Log.w(TAG, "Adding item beer: " + this.beer[item]);
            this.addItem(beer[item]);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View selectedView, int position, long id) {

//        /listItems.getChildAt(position).setOnTouchListener(gestureListener);
        Log.w(TAG,"clicked" + position);
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {


        public boolean onDoubleTap(MotionEvent e) {
            Log.d("MyActivity - MyGestureDetector","double tap");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("MyActivity - MyGestureDetector","detecting");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    //Toast.makeText(MainActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    return true;
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    //Toast.makeText(MainActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } catch (Exception e) {
                // nothing
            }



            return true;
        }
    }


    // fancy up the listview
    private class MyArrayAdapter<String> extends ArrayAdapter<String> {
        private LayoutInflater m_inflater = null;
        private ArrayList<String> m_listArray = null;

        public MyArrayAdapter(Context context, int resource, ArrayList<String> listItem)
        {
            super(context, resource, listItem);
            m_inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            m_listArray = listItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            super.getView(position, convertView, parent);
            View vi = convertView;
            if (vi == null)
                vi = m_inflater.inflate(R.layout.row_layout, parent);
            TextView text = (TextView) vi.findViewById(R.id.label1);
            text.setText("Test");
            return vi;
        }

        @Override
        public int getCount()
        {
            return m_listArray.size();
        }

        @Override
        public String getItem(int position)
        {
            return m_listArray.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
