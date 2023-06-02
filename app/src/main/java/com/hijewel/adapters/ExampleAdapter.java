package com.hijewel.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.hijewel.R;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private Context context;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        LinearLayout lay;
        LinearLayout lay_share;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.tv_title);
            lay = itemView.findViewById(R.id.lay);
            lay_share = itemView.findViewById(R.id.lay_share);
        }
    }
    public ExampleAdapter(Context context,ArrayList<ExampleItem> exampleList) {
        this.context = context;
        mExampleList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        final ExampleItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem.getText1());

        Log.e("pdfpath",""+currentItem.getPDFFilesPath());

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem.getPDFFilesPath().equals("")){
                    Toast.makeText(context,"pdf file path does not exist",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(currentItem.getPDFFilesPath()));
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
                intent.putExtra(Intent.EXTRA_TEXT, "*ROHIT CHAIN | HI JEWEL*  \nDownload Rohit app for latest gold,platinum& diamond jewellery catalogue, live gold rates & much more.\n\n*Download PDF*\n"+currentItem.getPDFFilesPath()+"\n\n*Download Android App*\nhttps://play.google.com/store/apps/details?id=com.hijewel\n\n*Download IOS App*\nhttps://apps.apple.com/in/app/hi-jewel/id1355586666");
                context.startActivity(Intent.createChooser(intent, "choose one"));
            }
        });




    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void filterList(ArrayList<ExampleItem> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}
