package ah1.com.advertisements_v1;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;


public class AddAdvertisements extends AppCompatActivity {

    private ImageButton aSelectedImag;
    private static final int GALLERY_REQUEST = 1;

    private EditText aEditTitle;
    private EditText aEditDecrption;
    //private EditText aEditCreationDate;
    private Spinner aSpinnerTypeAds;
    private Button aSubmitBut;

    private String TypeAdsSelected_string="";


    private Uri aImageUri = null;

    private StorageReference aStorage;
    private DatabaseReference aDarabase;
    private DatabaseReference aDatabaseUsers;


    private FirebaseAuth aAuth;
    private FirebaseAuth.AuthStateListener aAuthListener;
    private FirebaseUser aCurrentUser;

    private ProgressDialog aProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisements);
        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"اضافة اعلان"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////




        ///----------------  Add User ------------------------------------------
        aAuth=FirebaseAuth.getInstance();
        aAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent loginIntet=new Intent(AddAdvertisements.this,LoginActivity.class);
                    loginIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                    startActivity(loginIntet);
                }
            }
        };
        //////////////////////////////////////////////////////////////////////////

        aCurrentUser=aAuth.getCurrentUser();  //من اجل سحب الايدي حق المستخدم

        aStorage= FirebaseStorage.getInstance().getReference();

        aDarabase= FirebaseDatabase.getInstance().getReference().child("Ads");

        aDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(aCurrentUser.getUid());
        aDatabaseUsers.keepSynced(true);



        aSelectedImag = (ImageButton) findViewById(R.id.imgSelected);
        aEditTitle = (EditText) findViewById(R.id.EditTitle);
        aEditDecrption = (EditText) findViewById(R.id.EditDescrption);
       // aEditCreationDate = (EditText) findViewById(R.id.EditCreationDate);
        aSpinnerTypeAds=(Spinner)findViewById(R.id.SpinnerTypeAds);
        aSubmitBut = (Button) findViewById(R.id.buSubmit);
        aProgress=new ProgressDialog(this);

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
        final List<String> SpinnerTypeAdsArray=new ArrayList<String>();
        SpinnerTypeAdsArray.add("نوع الاعلان");
        SpinnerTypeAdsArray.add("ملابس");
        SpinnerTypeAdsArray.add("مطاعم");
        SpinnerTypeAdsArray.add("الكترونيات");
        SpinnerTypeAdsArray.add("مكتبات");
        SpinnerTypeAdsArray.add("فنادق");
        SpinnerTypeAdsArray.add("اخرى");

        ArrayAdapter<String> SpinnerAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,SpinnerTypeAdsArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aSpinnerTypeAds.setAdapter(SpinnerAdapter);

        aSpinnerTypeAds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int positon, long id) {
                TypeAdsSelected_string=SpinnerTypeAdsArray.get(positon);
                //SpinnerTypeAdsArray.get(positon);
               // aSpinnerTypeAds.setSelection(positon);
                if (TypeAdsSelected_string=="نوع الاعلان")
                {
                    TypeAdsSelected_string="اخرى";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               //aSpinnerTypeAds.setSelection(6);
                TypeAdsSelected_string="اخرى";
                //TypeAdsSelected_string=SpinnerTypeAdsArray.get(6);


            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
    }
//---------------------------- داله رفع الاعلان
    private void startAds() {
        aProgress.setMessage("يتم رفع الاعلان");
        aProgress.show();

        final String title_value = aEditTitle.getText().toString().trim();
        final String descrption_value = aEditDecrption.getText().toString().trim();
       // final String creationDate_value = aEditCreationDate.getText().toString().trim();
        final String typeads_value=TypeAdsSelected_string;
        //final String typeads_value=aSpinnerTypeAds.getOnItemSelectedListener().toString();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(descrption_value) && aImageUri != null) {

            StorageReference filepath=aStorage.child("Ads_Images").child(aImageUri.getLastPathSegment());

            filepath.putFile(aImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downlodeUri=taskSnapshot.getDownloadUrl();

                    final DatabaseReference newAds=aDarabase.push();


                    aDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newAds.child("title").setValue(title_value);
                            newAds.child("descrption").setValue(descrption_value);
                           // newAds.child("creationDate").setValue(creationDate_value);
                            newAds.child("image").setValue(downlodeUri.toString());
                            newAds.child("TypeAds").setValue(typeads_value);
                            newAds.child("uid").setValue(aCurrentUser.getUid());
                            newAds.child("phone").setValue(dataSnapshot.child("Phone").getValue());
                            newAds.child("location").setValue(dataSnapshot.child("LocationUser").getValue());
                            newAds.child("iamgeuser").setValue(dataSnapshot.child("IamgeUser").getValue());
                            newAds.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        startActivity(new Intent(AddAdvertisements.this,TabActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(AddAdvertisements.this, "خطاء في اضافة الاعلان", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    aProgress.dismiss();


                }
            });

        }
        else
        {
            aProgress.dismiss();
            Toast.makeText(this, "الرجاء التاكد من ادخال البيانات", Toast.LENGTH_SHORT).show();
        }

    }
//////////////////////////////////////////////////////////////////////////////////////




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            aImageUri = data.getData();
            aSelectedImag.setImageURI(aImageUri);
        }
        */

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                aImageUri = result.getUri();

                aSelectedImag.setImageURI(aImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

       // CheckUserExis();

        aAuth.addAuthStateListener(aAuthListener);
    }



    //------------------------دالة التحقق هل تم تثبيت حسابك او لا قبل اضافه اعلان

    private void CheckUserExis() {

        final String user_id = aAuth.getCurrentUser().getUid();

        aDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(user_id))
                {
                    Intent accountIntet=new Intent(AddAdvertisements.this,AccountActivity.class);
                    accountIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                    startActivity(accountIntet);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}