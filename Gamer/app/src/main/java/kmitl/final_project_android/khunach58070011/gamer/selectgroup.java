package kmitl.final_project_android.khunach58070011.gamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.Requests;

public class selectgroup extends AppCompatActivity {
    DatabaseReference mRootRef;
    private static final String TAG = "MyApp";
    private ArrayList<Requests> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private FirebaseAuth mAuth;
    private ListView viewList;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectgroup);
        id = getIntent().getStringExtra("ID");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadgamelist(mRootRef);
    }

    private void loadgamelist(DatabaseReference mRootRef) {
        mRootRef.child("user-groups").child(id).orderByKey().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        key = new ArrayList<>();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            Requests requests;
                            requests = singleSnapshot.getValue(Requests.class);
                            key.add(singleSnapshot.getKey());
                            list.add(requests);
                        }
                        listingGame();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void listingGame() {
        viewList = (ListView) findViewById(R.id.list_game);
        ArrayList<HashMap> sentlist = new ArrayList<HashMap>();
        HashMap temp;
        int count = 0;
        for (Requests items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items.getName());
            temp.put("ID_COLUMN", key.get(count));
            sentlist.add(temp);
            count += 1;
        }
        listviewAdapter adapter = new listviewAdapter(selectgroup.this, sentlist, id);
        viewList.setAdapter(adapter);
    }
}
