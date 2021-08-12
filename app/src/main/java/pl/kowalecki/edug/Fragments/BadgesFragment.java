package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import pl.kowalecki.edug.Model.Badges.Badge;
import pl.kowalecki.edug.Model.Badges.ListBadge;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.Retrofit.ServiceGenerator;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgesFragment extends Fragment {

    private static final String ARG_NUMBER = "argNumber";
    private static final String ARG_BADGE = "argBadge";
    private final String TAG = BadgesFragment.class.getSimpleName();
    SessionManagement sessionManagement;
    UserService userService = ServiceGenerator.getRetrofit().create(UserService.class);
    ImageView specI, laboI, blysI, hazaI, niepI, setnI, globI, persI;
    TextView specBn, specPer, laboBn, laboPer, blysBn, blysPer, hazaBn, hazaPer, niepBn, niepPer, setnBn, setnPer, globBn, globPer, persBn, persPer;
    CardView specCard, laboCard, blysCard, hazaCard, niepCard, setCard, globCard, persCard;
    Button specButton, laboButton, blysButton, hazaButton, niepButton, setnButton, globButton, persButton;
    Badge badge;
    String specDesc, laboDesc, blysDesc, hazarDesc, niepDesc, setDesc, globDesc, persDesc;
    private String agentIdu;
    private int agentBadge;

    public static BadgesFragment newInstance(String idu, int badge) {
        BadgesFragment fragment = new BadgesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, idu);
        args.putInt(ARG_BADGE, badge);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.badges_fragment, container, false);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        String sLang = sessionManagement.getLang();
        badge = new Badge();

        specI = (ImageView) v.findViewById(R.id.specagentBadgeImage);
        specBn = (TextView) v.findViewById(R.id.specagentBadgeText);
        specPer = (TextView) v.findViewById(R.id.specagentBadgePercentage);
        specCard = (CardView) v.findViewById(R.id.specagentCardView);
        specButton = (Button) v.findViewById(R.id.specagentBadgeButton);
        laboI = (ImageView) v.findViewById(R.id.laborantBadgeImage);
        laboBn = (TextView) v.findViewById(R.id.laborantBadgeText);
        laboPer = (TextView) v.findViewById(R.id.laborantBadgePercentage);
        laboCard = (CardView) v.findViewById(R.id.laborantCardView);
        laboButton = (Button) v.findViewById(R.id.laborantBadgeButton);
        blysI = (ImageView) v.findViewById(R.id.blyskawicaBadgeImage);
        blysBn = (TextView) v.findViewById(R.id.blyskawicaBadgeText);
        blysPer = (TextView) v.findViewById(R.id.blyskawicaBadgePercentage);
        blysCard = (CardView) v.findViewById(R.id.blyskawicaCardView);
        blysButton = (Button) v.findViewById(R.id.blyskawicaBadgeButton);
        hazaI = (ImageView) v.findViewById(R.id.hazardzistatBadgeImage);
        hazaBn = (TextView) v.findViewById(R.id.hazardzistaBadgeText);
        hazaPer = (TextView) v.findViewById(R.id.hazardzistaBadgePercentage);
        hazaCard = (CardView) v.findViewById(R.id.hazardzistaCardView);
        hazaButton = (Button) v.findViewById(R.id.hazardzistaBadgeButton);
        niepI = (ImageView) v.findViewById(R.id.nieprawiczekBadgeImage);
        niepBn = (TextView) v.findViewById(R.id.nieprawiczekBadgeText);
        niepPer = (TextView) v.findViewById(R.id.nieprawiczekBadgePercentage);
        niepCard = (CardView) v.findViewById(R.id.nieprawiczekCardView);
        niepButton = (Button) v.findViewById(R.id.nieprawiczekBadgeButton);
        setnI = (ImageView) v.findViewById(R.id.setnikBadgeImage);
        setnBn = (TextView) v.findViewById(R.id.setnikBadgeText);
        setnPer = (TextView) v.findViewById(R.id.setnikBadgePercentage);
        setCard = (CardView) v.findViewById(R.id.setnikCardView);
        setnButton = (Button) v.findViewById(R.id.setnikBadgeButton);
        globI = (ImageView) v.findViewById(R.id.globtroterBadgeImage);
        globBn = (TextView) v.findViewById(R.id.globtroterBadgeText);
        globPer = (TextView) v.findViewById(R.id.globtroterBadgePercentage);
        globCard = (CardView) v.findViewById(R.id.globtroterCardView);
        globButton = (Button) v.findViewById(R.id.globtroterBadgeButton);
        persI = (ImageView) v.findViewById(R.id.personalizatorBadgeImage);
        persBn = (TextView) v.findViewById(R.id.personalizatorBadgeText);
        persPer = (TextView) v.findViewById(R.id.personalizatorBadgePercentage);
        persCard = (CardView) v.findViewById(R.id.personalizatorCardView);
        persButton = (Button) v.findViewById(R.id.personalizatorBadgeButton);

        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
            agentBadge = getArguments().getInt(ARG_BADGE);
        }
        receiveBadges(sLang, agentIdu, sGame, agentBadge);


        return v;
    }

    private void receiveBadges(String sLang, String agentIdu, String sGame, int agentBadge) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Call<ListBadge> call = userService.extraBadges(sLang, sGame, agentIdu);
        Log.e(TAG, "RESPO URL: " + call.request().url().toString());
        call.enqueue(new Callback<ListBadge>() {
            @Override
            public void onResponse(Call<ListBadge> call, Response<ListBadge> response) {
                ListBadge res = response.body();
                int percentage;

                String url = "https://www.EduG.pl/_odznaki/" + agentBadge + "/";
                String urlPlaceholder = "https://www.EduG.pl/_odznaki/1/";
                for (int i = 0; i < res.getExtraBadges().size(); i++) {

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Specagent")) {
                        specBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        specDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            specPer.setText(percentage + "%");
                            specI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get()
                                    .load(url + response.body().getExtraBadges().get(i).getBadge().getFile())
                                    .into(specI);

                        }
                    }


                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Laborant")) {
                        laboBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        laboDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            Log.e(TAG, "LABO PERCETAGE: " + response.body().getExtraBadges().get(i).getBadge().getPerc());
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            laboPer.setText(percentage + "%");
                            laboI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(laboI);
                            Log.e(TAG, "Image url: " + url + response.body().getExtraBadges().get(i).getBadge().getFile());
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Błyskawica")) {
                        blysBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        blysDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            blysPer.setText(percentage + "%");
                            blysI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(blysI);
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Hazardzista")) {
                        hazarDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        hazaBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            hazaPer.setText(percentage + "%");
                            hazaI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(hazaI);
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Nieprawiczek")) {
                        niepDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        niepBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            niepPer.setText(percentage + "%");
                            niepI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(niepI);
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Setnik")) {
                        setDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        setnBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            setnPer.setText(percentage + "%");
                            setnI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(setnI);
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Globtroter")) {
                        globDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        globBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            globPer.setText(percentage + "%");
                            globI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(globI);
                        }
                    }

                    if (response.body().getExtraBadges().get(i).getBadge().getName().equals("Personalizator")) {
                        persDesc = response.body().getExtraBadges().get(i).getBadge().getDesc();
                        persBn.setText(response.body().getExtraBadges().get(i).getBadge().getName());
                        if (response.body().getExtraBadges().get(i).getBadge().getPerc() < 100) {
                            percentage = response.body().getExtraBadges().get(i).getBadge().getPerc();
                            persPer.setText(percentage + "%");
                            persI.getBackground().setLevel(percentage * 100);
                        } else {
                            Picasso.get().load(url + response.body().getExtraBadges().get(i).getBadge().getFile()).into(persI);
                        }
                    }

                }

                specButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(specDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                blysButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(blysDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                globButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(globDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                hazaButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(hazarDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                laboButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(laboDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                niepButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(niepDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                persButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(persDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
                setnButton.setOnClickListener(v -> {
                    builder.setTitle("Jak mnie zdobyć?")
                            .setMessage(setDesc)
                            .setCancelable(true)
                            .setPositiveButton("ok", (dialog, which) -> dialog.cancel());
                    builder.show();
                });


            }

            @Override
            public void onFailure(Call<ListBadge> call, Throwable t) {

            }
        });


    }
}
