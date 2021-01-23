package com.frangoudes.myfilepicker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 77;

    List<Uri> uriList = new ArrayList<>();
    List<String> trackList = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.select_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFileManager(view);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Add the Uri of the selected file to the list
                uriList.add(data.getData());
            } else {
                if (data.getClipData() != null) {
                    // multiple files selected
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        uriList.add(data.getClipData().getItemAt(i).getUri());
                    }
                    Collections.sort(uriList);
                }
            }
            // displayFileListPath();
            displayFileList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void launchFileManager(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, getResources().getString(R.string.file_list_string)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void displayFileListPath(){
        TextView tvFileList = (TextView) getView().findViewById(R.id.file_list);
        tvFileList.setMovementMethod(new ScrollingMovementMethod());
        tvFileList.setText(null);
        if (!uriList.isEmpty()) {
            tvFileList.setText(uriList.toString());
        }
    }

    private void displayFileList(){
        TextView tvFileList = (TextView) getView().findViewById(R.id.file_list);
        tvFileList.setMovementMethod(new ScrollingMovementMethod());
        tvFileList.setText(null);
        if (uriList.isEmpty()) {
            tvFileList.setText(getResources().getString(R.string.file_list_string));
        } else {
            for (int i = uriList.size() -1; i >=0; i--) {
                tvFileList.append("\n" + getFileName(uriList.get(i)));
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}