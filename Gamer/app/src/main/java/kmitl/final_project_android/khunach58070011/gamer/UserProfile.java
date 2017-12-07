package kmitl.final_project_android.khunach58070011.gamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;

public class UserProfile extends AppCompatActivity {
    private static final String TAG = "Myapp";
    ImageView imageView;
    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    String id;
    String leader;
    String gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setloading();
        id = getIntent().getStringExtra("ID");
        leader = getIntent().getStringExtra("leader");
        gid = getIntent().getStringExtra("gid");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadUserProfile(mRootRef);
        Button b = (Button) findViewById(R.id.kick);
        if (mAuth.getCurrentUser().getUid().equals(leader)){

        }else {
            b.setVisibility(View.INVISIBLE);
        }

    }
    ProgressDialog progress;
    Handler pdCanceller;
    private void setloading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }
    private void loadUserProfile(DatabaseReference mRootRef) {
        mRootRef.child("users").child(id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfoSent user = dataSnapshot.getValue(UserInfoSent.class);
                        if (user == null) {
                            Toast.makeText(UserProfile.this, id, Toast.LENGTH_LONG).show();
                        } else {
                            String sendname;
                            if (user.getAppname() == null){
                                sendname = user.getName();
                            }
                            else if (user.getAppname().equals("")){
                                sendname = user.getName();
                            }else {
                                sendname = user.getAppname();
                            }
                            loadUserPic(user.getPic());
                            writeUserProfile(sendname, user.getEmail(), user.getFavgame(), user.getFavgroup(), user.getDesc());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void writeUserProfile(String s, String userEmail, String favgame, String favgroup, String desc) {
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Name : "+s);
        TextView showemail = (TextView) findViewById(R.id.email);
        showemail.setText("Email : "+userEmail);
        TextView showgame = (TextView) findViewById(R.id.games);
        showgame.setText("favoritegame : "+favgame);
        TextView showgroup = (TextView) findViewById(R.id.group);
        showgroup.setText("favoriteGroup : "+favgroup);
        TextView showdesc = (TextView) findViewById(R.id.desc);
        showdesc.setText(""+desc);
        progress.dismiss();
    }

    private void loadUserPic(String pic) {
        imageView = (ImageView) findViewById(R.id.picprofile);
        String photoUrl = "https://graph.facebook.com/" + pic + "/picture?type=large";
        Glide.with(UserProfile.this).load(photoUrl).into(imageView);
    }

    public void backtogroup(View view) {
        finish();
    }

    public void kick(View view) {
        setloading();
        mRootRef.child("group-users").child(gid).child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                mRootRef.child("messages").child(id).child(gid).removeValue();
                mRootRef.child("requests").child(id).child(gid).removeValue();
                mRootRef.child("user-groups").child(id).child(gid).removeValue();
                progress.dismiss();
                finish();
            }
        });

    }
}
