package ah1.com.advertisements_v1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by A on 7/31/2018.
 */

public class Tab4Education extends Fragment {

    private RecyclerView aAdsList;
    private DatabaseReference aDatabase;
    private FirebaseAuth aAuth;

    private DatabaseReference aDatabaseClothes;
    private Query aQueryClothes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4education, container, false);
        final Context context;

        aAuth= FirebaseAuth.getInstance();

        aDatabase= FirebaseDatabase.getInstance().getReference().child("Ads");

        aDatabaseClothes= FirebaseDatabase.getInstance().getReference().child("Ads");
        aQueryClothes=aDatabaseClothes.orderByChild("TypeAds").equalTo("مكتبات");


        aAdsList=(RecyclerView)rootView.findViewById(R.id.ads_list);
        aAdsList.setHasFixedSize(true);
        aAdsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return rootView;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////
    //----------------------------  RecyclerView ------------------------------------------
    @Override
    public void onStart() {
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
                //  viewHolder.setDesc(model.getDescrption());
                // viewHolder.setCreationDate(model.getCreationDate());
                viewHolder.setImage(getContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setPhone(model.getPhone());
                viewHolder.setLocation(model.getLocation());
                // viewHolder.setImageUser(getApplicationContext(),model.getImageuser());

                //--------------------عند الضغط على الريسايكل الفيو المحدد
                viewHolder.aView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(MainActivity.this, ads_key, Toast.LENGTH_SHORT).show();
                        Intent singleAdsIntent=new Intent(Tab4Education.this.getContext(),AdsSingleActivity.class);
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


}
