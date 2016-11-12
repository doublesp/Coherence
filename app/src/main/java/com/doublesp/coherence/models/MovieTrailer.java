package com.doublesp.coherence.models;

import com.doublesp.coherence.database.MovieDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by pinyaoting on 10/14/16.
 */

@Table(database = MovieDatabase.class)
public class MovieTrailer extends BaseModel {

    @PrimaryKey
    @Column
    Long id;

    @Column
    String name;

    @Column
    String size;

    @Column
    String source;

    @Column
    String type;

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }
}
