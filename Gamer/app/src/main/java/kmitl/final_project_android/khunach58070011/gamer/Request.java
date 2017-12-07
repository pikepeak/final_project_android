package kmitl.final_project_android.khunach58070011.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.Requests;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;
import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

public class Request extends AppCompatActivity {
    DatabaseReference mRootRef;
    private static final String TAG = "MyApp";
    private ArrayList<Requests> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private ListView viewList;
    private TextView viewemail;
    String id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        setloading();
        id = getIntent().getStringExtra("ID");
        name = getIntent().getStringExtra("Name");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        viewemail = (TextView) findViewById(R.id.inputname);
        loadMessagelist(mRootRef);
    }
    ProgressDialog progress;
    private void setloading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
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
        progress.dismiss();
    }
    public void sentinv(View view) {
        setloading();
        final validationNull validationNull = new validationNull();
        mRootRef.child("users").orderByChild("email").equalTo(viewemail.getText().toString()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    String key;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UserInfoSent user = userSnapshot.getValue(UserInfoSent.class);
                            key = userSnapshot.getKey();
                            count += 1;
                        }
                        Log.d(TAG, String.valueOf(count));
                        if (validationNull.validationInvInputIsNull(viewemail.getText().toString())){
                            progress.dismiss();
                            Toast.makeText(Request.this, "pls enter email.", Toast.LENGTH_LONG).show();
                        } else if (count <= 0){
                            progress.dismiss();
                            Toast.makeText(Request.this, "not found user.", Toast.LENGTH_LONG).show();
                        }else{
                        final DatabaseReference mUserMessageRef = mRootRef.child("messages");
                        mUserMessageRef.child(key).child(id).child("name").setValue(name, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mUserMessageRef.child(key).child(id).child("type").setValue("inv");
                                mUserMessageRef.child(key).child(id).child("ans").setValue("wait");
                                mUserMessageRef.child(key).child(id).child("message").setValue("unread");
                                progress.dismiss();
                                finish();
                            }
                        });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                        progress.dismiss();
                    }
                });

    }
}
