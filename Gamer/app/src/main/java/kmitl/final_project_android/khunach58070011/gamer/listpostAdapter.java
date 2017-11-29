package kmitl.final_project_android.khunach58070011.gamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import kmitl.final_project_android.khunach58070011.gamer.model.GamerGroup;

public class listpostAdapter extends BaseAdapter {
    private static final String TAG = "MyApp";
    public ArrayList<HashMap> list;
    Activity activity;
    int select = 0;
    DatabaseReference mRootRef;
    private GamerGroup gamegroup;
    String gid;

    public listpostAdapter(post post, ArrayList<HashMap> sentlist, String gid) {
        this.activity = post;
        this.list = sentlist;
        this.gid = gid;
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
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_listpost_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
            holder.txtsec = (TextView) convertView.findViewById(R.id.sec);
            holder.txtthree = (TextView) convertView.findViewById(R.id.desc);
            holder.txtgame = (TextView) convertView.findViewById(R.id.games);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtFirst.setText("Name : " + (CharSequence) map.get("FIRST_COLUMN"));
        holder.txtsec.setText("Type : " + (CharSequence) map.get("SEC_COLUMN"));
        holder.txtthree.setText(""+(CharSequence) map.get("THR_COLUMN"));
        holder.txtgame.setText("About Game : "+ map.get("GAME_COLUMN"));
        return convertView;
    }
    private class ViewHolder {
        TextView txtFirst;
        TextView txtsec;
        TextView txtthree;
        TextView txtgame;
    }
}
