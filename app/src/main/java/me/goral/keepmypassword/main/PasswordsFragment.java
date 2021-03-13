package me.goral.keepmypassword.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.goral.keepmypassword.R;

public class PasswordsFragment extends Fragment {

    private String username;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passwords_fragment, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            username = bundle.getString("username");
            uid = bundle.getString("uid");
        }

        Button btnTEST = (Button) view.findViewById(R.id.btnTEST);
        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "TEST", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
