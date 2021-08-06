package pl.kowalecki.edug.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import pl.kowalecki.edug.Activities.MainActivity;
import pl.kowalecki.edug.Cipher.MD5Cipher;
import pl.kowalecki.edug.Model.MissionSpec.Answers1;
import pl.kowalecki.edug.Model.MissionSpec.Answers2;
import pl.kowalecki.edug.Model.MissionSpec.Answers3;
import pl.kowalecki.edug.Model.MissionSpec.Answers4;
import pl.kowalecki.edug.Model.MissionSpec.MissionSpec;
import pl.kowalecki.edug.Model.User.UserLogin;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialMissionFragment extends Fragment {
    SessionManagement sessionManagement;
    private String sSys, sLang, sGame, sLogin, sHash;
    private static final String arg_codename = "arg_codename";
    private static final String arg_missionStart = "arg_missionStart";
    private static final String arg_introText = "arg_introText";
    private static final String arg_question1 = "arg_question1";
    private static final String arg_question2 = "arg_question2";
    private static final String arg_question3 = "arg_question3";
    private static final String arg_question4 = "arg_question4";
    private static final String arg_finishTime = "arg_finishTime";
    private static final String arg_finishText = "arg_finishText";
    private static final String arg_missionNumber = "arg_missionNumber";
    private static final String arg_answers1 = "arg_answers1";
    private static final String arg_answers2 = "arg_answers2";
    private static final String arg_answers3 = "arg_answers3";
    private static final String arg_answers4 = "arg_answers4";
    private String mCodename, mMissionStart, mIntroText, mQuestion1, mQuestion2, mQuestion3, mQuestion4, mFinishTime, mFinishText, mMissionNumber, mCrc;
    private Random random = new Random();
    private ArrayList<Answers1> answers1s;
    private ArrayList<Answers2> answers2s;
    private ArrayList<Answers3> answers3s;
    private ArrayList<Answers4> answers4s;
    private CheckBox q1a1,q1a2,q1a3,q1a4,q2a1,q2a2,q2a3,q2a4,q3a1,q3a2,q3a3,q3a4,q4a1,q4a2,q4a3,q4a4;
    private TextView codename, missionStart, introText, question1, question2, question3, question4, finishText, finishTime;
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);
    ArrayList<String> orderedAnswers1 = new ArrayList<>();
    ArrayList<String> mixedAns1 = new ArrayList<>();
    ArrayList<String> mixedKeys1 = new ArrayList<>();
    ArrayList<String> orderedAnswers2 = new ArrayList<>();
    ArrayList<String> mixedAns2 = new ArrayList<>();
    ArrayList<String> mixedKeys2 = new ArrayList<>();
    ArrayList<String> orderedAnswers3 = new ArrayList<>();
    ArrayList<String> mixedAns3 = new ArrayList<>();
    ArrayList<String> mixedKeys3 = new ArrayList<>();
    ArrayList<String> orderedAnswers4 = new ArrayList<>();
    ArrayList<String> mixedAns4 = new ArrayList<>();
    ArrayList<String> mixedKeys4 = new ArrayList<>();
    MissionSpec missionSpec = new MissionSpec();
    StringBuffer sb1, sb2, sb3, sb4;
    Button button;
    private UserLogin userLogin = new UserLogin();
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_special_mission, container, false);
        context = getContext();
        if (getArguments() != null) {
            mCodename = getArguments().getString(arg_codename);
            mMissionStart = getArguments().getString(arg_missionStart);
            mIntroText = getArguments().getString(arg_introText);
            mQuestion1 = getArguments().getString(arg_question1);
            mQuestion2 = getArguments().getString(arg_question2);
            mQuestion3 = getArguments().getString(arg_question3);
            mQuestion4 = getArguments().getString(arg_question4);
            mFinishTime = getArguments().getString(arg_finishTime);
            mFinishText = getArguments().getString(arg_finishText);
            mMissionNumber = getArguments().getString(arg_missionNumber);
            answers1s = getArguments().getParcelableArrayList(arg_answers1);
            answers2s = getArguments().getParcelableArrayList(arg_answers2);
            answers3s = getArguments().getParcelableArrayList(arg_answers3);
            answers4s = getArguments().getParcelableArrayList(arg_answers4);
        }

        sessionManagement = new SessionManagement(getContext());
        sSys = sessionManagement.getSys();
        sLang = sessionManagement.getLang();
        sGame = sessionManagement.getGame();
        sLogin = sessionManagement.getLogin();
        sHash = sessionManagement.getHash();
        codename = (TextView) v.findViewById(R.id.spec_mission_codename);
        codename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        codename.setText(Html.fromHtml(mCodename));

        missionStart = (TextView) v.findViewById(R.id.spec_mission_missionStart);
        missionStart.setText(Html.fromHtml(mMissionStart));
        introText = (TextView) v.findViewById(R.id.spec_mission_introText);
        introText.setText(Html.fromHtml(mIntroText));
        question1 = (TextView) v.findViewById(R.id.spec_mission_question1);
        question1.setText(Html.fromHtml(mQuestion1));
        question2 = (TextView) v.findViewById(R.id.spec_mission_question2);
        question2.setText(Html.fromHtml(mQuestion2));
        question3 = (TextView) v.findViewById(R.id.spec_mission_question3);
        question3.setText(Html.fromHtml(mQuestion3));
        question4 = (TextView) v.findViewById(R.id.spec_mission_question4);
        question4.setText(Html.fromHtml(mQuestion4));

        q1a1 = (CheckBox) v.findViewById(R.id.spec_mission_question1_ans1);
        q1a2 = (CheckBox) v.findViewById(R.id.spec_mission_question1_ans2);
        q1a3 = (CheckBox) v.findViewById(R.id.spec_mission_question1_ans3);
        q1a4 = (CheckBox) v.findViewById(R.id.spec_mission_question1_ans4);
        q2a1 = (CheckBox) v.findViewById(R.id.spec_mission_question2_ans1);
        q2a2 = (CheckBox) v.findViewById(R.id.spec_mission_question2_ans2);
        q2a3 = (CheckBox) v.findViewById(R.id.spec_mission_question2_ans3);
        q2a4 = (CheckBox) v.findViewById(R.id.spec_mission_question2_ans4);
        q3a1 = (CheckBox) v.findViewById(R.id.spec_mission_question3_ans1);
        q3a2 = (CheckBox) v.findViewById(R.id.spec_mission_question3_ans2);
        q3a3 = (CheckBox) v.findViewById(R.id.spec_mission_question3_ans3);
        q3a4 = (CheckBox) v.findViewById(R.id.spec_mission_question3_ans4);
        q4a1 = (CheckBox) v.findViewById(R.id.spec_mission_question4_ans1);
        q4a2 = (CheckBox) v.findViewById(R.id.spec_mission_question4_ans2);
        q4a3 = (CheckBox) v.findViewById(R.id.spec_mission_question4_ans3);
        q4a4 = (CheckBox) v.findViewById(R.id.spec_mission_question4_ans4);
        finishTime = (TextView) v.findViewById(R.id.spec_mission_finish_time);
        finishTime.setText(Html.fromHtml(mFinishTime));
        finishText = (TextView) v.findViewById(R.id.spec_mission_finish_text);
        finishText.setText(Html.fromHtml(mFinishText));

        HashMap<Integer, String> hashMapAns1 = new HashMap<>();
        hashMapAns1.put(1, answers1s.get(0).get1());
        hashMapAns1.put(2, answers1s.get(0).get2());
        hashMapAns1.put(3, answers1s.get(0).get3());
        hashMapAns1.put(4, answers1s.get(0).get4());
        List keysa1 = new ArrayList(hashMapAns1.keySet());
        Collections.shuffle(keysa1);


        for (Object o : keysa1){
            mixedAns1.add(hashMapAns1.get(o));
            mixedKeys1.add(o.toString());
        }


        HashMap<Integer, String> hashMapAns2 = new HashMap<>();
        hashMapAns2.put(1, answers2s.get(0).get1());
        hashMapAns2.put(2, answers2s.get(0).get2());
        hashMapAns2.put(3, answers2s.get(0).get3());
        hashMapAns2.put(4, answers2s.get(0).get4());
        List keysa2 = new ArrayList(hashMapAns2.keySet());
        Collections.shuffle(keysa2);


        for (Object o : keysa2){
            mixedAns2.add(hashMapAns2.get(o));
            mixedKeys2.add(o.toString());
        }

        HashMap<Integer, String> hashMapAns3 = new HashMap<>();
        hashMapAns3.put(1, answers3s.get(0).get1());
        hashMapAns3.put(2, answers3s.get(0).get2());
        hashMapAns3.put(3, answers3s.get(0).get3());
        hashMapAns3.put(4, answers3s.get(0).get4());
        List keysa3 = new ArrayList(hashMapAns3.keySet());
        Collections.shuffle(keysa3);


        for (Object o : keysa3){
            mixedAns3.add(hashMapAns3.get(o));
            mixedKeys3.add(o.toString());
        }


        HashMap<Integer, String> hashMapAns4 = new HashMap<>();
        hashMapAns4.put(1, answers4s.get(0).get1());
        hashMapAns4.put(2, answers4s.get(0).get2());
        hashMapAns4.put(3, answers4s.get(0).get3());
        hashMapAns4.put(4, answers4s.get(0).get4());
        List keysa4 = new ArrayList(hashMapAns4.keySet());
        Collections.shuffle(keysa4);


        for (Object o : keysa4){
            mixedAns4.add(hashMapAns4.get(o));
            mixedKeys4.add(o.toString());
        }



        q1a1.setText(Html.fromHtml(mixedAns1.get(0)));
        q1a2.setText(Html.fromHtml(mixedAns1.get(1)));
        q1a3.setText(Html.fromHtml(mixedAns1.get(2)));
        q1a4.setText(Html.fromHtml(mixedAns1.get(3)));

        q2a1.setText(Html.fromHtml(mixedAns2.get(0)));
        q2a2.setText(Html.fromHtml(mixedAns2.get(1)));
        q2a3.setText(Html.fromHtml(mixedAns2.get(2)));
        q2a4.setText(Html.fromHtml(mixedAns2.get(3)));

        q3a1.setText(Html.fromHtml(mixedAns3.get(0)));
        q3a2.setText(Html.fromHtml(mixedAns3.get(1)));
        q3a3.setText(Html.fromHtml(mixedAns3.get(2)));
        q3a4.setText(Html.fromHtml(mixedAns3.get(3)));

        q4a1.setText(Html.fromHtml(mixedAns4.get(0)));
        q4a2.setText(Html.fromHtml(mixedAns4.get(1)));
        q4a3.setText(Html.fromHtml(mixedAns4.get(2)));
        q4a4.setText(Html.fromHtml(mixedAns4.get(3)));


        button = (Button) v.findViewById(R.id.spec_mission_button);
        button.setOnClickListener(v1 -> {
                            if (q1a1.isChecked()){
//                                q1a1.toggle();
                                orderedAnswers1.add(mixedKeys1.get(0));
                            }
                            if (q1a2.isChecked()){
//                                q1a2.toggle();
                                orderedAnswers1.add(mixedKeys1.get(1));
                            }
                            if (q1a3.isChecked()){
//                                q1a3.toggle();
                                orderedAnswers1.add(mixedKeys1.get(2));
                            }
                            if (q1a4.isChecked()){
//                                q1a4.toggle();
                                orderedAnswers1.add(mixedKeys1.get(3));
                            }
                            if (q2a1.isChecked()){
//                                q2a1.toggle();
                                orderedAnswers2.add(mixedKeys2.get(0));
                            }
                            if (q2a2.isChecked()){
//                                q2a2.toggle();
                                orderedAnswers2.add(mixedKeys2.get(1));
                            }
                            if (q2a3.isChecked()){
//                                q2a3.toggle();
                                orderedAnswers2.add(mixedKeys2.get(2));
                            }
                            if (q2a4.isChecked()){
//                                q2a4.toggle();
                                orderedAnswers2.add(mixedKeys2.get(3));
                            }
                            if (q3a1.isChecked()){
//                                q3a1.toggle();
                                orderedAnswers3.add(mixedKeys3.get(0));
                            }
                            if (q3a2.isChecked()){
//                                q3a2.toggle();
                                orderedAnswers3.add(mixedKeys3.get(1));
                            }
                            if (q3a3.isChecked()){
//                                q3a3.toggle();
                                orderedAnswers3.add(mixedKeys3.get(2));
                            }
                            if (q3a4.isChecked()){
//                                q3a4.toggle();
                                orderedAnswers3.add(mixedKeys3.get(3));
                            }
                            if (q4a1.isChecked()){
//                                q4a1.toggle();
                                orderedAnswers4.add(mixedKeys4.get(0));
                            }
                            if (q4a2.isChecked()) {
//                                q4a2.toggle();
                                orderedAnswers4.add(mixedKeys4.get(1));
                            }
                            if (q4a3.isChecked()){
//                                q4a3.toggle();
                                orderedAnswers4.add(mixedKeys4.get(2));
                            }

                            if (q4a4.isChecked()){
//                                q4a4.toggle();
                                orderedAnswers4.add(mixedKeys4.get(3));
                            }



            sb1 = new StringBuffer();
            sb2 = new StringBuffer();
            sb3 = new StringBuffer();
            sb4 = new StringBuffer();
            Collections.sort(orderedAnswers1, String::compareTo);

            for (String s1 : orderedAnswers1){
                sb1.append(s1);
            }
            String str1 = sb1.toString();

            Collections.sort(orderedAnswers2, String::compareTo);
            for (String s2 : orderedAnswers2){
                sb2.append(s2);
            }
            String str2 = sb2.toString();


            Collections.sort(orderedAnswers3, String::compareTo);
            for (String s3 : orderedAnswers3){
                sb3.append(s3);
            }
            String str3 = sb3.toString();
            Collections.sort(orderedAnswers4, String::compareTo);
            for (String s4 : orderedAnswers4){
                sb4.append(s4);
            }
            String str4 = sb4.toString();

            mCrc = MD5Cipher.md5(userLogin.getPassword()+sSys+sLang+sGame+mMissionNumber+str1+str2+str3+str4+sLogin+sHash);

            if (str1.isEmpty() || str2.isEmpty() || str3.isEmpty() || str4.isEmpty()){
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                orderedAnswers1.clear();
                orderedAnswers2.clear();
                orderedAnswers3.clear();
                orderedAnswers4.clear();

                builder.setTitle("Brak odpowiedzi");
                builder.setMessage("Musisz udzielić odpowiedzi na każde pytanie");
                builder.setNegativeButton("Ok", null);
                builder.create().show();


            }else{
                finishMission(sSys, sLang, sGame, mMissionNumber,str1, str2, str3, str4, sLogin, sHash, mCrc);
            }





        });
        return v;
    }

    private boolean validateAnswers() {
        if (q1a1.isChecked() || q1a2.isChecked() || q1a3.isChecked() || q1a4.isChecked()) {
            if (q2a1.isChecked() || q2a2.isChecked() || q2a3.isChecked() || q2a4.isChecked()) {
                if (q3a1.isChecked() || q3a2.isChecked() || q3a3.isChecked() || q3a4.isChecked()) {
                    if (q4a1.isChecked() || q4a2.isChecked() || q4a3.isChecked() || q4a4.isChecked()) {
                        return true;
                    }
                }
            }

        }
        return false;

    }

    private void finishMission(String sSys, String sLang, String sGame, String mMissionNumber, String str1, String str2, String str3, String str4, String sLogin, String sHash, String mCrc) {
        Call<MissionSpec> call = userService.setSpecMissionData(sSys, sLang, sGame, mMissionNumber, str1, str2, str3, str4, sLogin, sHash, mCrc);

        call.enqueue(new Callback<MissionSpec>() {
            @Override
            public void onResponse(Call<MissionSpec> call, Response<MissionSpec> response) {
                if (response.isSuccessful()){
                    startActivity(new Intent(context, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<MissionSpec> call, Throwable t) {
                Log.e("FAILURE", "Unable to submit post to API" );
            }
        });
    }
}
