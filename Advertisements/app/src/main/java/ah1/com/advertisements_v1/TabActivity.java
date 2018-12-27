package ah1.com.advertisements_v1;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TabActivity extends AppCompatActivity {

    private DatabaseReference aDatabase;
    private FirebaseAuth aAuth;
    private DatabaseReference aDatabaseUsers;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"الرئيسية"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




        aAuth=FirebaseAuth.getInstance();

        aDatabase= FirebaseDatabase.getInstance().getReference().child("Ads");


        aDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        aDatabaseUsers.keepSynced(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final    MenuItem menu_login=menu.findItem(R.id.action_login);
        final  MenuItem menu_account=menu.findItem(R.id.action_account) ;

        final String user_id = aAuth.getCurrentUser().getUid();

        aDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(user_id)) {


                   // menu_account.setVisible(true);
                    //  menu_logout.setVisible(true);
                    menu_account.setVisible(false);


                } else {

                    menu_login.setVisible(false);

                    // menu_logout.setVisible(false);
                   // menu_login.setVisible(true);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        //menu_login.setVisible(false);

        return true;
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
        //    return true;
       // }

        if (item.getItemId()== R.id.action_login)
        {
            startActivity(new Intent(TabActivity.this,LoginActivity.class));
        }

        if (item.getItemId()== R.id.action_account)
        {
            startActivity(new Intent(TabActivity.this,AccountActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Tab1Main tab1Main = new Tab1Main();
                    return tab1Main;
                case 1:
                    Tab2Clothes tab2Clothes = new Tab2Clothes();
                    return tab2Clothes;
                case 2:
                    Tab3Restaurants tab3Restaurants = new Tab3Restaurants();
                    return tab3Restaurants;
                case 3:
                    Tab4Education tab4Education = new Tab4Education();
                    return tab4Education;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "الرئيسية";
                case 1:
                    return "ملابس";
                case 2:
                    return "مطاعم";
                case 3:
                    return "مكتبات";
            }
            return null;
        }
    }


}
