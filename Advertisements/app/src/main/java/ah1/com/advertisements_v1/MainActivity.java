package ah1.com.advertisements_v1;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView aAdsList;
    private DatabaseReference aDatabase;
    private FirebaseAuth aAuth;

    private DatabaseReference aDatabaseUsers;

  //  private DatabaseReference aDatabaseAds;
  //  private DatabaseReference aDatabaseUsersPhone;

   // private FirebaseUser aCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"الرئيسية"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////




       // Toast.makeText(this, "aaaaaaaa", Toast.LENGTH_SHORT).show();

        aAuth=FirebaseAuth.getInstance();

        aDatabase= FirebaseDatabase.getInstance().getReference().child("Ads");


        aDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        aDatabaseUsers.keepSynced(true);

///---------------------------------------------------------------------------------------------------
        //aDatabaseAds= FirebaseDatabase.getInstance().getReference().child("Ads").child("uid");

       //  String aCurrentUser=aDatabaseAds.getKey();
       //  aDatabaseUsersPhone=FirebaseDatabase.getInstance().getReference().child("Users").child(aCurrentUser).child("Phone");
       //final String UserPhoneAds=aDatabaseUsersPhone.getKey();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        aAdsList=(RecyclerView)findViewById(R.id.ads_list);
        aAdsList.setHasFixedSize(true);
        aAdsList.setLayoutManager(new LinearLayoutManager(this));



    }




////////////////////////////////////////////////////////////////////////////////////////////
    //----------------------------  RecyclerView ------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();

        /////////////////33333333333333333333333333333333
        FirebaseRecyclerAdapter<Ads,AdsViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Ads, AdsViewHolder>
                (

                Ads.class,
                R.layout.ads_row,
                AdsViewHolder.class,
                aDatabase

        )
        {
            /// انتبه للاقواس
            @Override
            protected void populateViewHolder(AdsViewHolder viewHolder, Ads model, int position) {

                final String ads_key=getRef(position).getKey();//يسحب رقم الاي دي حق الريسايكل فيو للمحدده ب البوزيشن الموقع المحدد عند الضغط

                viewHolder.setTitle(model.getTitle());
              //  viewHolder.setDesc(model.getDescrption());
               // viewHolder.setCreationDate(model.getCreationDate());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setPhone(model.getPhone());
                viewHolder.setLocation(model.getLocation());
               // viewHolder.setImageUser(getApplicationContext(),model.getImageuser());

                //--------------------عند الضغط على الريسايكل الفيو المحدد
                viewHolder.aView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(MainActivity.this, ads_key, Toast.LENGTH_SHORT).show();
                        Intent singleAdsIntent=new Intent(MainActivity.this,AdsSingleActivity.class);
                        singleAdsIntent.putExtra("ads_id",ads_key);
                        startActivity(singleAdsIntent);

                    }
                });

            }
        };
        ////////33333333333333333333333333333333333333333

        aAdsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AdsViewHolder extends RecyclerView.ViewHolder{

        View aView;



        public AdsViewHolder(View itemView) {
            super(itemView);

           aView=itemView;


        }

        public void setTitle(String title)
        {
            TextView ads_title=(TextView)aView.findViewById(R.id.ads_title);
            ads_title.setText(title);
        }

        public void setDesc(String descrption)
        {
           // TextView ads_descrption=(TextView)aView.findViewById(R.id.ads_descrption);
           // ads_descrption.setText(descrption);

            /*
                 xml
                 <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/ads_descrption"
            tools:text="وصف الاعلان"
            android:textSize="14sp"
            android:paddingBottom="15dp"
            android:paddingLeft="15dp"
            android:paddingRight="15dp" />


             */
        }

        public void setUsername(String username)
        {
            TextView ads_username=(TextView)aView.findViewById(R.id.ads_username);
            ads_username.setText(username);
        }

        public void setCreationDate(String creationDate)
        {
          //  TextView ads_creationDate=(TextView)aView.findViewById(R.id.ads_creationDate);
          //  ads_creationDate.setText(creationDate);

            /*
            xml
             <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/ads_creationDate"
            tools:text="تاريخ الاعلان"
            android:textSize="14sp"
            android:paddingBottom="15dp"
            android:paddingLeft="15dp"
            android:paddingRight="15dp" />


             */
        }

        public void setPhone(String phone)
        {
            TextView ads_phone=(TextView)aView.findViewById(R.id.ads_userphone);
            ads_phone.setText(phone);
        }

        public void setLocation(String location)
        {
            TextView ads_location=(TextView)aView.findViewById(R.id.ads_location);
            ads_location.setText(location);
        }

        public void setImage(final Context ctx, final String image)
        {
           final ImageView ads_image=(ImageView)aView.findViewById(R.id.ads_image);
           // Picasso.with(ctx).load(image).into(ads_image);
           Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(ads_image, new Callback() {
               @Override
               public void onSuccess() {

               }

               @Override
               public void onError() {
                   Picasso.with(ctx).load(image).into(ads_image);
               }
           });
        }


        public void setImageUser(final Context ctx, final String imageuser)
        {
            final ImageView ads_imageUser=(ImageView)aView.findViewById(R.id.ads_imageUser);
            // Picasso.with(ctx).load(image).into(ads_image);
            Picasso.with(ctx).load(imageuser).networkPolicy(NetworkPolicy.OFFLINE).into(ads_imageUser, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(imageuser).into(ads_imageUser);
                }
            });
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////




   /////////////////////////////////////////////////////////////////////////////////////////////////
    //------------------------------------------ Menu -------------------------------------------
