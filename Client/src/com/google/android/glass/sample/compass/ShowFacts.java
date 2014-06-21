package com.google.android.glass.sample.compass;

import java.util.List;

import com.google.android.glass.sample.compass.model.Place;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowFacts extends Activity {

	private TextView mFactView;
	private TextView mTapView;
	private Place mPlace;
	private List<String> anwsers;
	private int current = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPlace = (Place) getIntent().getExtras().getSerializable("place");
		anwsers = mPlace.getAnswerChoices();
		mFactView = (TextView) findViewById(R.id.fact);
		mFactView.setText(mPlace.getFacts());//TODO add Place.fact.
		mTapView = (TextView) findViewById(R.id.tip_tap_for_options);
		mTapView.setText("Tap to Answer");
	}

	protected void handleGameGesture(Gesture gesture) {
		switch (gesture) {
		case TAP:
			if (current < 0) {
				mFactView.setText(mPlace.getQuestion());
				current++;
			} else {
				choose();
			}
			break;
		case SWIPE_RIGHT:
			if (current >= 0) {
				pass();
			}
			break;
		case SWIPE_LEFT:
			back();
			break;
		}
	}

	private void back() {
		// TODO Auto-generated method stub
		current--;
		if (current >= 0 && current < anwsers.size()) {
			updateView();
		}
	}


	private void pass() {
		// TODO Auto-generated method stub
		if (current >= 0 && current < anwsers.size()) {
			updateView();
		}
		current++;
	}

	private void choose() {
		// TODO Auto-generated method stub
		if (current >= 0 && current < anwsers.size()) {
			if (anwsers.get(current).equals(mPlace.getCorrectAnswer())) {
				mFactView.setText("Correct!");
			} else {
				mFactView.setText("Incorrect!");
			}
			finish();
		}
		current++;
	}
	

	private void updateView() {
		// TODO Auto-generated method stub
		mFactView.setText(anwsers.get(current));
	}
}
