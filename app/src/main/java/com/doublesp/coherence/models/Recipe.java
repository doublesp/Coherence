package com.doublesp.coherence.models;

import com.google.gson.annotations.SerializedName;

import com.doublesp.coherence.database.RecipeDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by pinyaoting on 11/17/16.
 */

@Table(database = RecipeDatabase.class)
public class Recipe extends BaseModel {

    @PrimaryKey
    @Column
    private String uri;

    @Column
    private String label;

    @SerializedName("image")
    @Column
    private String imageUrl;

    private List<String> ingredientLines;

    public List<Ingredient> ingredients;

    public Recipe() {
        super();
    }

    public static Recipe byUri(String uri) {
        return new Select().from(Recipe.class).where(Recipe_Table.uri.eq(uri)).querySingle();
    }

    public static List<Recipe> recentItems() {
        return new Select().from(Recipe.class).limit(50).queryList();
    }

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "ingredients")
    public List<Ingredient> getIngredients() {
        if (ingredients == null || ingredients.isEmpty()) {
            ingredients = SQLite.select()
                    .from(Ingredient.class)
                    .where(Ingredient_Table.uri.eq(uri))
                    .queryList();
        }
        return ingredients;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
