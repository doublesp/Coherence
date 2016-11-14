package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.MovieRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.models.Movie;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class MovieInteractor extends IdeaInteractorBase implements IdeaInteractorInterface {

    IdeaDataStoreInterface mIdeaDataStore;
    MovieRepositoryInterface mMovieRepository;

    public MovieInteractor(IdeaDataStoreInterface ideaDataStore,
                           MovieRepositoryInterface movieRepository) {
        super(ideaDataStore);
        mIdeaDataStore = ideaDataStore;
        mMovieRepository = movieRepository;
        mMovieRepository.subscribe(new Observer<List<Movie>>() {
            List<Movie> mMovies;

            @Override
            public void onCompleted() {
                List<Idea> ideas = new ArrayList<>();
                for (Movie movie : mMovies) {
                    ideas.add(new Idea(movie.getId(),
                            R.id.idea_category_movies,
                            movie.getOriginalTitle(),
                            false,
                            R.id.idea_type_suggestion,
                            new IdeaMeta(movie.getPosterPath(), movie.getOriginalTitle(), movie.getOverview())
                    ));
                }
                mIdeaDataStore.setSuggestions(ideas);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                mMovies = movies;
            }
        });
    }

    @Override
    public void getRelatedIdeas(Idea idea) {
        // TODO: instead of just return recent movies, use search api to search for related movies
        mMovieRepository.getNowPlayingMovies();
    }

    @Override
    int getCategory() {
        return R.id.idea_category_movies;
    }
}
