package kmitl.final_project_android.khunach58070011.gamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String leader;
    private String gid;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        id = getIntent().getStringExtra("ID");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadGroupProfile(mRootRef);
        loadGrouplist(mRootRef);
        gid = id;
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
        listviewAdapter adapter = new listviewAdapter(groupActivity.this, sentlist, leader, gid);
        viewList.setAdapter(adapter);
    }
    private void loadGroupProfile(final DatabaseReference mRootRef) {
        mRootRef.child("groups").child(id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(GamerGroup.class);
                        if (user == null) {
                            Toast.makeText(groupActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                        } else {
                            updatedata(mRootRef);
                            writeProfile();
                            Button b = (Button) findViewById(R.id.select);
                            if (mAuth.getCurrentUser().getUid().equals(leader)){
                                b.setText("edit");
                                level = 1;
                            }else {
                                b.setText("leave");
                                level = 0;
                            }
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
        leader = user.getLeader();
    }

    public void news(View view) {
        Intent intent = new Intent(groupActivity.this, post.class);
        intent.putExtra("ID", gid);
        startActivity(intent);
    }

    public void list(View view) {
        Intent intent = new Intent(groupActivity.this, listgame.class);
        intent.putExtra("ID", gid);
        startActivity(intent);
    }

    public void chat(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroupProfile(mRootRef);
        loadGrouplist(mRootRef);

    }

    public void inv_request(View view) {
        Intent intent = new Intent(groupActivity.this, Request.class);
        intent.putExtra("ID", id);
        intent.putExtra("Name", user.getName());
        startActivity(intent);
    }

    public void selection(View view) {
        if (level == 1){
            Intent intent = new Intent(groupActivity.this, EditGroup.class);
            intent.putExtra("ID", gid);
            startActivity(intent);
        }else {
            mRootRef.child("group-users").child(gid).child(mAuth.getCurrentUser().getUid()).removeValue();
            mRootRef.child("messages").child(mAuth.getCurrentUser().getUid()).child(gid).removeValue();
            mRootRef.child("requests").child(mAuth.getCurrentUser().getUid()).child(gid).removeValue();
            mRootRef.child("user-groups").child(mAuth.getCurrentUser().getUid()).child(gid).removeValue();
            finish();
        }
    }
    private void updatedata(final DatabaseReference mRootRef) {
        mRootRef.child("group-users").child(gid).orderByKey().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            mRootRef.child("user-groups").child(singleSnapshot.getKey()).child(gid).setValue(user);
                            count += 1;
                        }
                        Log.d(TAG, String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }
}
