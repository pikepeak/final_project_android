package kmitl.final_project_android.khunach58070011.gamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;

public class groupActivity extends AppCompatActivity {
    private static final String TAG = "MyApp";
    String id;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private ListView viewList;
    private ArrayList<UserInfoSent> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    GamerGroup user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        id = getIntent().getStringExtra("ID");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadGroupProfile(mRootRef);
        loadGrouplist(mRootRef);
    }
    private void loadGrouplist(DatabaseReference mRootRef) {
        mRootRef.child("group-users").child(id).orderByChild("name").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        key = new ArrayList<>();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            UserInfoSent userInfoSent;
                            userInfoSent = singleSnapshot.getValue(UserInfoSent.class);
                            key.add(singleSnapshot.getKey());
                            list.add(userInfoSent);
                        };
                        listingGroup();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });

    }

    private void listingGroup() {
        viewList = (ListView) findViewById(R.id.list_request);
        ArrayList<HashMap> sentlist = new ArrayList<HashMap>();
        HashMap temp;
        int count = 0;
        for (UserInfoSent items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items.getName());
            temp.put("ID_COLUMN", key.get(count));
            sentlist.add(temp);
            count += 1;
        }
        listviewAdapter adapter = new listviewAdapter(groupActivity.this, sentlist);
        viewList.setAdapter(adapter);
    }
    private void loadGroupProfile(DatabaseReference mRootRef) {
        mRootRef.child("groups").child(id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(GamerGroup.class);
                        if (user == null) {
                            Toast.makeText(groupActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                        } else {
                            writeProfile();
                        }
                        //finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void writeProfile() {
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Name : "+user.getName());
        TextView favgame = (TextView) findViewById(R.id.favgame);
        favgame.setText("Game : \n"+user.getFavgame());
        TextView desc = (TextView) findViewById(R.id.desc);
        desc.setText(user.getDesc());
    }

    public void news(View view) {
    }

    public void list(View view) {
    }

    public void chat(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroupProfile(mRootRef);
    }

    public void inv_request(View view) {
        Intent intent = new Intent(groupActivity.this, Request.class);
        intent.putExtra("ID", id);
        intent.putExtra("Name", user.getName());
        startActivity(intent);
    }
}
