package pl.kowalecki.edug.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import pl.kowalecki.edug.Model.Missions.ListMission;
import pl.kowalecki.edug.R;

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.MissionsViewHolder> {

    private List<ListMission> mListMissions;
    private ArrayList<String> mHelperList = new ArrayList<>();
    private String mMission;
    private String mMissionName;
    private String mDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class MissionsViewHolder extends RecyclerView.ViewHolder {

        public TextView mMissionIdu;
        public TextView mMissionNameText;

        public MissionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mMissionIdu = itemView.findViewById(R.id.mission_idu);
            mMissionNameText = itemView.findViewById(R.id.mission_type_name);
        }

    }


    public MissionsAdapter(List<ListMission> listMissions, String mission, String missionName, String date) throws ParseException {
        this.mListMissions = listMissions;
        this.mMission = mission;
        this.mMissionName = missionName;
        this.mDate = date;


        for (int i = 0; i < mListMissions.size(); i++) {
            Date dateTimeNow = simpleDateFormat.parse(mDate);
            Date dateTimeStart = simpleDateFormat.parse(mListMissions.get(i).getMission().getStart());
            Date dateTimeEnd = simpleDateFormat.parse(mListMissions.get(i).getMission().getStop());

            if (mListMissions.get(i).getMission().getType().equals(mMission)) {
                if (dateTimeNow.after(dateTimeStart)) {
                    if(dateTimeNow.before(dateTimeEnd)){
                    mHelperList.add(mListMissions.get(i).getMission().getIdm());
                }
                }
            }
        }

    }

    @NonNull
    @Override
    public MissionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.missions_cardview, parent, false);
        MissionsViewHolder lvh = new MissionsViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MissionsViewHolder holder, int position) {
        String currentMissionName = mMissionName;
        String currentIdu = mHelperList.get(position);
        holder.mMissionNameText.setText(currentMissionName);
        holder.mMissionIdu.setText(currentIdu);


    }

    @Override
    public int getItemCount() {
        return mHelperList.size();
    }




}

