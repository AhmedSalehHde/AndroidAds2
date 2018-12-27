package ah1.com.advertisements_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdsSingleActivity extends AppCompatActivity {

    private String aAds_key=null;

    private DatabaseReference aDatabase;

    private FirebaseAuth aAuth;

    private TextView aAdsSingleTitle;
    private TextView aAdsSingleDesc;
    private TextView aAdsSingle_userphone;
    private TextView aAdsSingle_userlocation;
    private TextView aAdsSingleUser;

    private ImageView aAdsSingleImage;

    private Button aAdsSingleRemoveBu;
    private Button aAdsSingleModifyBu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_single);

        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"الاعلان"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////



        aAds_key=getIntent().getExtras().getString("ads_id");

        //Toast.makeText(this, ads_key, Toast.LENGTH_SHORT).show();

        aAdsSingleTitle=(TextView)findViewById(R.id.adsSingleTitle);
        aAdsSingleDesc=(TextView)findViewById(R.id.adsSingleDec);
        aAdsSingle_userphone=(TextView)findViewById(R.id.adsSingle_userphone);
        aAdsSingle_userlocation=(TextView)findViewById(R.id.adsSingle_userlocation);
        aAdsSingleUser=(TextView)findViewById(R.id.adsSingleUser);

        aAdsSingleImage=(ImageView) findViewById(R.id.adsSingleImage);

        aAdsSingleRemoveBu=(Button)findViewById(R.id.adsSingleRemoveBu);
        aAdsSingleModifyBu=(Button)findViewById(R.id.adsSingleModifyBu);


        aAuth=FirebaseAuth.getInstance();

        aDatabase= FirebaseDatabase.getInstance().getReference().child("Ads");

            aDatabase.child(aAds_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String ads_title= (String) dataSnapshot.child("title").getValue();
                    String ads_desc= (String) dataSnapshot.child("descrption").getValue();
                    String ads_userphone= (String) dataSnapshot.child("phone").getValue();
                    String ads_userlocation= (String) dataSnapshot.child("location").getValue();
                    String ads_image= (String) dataSnapshot.child("image").getValue();
                    String ads_uid= (String) dataSnapshot.child("uid").getValue();
                    String ads_userName= (String) dataSnapshot.child("username").getValue();

                    aAdsSingleTitle.setText(ads_title);
                    aAdsSingleDesc.setText(ads_desc);
                    aAdsSingle_userphone.setText(ads_userphone);
                    aAdsSingle_userlocation.setText(ads_userlocation);
                    aAdsSingleUser.setText(ads_userName);

                    Picasso.with(AdsSingleActivity.this).load(ads_image).into(aAdsSingleImage);

                    if(aAuth.getCurrentUser().getUid().equals(ads_uid))
                    {
                        aAdsSingleRemoveBu.setVisibility(View.VISIBLE);
                        aAdsSingleModifyBu.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        aAdsSingleRemoveBu.setVisibility(View.INVISIBLE);
                        aAdsSingleModifyBu.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





        ////////////////////////////////////////////////////////////////////////////


        aAdsSingleRemoveBu.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {



                MassgOnRemove();
                /*
                aDatabase.child(aAds_key).removeValue();

               // Intent mainIntent=new Intent(AdsSingleActivity.this,MainActivity.class);
                //startActivity(mainIntent);
                finish();
                */

            }
        });

        aAdsSingleModifyBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String ads_key=getRef(position).getKey();//يسحب رقم الاي دي حق الريسايكل فيو للمحدده ب البوزيشن الموقع المحدد عند الضغط

                Intent singleAdsIntent=new Intent(AdsSingleActivity.this,ModifyAdvertisements.class);
                singleAdsIntent.putExtra("ads_id",aAds_key);
                startActivity(singleAdsIntent);

            }
        });
    }



    //رسالة الديالوق لتاكيد حذف الاعلان او تراجع عن الحذف
    public void MassgOnRemove()
    {
        AlertDialog.Builder build=new  AlertDialog.Builder(this);
        build.setMessage("هل تريد حذف الاعلان ؟")
                .setTitle("حذف الاعلان")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        aDatabase.child(aAds_key).removeValue();
                        finish();

                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}
