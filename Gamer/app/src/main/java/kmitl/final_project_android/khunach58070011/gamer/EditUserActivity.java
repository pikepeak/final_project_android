package kmitl.final_project_android.khunach58070011.gamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;
import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

import static kmitl.final_project_android.khunach58070011.gamer.MainActivity.nameGB;

public class EditUserActivity extends AppCompatActivity {
    private static final String TAG = "MyApp";
    private FirebaseAuth mAuth;
    TextView showname;
    TextView showdesc;
    TextView showgame;
    Button showgroup;
    validationNull validationnull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        loadUserProfile(mRootRef);
        validationnull = new validationNull();
    }

    private void loadUserProfile(DatabaseReference mRootRef) {
        mRootRef.child("users").child(mAuth.getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfoSent user = dataSnapshot.getValue(UserInfoSent.class);
                        if (user == null) {
                            Toast.makeText(EditUserActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                        } else {
                            String sendname;
                            if (user.getAppname() == null){
                                sendname = user.getName();
                            }else {
                                sendname = user.getAppname();
                            }
                            writeUserProfile(sendname, user.getDesc(), user.getFavgame(), user.getFavgroup());
                        }
                        //finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void writeUserProfile(String sendname, String desc, String favgame, String favgroup) {
        showname = (TextView) findViewById(R.id.editName);
        showname.setText(sendname);
        showdesc = (TextView) findViewById(R.id.editDesc);
        showdesc.setText(desc);
        showgame = (TextView) findViewById(R.id.favgame);
        showgame.setText(favgame);
        showgroup = (Button) findViewById(R.id.favgroup);
        showgroup.setText(favgroup);
    }

    public void save(View view) {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("users");
        //mUsersRef.child(user.getUid()).setValue(userInfoSent);
        if (validationnull.validationEditProfileInputIsNull(showname.getText().toString(),showdesc.getText().toString(),showgame.getText().toString(),showgroup.getText().toString())){
            Toast.makeText(EditUserActivity.this, "pls enter all infomation.", Toast.LENGTH_LONG).show();
        }else {
            mUsersRef.child(mAuth.getUid()).child("appname").setValue(showname.getText().toString());
            mUsersRef.child(mAuth.getUid()).child("desc").setValue(showdesc.getText().toString());
            mUsersRef.child(mAuth.getUid()).child("favgame").setValue(showgame.getText().toString());
            mUsersRef.child(mAuth.getUid()).child("favgroup").setValue(showgroup.getText().toString());
            finish();
        }

    }

    public void back(View view) {
        finish();
    }

    public void selection(View view) {
        Intent intent = new Intent(EditUserActivity.this, selectgroup.class);
        intent.putExtra("ID", mAuth.getCurrentUser().getUid());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showgroup = (Button) findViewById(R.id.favgroup);
        showgroup.setText(nameGB);
        nameGB = "";
    }
}
