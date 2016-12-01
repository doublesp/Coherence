package com.doublesp.coherence.models.v2;

import com.doublesp.coherence.database.RecipeV2Database;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by pinyaoting on 11/27/16.
 */

@Table(database = RecipeV2Database.class)
public class SavedRecipe extends BaseModel {

    @PrimaryKey
    @Column
    String id;

    public SavedRecipe() {
        super();
    }

    public static SavedRecipe byId(String id) {
        return new Select().from(SavedRecipe.class).where(
                SavedRecipe_Table.id.eq(id)).querySingle();
    }

    public static List<RecipeV2> savedRecipes() {
        return new Select().from(RecipeV2.class).as("T")
                .join(SavedRecipe.class, Join.JoinType.INNER).as("U")
                .on(RecipeV2_Table.id.withTable(NameAlias.builder("T").build())
                        .eq(SavedRecipe_Table.id.withTable(NameAlias.builder("U").build())))
                .orderBy(RecipeV2_Table.id, false).queryList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
