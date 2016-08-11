package me.ashif.microcorpusclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import junit.framework.TestCase;

import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.model.Collection;


/**
 * Created by almukthar on 6/8/16.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder>{

    private Context mContext;
    private List<Collection> collectionList;

    public CollectionAdapter(Context mContext, List<Collection> collectionList) {
        this.mContext = mContext;
        this.collectionList = collectionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.customerID.setText(collection.getCustomerID());
        holder.customerIDtitle.setText(collection.getCustomerID());
        holder.collectedBy.setText(collection.getCollectedBy());
        holder.collectionAmount.setText(String.valueOf(collection.getCollectionAmount()));
        holder.doc.setText(String.valueOf(collection.getDateOfCollection()));
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerIDtitle,customerID,collectionAmount,collectedBy,doc;

        public MyViewHolder(View itemView) {
            super(itemView);

            customerIDtitle = (TextView) itemView.findViewById(R.id.coll_customerID);
            customerID = (TextView) itemView.findViewById(R.id.coll_customerIDtextDisplay);
            collectionAmount = (TextView) itemView.findViewById(R.id.coll_amounttextdisplay);
            collectedBy = (TextView) itemView.findViewById(R.id.coll_collectedbytextdisplay);
            doc = (TextView) itemView.findViewById(R.id.coll_doctextdisplay);
        }
    }
}
