package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class MockPlanInteractor {

    List<Plan> mPlans;

    public MockPlanInteractor() {
        mPlans = mockPlan();
    }

    List<Plan> mockPlan() {
        List<Plan> mockPlans = new ArrayList<>();
        mockPlans.add(new Plan(mockIdeas(), "Movie Watch List"));
        mockPlans.add(new Plan(mockIdeas(), "Movies recommended by Emma"));
        mockPlans.add(new Plan(mockIdeas(), "10 brain-twisting movies"));
        return mockPlans;
    }

    List<Idea> mockIdeas() {
        List<Idea> ideas = new ArrayList<>();
        ideas.add(new Idea(0L, R.id.idea_category_movies, "Doctor Strange", false, R.id.idea_type_user_generated, null));
        ideas.add(new Idea(1L, R.id.idea_category_movies, "Star Wars: Rogue One", false, R.id.idea_type_user_generated, null));
        ideas.add(new Idea(2L, R.id.idea_category_movies, "Inferno", false, R.id.idea_type_user_generated, null));
        return ideas;
    }

    public Plan getItemAtPos(int position) {
        return mPlans.get(position);
    }

    public int getItemCount() {
        return mPlans.size();
    }
}
