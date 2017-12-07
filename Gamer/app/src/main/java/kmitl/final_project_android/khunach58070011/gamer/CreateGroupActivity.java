package kmitl.final_project_android.khunach58070011.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;
import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

public class CreateGroupActivity extends AppCompatActivity {
    validationNull validationNull = new validationNull();
    private static final String TAG = "MyApp";
    private FirebaseAuth mAuth;
    TextView showname;
    TextView showdesc;
    TextView showgame;
    String uniqueKey;
    UserInfoSent user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mAuth = FirebaseAuth.getInstance();
    }

    public void save(View view) {
        setloading();
        showname = (TextView) findViewById(R.id.editName);
        showdesc = (TextView) findViewById(R.id.editDesc);
        showgame = (TextView) findViewById(R.id.favgame);
        if (validationNull.validationCreateGroupInputIsNull(showname.getText().toString(),showdesc.getText().toString(),showgame.getText().toString())){
            progress.dismiss();
            Toast.makeText(CreateGroupActivity.this, "Infomation cant be null", Toast.LENGTH_LONG).show();
        }else{
            final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            loadUserProfile(mRootRef);
            DatabaseReference mGroupRef = mRootRef.child("groups");
            final GamerGroup gamerGroup = new GamerGroup(showname.getText().toString(),showdesc.getText().toString(),showgame.getText().toString(), mAuth.getUid().toString());
            mGroupRef.push().setValue(gamerGroup , new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    uniqueKey = databaseReference.getKey();
                    DatabaseReference mUserGroup = mRootRef.child("user-groups");
                    mUserGroup.child(mAuth.getUid().toString()).child(uniqueKey).setValue(gamerGroup);
                    DatabaseReference mGroupUser = mRootRef.child("group-users");
                    mGroupUser.child(uniqueKey).child(mAuth.getUid().toString()).setValue(user);
                    final DatabaseReference mUserMessageRef = mRootRef.child("group-list");
                    mUserMessageRef.child(uniqueKey).push().child("name").setValue(gamerGroup.getFavgame());
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
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }
    public void back(View view) {
        finish();
    }
    ProgressDialog progress;
    private void setloading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

}
