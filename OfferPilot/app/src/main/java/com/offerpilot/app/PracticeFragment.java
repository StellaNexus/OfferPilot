package com.offerpilot.app;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.LinearLayout;


public class PracticeFragment extends Fragment {
    private String currentMode = "quick";
    private PracticeRepository practiceRepository;


    private final int selectedColor = Color.rgb(210, 225, 250);
    private final int selectedStrokeColor = Color.rgb(210, 225, 250);
    private final int normalColor = Color.TRANSPARENT;
    private final int normalStrokeColor = Color.rgb(221, 221, 221);

    @Nullable
    @Override

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(
                R.layout.fragment_practice,
                container,
                false
        );

        practiceRepository = new PracticeRepository(requireContext());


        EditText chatInput = view.findViewById(R.id.edit_chat_input);
        Button sendButton = view.findViewById(R.id.button_send_message);

        chatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean hasText = s.toString().trim().length() > 0;
                sendButton.setEnabled(hasText);
                if (hasText) {
                    sendButton.setBackgroundTintList(
                            ColorStateList.valueOf(Color.rgb(33, 150, 243))
                    );
                } else {
                    sendButton.setBackgroundTintList(
                            ColorStateList.valueOf(Color.rgb(189, 189, 189))
                    );
                }
            }
        });

        View mainContent = requireActivity().findViewById(R.id.main_content);
        FrameLayout fragmentContainer = requireActivity().findViewById(R.id.fragment_container);

        sendButton.setOnClickListener(v -> {
            String message = chatInput.getText().toString().trim();

            Bundle args = new Bundle();
            args.putString("practice_mode",currentMode);
            args.putString("first_message",message);

            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(args);

            mainContent.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);


            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,  questionFragment)
                    .addToBackStack(null)
                    .commit();
        });

        getParentFragmentManager().addOnBackStackChangedListener(() -> {
            if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                fragmentContainer.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
            }
        });

        TextView quickPracticeCard = view.findViewById(R.id.card_quick_practice);
        TextView standardPracticeCard = view.findViewById(R.id.card_standard_practice);
        TextView fullMockInterviewCard = view.findViewById(R.id.card_full_mock_interview);


        quickPracticeCard.setOnClickListener(v -> {
            currentMode = "quick";
            updateModeSelection(
                    quickPracticeCard,
                    standardPracticeCard,
                    fullMockInterviewCard
                    );
        });
        standardPracticeCard.setOnClickListener(v -> {
            currentMode = "standard";
            updateModeSelection(
                    quickPracticeCard,
                    standardPracticeCard,
                    fullMockInterviewCard
            );
        });

        fullMockInterviewCard.setOnClickListener(v -> {
            currentMode = "full_interview";
            updateModeSelection(
                    quickPracticeCard,
                    standardPracticeCard,
                    fullMockInterviewCard
            );
        });

        updateModeSelection(
                quickPracticeCard,
                standardPracticeCard,
                fullMockInterviewCard
        );

        DrawerLayout practiceDrawer = view.findViewById(R.id.practice_drawer);
        View historyButton = view.findViewById(R.id.button_history);
        View closeHistoryButton = view.findViewById(R.id.button_close_history);

        historyButton.setOnClickListener(v -> {
            LinearLayout historyList = view.findViewById(R.id.history_list_container);
            TextView emptyHistory = view.findViewById(R.id.text_empty_history);

            practiceRepository.getSessions(sessions -> {
                if (!isAdded() || getView() != view) {
                    return;
                }

                historyList.removeAllViews();
                emptyHistory.setVisibility(sessions.isEmpty() ? View.VISIBLE : View.GONE);

                for (PracticeSession session : sessions) {
                    TextView item = new TextView(requireContext());
                    String modeText;

                    if ("quick".equals(session.getPracticeMode())) {
                        modeText = "快速练习";
                    } else if ("standard".equals(session.getPracticeMode())) {
                        modeText = "标准练习";
                    } else {
                        modeText = "完整面试";
                    }

                    String statusText =
                            "FINISHED".equals(session.getStatus())
                                    ? "已完成"
                                    : "进行中";

                    item.setText(modeText + " · " + statusText);
                    item.setTextSize(16);
                    item.setPadding(12, 20, 12, 20);

                    item.setOnClickListener(clicked -> {
                        Bundle args = new Bundle();
                        args.putLong("session_id", session.getId());
                        args.putString("practice_mode", session.getPracticeMode());

                        QuestionFragment questionFragment = new QuestionFragment();
                        questionFragment.setArguments(args);

                        practiceDrawer.closeDrawer(Gravity.LEFT);
                        mainContent.setVisibility(View.GONE);
                        fragmentContainer.setVisibility(View.VISIBLE);

                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, questionFragment)
                                .addToBackStack(null)
                                .commit();
                    });


                    historyList.addView(item);
                }
            });

            practiceDrawer.openDrawer(Gravity.LEFT);
        });

        closeHistoryButton.setOnClickListener(v -> {
            practiceDrawer.closeDrawer(Gravity.LEFT);
        });

        View newPracticeButton = view.findViewById(R.id.button_new_practice);

        newPracticeButton.setOnClickListener(v -> {
            chatInput.setText("");
            currentMode = "quick";
            updateModeSelection(
                    quickPracticeCard,
                    standardPracticeCard,
                    fullMockInterviewCard
            );
            practiceDrawer.closeDrawer(Gravity.LEFT);
        });


        return view;
    }

    private void updateModeSelection(
            TextView quickCard,
            TextView standardCard,
            TextView fullInterviewCard) {

        updateCardStyle(quickCard, "quick".equals(currentMode));
        updateCardStyle(standardCard, "standard".equals(currentMode));
        updateCardStyle(fullInterviewCard, "full_interview".equals(currentMode));
    }

    private void updateCardStyle(TextView card, boolean selected) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(selected ? selectedColor : normalColor);

        if (selected) {
            background.setCornerRadius(32);
            card.setTextColor(Color.rgb(53, 105, 212));
            card.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            background.setCornerRadius(0);
            card.setTextColor(Color.rgb(40, 40, 40));
            card.setTypeface(null, android.graphics.Typeface.NORMAL);
        }

        card.setBackground(background);
    }

}