/*

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final    MenuItem menu_login=menu.findItem(R.id.action_login);
        final  MenuItem menu_account=menu.findItem(R.id.action_account) ;
        final  MenuItem menu_logout=menu.findItem(R.id.action_logout) ;

        final String user_id = aAuth.getCurrentUser().getUid();

        aDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id))
                {


                     menu_account.setVisible(true);
                   //  menu_logout.setVisible(true);
                     menu_login.setVisible(false);


                }else
                {

                    menu_account.setVisible(false);
                   // menu_logout.setVisible(false);
                    menu_login.setVisible(true);

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
    public boolean onCreateOptionsMenu(final Menu menu) {
     //   getMenuInflater().inflate(R.menu.main_menu,menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        /*

//////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        final String user_id = aAuth.getCurrentUser().getUid();
        View view;

        aDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // MenuItem aa=(MenuItem) MenuItemCompat.getActionView(menu.findItem(R.id.action_login));

                MenuItem menu_login=(MenuItem) MenuItemCompat.getActionView(menu.findItem(R.id.action_login));
                MenuItem menu_account=(MenuItem)MenuItemCompat.getActionView(menu.findItem(R.id.action_account)) ;
                MenuItem menu_logout=(MenuItem)MenuItemCompat.getActionView(menu.findItem(R.id.action_logout)) ;
                // MenuItem menu_add=(MenuItem)findViewById(R.id.action_add) ;
                //  MenuItem menu_userAds=(MenuItem)findViewById(R.id.action_userAds) ;
                // MenuItem menu_clothes=(MenuItem)findViewById(R.id.action_clothes) ;
                // MenuItem menu_tab=(MenuItem)findViewById(R.id.action_tab) ;

                if(dataSnapshot.hasChild(user_id))
                {


                    // menu_account.setVisible(true);
                    // menu_logout.setVisible(true);
                    // menu_login.setVisible(false);
                    //menu_account.isVisible();
                    // menu_logout.isVisible();
                    menu_login.isEnabled();

                }else
                {

                    //menu_account.setVisible(false);
                    //menu_logout.setVisible(false);
                    // menu_login.setVisible(true);
                    //   menu_account.isEnabled();
                    //  menu_logout.isEnabled();
                    menu_login.isVisible();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        return true;

*/
       // return super.onCreateOptionsMenu(menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add)
        {
            startActivity(new Intent(MainActivity.this,AddAdvertisements.class));
           // startActivity(new Intent(MainActivity.this,AccountActivity.class));
        }

        if (item.getItemId()== R.id.action_login)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }

        if (item.getItemId()== R.id.action_account)
        {
            startActivity(new Intent(MainActivity.this,AccountActivity.class));
        }

        if (item.getItemId()== R.id.action_userAds)
        {
            startActivity(new Intent(MainActivity.this,UserAdsActivity.class));
        }

        if (item.getItemId()== R.id.action_clothes)
        {
            startActivity(new Intent(MainActivity.this,ClothesAdsActivity.class));
        }

        if (item.getItemId()== R.id.action_tab)
        {
            startActivity(new Intent(MainActivity.this,TabActivity.class));
        }


        if (item.getItemId()== R.id.action_logout)
        {
            logout();// لتسجيل الخروج
        }


        return super.onOptionsItemSelected(item);
    }


    //----------------------  دالة لتسجيل الخروج
    private void logout() {
        aAuth.signOut();
    }
    ////////////////////////////////////////////////////////////
}
