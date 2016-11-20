package com.doublesp.coherence.models;

import com.doublesp.coherence.database.RecipeDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by pinyaoting on 11/17/16.
 */

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

    public Integer getId() {
        return id;
    }

    public String getFood() {
        return food;
    }

    public String getText() {
        return text;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setText(String text) {
        this.text = text;
    }
}
