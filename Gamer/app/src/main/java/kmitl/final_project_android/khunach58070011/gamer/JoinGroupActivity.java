package kmitl.final_project_android.khunach58070011.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.Requests;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;
import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

public class JoinGroupActivity extends AppCompatActivity {
    private static final String TAG = "Myapp";
    TextView gid;
    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    UserInfoSent user;
    String gidsave;
    String sendname;
    validationNull validationnull;
    String leader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        validationnull = new validationNull();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        setloading();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadUserProfile(mRootRef);
        gid = (TextView) findViewById(R.id.groupid);
    }
    ProgressDialog progress;
    private void setloading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }
    public void check(View view) {
        setloading();
        mRootRef.child("groups").orderByKey().equalTo(gid.getText().toString()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            GamerGroup gamerGroup;
                            gamerGroup = singleSnapshot.getValue(GamerGroup.class);
                            TextView name = (TextView) findViewById(R.id.request);
                            count = 1;
                            leader = gamerGroup.getLeader();
                            if (leader.equals(mAuth.getCurrentUser().getUid())){
                                Toast.makeText(JoinGroupActivity.this, "you are leader of this group.", Toast.LENGTH_LONG).show();
                            }else {
                                name.setText("Group : \n"+gamerGroup.getName());
                                gidsave = gid.getText().toString();
                            }
                        }
                        if (validationnull.validationJoinGroupInputIsNull(gid.getText().toString())){
                            Toast.makeText(JoinGroupActivity.this, "Input Is Empty.", Toast.LENGTH_LONG).show();
                        }
                        else if (count == 0){
                            Toast.makeText(JoinGroupActivity.this, "Not Found.", Toast.LENGTH_LONG).show();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                        progress.dismiss();
                    }
                });
    }

    public void sentrequest(View view) {
        setloading();
        if (gidsave == null){
            progress.dismiss();
            Toast.makeText(JoinGroupActivity.this, "Pls Check Group First.", Toast.LENGTH_LONG).show();
        }else{
            final DatabaseReference mRequestRef = mRootRef.child("requests");
            final Requests requests = new Requests(sendname, user.getEmail());
            mRequestRef.child(gidsave).child(mAuth.getCurrentUser().getUid()).setValue(requests, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    mRequestRef.child(gidsave).child(mAuth.getCurrentUser().getUid()).child("status").setValue("wait");
                    progress.dismiss();
                    finish();
                }
            });

        }
    }
    private void loadUserProfile(DatabaseReference mRootRef) {
        mRootRef.child("users").child(mAuth.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(UserInfoSent.class);

                        if (user.getAppname() == null){
                            sendname = user.getName();
                        }else {
                            sendname = user.getAppname();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                        progress.dismiss();
                    }
                });
    }
}
