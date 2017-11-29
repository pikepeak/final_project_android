package kmitl.final_project_android.khunach58070011.gamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;

public class CreateGroupActivity extends AppCompatActivity {
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
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    public void save(View view) {
        showname = (TextView) findViewById(R.id.editName);
        showdesc = (TextView) findViewById(R.id.editDesc);
        showgame = (TextView) findViewById(R.id.favgame);

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
                mGroupUser.child(uniqueKey).child(mAuth.getUid().toString()).child("Type").setValue("owner");
                finish();
            }
        });
    }
    private void loadUserProfile(DatabaseReference mRootRef) {
        mRootRef.child("users").child(mAuth.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(UserInfoSent.class);
                        //finish();
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
}
