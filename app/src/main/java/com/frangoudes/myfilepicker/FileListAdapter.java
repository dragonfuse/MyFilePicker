package com.frangoudes.myfilepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListRVH> {

    private List<String> localFileList = new ArrayList<>();

    //private ArrayList<String> localFileList;

    public FileListAdapter(List<String> fileList) {
        localFileList = fileList;
    }

    public class FileListRVH extends RecyclerView.ViewHolder {

        private TextView view;
        public FileListRVH(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.randomText);
        }
        public TextView getView(){
            return view;
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public FileListRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new FileListRVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListRVH fileListVH, int position) {
        fileListVH.getView().setText(localFileList.get(position));
    }

    @Override
    public int getItemCount() {
        return localFileList.size();
    }
}
