package jason.liftingspiritanimal.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import jason.liftingspiritanimal.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI components
    private NumberPicker numberPicker;
    private TextView numTextView;
    private ImageButton doneButton;

    //Constants
    private static final int NUM_PICK_MIN_VALUE = 0;
    private static final int NUM_PICK_MAX_VALUE = 1000;
    private static final String ANIMAL_STRING = "animal_string";
    private static final String FONT_PATH = "fonts/Lato-Light.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set references to our view items
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numTextView = (TextView) findViewById(R.id.enterNumTextView);
        doneButton = (ImageButton) findViewById(R.id.doneButton);

        //Set on-click listener for our view items
        assert doneButton != null;
        doneButton.setOnClickListener(this);

        //Additional settings
        setNumberPickerSettings();
        setTextViewFont();
    }


    private void setTextViewFont(){
        numTextView.setTypeface(Typeface.createFromAsset(getAssets(), FONT_PATH));
    }


    private void setNumberPickerSettings(){
        numberPicker.setMinValue(NUM_PICK_MIN_VALUE);
        numberPicker.setMaxValue(NUM_PICK_MAX_VALUE);
        numberPicker.setWrapSelectorWheel(true);
    }


    @Override
    public void onClick(View v) {
        if(v == doneButton){

            //Get the animal on which value the spinner is on
            String animal = getAnimal(numberPicker.getValue());

            if(animal != null){
                //Pass our chosen animal to the results intent
                Intent resultsIntent = new Intent(this, DisplayResultsActivity.class);
                resultsIntent.putExtra(ANIMAL_STRING, animal);
                startActivity(resultsIntent);
            }
        }
    }


    /*
        This method compares the value from the number picker in order to
        determine what animal is associated with the number picker value.
        Once a number is found, it is returned.
     */
    private String getAnimal(int value){
        if(value == 0){
            return "Nothing";
        }
        else if(value == 999){
            return "999";
        }
        else if(value <= 5){
            return "Meerkat";
        }
        else if(value <= 10){
            return "Sloth";
        }
        else if(value <= 20){
            return "Oribi";
        }
        else if(value <= 30){
            return "Sea Otter";
        }
        else if(value <= 40){
            return "Hyena";
        }
        else if(value <= 50){
            return "Cheetah";
        }
        else if(value <= 75){
            return "Aardvark";
        }
        else if(value <= 100){
            return "Goat";
        }
        else if(value <= 125){
            return "Tiger";
        }
        else if(value <= 150){
            return "Gorilla";
        }
        else if(value <= 175){
            return "Lion";
        }
        else if(value <= 200){
            return "Sambar";
        }
        else if(value <= 225){
            return "Okapi";
        }
        else if(value <= 250){
            return "Tapir";
        }
        else if(value <= 275){
            return "Seal";
        }
        else if(value <= 300){
            return "Dolphin";
        }
        else if(value <= 325){
            return "Manatee";
        }
        else if(value <= 350){
            return "Dugong";
        }
        else if(value <= 375){
            return "Zebra";
        }
        else if(value <= 400){
            return "Moose";
        }
        else if(value <= 450){
            return "Camel";
        }
        else if(value <= 475){
            return "Polar Bear";
        }
        else if(value <= 500){
            return "Bovine";
        }
        else if(value <= 650){
            return "Bison";
        }
        else if(value <= 700){
            return "Yak";
        }
        else if(value <= 750){
            return "Buffalo";
        }
        else if(value <= 800){
            return "Giraffe";
        }
        else if(value <= 850){
            return "Gaur";
        }
        else if(value <= 1000){
            return "Walrus";
        }
        else{
            return null;
        }
    }
}
