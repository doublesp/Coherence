package com.doublesp.coherence.models.v1;

import com.doublesp.coherence.database.RecipeDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = RecipeDatabase.class)
public class Ingredient extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public Integer id;

    @Column
    private String uri;

    @Column
    private String food;

    @Column
    private String text;

    public Ingredient() {
        super();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
