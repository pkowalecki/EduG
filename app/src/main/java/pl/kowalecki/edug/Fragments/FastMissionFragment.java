package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Model.MissionFast.MissionFast;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FastMissionFragment extends Fragment {
    private static final String arg_codename = "arg_codename";
    private static final String arg_picture = "arg_picture";
    private static final String arg_introTime = "arg_introTime";
    private static final String arg_introText = "arg_introText";
    private static final String arg_missionStart = "arg_missionStart";
    private static final String arg_missionText = "arg_missionText";
    private static final String arg_finishTime = "arg_finishTime";
    private static final String arg_finishText = "arg_finishText";
    private static final String arg_missionNumber = "arg_missionNumber";

    SessionManagement sessionManagement;
    private String mCodename, mPicture, mIntroTime, mIntroText, mMissionStart, mMissionText, mFinishTime, mFinishText;
    TextView mTextCodename, mTextPicture, mTextIntroTime, mTextIntroText, mTextMissionStart, mTextMissionText, mTextFinishTime, mTextFinishText;
    private TextInputLayout answerInputField;
    String mMissionNumber;
    private String sSys, sLang, sGame, sLogin, sHash, sCrc;
    Button answerButton;
    public static FastMissionFragment newInstance(String codename, String picture, String introTime, String introText ,
                                                  String missionStart , String missionText , String finishTime,
                                                  String finishText, String missionNumber){
        FastMissionFragment fragment = new FastMissionFragment();
        Bundle args = new Bundle();
        args.putString(arg_codename, codename);
        args.putString(arg_picture, picture);
        args.putString(arg_introTime, introTime);
        args.putString(arg_introText, introText);
        args.putString(arg_missionStart, missionStart);
        args.putString(arg_missionText, missionText);
        args.putString(arg_finishTime, finishTime);
        args.putString(arg_finishText, finishText);
        args.putString(arg_missionNumber, missionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fast_mission, container, false);
        sessionManagement = new SessionManagement(getContext());
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();

        if (getArguments() != null) {
            mCodename = getArguments().getString(arg_codename);
            mPicture = getArguments().getString(arg_picture);
            mIntroTime = getArguments().getString(arg_introTime);
            mIntroText = getArguments().getString(arg_introText);
            mMissionStart = getArguments().getString(arg_missionStart);
            mMissionText = getArguments().getString(arg_missionText);
            mFinishTime = getArguments().getString(arg_finishTime);
            mFinishText = getArguments().getString(arg_finishText);
            mMissionNumber = getArguments().getString(arg_missionNumber);
        }
        answerInputField=(TextInputLayout) v.findViewById(R.id.answerField);
        mTextCodename =(TextView) v.findViewById(R.id.fast_mission_name);
        mTextCodename.setText(mCodename);
        //mTextPicture =(TextView) v.findViewById(R.id.);
        mTextIntroTime =(TextView) v.findViewById(R.id.fast_mission_intro_time);
        mTextIntroTime.setText(mIntroTime);
        mTextIntroText =(TextView) v.findViewById(R.id.fast_mission_intro_text);
        mTextIntroText.setText(mIntroText);
        mTextMissionStart =(TextView) v.findViewById(R.id.fast_mission_start);
        mTextMissionStart.setText(mMissionStart);
        mTextMissionText =(TextView) v.findViewById(R.id.fast_mission_text);
        mTextMissionText.setText(mMissionText);
        mTextFinishTime =(TextView) v.findViewById(R.id.fast_mission_finish_time);
        mTextFinishTime.setText(mFinishTime);
        mTextFinishText =(TextView) v.findViewById(R.id.fast_mission_finish_text);
        mTextFinishText.setText(Html.fromHtml(mFinishText));
        answerButton = (Button) v.findViewById(R.id.fast_mission_answer_button);

        answerButton.setOnClickListener(v1 ->{
            String answer = answerInputField.getEditText().getText().toString();
            Log.e("FastMisisonsFragment", "Answer: " + answer);
            sCrc = "grauman" + sSys+sLang+sGame+mMissionNumber+answer+sLogin+sHash;
            String logAnswer = sSys+sLang+sGame+mMissionNumber+answer+sLogin+sHash + sCrc;
            Log.e("FastFragmentAnswer",logAnswer);
            //Zrobić wysyłanie odpowiedzi do webserwisu
//            callAnsResponse(sSys, sLang, sGame, mMissionNumber,answerInputField, sLogin, sHash, sCrc);
        } );

        return v;
    }

}
