package com.akp.blackbulls.Recharge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.blackbulls.R;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.HashMap;
import java.util.List;

public class CategoryServiceListAdapter extends RecyclerView.Adapter<CategoryServiceListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public CategoryServiceListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_recharge_category, viewGroup, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
//        vh.title_tv.setText(arrayList.get(i).get("service_name"));
        vh.provider_name_tv.setText(arrayList.get(i).get("OperatorName"));
        if (arrayList.get(i).get("Image").equalsIgnoreCase("")){
            Toast.makeText(context,"Image not found!", Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("Image")).error(R.drawable.appicon).into(vh.provider_img);
        }
     /*   vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MobileRecharge.class);
                intent.putExtra("servicename",arrayList.get(i).get("OperatorName"));
                intent.putExtra("provider_id",arrayList.get(i).get("OperatorId"));
                intent.putExtra("onlyservice",arrayList.get(i).get("ProviderId"));
                intent.putExtra("Code",arrayList.get(i).get("Code"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
*/




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView provider_name_tv;
        CircularImageView provider_img;

        public VH(@NonNull View itemView) {
            super(itemView);
            provider_name_tv = itemView.findViewById(R.id.provider_name_tv);
            provider_img = itemView.findViewById(R.id.provider_img);
        }
    }
}



