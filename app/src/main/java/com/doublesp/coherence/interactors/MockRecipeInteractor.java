package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.datastore.IdeaSnapshotStore;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;
import com.doublesp.coherence.viewmodels.Plan;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class MockRecipeInteractor extends RecipeInteractor {

    public MockRecipeInteractor(IdeaDataStoreInterface ideaDataStore,
                                RecipeRepositoryInterface recipeRepository) {
        super(ideaDataStore, recipeRepository);
        ideaDataStore.setSnapshot(Parcels.wrap(new IdeaSnapshotStore(mockIdeas())));
    }

    Plan mockPlan() {
        return new Plan(mockIdeas(), "Dinner 11/18");
    }

    List<Idea> mockIdeas() {
        List<Idea> ideas = new ArrayList<>();
        ideas.add(new Idea(
                "https://www.edamam.com/#recipe_ba693321d74b26b5bb14cf0df856763e",
                R.id.idea_category_recipe,
                "red potatoes",
                false,
                R.id.idea_type_user_generated,
                new IdeaMeta("http://www.specialtyproduce.com/sppics/2015.png", "red potatoes",
                        "3 lbs. small red potatoes halved"),
                null));
        ideas.add(new Idea("https://www.edamam.com/#recipe_3da57f2d5580d86d21b6cb569d1974f9",
                R.id.idea_category_recipe,
                "olive oil",
                false,
                R.id.idea_type_user_generated,
                new IdeaMeta(
                        "http://www.medicalnewstoday"
                                + ".com/content/images/articles/266/266258/olive-oil-and-olives"
                                + ".jpg",
                        "olive oil", "3 tablespoons olive oil"),
                null));
        ideas.add(new Idea("https://www.edamam.com/#recipe_b606ed3695ad9202c0342d816a236196",
                R.id.idea_category_recipe,
                "ground pepper",
                false,
                R.id.idea_type_user_generated,
                new IdeaMeta(
                        "http://rainydayfoods"
                                + ".com/media/catalog/product/cache/1/image/800x800/9df78eab33525d08d6e5fb8d27136e95/p/e/pepper_1.jpg",
                        "ground pepper", "Coarse salt and ground pepper"),
                null));
        return ideas;
    }

}
