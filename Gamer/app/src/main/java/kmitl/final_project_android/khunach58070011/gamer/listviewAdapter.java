package kmitl.final_project_android.khunach58070011.gamer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;
import kmitl.final_project_android.khunach58070011.gamer.model.UserInfoSent;

import static kmitl.final_project_android.khunach58070011.gamer.MainActivity.nameGB;


class listviewAdapter extends BaseAdapter {
    private static final String TAG = "MyApp";
    public ArrayList<HashMap> list;
    Activity activity;
    int select = 0;
    DatabaseReference mRootRef;
    String id = "test";
    UserInfoSent user;
    private GamerGroup gamegroup;
    String leader;
    String gid;

    public listviewAdapter(MainActivity mainActivity, ArrayList<HashMap> list) {
        this.list = list;
        this.activity = mainActivity;
        select = 0;
    }

    public listviewAdapter(groupActivity groupActivity, ArrayList<HashMap> sentlist, String leader, String gid) {
        this.list = sentlist;
        this.activity = groupActivity;
        select = 1;
        this.leader = leader;
        this.gid = gid;
    }

    public listviewAdapter(Request request, ArrayList<HashMap> sentlist, String id) {
        this.list = sentlist;
        this.activity = request;
        select = 2;
        this.id = id;
    }

    public listviewAdapter(Message message, ArrayList<HashMap> sentlist,String id) {
        this.list = sentlist;
        this.activity = message;
        select = 3;
        this.id = id;
    }

    public listviewAdapter(listgame listgame, ArrayList<HashMap> sentlist, String gid) {
        this.list = sentlist;
        this.activity = listgame;
        select = 4;
        this.gid = gid;
    }

    public listviewAdapter(selectgame selectgame, ArrayList<HashMap> sentlist, String gid) {
        this.list = sentlist;
        this.activity = selectgame;
        select = 5;
        this.gid = gid;
    }

