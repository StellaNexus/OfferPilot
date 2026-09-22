package com.offerpilot.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.TextView;

import android.widget.EditText;
import android.widget.Button;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;



import android.content.Context;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import android.os.Handler;
import android.os.Looper;

public class QuestionFragment extends Fragment {
    private String practiceMode;
    private String firstMessage;

    private LinearLayout chatContainer;
    private ScrollView scrollChat;

    private TextView modeText;

    private EditText chatInput;
    private Button sendButton;


    private int currentQuestionIndex = 1;
    private int totalQuestionCount = 0;
    private int followUpCount = 0;
    private boolean practiceFinished = false;

    private PracticeRepository practiceRepository;
    private long sessionId = -1L;
    private boolean sessionCreationStarted = false;



    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(
                R.layout.fragment_question,
                container,
                false
        );

        chatContainer = view.findViewById(R.id.chat_container);
        scrollChat = view.findViewById(R.id.scroll_chat);

        practiceRepository = new PracticeRepository(requireContext());


        chatInput = view.findViewById(R.id.edit_chat_input);
        sendButton = view.findViewById(R.id.button_question_send);


        scrollChat.setOnTouchListener((v,event)->{
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                hideKeyboard(chatInput);
            }
            return false;
        });



        chatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {
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

        sendButton.setOnClickListener(v -> {
            String text = chatInput.getText().toString().trim();

            if (practiceFinished) {
                return;
            }


            if (text.isEmpty()) {
                return;
            }

            addMessage(new ChatMessage(text, true));

            chatInput.setText("");

            scrollToBottom();

            new Handler(Looper.getMainLooper()).postDelayed(
                    this::addMockAiReply,
                    600
            );


        });


        Bundle args = getArguments();

        if (args != null) {
            practiceMode = args.getString("practice_mode");
            firstMessage = args.getString("first_message");
        }

        if ("quick".equals(practiceMode)){
            totalQuestionCount = 3;
        } else if ("standard".equals(practiceMode)) {
            totalQuestionCount = 5;
        } else  {totalQuestionCount = 0;}

        modeText = view.findViewById(R.id.text_question_progress);
        updateProgressText();


        if (sessionId > 0) {
            restoreMessages();

            if (practiceFinished) {
                chatInput.setEnabled(false);
                sendButton.setEnabled(false);
            }
        } else if (!sessionCreationStarted && !practiceFinished) {
            sessionCreationStarted = true;

            practiceRepository.createSession(
                    practiceMode,
                    totalQuestionCount,
                    createdSessionId -> {
                        if (practiceFinished) {
                            return;
                        }

                        sessionId = createdSessionId;

                        addMessage(new ChatMessage(
                                "你好，欢迎参加模拟面试",
                                false
                        ));

                        if (firstMessage != null && !firstMessage.isEmpty()) {
                            addMessage(new ChatMessage(firstMessage, true));

                            new Handler(Looper.getMainLooper()).postDelayed(
                                    () -> {
                                        if (!practiceFinished) {
                                            addMockAiReply();
                                        }
                                    },
                                    600
                            );
                        }
                    }
            );
        }


        view.setFocusableInTouchMode(true);
        view.requestFocus();


        return view;
    }

    private void scrollToBottom() {
        scrollChat.post(() -> scrollChat.fullScroll(View.FOCUS_DOWN));
    }

    private void updateProgressText() {
        if ("quick".equals(practiceMode)) {
            modeText.setText(
                    "快速练习 · "
                            + currentQuestionIndex
                            + " / "
                            + totalQuestionCount
            );
        } else if ("standard".equals(practiceMode)) {
            modeText.setText(
                    "标准练习 · "
                            + currentQuestionIndex
                            + " / "
                            + totalQuestionCount
            );
        } else {
            modeText.setText("完整面试演练");
        }
    }


    private void hideKeyboard(EditText chatInput){
        chatInput.clearFocus();

        InputMethodManager inputMethodManager=(InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(chatInput.getWindowToken(),0);
    }

    private  int dp(int value)
    {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density +0.5f);
    }
    private  void  addMessage(ChatMessage message) {
        if (sessionId > 0) {
            practiceRepository.saveMessage(
                    sessionId,
                    message.getContent(),
                    message.isFromUser() ? "USER" : "AI"
            );
        }

        renderMessage(message);
    }


    private void renderMessage(ChatMessage message) {
        TextView bubble = new TextView(requireContext());

        bubble.setText(message.getContent());
        bubble.setTextSize(18);
        bubble.setTextColor(Color.rgb(30, 30, 30));
        bubble.setPadding(dp(16), dp(12), dp(16), dp(12));
        bubble.setBackgroundResource(message.isFromUser()
                ? R.drawable.bg_user_message
                : R.drawable.bg_ai_message);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.gravity = message.isFromUser()
                ? Gravity.END
                : Gravity.START;

        params.topMargin = dp(12);
        params.setMarginStart(dp(24));
        params.setMarginEnd(dp(24));

        bubble.setLayoutParams(params);

        int maxWidth = (int) (
                getResources().getDisplayMetrics().widthPixels * 0.75f
        );

        bubble.setMaxWidth(maxWidth);
        chatContainer.addView(bubble);
    }

    private void restoreMessages() {
        if (sessionId <= 0) {
            return;
        }

        practiceRepository.getMessages(
                sessionId,
                messages -> {
                    if (!isAdded()) {
                        return;
                    }

                    chatContainer.removeAllViews();

                    for (PracticeMessage message : messages) {
                        renderMessage(
                                new ChatMessage(
                                        message.getContent(),
                                        "USER".equals(message.getSender())
                                )
                        );
                    }

                    scrollToBottom();
                }
        );
    }

    private void addMockAiReply() {
        AiResponse response = createMockResponse();
        handleAiResponse(response);
    }

    private void finishPractice() {
        if (practiceFinished) {
            return;
        }

        practiceFinished = true;
        if (sessionId > 0) {
            practiceRepository.updateProgress(
                    sessionId,
                    currentQuestionIndex,
                    "FINISHED"
            );
        }

        chatInput.setEnabled(false);
        sendButton.setEnabled(false);

        addMessage(new ChatMessage(
                "本轮练习已完成，正在生成练习总结。",
                false
        ));

        scrollToBottom();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SummaryFragment())
                    .addToBackStack(null)
                    .commit();
        }, 800);
    }


    private void handleAiResponse(AiResponse response) {
        addMessage(new ChatMessage(
                response.getMessage(),
                false
        ));

        if ("FOLLOW_UP".equals(response.getAction())) {
            followUpCount++;
        } else if ("NEXT_QUESTION".equals(response.getAction())) {
            currentQuestionIndex++;
            followUpCount = 0;
        } else if ("FINISH".equals(response.getAction())) {
            finishPractice();
            return;
        }

        updateProgressText();
        scrollToBottom();
    }


    private AiResponse createMockResponse() {
        if (totalQuestionCount == 0) {
            return new AiResponse(
                    "请继续回答，我会根据你的内容进行追问。",
                    "FOLLOW_UP",
                    false
            );
        }

        if (followUpCount == 0) {
            return new AiResponse(
                    "请具体说说你在这个项目中负责了哪些工作？",
                    "FOLLOW_UP",
                    false
            );
        }

        if (currentQuestionIndex < totalQuestionCount) {
            return new AiResponse(
                    "好的，这一题先到这里。接下来请说说项目中的一个技术难点。",
                    "NEXT_QUESTION",
                    true
            );
        }

        return new AiResponse(
                "本轮练习已完成，接下来可以查看你的练习总结。",
                "FINISH",
                true
        );
    }




}
