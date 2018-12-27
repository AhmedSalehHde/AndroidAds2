package ah1.com.advertisements_v1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText aEditName;
    private EditText aEditPhone;
    private EditText aEditWhatsupNum;
    private EditText aEditEmail;
    private EditText aEditPassword;
    private EditText aEditPasswordSure;
    private Button abuRegister;

    private FirebaseAuth aAuth;
    private DatabaseReference aDatabase;

    private ProgressDialog aProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"انشاء حساب"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////


        aAuth=FirebaseAuth.getInstance();
        aDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        aProgress=new ProgressDialog(this);

        aEditName = (EditText) findViewById(R.id.EditName);
        aEditPhone = (EditText) findViewById(R.id.EditPhone);
        aEditWhatsupNum = (EditText) findViewById(R.id.EditWhatsupNum);
        aEditEmail = (EditText) findViewById(R.id.EditEmail);
        aEditPassword = (EditText) findViewById(R.id.EditPassword);
        aEditPasswordSure = (EditText) findViewById(R.id.EditePasswordSure);
        abuRegister = (Button) findViewById(R.id.buRegister);

        abuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
            }
        });
    }

    private void startRegister() {

        final String Name = aEditName.getText().toString().trim();
        final String Phone = aEditPhone.getText().toString().trim();
        final String WhatsupNum = aEditWhatsupNum.getText().toString().trim();
        final String Email = aEditEmail.getText().toString().trim();
        final String Password = aEditPassword.getText().toString().trim();
        final String PasswordSure = aEditPasswordSure.getText().toString().trim();

        if(Password.equals(PasswordSure))
        {
            if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(WhatsupNum) && !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)) {

                aProgress.setMessage("جاري التسجيل");
                aProgress.show();

                aAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String user_id=aAuth.getCurrentUser().getUid();
                            DatabaseReference cureent_user_db = aDatabase.child(user_id);
                            cureent_user_db.child("Name").setValue(Name);
                            cureent_user_db.child("Phone").setValue(Phone);
                            cureent_user_db.child("WhatsupNum").setValue(WhatsupNum);
                            cureent_user_db.child("Email").setValue(Email);
                            cureent_user_db.child("Password").setValue(Password);
                            cureent_user_db.child("State").setValue("0");
                            cureent_user_db.child("Iamge").setValue("default");
                            cureent_user_db.child("LocationUser").setValue("المكان");
                            cureent_user_db.child("Note").setValue("null");

                            aProgress.dismiss();

                            Intent addAdsIntent=new Intent(RegisterActivity.this,AccountActivity.class);
                           // addAdsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(addAdsIntent);
                            finish();

                        }
                        else
                        {
                            aProgress.dismiss();
                            Toast.makeText(RegisterActivity.this, "الرجاء التأكد من البيانات التي ادخلتها ووجود الانترنت", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else
            {
                Toast.makeText(this, "ارجاء اكمال جميع البيانات", Toast.LENGTH_SHORT).show();
            }




        }
        else
        {
            Toast.makeText(this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
        }
    }
}