    public listviewAdapter(selectgroup selectgroup, ArrayList<HashMap> sentlist, String id) {
        this.list = sentlist;
        this.activity = selectgroup;
        select = 6;
        this.gid = id;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        mRootRef = FirebaseDatabase.getInstance().getReference();
        final HashMap map = list.get(position);
        if (select == 2){
            loaddata(map);
            loadgroup();
        }else if (select == 3){
            loaddatamessage();
            loadgroupmessage(map);
        }
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_group_view, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
            holder.txtsec = (TextView) convertView.findViewById(R.id.sec);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (select == 1) {
            holder.txtsec.setVisibility(View.GONE);
            holder.txtFirst.setText("Name : " + (CharSequence) map.get("FIRST_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            view.setText("View");
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, UserProfile.class);
                    intent.putExtra("ID", String.valueOf(map.get("ID_COLUMN")));
                    intent.putExtra("leader", leader);
                    intent.putExtra("gid", gid);
                    activity.startActivity(intent);
                }
            });
        } else if (select == 2) {
            final DatabaseReference mMessageRef = mRootRef.child("requests");
            final DatabaseReference mUserMessageRef = mRootRef.child("messages");
            holder.txtFirst.setText("user:" + (CharSequence) map.get("FIRST_COLUMN"));
            holder.txtsec.setText("email: " + (CharSequence) map.get("SEC_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            view.setText("NO");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setloading();
                    mMessageRef.child(id).child(String.valueOf(map.get("ID_COLUMN"))).child("status").setValue("no", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            progress.dismiss();
                            activity.finish();
                        }
                    });

                }
            });
            viewyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setloading();
                    mMessageRef.child(id).child(String.valueOf(map.get("ID_COLUMN"))).child("status").setValue("yes", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            DatabaseReference mUserGroup = mRootRef.child("user-groups");
                            mUserGroup.child(String.valueOf(map.get("ID_COLUMN"))).child(id).setValue(gamegroup);
                            DatabaseReference mGroupUser = mRootRef.child("group-users");
                            mGroupUser.child(id).child(String.valueOf(map.get("ID_COLUMN"))).setValue(user);
                            mGroupUser.child(id).child(String.valueOf(map.get("ID_COLUMN"))).child("Type").setValue("user");
                            progress.dismiss();
                            activity.finish();
                        }
                    });

                }
            });
        }else if(select == 3){
            holder.txtsec.setVisibility(View.GONE);
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final DatabaseReference mUserMessageRef = mRootRef.child("messages");
            holder.txtFirst.setText("group:" +map.get("FIRST_COLUMN")+"have invite you");
            Button view = (Button) convertView.findViewById(R.id.mod);
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setText("Join");
            view.setText("Deny");
            mUserMessageRef.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(map.get("ID_COLUMN"))).child("message").setValue("read");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setloading();
                    mUserMessageRef.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(map.get("ID_COLUMN"))).child("ans").setValue("no", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            progress.dismiss();
                            activity.finish();
                        }
                    });
                }
            });
            viewyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setloading();
                    mUserMessageRef.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(map.get("ID_COLUMN"))).child("ans").setValue("yes", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            DatabaseReference mUserGroup = mRootRef.child("user-groups");
                            mUserGroup.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(map.get("ID_COLUMN"))).setValue(gamegroup);
                            DatabaseReference mGroupUser = mRootRef.child("group-users");
                            mGroupUser.child(String.valueOf(map.get("ID_COLUMN"))).child(mAuth.getCurrentUser().getUid()).setValue(user);
                            mGroupUser.child(String.valueOf(map.get("ID_COLUMN"))).child(mAuth.getCurrentUser().getUid()).child("Type").setValue("user");
                            progress.dismiss();
                            activity.finish();
                        }
                    });

                }
            });

        }else if(select == 4){
            holder.txtsec.setVisibility(View.GONE);
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final DatabaseReference mUserMessageRef = mRootRef.child("messages");
            holder.txtFirst.setText("game:" +map.get("FIRST_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            view.setVisibility(View.GONE);
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setVisibility(View.GONE);
        }else if (select == 5) {
            holder.txtsec.setVisibility(View.GONE);
            holder.txtFirst.setText("Name: " + (CharSequence) map.get("FIRST_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            view.setText("select");
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameGB = map.get("FIRST_COLUMN").toString();
                    activity.finish();
                }
            });
        }else if (select == 6) {
            holder.txtsec.setVisibility(View.GONE);
            holder.txtFirst.setText("GroupName: " + (CharSequence) map.get("FIRST_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            view.setText("select");
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameGB = map.get("FIRST_COLUMN").toString();
                    activity.finish();
                }
            });
        }  else {
            holder.txtsec.setVisibility(View.GONE);
            holder.txtFirst.setText("Group Name : " + (CharSequence) map.get("FIRST_COLUMN"));
            Button view = (Button) convertView.findViewById(R.id.mod);
            Button viewyes = (Button) convertView.findViewById(R.id.buttonyes);
            viewyes.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, groupActivity.class);
                    intent.putExtra("ID", String.valueOf(map.get("ID_COLUMN")));
                    activity.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private void loaddatamessage() {
        mRootRef.child("users").child(id).
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
    private void loadgroupmessage(HashMap map){
        mRootRef.child("groups").child(String.valueOf(map.get("ID_COLUMN"))).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        gamegroup = dataSnapshot.getValue(GamerGroup.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void loadgroup() {
        mRootRef.child("groups").child(id).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        gamegroup = dataSnapshot.getValue(GamerGroup.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.getMessage());
                    }
                });
    }

    private void loaddata(HashMap map) {
        mRootRef.child("users").child(String.valueOf(map.get("ID_COLUMN"))).
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

    private class ViewHolder {
        TextView txtFirst;
        TextView txtsec;
    }
    ProgressDialog progress;
    private void setloading() {
        progress = new ProgressDialog(this.activity);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
    }
}
