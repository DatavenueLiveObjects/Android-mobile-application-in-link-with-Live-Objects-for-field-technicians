/*
* Software Name : IoT for technicians
* Version: 1.0
*
* Copyright (c) 2020 Orange
*
* This software is distributed under the Apache License, Version 2.0,
* the text of which is available at http://www.apache.org/licenses/
* or see the "license.txt" file for more details.
*
*/

package com.company.iotfortechnicians.edittag;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.android.material.textfield.TextInputEditText;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.AbstractIOTApiCallbackFragment;
import com.company.iotfortechnicians.common.Constants;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.dto.devicemanagement.DeviceDTO;
import com.company.iotfortechnicians.common.dto.devicemanagement.PatchTagsRequestDTO;
import com.company.iotfortechnicians.common.service.DeviceManagementService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditTagFragment extends AbstractIOTApiCallbackFragment<DeviceDTO> {

    private DeviceDTO deviceDTO;
    private TagView tagsView;
    private TextInputEditText editTagText;

    public static EditTagFragment newInstance(DeviceDTO deviceDTO) {
        EditTagFragment fragment = new EditTagFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE_DATA_KEY, deviceDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            deviceDTO = (DeviceDTO) getArguments().getSerializable(Constants.DEVICE_DATA_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_edit_tag, container, false);

        tagsView = inflatedView.findViewById(R.id.tag_group);
        tagsView.setOnTagDeleteListener((view, tag, position) -> {
            tagsView.remove(position);
        });

        addInitialTags();

        ImageView goBack = inflatedView.findViewById(R.id.edit_tag_back);
        goBack.setOnClickListener(v -> goToPreviousScreen());

        Button addNewTag = inflatedView.findViewById(R.id.add_new_tag);
        addNewTag.setEnabled(false);
        addNewTag.setOnClickListener(v -> putNewTag(editTagText.getText().toString().trim()));

        Button saveBtn = inflatedView.findViewById(R.id.save_edit_tag);
        saveBtn.setOnClickListener(v -> executeTagsPatch());

        editTagText = inflatedView.findViewById(R.id.edit_tag_text);
        editTagText.addTextChangedListener(switchButtonWatcher(addNewTag));

        addGlobalLayoutListener(R.id.abstractFragmentScrollView, null);

        return inflatedView;
    }

    private void goToPreviousScreen() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @NonNull
    private TextWatcher switchButtonWatcher(Button addNewTag) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNewTag.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not needed
            }
        };
    }

    private void executeTagsPatch() {
        PatchTagsRequestDTO patchTagsRequestDTO = getTagsList();
        DeviceManagementService patchTagsService = ApiClient.createApiService(DeviceManagementService.class);
        patchTagsService.patchTags(deviceDTO.getId(), patchTagsRequestDTO).enqueue(this);
    }

    private PatchTagsRequestDTO getTagsList() {
        List<String> tagsTextList = getTagsTextList();
        return new PatchTagsRequestDTO(tagsTextList);
    }

    @NonNull
    private List<String> getTagsTextList() {
        List<String> tagsTextList = new ArrayList<>();
        for (Tag tag : tagsView.getTags()) {
            tagsTextList.add(tag.text.toLowerCase());
        }
        return tagsTextList;
    }

    private void putNewTag(String text) {
        putTag(text);
        editTagText.setText("");
    }

    private void addInitialTags() {
        for (String text : deviceDTO.getTags()) {
            putTag(text);
        }
    }

    private void putTag(String text) {
        Tag tag = new Tag(text);
        tag.radius = 16;
        tag.text = tag.text.toUpperCase();
        tag.tagTextSize = 18;
        Resources resources = getResources();
        FragmentActivity activity = requireActivity();
        tag.tagTextColor = resources.getColor(R.color.black, activity.getTheme());
        tag.layoutColor = resources.getColor(R.color.locationBorder, activity.getTheme());
        tag.deleteIndicatorColor = resources.getColor(R.color.black, activity.getTheme());
        tag.isDeletable = true;
        tag.deleteIndicatorSize = 18f;
        tagsView.addTag(tag);
    }

    @Override
    protected void processData(DeviceDTO data) {
        Toast.makeText(getActivity(), "Updated tags succssfully", Toast.LENGTH_LONG).show();
        Intent goBackIntent = new Intent();
        goBackIntent.putExtra(Constants.DEVICE_TAGS_KEY, (Serializable) getTagsTextList());
        getActivity().setResult(Activity.RESULT_OK, goBackIntent);
        getActivity().finish();
    }

}
