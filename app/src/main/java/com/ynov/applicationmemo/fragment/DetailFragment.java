package com.ynov.applicationmemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ynov.applicationmemo.R;

public class DetailFragment extends Fragment {

    // Constantes :
    public static final String EXTRA_MEMO = "EXTRA_MEMO";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        // init :
        super.onActivityCreated(savedInstanceState);
        if (getView() != null && getContext() != null)
        {
            // vues :
            TextView textViewMemo = getView().findViewById(R.id.detail_memo);

            if(getArguments() != null) {
                textViewMemo.setText(getArguments().getString(EXTRA_MEMO));
            }
        }
    }
}
