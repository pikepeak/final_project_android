package kmitl.final_project_android.khunach58070011.gamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.Requests;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;

public class Request extends AppCompatActivity {
    DatabaseReference mRootRef;
    private static final String TAG = "MyApp";
    private ArrayList<Requests> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private FirebaseAuth mAuth;
    private ListView viewList;
    private TextView viewemail;
    String id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        id = getIntent().getStringExtra("ID");
        name = getIntent().getStringExtra("Name");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        viewemail = (TextView) findViewById(R.id.inputemail);
        loadMessagelist(mRootRef);
    }

    private void loadMessagelist(DatabaseReference mRootRef) {
        mRootRef.child("requests").child(id).orderByChild("status").equalTo("wait").
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
                        listingMessage();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void listingMessage() {
        viewList = (ListView) findViewById(R.id.list_request);
        ArrayList<HashMap> sentlist = new ArrayList<HashMap>();
        HashMap temp;
        int count = 0;
        for (Requests items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items.getName());
            temp.put("SEC_COLUMN", items.getEmail());
            temp.put("ID_COLUMN", key.get(count));
            sentlist.add(temp);
            count += 1;
        }
        listviewAdapter adapter = new listviewAdapter(Request.this, sentlist, id);
        viewList.setAdapter(adapter);
    }
    public void sentinv(View view) {
        mRootRef.child("users").orderByChild("email").equalTo(viewemail.getText().toString()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    String key;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                            UserInfoSent user = userSnapshot.getValue(UserInfoSent.class);
                            key = userSnapshot.getKey();
                        }
                        final DatabaseReference mUserMessageRef = mRootRef.child("messages");
                        mUserMessageRef.child(key).child(id).child("name").setValue(name);
                        mUserMessageRef.child(key).child(id).child("type").setValue("inv");
                        mUserMessageRef.child(key).child(id).child("ans").setValue("wait");
                        mUserMessageRef.child(key).child(id).child("message").setValue("unread");
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });

    }
}
