package me.goral.keepmypassword.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.security.PrivateKey;

import me.goral.keepmypassword.R;
import me.goral.keepmypassword.utils.FormsClasses;
import me.goral.keepmypassword.utils.asyncTasks.DeleteIndividualContentDB;
import me.goral.keepmypassword.utils.asyncTasks.GetContentDB;
import me.goral.keepmypassword.utils.asyncTasks.SetContentDB;

public class PasswordsFragment extends Fragment implements View.OnClickListener{

    private String uid;
    private View view;
    private TableLayout table;
    private String btnId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.passwords_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null){
            uid = bundle.getString("uid");
        }
        //set content data
        table = view.findViewById(R.id.table);
        setHeader(table);
        setContent();
        //refresh screen
        Button btnRefresh = view.findViewById(R.id.refreshButton);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getFragmentManager().findFragmentByTag("PasswordFragment");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(f).attach(f).commit();
            }
        });

        //add another password
        Button btnAdd = view.findViewById(R.id.addButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity parentActivity = getActivity();
                final ProgressBar pb = view.findViewById(R.id.progressBarPassword);
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Add new password to your list: ");

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText inputDesc = new EditText(getActivity());
                inputDesc.setHint("Description for password");
                linearLayout.addView(inputDesc);
                final EditText inputPassword = new EditText(getActivity());
                inputPassword.setHint("Password");
                linearLayout.addView(inputPassword);
                alert.setView(linearLayout);
                alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String desc = inputDesc.getText().toString().trim();
                        String pwd = inputPassword.getText().toString().trim();
                        if  (desc.isEmpty() || pwd.isEmpty())
                            Toast.makeText(getActivity(), "Description or password can't be empty", Toast.LENGTH_LONG).show();
                        else {

                            FormsClasses.SetContentParams params = new FormsClasses.SetContentParams(uid, desc, pwd);
                            SetContentDB async = new SetContentDB(new SetContentDB.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    String[] result = output.split(";");
                                    Toast.makeText(getActivity(), result[1], Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            async.setProgressBar(pb);
                            async.setParentActivity(parentActivity);
                            async.execute(params);
                        }

                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
        return view;
    }

    private void setHeader(TableLayout table){
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setBackgroundColor(Color.parseColor("#F44336"));
        tr_head.setBackgroundResource(R.drawable.row_border_header);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView labelNum = new TextView(getActivity());
        labelNum.setText(R.string.number);
        labelNum.setTextSize(20);
        labelNum.setGravity(Gravity.CENTER);
        labelNum.setTextColor(Color.BLACK);
        labelNum.setPadding(10,10,10,10);
        tr_head.addView(labelNum);

        TextView labelDesc = new TextView(getActivity());
        labelDesc.setText(R.string.description);
        labelDesc.setTextSize(20);
        labelDesc.setGravity(Gravity.CENTER);
        labelDesc.setTextColor(Color.BLACK);
        labelDesc.setPadding(5,5,5,5);
        tr_head.addView(labelDesc);

        TextView labelPass = new TextView(getActivity());
        labelPass.setText(R.string.passwordLabel);
        labelPass.setTextSize(20);
        labelPass.setGravity(Gravity.CENTER);
        labelPass.setTextColor(Color.BLACK);
        labelPass.setPadding(5,5,5,5);
        tr_head.addView(labelPass);

        table.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }

    private void setContent(){
        final ProgressBar pb = view.findViewById(R.id.progressBarPassword);
        GetContentDB async = new GetContentDB(new GetContentDB.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if(!output.equals("ERR::1")){
                    String[] row = output.split("#&");
                    String desc, pwd, id;
                    for (int i = 0; i < row.length; i++){
                        String[] r = row[i].split(";");
                        desc = r[0];
                        pwd = r[1];
                        id = r[2];
                        generateRow(table, desc, pwd, id, i );
                    }

                } else
                    Toast.makeText(getActivity(), "You have no passwords stored yet. Try to add one by clicking this blue button!", Toast.LENGTH_SHORT).show();

            }
        });
        async.setProgressBar(pb);
        async.setParentActivity(getActivity());
        async.execute(uid);
    }

    private void generateRow(TableLayout table, String desc, String pwd, String id, Integer counter) {

        Integer idV = Integer.valueOf(id);

        TableRow tr = new TableRow(getActivity());
        tr.setId(idV);
        tr.setBackgroundResource(R.drawable.column_border);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView number = new TextView(getActivity());
        number.setId(10000 + idV);
        number.setText(String.valueOf(counter+1));
        number.setTextSize(15);
        number.setGravity(Gravity.CENTER);
        number.setPadding(5, 5, 5, 5);
        tr.addView(number);

        TextView description = new TextView(getActivity());
        description.setId(20000 + idV);
        description.setTextSize(15);
        description.setText(desc);
        description.setGravity(Gravity.CENTER);
        description.setPadding(5, 5, 5, 5);
        tr.addView(description);

        TextView password = new TextView(getActivity());
        password.setId(30000 + idV);
        password.setTextSize(15);
        password.setText(pwd);
        password.setGravity(Gravity.CENTER);
        password.setPadding(5, 5, 5, 5);
        tr.addView(password);

        Button btnDelete = new Button(getActivity());
        btnDelete.setText(R.string.delete);
        btnDelete.setId(idV);
        btnDelete.setBackgroundResource(R.drawable.btn_delete);
        btnDelete.setPadding(5, 5, 5, 5);
        btnDelete.setTextSize(15);
        btnDelete.setOnClickListener(this);
        tr.addView(btnDelete);

        table.addView(tr);
    }


    @Override
    public void onClick(View v) {

        btnId = String.valueOf(v.getId());

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Are you sure?");
        alert.setPositiveButton("YES, DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                DeleteIndividualContentDB async = new DeleteIndividualContentDB(new DeleteIndividualContentDB.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        String[] result = output.split(";");
                        Toast.makeText(getActivity(), result[1], Toast.LENGTH_SHORT).show();
                    }
                });
                async.setParentActivity(getActivity());
                async.execute(btnId);
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
