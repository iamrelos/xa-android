package io.keypunchers.xa.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.misc.SingletonVolley;

public class ImageListFragment extends Fragment {
    private ArrayList<String> mData;
    private GenericAdapter<String> mAdapter;


    public ImageListFragment() {
    }

    public static Fragment newInstance(ArrayList<String> data) {
        ImageListFragment fragment = new ImageListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setAdapter();

        ListView mLvContent = (ListView) view.findViewById(R.id.lv_image_list);
        mLvContent.setAdapter(mAdapter);
    }

    private void setAdapter() {
        mAdapter = new GenericAdapter<>(getActivity(), mData, new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(final int position, View convertView, ViewGroup parent, Context context, ArrayList<?> data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.row_image_list, parent, false);

                    viewHolder = new ViewHolder();
                    assert convertView != null;

                    viewHolder.mIvImage = (NetworkImageView) convertView.findViewById(R.id.iv_image_list);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.mIvImage.setImageUrl(mData.get(position), SingletonVolley.getImageLoader());
                viewHolder.mIvImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position)));
                        startActivity(mIntent);
                    }
                });

                return convertView;
            }
        });
    }

    private class ViewHolder {
        NetworkImageView mIvImage;
    }
}
