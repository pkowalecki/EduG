package pl.kowalecki.edug.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import pl.kowalecki.edug.Activities.MainActivity;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ApiRequest;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.MissionFastViewModel;

public class FastMissionFragment extends Fragment {

    //TODO: Przetestować misję gdy jakaś będzie aktywna

    Context context;
    SessionManagement sessionManagement;
    MissionFastViewModel missionFastViewModel;
    private final UserLogin userLogin = new UserLogin();

    private static final String arg_codename = "arg_codename";
    private static final String arg_picture = "arg_picture";
    private static final String arg_introTime = "arg_introTime";
    private static final String arg_introText = "arg_introText";
    private static final String arg_missionStart = "arg_missionStart";
    private static final String arg_missionText = "arg_missionText";
    private static final String arg_finishTime = "arg_finishTime";
    private static final String arg_finishText = "arg_finishText";
    private static final String arg_missionNumber = "arg_missionNumber";

    private TextView mTextCodename, mTextIntroText, mTextMissionStart, mTextMissionText, mTextFinishTime, mTextFinishText;
    private Button answerButton;
    private ImageView imageView;
    private TextInputLayout answerInputField;

    private String mCodename, mPicture, mIntroTime, mIntroText, mMissionStart, mMissionText, mFinishTime, mFinishText;
    private String sSys, sLang, sGame, sLogin, sHash, sCrc, mMissionNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new SessionManagement(getContext());
        missionFastViewModel = new ViewModelProvider(this).get(MissionFastViewModel.class);
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fast_mission, container, false);

        answerInputField = (TextInputLayout) v.findViewById(R.id.answerField);


        mTextCodename = (TextView) v.findViewById(R.id.fast_mission_name);
        mTextCodename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mTextCodename.setText(Html.fromHtml(mCodename, HtmlCompat.FROM_HTML_MODE_LEGACY));
        mTextCodename.setPaintFlags(mTextCodename.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mTextIntroText = (TextView) v.findViewById(R.id.fast_mission_intro_text);
        mTextIntroText.setText(Html.fromHtml(mIntroText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        mTextMissionStart = (TextView) v.findViewById(R.id.fast_mission_start);
        mTextMissionStart.setText(Html.fromHtml(mMissionStart, HtmlCompat.FROM_HTML_MODE_LEGACY));

        mTextMissionText = (TextView) v.findViewById(R.id.fast_mission_text);
        mTextMissionText.setText(Html.fromHtml(mMissionText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        mTextFinishTime = (TextView) v.findViewById(R.id.fast_mission_finish_time);
        mTextFinishTime.setText(Html.fromHtml(mFinishTime, HtmlCompat.FROM_HTML_MODE_LEGACY));

        mTextFinishText = (TextView) v.findViewById(R.id.fast_mission_finish_text);
        mTextFinishText.setText(Html.fromHtml(mFinishText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        answerButton = (Button) v.findViewById(R.id.fast_mission_answer_button);

        if (!mPicture.isEmpty()) {
            imageView = (ImageView) v.findViewById(R.id.fast_mission_image);
            Picasso.get().load(mPicture).into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }

        answerButton.setOnClickListener(v1 -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String answer = answerInputField.getEditText().getText().toString();
            if (answer.isEmpty()) {
                builder.setTitle("Brak odpowiedzi");
                builder.setMessage("Musisz udzielić odpowiedzi na pytanie");
                builder.setNegativeButton("Ok", null);
                builder.create().show();
            } else {
                Log.e("FastMisisonsFragment", "Answer: " + answer);
                sCrc = MD5Cipher.md5(userLogin.getPassword() + sSys + sLang + sGame + mMissionNumber + answer + sLogin + sHash);
                Toast.makeText(context, "Odpowiedź została wysłana", Toast.LENGTH_SHORT).show();
                missionFastViewModel.setFastMission(sSys, sLang, sGame, mMissionNumber, answer, sLogin, sHash, sCrc);
                startActivity(new Intent(context, MainActivity.class));
            }
        });
        checkMode();

        return v;
    }

    private void checkMode() {
        if (sessionManagement.loadNightModeState()) {
            mTextCodename.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_top));
            mTextIntroText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
            mTextFinishText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
            mTextMissionText.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corners_missions_all));
        }
    }

}
