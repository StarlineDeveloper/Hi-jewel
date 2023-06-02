package com.hijewel.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijewel.R;
import com.hijewel.models.SocialModel;
import com.hijewel.models.UpdatesModel;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hijewel.utils.Constatnts.URL;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.Holder> {

    private Context context;
    private ArrayList<SocialModel> arrayList;

    public SocialAdapter(Context context, ArrayList<SocialModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_social, null);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        try {
            final SocialModel updateAdapterBullion = arrayList.get(position);


//
//            Picasso.with(context).load(updateAdapterBullion.geticon()).into(holder.profile_image);
//
//            holder.profile_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Uri uri = Uri.parse(updateAdapterBullion.getlink());
//                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
//                    likeIng.setPackage("com."+updateAdapterBullion.getTitle()+".android");
//                    try {
//                        context.startActivity(likeIng);
//                    } catch (ActivityNotFoundException e) {
//                        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
//                    }
//
//                    dialog.dismiss();
//                    social.setVisibility(View.VISIBLE);

                    /*if(updateAdapterBullion.getTitle().equals("facebook")){

                    }
                    else if(updateAdapterBullion.getTitle().equals("INSTAGRAM")){

                    }
                    else if(updateAdapterBullion.getTitle().equals("LINKEDIN")){

                    }else {



                    }*/

          //      }
         //   });



        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public SocialModel getItem(int pos) {
        return arrayList.get(pos);
    }


    class Holder extends RecyclerView.ViewHolder {




        CircleImageView profile_image;



        private Holder(View vi) {
            super(vi);
            profile_image=vi.findViewById(R.id.profile_image);



        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
