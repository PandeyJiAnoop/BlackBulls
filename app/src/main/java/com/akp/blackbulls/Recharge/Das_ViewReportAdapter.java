package com.akp.blackbulls.Recharge;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akp.blackbulls.R;

import java.util.HashMap;
import java.util.List;

public class Das_ViewReportAdapter extends RecyclerView.Adapter<Das_ViewReportAdapter.VH> {
    Context context;
    List<HashMap<String, String>> arrayList;
    public Das_ViewReportAdapter(Context context, List<HashMap<String, String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_das_viewreport, viewGroup, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.tv.setText(arrayList.get(i).get("MemberName"));
        vh.tv1.setText("Status:- "+arrayList.get(i).get("Remark"));
        vh.tv5.setText(arrayList.get(i).get("Date"));
        vh.tv4.setText("Transaction Type:- "+arrayList.get(i).get("TransType"));
        vh.tv3.setText("\u20B9 "+arrayList.get(i).get("Amount"));


        /*Toast.makeText(context,arrayList.get(i).get("Remark"),Toast.LENGTH_LONG).show();
        Log.d("responsedes","des"+arrayList.get(i).get("Amount"));*/

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Your Transaction Status is: - "+arrayList.get(i).get("Remark"),Toast.LENGTH_LONG).show();
            }
        });
      /*  if (arrayList.get(i).get("TransType").equalsIgnoreCase("Credit")){
            vh.tv5.setText("+ \u20B9 "+arrayList.get(i).get("Amount"));
            vh.tv5.setTextColor(Color.GREEN);
        }
        else {
            vh.tv5.setText("- \u20B9 "+arrayList.get(i).get("Amount"));
            vh.tv5.setTextColor(Color.RED);
        }
        if (arrayList.get(i).get("Status").equalsIgnoreCase("Success")){
            vh.tv3.setText("Status:- "+arrayList.get(i).get("Status"));
            vh.tv3.setTextColor(Color.GREEN);
        }
        else {
            vh.tv3.setText("Status:- "+arrayList.get(i).get("Status"));
            vh.tv3.setTextColor(Color.RED);
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv3,tv4,tv5;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv=itemView.findViewById(R.id.tv);
        }
    }
}




