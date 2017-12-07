package kmitl.final_project_android.khunach58070011.gamer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kmitl.final_project_android.khunach58070011.gamer.model.Posts;
import kmitl.final_project_android.khunach58070011.gamer.validation.validationNull;

import static kmitl.final_project_android.khunach58070011.gamer.MainActivity.nameGB;

public class CreatePost extends AppCompatActivity {
    private static final String TAG = "MyApp";
    private FirebaseAuth mAuth;
    TextView showname;
    TextView showdesc;
    Button game;
    private ToggleButton swap_button;
    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        gid = getIntent().getStringExtra("ID");
        mAuth = FirebaseAuth.getInstance();
        swap_button = (ToggleButton) findViewById(R.id.type);
    }

    @SuppressLint("WrongViewCast")
    public void save(View view) {
        setloading();
        showname = (TextView) findViewById(R.id.editName);
        showdesc = (TextView) findViewById(R.id.editDesc);
        game = (Button) findViewById(R.id.games);
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mGroupRef = mRootRef.child("post").child(gid);
        final Posts post = new Posts(showname.getText().toString(),showdesc.getText().toString(),swap_button.getText().toString(),game.getText().toString());
        validationNull validationnull = new validationNull();
        if (validationnull.validationPostInputIsNull(showname.getText().toString(),showdesc.getText().toString(),swap_button.getText().toString(),game.getText().toString())){
            progress.dismiss();
            Toast.makeText(CreatePost.this, "pls enter all infomation.", Toast.LENGTH_LONG).show();
        }else{
            mGroupRef.push().setValue(post , new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    progress.dismiss();
                    finish();
                }
            });
        }

    }
    ProgressDialog progress;
    private void setloading() {
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }

    public void back(View view) {
        finish();
    }

    public void selection(View view) {
        Intent intent = new Intent(CreatePost.this, selectgame.class);
        intent.putExtra("ID", gid);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        game = (Button) findViewById(R.id.games);
        game.setText(nameGB);
    }
}
