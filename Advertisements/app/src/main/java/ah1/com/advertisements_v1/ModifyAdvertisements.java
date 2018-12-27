package ah1.com.advertisements_v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ModifyAdvertisements extends AppCompatActivity {

    private String aAds_key=null;
    private String ads_uid=null;
    private String ads_username=null;


    private ImageButton aSelectedImag;
    private static final int GALLERY_REQUEST = 1;

    private EditText aEditTitle;
    private EditText aEditDecrption;
  //  private EditText aEditCreationDate;
    private Spinner aSpinnerTypeAds;
    private Button aSubmitBut;

    private String TypeAdsSelected_string = "";


    private Uri aImageUri = null;

    private StorageReference aStorage;
    private DatabaseReference aDatabase;
    private DatabaseReference aDatabaseUsers;

    private FirebaseAuth aAuth;
    private FirebaseAuth.AuthStateListener aAuthListener;
    private FirebaseUser aCurrentUser;

    private ProgressDialog aProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_advertisements);

        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"تعديل اعلان"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////



        ///----------------  Add User ------------------------------------------
        aAuth = FirebaseAuth.getInstance();
        /*
        aAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntet = new Intent(ModifyAdvertisements.this, LoginActivity.class);
                    loginIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                    startActivity(loginIntet);
                }
            }
        };
        */
        //////////////////////////////////////////////////////////////////////////

        aCurrentUser = aAuth.getCurrentUser();  //من اجل سحب الايدي حق المستخدم

        aStorage = FirebaseStorage.getInstance().getReference();



       // aDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(aCurrentUser.getUid());
       // aDatabaseUsers.keepSynced(true);

        aSelectedImag = (ImageButton) findViewById(R.id.imgSelected);
        aEditTitle = (EditText) findViewById(R.id.EditTitle);
        aEditDecrption = (EditText) findViewById(R.id.EditDescrption);
      //  aEditCreationDate = (EditText) findViewById(R.id.EditCreationDate);
        aSpinnerTypeAds = (Spinner) findViewById(R.id.SpinnerTypeAds);
        aSubmitBut = (Button) findViewById(R.id.buSubmit);
        aProgress = new ProgressDialog(this);

        //------------------------------------------------------------------------

        aAds_key=getIntent().getExtras().getString("ads_id");

        aDatabase = FirebaseDatabase.getInstance().getReference().child("Ads").child(aAds_key);

        aDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ads_title= (String) dataSnapshot.child("title").getValue();
                String ads_desc= (String) dataSnapshot.child("descrption").getValue();
                String ads_image= (String) dataSnapshot.child("image").getValue();
                         ads_uid= (String) dataSnapshot.child("uid").getValue();
                     ads_username= (String) dataSnapshot.child("username").getValue();

                aEditTitle.setText(ads_title);
                aEditDecrption.setText(ads_desc);

                Picasso.with(ModifyAdvertisements.this).load(ads_image).into(aSelectedImag);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /////////////////////////////////////////////////////////////////////////


////-------------------------Button Sumbint---------------------------------------
        aSelectedImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallryIntent.setType("image/*");
                startActivityForResult(gallryIntent, GALLERY_REQUEST);
            }
        });

        aSubmitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAds();
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////


        ///-----------------------Spinner ------------------------------------------------
        final List<String> SpinnerTypeAdsArray = new ArrayList<String>();
        SpinnerTypeAdsArray.add("نوع الاعلان");
        SpinnerTypeAdsArray.add("ملابس");
        SpinnerTypeAdsArray.add("مطاعم");
        SpinnerTypeAdsArray.add("الكترونيات");
        SpinnerTypeAdsArray.add("معاهد");
        SpinnerTypeAdsArray.add("فنادق");
        SpinnerTypeAdsArray.add("اخرى");

        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinnerTypeAdsArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aSpinnerTypeAds.setAdapter(SpinnerAdapter);

        aSpinnerTypeAds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int positon, long id) {
                TypeAdsSelected_string = SpinnerTypeAdsArray.get(positon);
                //SpinnerTypeAdsArray.get(positon);
                // aSpinnerTypeAds.setSelection(positon);
                if (TypeAdsSelected_string == "نوع الاعلان") {
                    TypeAdsSelected_string = "اخرى";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //aSpinnerTypeAds.setSelection(6);
                TypeAdsSelected_string = "اخرى";
                //TypeAdsSelected_string=SpinnerTypeAdsArray.get(6);


            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    //---------------------------- داله رفع الاعلان
    private void startAds() {
        aProgress.setMessage("يتم تعديل الاعلان");


        final String title_value = aEditTitle.getText().toString().trim();
        final String descrption_value = aEditDecrption.getText().toString().trim();
       // final String creationDate_value = aEditCreationDate.getText().toString().trim();
        final String typeads_value = TypeAdsSelected_string;
        //final String typeads_value=aSpinnerTypeAds.getOnItemSelectedListener().toString();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(descrption_value) && aImageUri != null) {

            aProgress.show();

            StorageReference filepath = aStorage.child("Ads_Images").child(aImageUri.getLastPathSegment());

            filepath.putFile(aImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downlodeUri = taskSnapshot.getDownloadUrl();

                    //final DatabaseReference newAds = aDatabase.push();
                  // final DatabaseReference newAds = aDatabase.child(aAds_key);

                    //final DatabaseReference newAds = (DatabaseReference) aDatabase.orderByChild("ud").equalTo(aAds_key);


                    aDatabase.child("title").setValue(title_value);
                    aDatabase.child("descrption").setValue(descrption_value);
                  //  aDatabase.child("creationDate").setValue(creationDate_value);
                    aDatabase.child("image").setValue(downlodeUri.toString());
                    aDatabase.child("TypeAds").setValue(typeads_value);
                    aDatabase.child("uid").setValue(ads_uid);
                    aDatabase.child("username").setValue(ads_username);

                    aProgress.dismiss();
                    startActivity(new Intent(ModifyAdvertisements.this, AccountActivity.class));



                }
            });

        }
        else
        {
            //aProgress.dismiss();
            Toast.makeText(this, "تأكد من اعادة رفع الصورة وان جميع الحقول ممتلئة", Toast.LENGTH_SHORT).show();
        }

    }
//////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            aImageUri = data.getData();
            aSelectedImag.setImageURI(aImageUri);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        // CheckUserExis();

        //aAuth.addAuthStateListener(aAuthListener);
    }


}
