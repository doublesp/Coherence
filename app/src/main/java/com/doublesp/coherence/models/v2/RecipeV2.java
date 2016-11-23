package com.doublesp.coherence.models.v2;

import com.doublesp.coherence.database.RecipeV2Database;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by pinyaoting on 11/22/16.
 */

@Table(database = RecipeV2Database.class)
public class RecipeV2 extends BaseModel {

    public List<IngredientV2> extendedIngredients;
    @PrimaryKey
    @Column
    String id;
    @Column
    String title;
    @Column
    Long readyInMinutes;
    @Column
    String image;
    @Column
    String instructions;

    public RecipeV2() {
        super();
    }

    public static RecipeV2 byId(String id) {
        return new Select().from(RecipeV2.class).where(RecipeV2_Table.id.eq(id)).querySingle();
    }

    public static List<RecipeV2> recentItems() {
        return new Select().from(RecipeV2.class).limit(50).queryList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Long readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "extendedIngredients")
    public List<IngredientV2> getExtendedIngredients() {
//        if (extendedIngredients == null || extendedIngredients.isEmpty()) {
//            extendedIngredients = SQLite.select()
//                    .from(Ingredient.class)
//                    .where(IngredientV2_Table.id.eq(id))
//                    .queryList();
//        }
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<IngredientV2> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
}
