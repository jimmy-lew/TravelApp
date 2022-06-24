package sg.edu.np.mad.travelapp.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.travelapp.NavbarFragment;
import sg.edu.np.mad.travelapp.R;

public class BaseActivity extends AppCompatActivity {

    public void initializeRecycler(RecyclerView.Adapter adapter, RecyclerView recycler, boolean isHorizontal){
        this.runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getApplicationContext(),
                    isHorizontal ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL,
                    false
            );
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(adapter);
            recycler.setAdapter(adapter);
        });
    }

    public void initializeNavbar(Location location){
        Bundle args = new Bundle();
        args.putParcelable("location", location);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navFragmentContainerView, NavbarFragment.class, args)
                .commit();
    }
}
