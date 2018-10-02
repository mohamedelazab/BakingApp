package mohamed.com.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import mohamed.com.bakingapp.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AllBakesTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickOnRecyclerViewItem_openDetailsActivity() {

        onView(withId(R.id.rv_backs))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null)
            IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
