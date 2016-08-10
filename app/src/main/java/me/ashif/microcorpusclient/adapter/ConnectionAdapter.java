package me.ashif.microcorpusclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.model.Collection;
import me.ashif.microcorpusclient.model.Connection;

/**
 * Created by almukthar on 8/8/16.
 */
public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.MyViewHolder> {
    private Context mContext;
    private List<Connection> connectionList;

    public ConnectionAdapter(Context mContext, List<Connection> connectionList) {
        this.mContext = mContext;
        this.connectionList = connectionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connection_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Connection connection = connectionList.get(position);
        holder.initialAmount.setText(String.valueOf(connection.getInitialAmount()));
        holder.customerNameTitle.setText(connection.getCustomerName());
        holder.customerName.setText(connection.getCustomerName());
        holder.customerType.setText(String.valueOf(connection.getCustomerType()));
        holder.connectedBy.setText(connection.getConnectedBy());
        holder.doc.setText(connection.getDoc());
        holder.address.setText(connection.getAddress());
    }

    @Override
    public int getItemCount() {
        return connectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView customerNameTitle,customerName,customerType,initialAmount,connectedBy, doc,address;

        public MyViewHolder(View itemView) {
            super(itemView);

            customerNameTitle = (TextView) itemView.findViewById(R.id.customernametext);
            customerName = (TextView) itemView.findViewById(R.id.customernamedisplay);
            customerType = (TextView) itemView.findViewById(R.id.customerTypeDisplay);
            connectedBy = (TextView) itemView.findViewById(R.id.connectedbydisplay);
            doc = (TextView) itemView.findViewById(R.id.dateofconnDisplay);
            initialAmount = (TextView) itemView.findViewById(R.id.initialanoutdisplay);
            address = (TextView) itemView.findViewById(R.id.addressTextdisplay);
        }
    }
}
