package com.google.android.glass.sample.compass;

import java.util.List;

import com.google.android.glass.sample.compass.model.Place;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class ShowFacts extends Activity {

	private TextView mFactView;
	private TextView mTapView;
	private Place mPlace;
	private List<String> anwsers;
	private int current = -1;
	private GestureDetector mGestureDetector;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//android.os.Debug.waitForDebugger();
		setContentView(R.layout.fact);
		mPlace = (Place) getIntent().getExtras().getSerializable("place");
		anwsers = mPlace.getAnswerChoices();
		mFactView = (TextView) findViewById(R.id.facts);
		mFactView.setText(mPlace.getFacts());//TODO add Place.fact.
		mTapView = (TextView) findViewById(R.id.tip_tap_for_options);
		mTapView.setText("Tap to Continue");
		
		mGestureDetector = createGestureDetector(this);
	}


	 private GestureDetector createGestureDetector(Context context) {
		    GestureDetector gestureDetector = new GestureDetector(context);
		        //Create a base listener for generic gestures
		        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
		            @Override
		            public boolean onGesture(Gesture gesture) {
		                if (gesture == Gesture.TAP) {
		                	if (current < 0) {
		        				mFactView.setText(mPlace.getQuestion());
		        				mTapView.setText("Tap to Answer");
		        				current++;
		        			} else {
		        				choose();
		        			}
		                    return true;
		                } else if (gesture == Gesture.TWO_TAP) {
		                    // do something on two finger tap
		                    return true;
		                } else if (gesture == Gesture.SWIPE_RIGHT) {
		                	if (current >= 0) {
		        				pass();
		        			}
		                    return true;
		                } else if (gesture == Gesture.SWIPE_LEFT) {
		                	back();
		                    return true;
		                }
		                return false;
		            }
		        });
		        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
		            @Override
		            public void onFingerCountChanged(int previousCount, int currentCount) {
		              // do something on finger count changes
		            }
		        });
		        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
		            @Override
		            public boolean onScroll(float displacement, float delta, float velocity) {
		                // do something on scrolling
		            	return false;
		            }
		        });
		        return gestureDetector;
		    }

		    /*
		     * Send generic motion events to the gesture detector
		     */
		    @Override
		    public boolean onGenericMotionEvent(MotionEvent event) {
		        if (mGestureDetector != null) {
		            return mGestureDetector.onMotionEvent(event);
		        }
		        return false;
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
