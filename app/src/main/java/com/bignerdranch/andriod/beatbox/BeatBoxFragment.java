package com.bignerdranch.andriod.beatbox;

import android.databinding.DataBindingUtil;
import android.databinding.adapters.SeekBarBindingAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bignerdranch.andriod.beatbox.databinding.FragmentBeatBoxBinding;
import com.bignerdranch.andriod.beatbox.databinding.ListItemSoundBinding;

import java.util.List;

/**
 * Created by Angel on 4/9/18.
 */

public class BeatBoxFragment extends Fragment {

    //create an instance of BeatBox to
    //get the sound assets
    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to retain fragment through fragment/activity lifecycle
        //because sound is not a stashable data... continuos
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beat_box, container, false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder{

        private ListItemSoundBinding mBinding;

       private SoundHolder(ListItemSoundBinding binding){
           super(binding.getRoot());
           mBinding = binding;
           mBinding.setViewModel(new SoundViewModel(mBeatBox));
       }

       public void bind(Sound sound){
           mBinding.getViewModel().setSound(sound);
           mBinding.executePendingBindings();
       }

    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

        //placeholder for the list of sounds from BeatBox
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_sound, parent, false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bind(sound);
        }



        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
