package com.hijewel.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hijewel.R;
import com.hijewel.models.UpdatesModel;

import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class UpdateAdapterBullion extends RecyclerView.Adapter<UpdateAdapterBullion.Holder> {

    private Context context;
    private ArrayList<UpdatesModel> arrayList;

    public UpdateAdapterBullion(Context context, ArrayList<UpdatesModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.row_updates, null);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        try {
            UpdatesModel updateAdapterBullion = arrayList.get(position);
            holder.tv_title.setText(updateAdapterBullion.getDate() + ", " + updateAdapterBullion.getMonth());
            holder.tv_time.setText("Time: " + updateAdapterBullion.getTime());

            holder.tv_heading.setText(Html.fromHtml(updateAdapterBullion.getTitle()));

            holder.tv_des.setText(Html.fromHtml(updateAdapterBullion.getDescription()));


            Log.e("Descrition", updateAdapterBullion.getDescription());


//            holder.tv_title.setText(html2text(updateAdapterBullion.getTitle()));
//            holder.tvDescription.setText(html2text(updateAdapterBullion.getDescription()));
//            holder.tvTime.setText(updateAdapterBullion.getTime());
//            holder.tvMonthYear.setText(updateAdapterBullion.getMonth() + "'" + updateAdapterBullion.getYear());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public UpdatesModel getItem(int pos) {
        return arrayList.get(pos);
    }


    class Holder extends RecyclerView.ViewHolder {


        TextView tv_title, tv_time, tv_heading, tv_des;


        private Holder(View vi) {
            super(vi);

            tv_title = vi.findViewById(R.id.tv_title);
            tv_time = vi.findViewById(R.id.tv_time);

            tv_heading = vi.findViewById(R.id.tv_heading);

            tv_des = vi.findViewById(R.id.tv_des);


        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
