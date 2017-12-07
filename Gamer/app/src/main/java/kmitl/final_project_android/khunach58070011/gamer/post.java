package kmitl.final_project_android.khunach58070011.gamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.Posts;
import kmitl.final_project_android.khunach58070011.gamer.model.Requests;

import static kmitl.final_project_android.khunach58070011.gamer.MainActivity.nameGB;

public class post extends AppCompatActivity {

    DatabaseReference mRootRef;
    private static final String TAG = "MyApp";
    private ArrayList<Posts> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private ListView viewList;
    String gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        gid = getIntent().getStringExtra("ID");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        loadpostlist(mRootRef);
    }

    private void loadpostlist(DatabaseReference mRootRef) {
        mRootRef.child("post").child(gid).orderByKey().
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        key = new ArrayList<>();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            Posts requests;
                            requests = singleSnapshot.getValue(Posts.class);
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
        for (Posts items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items.getName());
            temp.put("SEC_COLUMN", items.getType());
            temp.put("THR_COLUMN", items.getDesc());
            temp.put("GAME_COLUMN", items.getGame());
            sentlist.add(temp);
            count += 1;
        }
        listpostAdapter adapter = new listpostAdapter(post.this, sentlist, gid);
        viewList.setAdapter(adapter);
    }

    public void add(View view) {
        Intent intent = new Intent(post.this, CreatePost.class);
        intent.putExtra("ID", gid);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadpostlist(mRootRef);
    }
}
