package au.edu.jcu.guesstheceleb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import au.edu.jcu.guesstheceleb.game.Game;
import au.edu.jcu.guesstheceleb.game.Question;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StateListener stateListener;
    private Game currentGame;
    private Question currentQuestion;
    private String userGuess;

    private ImageView ivCelebImage;
    private String[] celebNames;
    private ListView lvCelebNames;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ivCelebImage = view.findViewById(R.id.ivCelebImage);
        lvCelebNames = view.findViewById(R.id.lvCelebNames);

        // Create array adapter for celeb names
        ArrayAdapter<String> celebNamesAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1);
        lvCelebNames.setAdapter(celebNamesAdapter);
        celebNamesAdapter.addAll(celebNames);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        stateListener = (StateListener) context;
    }

    public void setCurrentGame(Game game) {
        currentGame = game;
        currentQuestion = game.getFirstQuestion();
        celebNames = currentQuestion.getPossibleNames();
        ivCelebImage.setImageBitmap(currentQuestion.getCelebrityImage());
    }

    public String getScore() {
        return currentGame.getScore();
    }

    public void showNextQuestion() {
        // Check user input
        boolean guessIsCorrect = currentQuestion.check(userGuess);
        if (guessIsCorrect && !currentGame.isGameOver()){
            // Go to next question if guess is right, and there are still more questions
            currentQuestion = currentGame.next();
            ivCelebImage.setImageBitmap(currentQuestion.getCelebrityImage());
            stateListener.onUpdate(State.CONTINUE_GAME);
        } else {
            // Game over if guess is wrong
            stateListener.onUpdate(State.GAME_OVER);
        }
    }
}