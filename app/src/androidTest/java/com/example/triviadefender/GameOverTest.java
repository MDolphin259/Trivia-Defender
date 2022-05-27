package com.example.triviadefender;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GameOverTest {

    /*@Rule
    public ActivityTestRule<GameOverActivity> atr = new ActivityTestRule<>(GameOverActivity.class);

    @Test
    public void gameOverButtonPress() {
        onView(withId(R.id.button)).perform(ViewActions.click());
        assertEquals(PopUpHandler.getPopUpStatus(), false);
        assertEquals(Util.difficultyToQuestionList.isEmpty(), true);
    }*/



}
