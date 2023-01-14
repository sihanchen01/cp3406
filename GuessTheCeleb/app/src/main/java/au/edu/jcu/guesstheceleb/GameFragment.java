package au.edu.jcu.guesstheceleb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.builder.Diff;

import java.util.Locale;

import au.edu.jcu.guesstheceleb.game.Difficulty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StateListener stateListener;
    private Difficulty level;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        final Spinner spinner = view.findViewById(R.id.gameSpinner);
        final TextView tvLevelInfo = view.findViewById(R.id.tvCurrentLevel);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        level = Difficulty.EASY;
                        break;
                    case 1:
                        level = Difficulty.MEDIUM;
                        break;
                    case 2:
                        level = Difficulty.HARD;
                        break;
                    case 3:
                        level = Difficulty.EXPERT;
                        break;
                }
                tvLevelInfo.setText(String.format(Locale.getDefault(), "Level: %s", level));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                level = Difficulty.EASY;
            }
        });

        // handle button click by triggering state listener update
        // and record current difficulty level
        view.findViewById(R.id.bPlay).setOnClickListener(v -> {
            stateListener.onUpdate(State.START_GAME);
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        stateListener = (StateListener) context;
    }

    public Difficulty getLevel() {
        return level;
    }
}