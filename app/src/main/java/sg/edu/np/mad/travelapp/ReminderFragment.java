package sg.edu.np.mad.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.travelapp.data.model.Reminder;
import sg.edu.np.mad.travelapp.data.model.User;

public class ReminderFragment extends Fragment {
    User user;
    ArrayList<Reminder> reminderList;

    public ReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Profile activity = (Profile) getActivity();
            user = activity.passUser();
        }
        catch (Exception e) {

        }
        finally {
            if (user == null) {
                user = new User(new ArrayList<>());

            }
            reminderList = user.getReminderList();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button create = getView().findViewById(R.id.create);
        RecyclerView recyclerView = getView().findViewById(R.id.reminderRecyclerView);
        RecyclerView.Adapter ReminderAdapter = new ReminderAdapter(getActivity(), user.getReminderList());
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ReminderAdapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createReminderIntent = new Intent(getActivity(), NewReminder.class);
                Bundle extras = new Bundle();
                extras.putParcelable("USER", user);
                createReminderIntent.putExtras(extras);
                startActivity(createReminderIntent);
            }
        });
    }
}