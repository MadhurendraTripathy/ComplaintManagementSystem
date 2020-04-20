package com.example.complaintmanagementsystem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter_CmsActivity extends RecyclerView.Adapter<RecyclerViewAdapter_CmsActivity.inner_class_viewholder> implements Filterable {

    ArrayList<ComplaintParameters> marrayList;
    ArrayList<ComplaintParameters> marrayListFull;
    String fetchedunitid;

    public RecyclerViewAdapter_CmsActivity(ArrayList<ComplaintParameters> arrayList,String uid_received) {
       marrayList = arrayList;
       if(marrayList!=null){
           marrayListFull = new ArrayList<>(marrayList);
       }
       if(uid_received!=null){
            fetchedunitid = uid_received;
       }
    }


    class inner_class_viewholder extends RecyclerView.ViewHolder{

        TextView unitIdToBeShownInCardView;
        TextView complaintIdToBeShownInCardView;
        TextView categoeryToBeShownInCardView;
        TextView assignedTo_ToBeShownInCardView;
        ImageView urgencyFlagInCardView;

        public inner_class_viewholder(@NonNull final View itemView) {
            super(itemView);
            unitIdToBeShownInCardView = itemView.findViewById(R.id.unitIdToBeShownInCardView);
            complaintIdToBeShownInCardView = itemView.findViewById(R.id.cardViewComplaintId);
            categoeryToBeShownInCardView = itemView.findViewById(R.id.cardViewCategoery);
            assignedTo_ToBeShownInCardView = itemView.findViewById(R.id.cardViewAssignedTo);
            urgencyFlagInCardView = itemView.findViewById(R.id.cardViewUrgencyFlag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pointer_setUpitemViewClick != null && getAdapterPosition()!=RecyclerView.NO_POSITION){
                        pointer_setUpitemViewClick.itemViewClickSetUp(getAdapterPosition());
                    }
                }
            });

        }
    }

    interface setup_itemViewOnclicklistner{
        void itemViewClickSetUp(int pos);
    }

    setup_itemViewOnclicklistner pointer_setUpitemViewClick;

    void openCardView(setup_itemViewOnclicklistner pointer_to_setup_itemViewOnclicklistner){
        pointer_setUpitemViewClick = pointer_to_setup_itemViewOnclicklistner;
    }


    @NonNull
    @Override
    public inner_class_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_for_recyclerview,viewGroup,false);
        inner_class_viewholder vh = new inner_class_viewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull inner_class_viewholder vh, int position) {
        ComplaintParameters cp_current_item = marrayList.get(position);
        if(cp_current_item.getUnitid().equals(fetchedunitid)){
            vh.unitIdToBeShownInCardView.setText(cp_current_item.getUnitid());
            vh.complaintIdToBeShownInCardView.setText(cp_current_item.getComplainId());
            vh.categoeryToBeShownInCardView.setText(cp_current_item.getCategory());
            vh.assignedTo_ToBeShownInCardView.setText(cp_current_item.getAssignedTo());
           // Log.d("Severity " , cp_current_item.getSeverity() + cp_current_item.getUnitid());
            if(cp_current_item.getSeverity().equals("High")){
                vh.urgencyFlagInCardView.setImageResource(R.drawable.important_flag);
            }
            else{
                vh.urgencyFlagInCardView.setImageResource(R.drawable.plussign);
            }
        }

    }


    @Override
    public int getItemCount() {
        if(marrayList != null){
            return marrayList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
        }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ComplaintParameters> filteredlist = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredlist.addAll(marrayListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ComplaintParameters myitem : marrayListFull){
                    if(myitem.getUnitid().toLowerCase().contains(filterPattern) || myitem.getCategory().toLowerCase().contains(filterPattern)){
                        filteredlist.add(myitem);
                    }
                }
            }

            FilterResults  results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            marrayList.clear();
            marrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

    };



}
