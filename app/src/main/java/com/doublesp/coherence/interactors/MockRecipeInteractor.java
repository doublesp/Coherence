package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.ArrayList;
import java.util.List;

public class MockRecipeInteractor extends RecipeInteractor {

    public MockRecipeInteractor(DataStoreInterface ideaDataStore,
            RecipeRepositoryInterface recipeRepository) {
        super(ideaDataStore, recipeRepository);
    }

    Plan mockPlan() {
        return new Plan("", mockIdeas(), "Dinner 11/18", ConstantsAndUtils.ANONYMOUS);
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
                        "3 lbs. small red potatoes halved")));
        ideas.add(new Idea("https://www.edamam.com/#recipe_3da57f2d5580d86d21b6cb569d1974f9",
                R.id.idea_category_recipe,
                "olive oil",
                false,
                R.id.idea_type_user_generated,
                new IdeaMeta(
                        "http://www.medicalnewstoday"
                                + ".com/content/images/articles/266/266258/olive-oil-and-olives"
                                + ".jpg",
                        "olive oil", "3 tablespoons olive oil")));
        ideas.add(new Idea("https://www.edamam.com/#recipe_b606ed3695ad9202c0342d816a236196",
                R.id.idea_category_recipe,
                "ground pepper",
                false,
                R.id.idea_type_user_generated,
                new IdeaMeta(
                        "http://rainydayfoods"
                                + ".com/media/catalog/product/cache/1/image/800x800/9df78eab33525d08d6e5fb8d27136e95/p/e/pepper_1.jpg",
                        "ground pepper", "Coarse salt and ground pepper")));
        return ideas;
    }

}
