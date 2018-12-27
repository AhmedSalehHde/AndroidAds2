package ah1.com.advertisements_v1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AccountActivity extends AppCompatActivity {

    private ImageView aImageProfileUser;
    private ImageButton aBuProfileImage;
    private EditText aEditName;
    private EditText aEditLocationUser;
    private EditText aEditPhone;
    private EditText aEditWhatsupNum;
    private Button aBuSubmit;
    private Button aBuShowMyAds;
    private Button aBuAddAdvertisements;
    private Button aBuLogoutAccount;

    private static final int GALLERY_REQUEST = 1;

    private Uri aImageUri = null;


    private DatabaseReference aDatabaseUsers;
    private FirebaseAuth aAuth;
    private StorageReference aStorageImage;

    private DatabaseReference aDatabaseCurrentUser;

    private ProgressDialog aProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        aBuProfileImage = (ImageButton) findViewById(R.id.buProfileImage);
        aEditName = (EditText) findViewById(R.id.EditName);
        aEditLocationUser = (EditText) findViewById(R.id.EditLocationUser);
        aBuSubmit = (Button) findViewById(R.id.buAccountSubmit);

        aImageProfileUser=(ImageView)findViewById(R.id.ImageProfileUser);
        aEditPhone=(EditText) findViewById(R.id.EditPhone);
        aEditWhatsupNum=(EditText) findViewById(R.id.EditWhatsupNum);
        aBuShowMyAds = (Button) findViewById(R.id.buShowMyAds);
        aBuAddAdvertisements = (Button) findViewById(R.id.buAddAdvertisements);
        aBuLogoutAccount = (Button) findViewById(R.id.buLogoutAccount);


        aAuth=FirebaseAuth.getInstance();

        aStorageImage= FirebaseStorage.getInstance().getReference().child("Profile_images");

        aDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");

        aProgress=new ProgressDialog(this);



        //----------------- اذافه صوره في image button
        aBuProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        ///////////////////////////////////////////////////////////////////

        //--------------Submit Button----------------------------
        aBuSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSetupAccount();

            }
        });
/////////////////////////////////////////////////////////////////////////////


        //---------------- Show My Ads Button ----------------------------------------------
        aBuShowMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showAdsIntet=new Intent(AccountActivity.this,UserAdsActivity.class);
                // addAdsIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                startActivity(showAdsIntet);

            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////

        //---------------- Add Advertisements Button ----------------------------------------------
        aBuAddAdvertisements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAdsIntet=new Intent(AccountActivity.this,AddAdvertisements.class);
                // addAdsIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                startActivity(addAdsIntet);

            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////


        //---------------- Logout Account Button ----------------------------------------------
        aBuLogoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MassgOnRemove();



            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////



        //------------------------------------------------------------------------

       String currentUserId=aAuth.getCurrentUser().getUid();

        aDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        aDatabaseCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String user_Name= (String) dataSnapshot.child("Name").getValue();
                String user_Location= (String) dataSnapshot.child("LocationUser").getValue();
                String user_Phone= (String) dataSnapshot.child("Phone").getValue();
                String user_WhatseUpNum= (String) dataSnapshot.child("WhatsupNum").getValue();
                String user_image= (String) dataSnapshot.child("IamgeUser").getValue();




                aEditName.setText(user_Name);
                aEditLocationUser.setText(user_Location);
                aEditPhone.setText(user_Phone);
               aEditWhatsupNum.setText(user_WhatseUpNum);


               // Picasso.with(AccountActivity.this).load(user_image).into(aImageProfileUser);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /////////////////////////////////////////////////////////////////////////






    }




    private void startSetupAccount() {

        aProgress.setMessage("يتم تحديث البيانات");

        final String name = aEditName.getText().toString().trim();
        final String loctionUser = aEditLocationUser.getText().toString().trim();
        final String phone = aEditPhone.getText().toString().trim();
        final String whatseUpNum = aEditWhatsupNum.getText().toString().trim();
        final String user_id=aAuth.getCurrentUser().getUid();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(loctionUser) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(whatseUpNum) && aImageUri != null)
        {
            //aProgress.setMessage("رفع البيانات");
            aProgress.show();

            StorageReference filepath=aStorageImage.child(aImageUri.getLastPathSegment());

            filepath.putFile(aImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downlodUri=taskSnapshot.getDownloadUrl().toString();

                    aDatabaseUsers.child(user_id).child("Name").setValue(name);
                    aDatabaseUsers.child(user_id).child("LocationUser").setValue(loctionUser);
                    aDatabaseUsers.child(user_id).child("Phone").setValue(phone);
                    aDatabaseUsers.child(user_id).child("WhatsupNum").setValue(whatseUpNum);
                    aDatabaseUsers.child(user_id).child("IamgeUser").setValue(downlodUri);

                    aProgress.dismiss();

                    Toast.makeText(AccountActivity.this, "تم تحديث البيانات", Toast.LENGTH_SHORT).show();
                    //Intent addAdsIntet=new Intent(AccountActivity.this,AddAdvertisements.class);
                   // addAdsIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                   // startActivity(addAdsIntet);

                }
            });


        }
        else
        {
            aProgress.dismiss();
            Toast.makeText(this, "الرجاء التأكد من اخال البيانات ورفع صورة العرض", Toast.LENGTH_SHORT).show();
        }

    }


    ////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

                //aBuProfileImage.setImageURI(aImageUri);
                aImageProfileUser.setImageURI(aImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }



    }




    //رسالة الديالوق لتاكيد حذف الاعلان او تراجع عن الحذف
    public void MassgOnRemove()
    {
        AlertDialog.Builder build=new  AlertDialog.Builder(this);
        build.setMessage("هل تريد تسجيل الخروج ؟")
                .setTitle("تسجيل الخروج")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        logout();// لتسجيل الخروج
                        Intent LououtIntet=new Intent(AccountActivity.this,TabActivity.class);
                        // addAdsIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                        startActivity(LououtIntet);

                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }


    //----------------------  دالة لتسجيل الخروج
    private void logout() {
        aAuth.signOut();
    }
    ////////////////////////////////////////////////////////////
}
