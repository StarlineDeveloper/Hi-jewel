package com.hijewel.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hijewel.R;
import com.hijewel.models.PDFModel;
import com.hijewel.models.UpdatesModel;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class PDFAdapterBullion extends RecyclerView.Adapter<PDFAdapterBullion.Holder> {

    private Context context;
    private ArrayList<PDFModel> arrayList;

    public PDFAdapterBullion(Context context, ArrayList<PDFModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_pdf, null);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        try {
            final PDFModel updateAdapterBullion = arrayList.get(position);
            holder.tv_title.setText(updateAdapterBullion.getTitle());


            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(updateAdapterBullion.getPDFFilesPath().equals("")){
                        Toast.makeText(context,"pdf file path does not exist",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(updateAdapterBullion.getPDFFilesPath()));
                        context.startActivity(i);
                    }

                }
            });


            holder.lay_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ROHIT CHAIN | HI JEWEL");
                    intent.putExtra(Intent.EXTRA_TEXT, "*ROHIT CHAIN | HI JEWEL*  \nDownload Rohit app for latest gold,platinum& diamond jewellery catalogue, live gold rates & much more.\n\n*Download PDF*\n"+updateAdapterBullion.getPDFFilesPath()+"\n\n*Download Android App*\nhttps://play.google.com/store/apps/details?id=com.hijewel\n\n*Download IOS App*\nhttps://apps.apple.com/in/app/hi-jewel/id1355586666");
                    context.startActivity(Intent.createChooser(intent, "choose one"));
                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void filterList(ArrayList<PDFModel> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public PDFModel getItem(int pos) {
        return arrayList.get(pos);
    }


    class Holder extends RecyclerView.ViewHolder {


        TextView tv_title;
        LinearLayout lay;
        LinearLayout lay_share;



        private Holder(View vi) {
            super(vi);

            tv_title=vi.findViewById(R.id.tv_title);
            lay=vi.findViewById(R.id.lay);
            lay_share=vi.findViewById(R.id.lay_share);



        }
    }


}
