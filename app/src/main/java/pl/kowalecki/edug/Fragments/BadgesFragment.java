package pl.kowalecki.edug.Fragments;

import android.os.Bundle;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import pl.kowalecki.edug.Model.Badges.ListBadge;
import pl.kowalecki.edug.R;
import pl.kowalecki.edug.SessionManagement;
import pl.kowalecki.edug.ViewModel.BadgesViewModel;

public class BadgesFragment extends Fragment {

    private static final String ARG_NUMBER = "argNumber";
    private static final String ARG_BADGE = "argBadge";
    private final String TAG = BadgesFragment.class.getSimpleName();
    SessionManagement sessionManagement;
    ImageView specI, laboI, blysI, hazaI, niepI, setnI, globI, persI;
    TextView specBn, specPer, laboBn, laboPer, blysBn, blysPer, hazaBn, hazaPer, niepBn, niepPer, setnBn, setnPer, globBn, globPer, persBn, persPer;
    CardView specCard, laboCard, blysCard, hazaCard, niepCard, setCard, globCard, persCard;
    Button specButton, laboButton, blysButton, hazaButton, niepButton, setnButton, globButton, persButton;
    String specDesc, laboDesc, blysDesc, hazarDesc, niepDesc, setDesc, globDesc, persDesc;
    LifecycleOwner _lifecycleOwner;
    private String agentIdu;
    private int agentBadge;
    private BadgesViewModel badgesViewModel;

    public static BadgesFragment newInstance(String idu, int badge) {
        BadgesFragment fragment = new BadgesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, idu);
        args.putInt(ARG_BADGE, badge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagement = new SessionManagement(getContext());
        String sGame = sessionManagement.getGame();
        String sLang = sessionManagement.getLang();
        if (getArguments() != null) {
            agentIdu = getArguments().getString(ARG_NUMBER);
            agentBadge = getArguments().getInt(ARG_BADGE);
        }

        badgesViewModel = new ViewModelProvider(this).get(BadgesViewModel.class);
        searchAllBadges(sLang, agentIdu, sGame, agentBadge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.badges_fragment, container, false);

        _lifecycleOwner = getViewLifecycleOwner();

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        badgesViewModel.getListBadgeLiveData().observe(_lifecycleOwner, new Observer<ListBadge>() {
            @Override
            public void onChanged(ListBadge listBadge) {
                if (listBadge != null) {
                    int percentage;
                    String url = "https://www.EduG.pl/_odznaki/" + agentBadge + "/";
                    for (int i = 0; i < listBadge.getExtraBadges().size(); i++) {
                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Specagent")) {
                            specBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            specDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                specPer.setText(percentage + "%");
                                specI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(specI);
                            }
                        }


                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Laborant")) {
                            laboBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            laboDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                laboPer.setText(percentage + "%");
                                laboI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(laboI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Błyskawica")) {
                            blysBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            blysDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                blysPer.setText(percentage + "%");
                                blysI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(blysI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Hazardzista")) {
                            hazarDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            hazaBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                hazaPer.setText(percentage + "%");
                                hazaI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(hazaI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Nieprawiczek")) {
                            niepDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            niepBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                niepPer.setText(percentage + "%");
                                niepI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(niepI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Setnik")) {
                            setDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            setnBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                setnPer.setText(percentage + "%");
                                setnI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(setnI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Globtroter")) {
                            globDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            globBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                globPer.setText(percentage + "%");
                                globI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(globI);
                            }
                        }

                        if (listBadge.getExtraBadges().get(i).getBadge().getName().equals("Personalizator")) {
                            persDesc = listBadge.getExtraBadges().get(i).getBadge().getDesc();
                            persBn.setText(listBadge.getExtraBadges().get(i).getBadge().getName());
                            if (listBadge.getExtraBadges().get(i).getBadge().getPerc() < 100) {
                                percentage = listBadge.getExtraBadges().get(i).getBadge().getPerc();
                                persPer.setText(percentage + "%");
                                persI.getBackground().setLevel(percentage * 100);
                            } else {
                                Picasso.get().load(url + listBadge.getExtraBadges().get(i).getBadge().getFile()).into(persI);
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
                } badgesViewModel.getListBadgeLiveData().removeObservers(_lifecycleOwner);
            }
        });
        return v;
    }

    private void searchAllBadges(String sLang, String agentIdu, String sGame, int agentBadge) {
        badgesViewModel.getAllBadges(sLang, agentIdu, sGame);
    }


}
