package me.ashif.microcorpusclient.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import java.util.List;
import android.view.MenuInflater;
import android.widget.Toast;

import me.ashif.microcorpusclient.R;
import me.ashif.microcorpusclient.model.Employee;


/**
 * Created by almukthar on 5/8/16.
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Employee> employeeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView firstname,lastname,email,doj,empID,password,phone,address,qualification,username,empname;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            empname = (TextView) view.findViewById(R.id.employeenametext);
            firstname = (TextView) view.findViewById(R.id.firstNameDisplay);
            lastname = (TextView) view.findViewById(R.id.lastNameDisplay);
            email = (TextView) view.findViewById(R.id.emailDisplay);
            doj = (TextView) view.findViewById(R.id.dateofjoinDisplay);
            empID = (TextView) view.findViewById(R.id.empiddisplay);
            password = (TextView) view.findViewById(R.id.passwordboxdisplay);
            phone = (TextView) view.findViewById(R.id.empphonedisplay);
            address = (TextView) view.findViewById(R.id.addressDisplay);
            qualification = (TextView) view.findViewById(R.id.qDisplay);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
    public EmployeeAdapter(Context mContext, List<Employee> employeeList) {
        this.mContext = mContext;
        this.employeeList = employeeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.empname.setText(employee.getFirstName().toString().concat(" ").concat(employee.getLastName().toString()));
        holder.firstname.setText(employee.getFirstName());
        holder.lastname.setText(employee.getLastName());
        holder.email.setText(employee.getEmail());
        holder.doj.setText(String.valueOf(employee.getDateOfJoin()));
        holder.empID.setText(employee.getEmpID());
//        holder.username.setText(employee.getUsername());
        holder.password.setText(employee.getPassword());
        holder.phone.setText(employee.getPhone());
        holder.address.setText(employee.getAddress());
        holder.qualification.setText(employee.getQualification());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.emp_menu, popup.getMenu());
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
        return employeeList.size();
    }
}
