package ah1.com.advertisements_v1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ClothesAdsActivity extends AppCompatActivity {
    private RecyclerView aAdsList;
    private DatabaseReference aDatabase;
    private FirebaseAuth aAuth;

    private DatabaseReference aDatabaseClothes;
    private Query aQueryClothes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_ads);

        aAuth=FirebaseAuth.getInstance();

        aDatabase= FirebaseDatabase.getInstance().getReference().child("Ads");


        //-------------------------- من اجل يحدد اسم المستخدم الذي سيعرضعه من خلال الالي دي تبعه الاعلانات التي اضافها
        //String TypeAdsClothes=aAuth.getCurrentUser().getUid();

        aDatabaseClothes= FirebaseDatabase.getInstance().getReference().child("Ads");
        aQueryClothes=aDatabaseClothes.orderByChild("TypeAds").equalTo("ملابس");

        //////////////////////////////////////////////////////////////////////////////////////

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
        FirebaseRecyclerAdapter<Ads,MainActivity.AdsViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Ads, MainActivity.AdsViewHolder>
                (

                        Ads.class,
                        R.layout.ads_row,
                        MainActivity.AdsViewHolder.class,
                        aQueryClothes

                )
        {
            /// انتبه للاقواس
            @Override
            protected void populateViewHolder(MainActivity.AdsViewHolder viewHolder, Ads model, int position) {

                final String ads_key=getRef(position).getKey();//يسحب رقم الاي دي حق الريسايكل فيو للمحدده ب البوزيشن الموقع المحدد عند الضغط

                viewHolder.setTitle(model.getTitle());
               // viewHolder.setDesc(model.getDescrption());
               // viewHolder.setCreationDate(model.getCreationDate());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());

                //--------------------عند الضغط على الريسايكل الفيو المحدد
                viewHolder.aView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(MainActivity.this, ads_key, Toast.LENGTH_SHORT).show();
                        Intent singleAdsIntent=new Intent(ClothesAdsActivity.this,AdsSingleActivity.class);
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
        }

        public void setUsername(String username)
        {
            TextView ads_username=(TextView)aView.findViewById(R.id.ads_username);
            ads_username.setText(username);
        }

        public void setCreationDate(String creationDate)
        {
          //  TextView ads_creationDate=(TextView)aView.findViewById(R.id.ads_creationDate);
           // ads_creationDate.setText(creationDate);
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
    }
//////////////////////////////////////////////////////////////////////////////////////////////////

}

