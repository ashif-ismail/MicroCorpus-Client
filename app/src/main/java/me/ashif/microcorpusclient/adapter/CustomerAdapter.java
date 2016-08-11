package me.ashif.microcorpusclient.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;
import java.util.List;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.model.Customer;
import me.ashif.microcorpusclient.model.Employee;

/**
 * Created by almukthar on 6/8/16.
 */
public class CustomerAdapter extends  RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Customer> customerList;

    public CustomerAdapter(Context mContext, List<Customer> customerList) {
        this.mContext = mContext;
        this.customerList = customerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_row, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.customertitle.setText(customer.getCustomerName());
        holder.customerID.setText(customer.getCustomerID());
        holder.dateofConn.setText(String.valueOf(customer.getDateOfConn()));
        holder.initialAmnt.setText(String.valueOf(customer.getInitialAmount()));
        holder.dateOfDue.setText(String.valueOf(customer.getDateOfDue()));
        holder.installmentAmnt.setText(String.valueOf(customer.getInstallmentAmount()));
        holder.totalAmnt.setText(String.valueOf(customer.getTotalAmount()));
        holder.customerType.setText(String.valueOf(customer.getCustomerType()));
        holder.customerName.setText(customer.getCustomerName());
        holder.customerGuardian.setText(customer.getCustomerGuardian());
        holder.username.setText(customer.getUsername());
        holder.password.setText(customer.getPassword());
        holder.mobileNo.setText(String.valueOf(customer.getMobileNo()));
        holder.connectedBy.setText(customer.getConnectedBy());
        holder.address.setText(customer.getAddress());

//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.customer_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove:
                    Toast.makeText(mContext, "remove", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_edit:
                    Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

    }
    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customertitle,customerID,dateofConn,initialAmnt,dateOfDue,installmentAmnt,totalAmnt,customerType,customerName,customerGuardian,username,password,mobileNo,connectedBy,address;
        public ImageView overflow;
        public MyViewHolder(View view) {
            super(view);
            customertitle = (TextView) view.findViewById(R.id.customernametext);
            customerID = (TextView) view.findViewById(R.id.customeridDisplay);
            dateofConn = (TextView) view.findViewById(R.id.dateofconnDisplay);
            initialAmnt = (TextView) view.findViewById(R.id.initialanoutdisplay);
            dateOfDue = (TextView) view.findViewById(R.id.dateofduetextdisplay);
            installmentAmnt = (TextView) view.findViewById(R.id.installmentamntdisplay);
            totalAmnt = (TextView) view.findViewById(R.id.totaldisplay);
            customerType = (TextView) view.findViewById(R.id.customerTypeDisplay);
            customerName = (TextView) view.findViewById(R.id.customernamedisplay);
            customerGuardian = (TextView) view.findViewById(R.id.fathernameDisplay);
            username = (TextView) view.findViewById(R.id.customerusernametextdisplay);
            password  = (TextView) view.findViewById(R.id.customerpassworddisplay);
            mobileNo = (TextView) view.findViewById(R.id.mobileDisplay);
            connectedBy = (TextView) view.findViewById(R.id.connectedbydisplay);
            address = (TextView) view.findViewById(R.id.addressTextdisplay);
//            overflow = (ImageView) view.findViewById(R.id.coverflow);
        }
    }
    }

