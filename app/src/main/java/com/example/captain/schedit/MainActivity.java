package com.example.captain.schedit;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.TabItem;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import android.widget.Button;
import android.widget.Toast;

public  class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener, Tab3.OnFragmentInteractionListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    DbHelper dbHelper;
    CalHelper calHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        dbHelper = new DbHelper(this);
        lstTask = (ListView)findViewById(R.id.lstTask);
        //load the list when app starts
        loadTaskList();

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.app_endingsoon));//uses the strings from strings.xml rather than hardcoding them
        tabLayout.addTab(tabLayout.newTab().setText(R.string.app_favorite));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.app_calendar));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, Tab1.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab2.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab3.class.getName()));

        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

 //refresh when switching tabs------------------------------------------------------------------------------------------------------------------
        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager) ;
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override

            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                Fragment fragment = ((FragmentPagerAdapter)viewPager.getAdapter()).getItem(tab.getPosition());

                if ((tab.getPosition() ==0 || tab.getPosition() == 3 || tab.getPosition() == 2 || tab.getPosition() == 1) && fragment != null)
                {
                    fragment.onResume();
                }

            }
            @Override
            public  void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = ((FragmentPagerAdapter)viewPager.getAdapter()).getItem(tab.getPosition());
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = ((FragmentPagerAdapter)viewPager.getAdapter()).getItem(tab.getPosition());
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------
// load tasks into the list
    private void loadTaskList(){
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lstTask.setAdapter(mAdapter);
        }else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

//actions to do when the menu item is selected
    //contains the add a new task button
    //contains the settings button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_add_task:

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("what do you want to do next")
                        .setView(R.layout.custom_view)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final EditText text = (EditText) ((AlertDialog)dialog).findViewById(R.id.editText);
                                String task;
                                task = text.getText().toString();

                                dbHelper.insertNewTask(task);
                                if(getFragmentRefreshListener()!=null) {
                                    getFragmentRefreshListener().onRefresh();
                                }
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//delete the task with dbhelper and reload the list
    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());

        dbHelper.deleteTask(task);

        loadTaskList();

        if(getFragmentRefreshListener()!=null) {
            getFragmentRefreshListener().onRefresh();
        }

    }

    //add event using the Calendar Helper class
    //this function is still a work in progress
    public void addEvent()
    {
        calHelper.addEvent(MainActivity.this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
