package kmitl.final_project_android.khunach58070011.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static kmitl.final_project_android.khunach58070011.gamer.MainActivity.nameGB;

public class EditGroup extends AppCompatActivity {
    private static final String TAG = "MyApp";
    private FirebaseAuth mAuth;
    TextView showname;
    TextView showdesc;
    Button showgame;
    String gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        setloading();
        mAuth = FirebaseAuth.getInstance();
        gid = getIntent().getStringExtra("ID");
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        loadGroup(mRootRef);
    }

    private void loadGroup(DatabaseReference mRootRef) {
        mRootRef.child("groups").child(gid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GamerGroup gamerGroup = dataSnapshot.getValue(GamerGroup.class);
                        if (gamerGroup == null) {
                            Toast.makeText(EditGroup.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
                        } else {
                            writeGroup(gamerGroup.getName(), gamerGroup.getDesc(), gamerGroup.getFavgame());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void writeGroup(String sendname, String desc, String favgame) {
        showname = (TextView) findViewById(R.id.editName);
        showname.setText(sendname);
        showdesc = (TextView) findViewById(R.id.editDesc);
        showdesc.setText(desc);
        showgame = (Button) findViewById(R.id.favgame);
        showgame.setText(favgame);
        progress.dismiss();
    }

    public void save(View view) {
        setloading();
        validationNull validationnull = new validationNull();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUsersRef = mRootRef.child("groups");
        if (validationnull.validationEditGroupInputIsNull(showname.getText().toString(), showdesc.getText().toString(), showgame.getText().toString())){
            progress.dismiss();
            Toast.makeText(EditGroup.this, "pls enter all infomation.", Toast.LENGTH_LONG).show();
        }else {
            mUsersRef.child(gid).child("name").setValue(showname.getText().toString(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    mUsersRef.child(gid).child("desc").setValue(showdesc.getText().toString());
                    mUsersRef.child(gid).child("favgame").setValue(showgame.getText().toString());
                    progress.dismiss();
                    finish();
                }
            });

        }
    }

    public void back(View view) {
        finish();
    }

    public void selection(View view) {
        Intent intent = new Intent(EditGroup.this, selectgame.class);
        intent.putExtra("ID", gid);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showgame = (Button) findViewById(R.id.favgame);
        showgame.setText(nameGB);
        nameGB = "";
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
