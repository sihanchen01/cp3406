package au.edu.jcu.reflexchecker;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Button bDone;

    private final Random random = new Random();

    private static final int[] DRAWABLES = {
            R.drawable.baseline_face_black_48,
            R.drawable.baseline_dashboard_black_48,
            R.drawable.baseline_fingerprint_black_48,
            R.drawable.baseline_paid_black_48,
            R.drawable.baseline_question_answer_black_48
    };

    private static final int[] CHECKBOX_OPTIONS = {
            R.array.drinks,
            R.array.fruits
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bDone = findViewById(R.id.bDone);

        setupDescription(R.id.tvTask1, R.array.task1_descriptions);
        setupDescription(R.id.tvTask2, R.array.task2_descriptions);

        for (int i = 0; i < 5; i++) {
            addRandomImage();
            addRandomCheckboxes();
        }

        bDone.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void setupDescription(int taskID, int arrayID) {
        TextView task = findViewById(taskID);
        String[] descriptions = getResources().getStringArray(arrayID);

        int i = random.nextInt(descriptions.length);
        task.setText(descriptions[i]);
    }

    private void addRandomCheckboxes() {
        ViewGroup gameRows = findViewById(R.id.tlGameRows);
        getLayoutInflater().inflate(R.layout.checkboxes, gameRows);

        View childTableRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow checkBoxRow = (TableRow) childTableRow;
        int index = random.nextInt(CHECKBOX_OPTIONS.length);
        String[] options = getResources().getStringArray(CHECKBOX_OPTIONS[index]);

        for (int i = 0; i < 3; i++) {
            View childCheckBox = checkBoxRow.getChildAt(i);
            CheckBox checkBox = (CheckBox) childCheckBox;

            checkBox.setText(options[i]);
            checkBox.setChecked(random.nextBoolean());
        }

    }

    private void addRandomImage() {
        ViewGroup gameRows = findViewById(R.id.tlGameRows);
        getLayoutInflater().inflate(R.layout.image, gameRows);

        View lastChild = gameRows.getChildAt(gameRows.getChildCount() - 1);
        ImageView image = (ImageView) lastChild;

        int index = random.nextInt(DRAWABLES.length);
        image.setImageDrawable(getResources().getDrawableForDensity(DRAWABLES[index], 0));
    }
}