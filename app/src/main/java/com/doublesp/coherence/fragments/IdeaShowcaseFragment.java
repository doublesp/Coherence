package com.doublesp.coherence.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IdeaShowcaseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public IdeaShowcaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieShowcaseFragment.
     */
    public static IdeaShowcaseFragment newInstance() {
        IdeaShowcaseFragment fragment = new IdeaShowcaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_movie_showcase, container, false);
//        mUnbinder = ButterKnife.bind(this, view);
//        return view;
        return null;
    }

//    public void onClick(View view) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(MovieShowcaseInteractor.getInstance().getSelectedPos());
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int pos);
    }

    public void populateView(String movieTitle, String movieOverview, String moviewImageUri) {
//        if (tvTitle != null) {
//            tvTitle.setText(movieTitle);
//        }
//        if (tvOverview != null) {
//            tvOverview.setText(movieOverview);
//        }
//        if (ivImage != null) {
//            Picasso.with(getContext()).load(moviewImageUri).fit().centerCrop()
//                    .placeholder(mMoviePlaceholder)
//                    .error(mErrorIcon)
//                    .into(ivImage);
//        }
    }

}
