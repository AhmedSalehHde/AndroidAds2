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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText aEditLoginEmail;
    private EditText aEditLoginPassword;
    private Button abuLogin;
    private Button abuNewAccount;

    private FirebaseAuth aAuth;
    private DatabaseReference aDatabaseUsers;

    private ProgressDialog aProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ////////////////////////////////////////////////////////////////////////////////
        //-----------------------------------------------------------------------------

        //الشريط العلوي
        Toolbar my_toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("   "+"تسجيل الدخول"+"   ");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_adway);

        //-----------------------------------------------------------------------------
        ////////////////////////////////////////////////////////////////////////////////



        aAuth = FirebaseAuth.getInstance();

        aDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        aDatabaseUsers.keepSynced(true);

        aProgress=new ProgressDialog(this);

        aEditLoginEmail = (EditText) findViewById(R.id.EditLoginEmail);
        aEditLoginPassword = (EditText) findViewById(R.id.EditLoginPassword);
        abuLogin = (Button) findViewById(R.id.buLogin);
        abuNewAccount=(Button)findViewById(R.id.buNewAccount);


        abuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckLogin();
            }
        });

        abuNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private void CheckLogin() {

        String Email = aEditLoginEmail.getText().toString().trim();
        String Password = aEditLoginPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)) {

            aProgress.setMessage("جاء التحقق من البيانات");
            aProgress.show();

            aAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        aProgress.dismiss();

                        CheckUserExis();

                    } else {

                        aProgress.dismiss();

                        Toast.makeText(LoginActivity.this, "خطاء في تسجيل الدخول ..", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void CheckUserExis() {

        final String user_id = aAuth.getCurrentUser().getUid();

        aDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id))
                {
                    Intent addAdsIntet=new Intent(LoginActivity.this,AccountActivity.class);
                    //addAdsIntet.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// هذه من اجل يحط علم ويحذف الصفحه الي جا منها ولا يمكنه العوده
                    startActivity(addAdsIntet);
                    finish();

                }else
                {
                    Intent accountIntet=new Intent(LoginActivity.this,AccountActivity.class);
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
