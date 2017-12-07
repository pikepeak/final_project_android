package kmitl.final_project_android.khunach58070011.gamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import kmitl.final_project_android.khunach58070011.gamer.model.message;

public class Message extends AppCompatActivity {

    private ArrayList<String> list;
    private ArrayList<String> key;
    private String TAG = "Myapp";
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadMessagelist(mRootRef);
    }
    private void loadMessagelist(DatabaseReference mRootRef) {
        mRootRef.child("messages").child(mAuth.getCurrentUser().getUid()).orderByChild("ans").equalTo("wait").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        key = new ArrayList<>();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            String requests = singleSnapshot.child("name").getValue(String.class);
                            Log.d(TAG,requests.toString());
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
        ListView viewList = (ListView) findViewById(R.id.list_message);
        ArrayList<HashMap> sentlist = new ArrayList<HashMap>();
        HashMap temp;
        int count = 0;
        for (String items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items);
            temp.put("ID_COLUMN", key.get(count));
            sentlist.add(temp);
            count += 1;
        }
        listviewAdapter adapter = new listviewAdapter(Message.this, sentlist, mAuth.getCurrentUser().getUid());
        viewList.setAdapter(adapter);
    }
}
