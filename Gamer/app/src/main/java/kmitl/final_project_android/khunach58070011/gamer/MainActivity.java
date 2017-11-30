package kmitl.final_project_android.khunach58070011.gamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;


public class MainActivity extends AppCompatActivity {
    public static String nameGB;
    private static final String TAG = "MyApp";
    private ArrayList<GamerGroup> list = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private FirebaseAuth mAuth;
    ImageView imageView;
    DatabaseReference mRootRef;
    private ListView viewList;
    private int msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadUserProfile(mRootRef);
        loadGrouplist(mRootRef);
    }

    private void loadGrouplist(DatabaseReference mRootRef) {
        mRootRef.child("user-groups").child(mAuth.getCurrentUser().getUid()).orderByChild("name").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        key = new ArrayList<>();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            GamerGroup gamerGroup;
                            gamerGroup = singleSnapshot.getValue(GamerGroup.class);
                            key.add(singleSnapshot.getKey());
                            list.add(gamerGroup);
                        }
                        TextView name = (TextView) findViewById(R.id.groupcnt);
                        name.setText("Group : "+key.size());
                        listingGroup();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });

    }

    private void listingGroup() {
        viewList = (ListView) findViewById(R.id.list_request);
        ArrayList<HashMap> sentlist = new ArrayList<HashMap>();
        HashMap temp;
        int count = 0;
        for (GamerGroup items: list) {
            temp = new HashMap();
            temp.put("FIRST_COLUMN", items.getName());
            temp.put("ID_COLUMN", key.get(count));
            sentlist.add(temp);
            count += 1;
        }
        listviewAdapter adapter = new listviewAdapter(MainActivity.this, sentlist);
        viewList.setAdapter(adapter);
    }

    private void writeUserProfile(String sendname, String email) {
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Name : "+sendname);
        TextView showemail = (TextView) findViewById(R.id.email);
        showemail.setText("Email : \n"+email);
        getmsg();
    }

    private void getmsg() {
        mRootRef.child("messages").child(mAuth.getCurrentUser().getUid()).orderByChild("message").equalTo("unread").
                addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            count += 1;
                        }
                        msg = count;
                        TextView showmsg = (TextView) findViewById(R.id.messagecnt);
                        showmsg.setText("unreadmessage : "+msg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadUserProfile(final DatabaseReference mRootRef) {
        mRootRef.child("users").child(mAuth.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfoSent user = dataSnapshot.getValue(UserInfoSent.class);
                        if (user == null) {
                            Toast.makeText(MainActivity.this, "Error: could not fetch user.", Toast.LENGTH_LONG).show();
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
                            updatedata(mRootRef, user);
                            loadUserPic(user.getPic());
                            writeUserProfile(sendname, user.getEmail());

                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void loadUserPic(String pic) {
        imageView = (ImageView) findViewById(R.id.userpic);
        String photoUrl = "https://graph.facebook.com/" + pic + "/picture?type=large";
        Glide.with(MainActivity.this).load(photoUrl).into(imageView);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void create(View view) {
        Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
        startActivity(intent);
    }

    public void joinGroup(View view) {
        Intent intent = new Intent(MainActivity.this, JoinGroupActivity.class);
        startActivity(intent);
    }

    public void Usermessage(View view) {
        Intent intent = new Intent(MainActivity.this, Message.class);
        startActivity(intent);
    }

    public void edituserprofile(View view) {
        Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(mRootRef);
        loadGrouplist(mRootRef);
    }

    private void updatedata(DatabaseReference databaseReference, final UserInfoSent user) {
        mRootRef.child("user-groups").child(mAuth.getCurrentUser().getUid()).orderByKey().
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    mRootRef.child("group-users").child(singleSnapshot.getKey()).child(mAuth.getCurrentUser().getUid()).setValue(user);
                    count += 1;
                }
                Log.d(TAG, String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }
}
