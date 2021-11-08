package pl.kowalecki.edug.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;

public class LaboMissionFragment  extends Fragment {
    private static final String arg_codename = "arg_codename";
    private static final String arg_missionStart = "arg_missionStart";
    private static final String arg_missionText = "arg_missionText";
    private static final String arg_missionFile = "arg_missionFile";
    private static final String arg_finishTime = "arg_finishTime";
    private static final String arg_finishText = "arg_finishText";
    private static final String arg_missionNumber = "arg_missionNumber";
    String path;
    SessionManagement sessionManagement;
    private String mCodename, mMissionStart, mMissionText, mMissionFile, mFinishTime, mFinishText;
    TextView mTextCodename, mTextMissionStart, mTextMissionText, mTextMissionFile, mTextFinishTime, mTextFinishText;
    Context context;
    String mMissionNumber;
    private String sSys, sLang, sGame, sLogin, sHash, sCrc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_labo_mission, container, false);
        context = getContext();
        sessionManagement = new SessionManagement(getContext());
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();

        if (getArguments() != null) {
            mCodename = getArguments().getString(arg_codename);
            mMissionStart = getArguments().getString(arg_missionStart);
            mMissionText = getArguments().getString(arg_missionText);
            mMissionFile = getArguments().getString(arg_missionFile);
            mFinishTime = getArguments().getString(arg_finishTime);
            mFinishText = getArguments().getString(arg_finishText);
            mMissionNumber = getArguments().getString(arg_missionNumber);
        }
        path = mMissionFile;
        String[] laboMissionName = path.split("/");

        mTextCodename = (TextView) v.findViewById(R.id.labo_mission_name);
        mTextMissionStart = (TextView) v.findViewById(R.id.labo_mission_missionStart);
        mTextMissionText = (TextView) v.findViewById(R.id.labo_mission_text);
        mTextMissionFile = (TextView) v.findViewById(R.id.labo_mission_file);
        mTextFinishTime = (TextView) v.findViewById(R.id.labo_mission_finish_time);
        mTextFinishText = (TextView) v.findViewById(R.id.labo_mission_finish_text);

        mTextCodename.setText(Html.fromHtml(mCodename, HtmlCompat.FROM_HTML_MODE_LEGACY));
        Log.e("Labo", mCodename);
        mTextMissionStart.setText(Html.fromHtml(mMissionStart, HtmlCompat.FROM_HTML_MODE_LEGACY));
        mTextMissionText.setText(Html.fromHtml(mMissionText, HtmlCompat.FROM_HTML_MODE_LEGACY));
        mTextMissionFile.setText(laboMissionName[6]);
        mTextFinishTime.setText(Html.fromHtml(mFinishTime, HtmlCompat.FROM_HTML_MODE_LEGACY));
        mTextFinishText.setText(Html.fromHtml(mFinishText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        mTextMissionFile.setOnClickListener(v1 -> {
            Uri uri = Uri.parse(mMissionFile);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        checkMode();
        return v;
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()){
            mTextCodename.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_top));
            mTextMissionText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
            mTextMissionFile.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
            mTextFinishText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
        }
    }


}
