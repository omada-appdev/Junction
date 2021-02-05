package com.omada.junction.ui.uicomponents;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.omada.junction.R;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.viewmodels.content.EventViewHandler;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/*
*   This class uses a form that was already created and inflates a form view based on that form, by creating form components, attchinmg
*   listeners, etc based on the form
*
 */

public class FormView extends FrameLayout {

    // DO NOT GET A REFERENCE TO FORM INSTANCE FROM OUTSIDE THIS CLASS
    private Form<SectionType, QuestionType, ResponseType> form;

    private final Map <String, View> formSectionViews = new HashMap<>();
    private final Map <String, View> formQuestionViews = new HashMap<>();
    private final Map <String, View> formOptionViews = new HashMap<>();

    private EventModel eventModel;
    private EventViewHandler viewModel;

    public FormView(Context context) {
        super(context);
    }

    public FormView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FormView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewModel(EventViewHandler eventViewHandler){
        this.viewModel = eventViewHandler;
    }

    public void setForm(EventModel eventModel) {

        this.eventModel = eventModel;

        try {

            // Creation of form class itself handles all the View inflation, etc
            this.form = new RegistrationForm(eventModel.getForm() == null ? new HashMap<>() : eventModel.getForm());

            addView(
                    formSectionViews.get(
                            form.sectionsList.get(0).getID()
                    )
            );

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // These inflate methods are used to inflate views, attach listeners where needed and return the resulting view

    private View inflateSectionView() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        ConstraintLayout inflatedSectionView = (ConstraintLayout) inflater.inflate(R.layout.form_section_layout, this, false);

        // TODO check section button types here

        inflatedSectionView.findViewById(R.id.section_action_button)
                .setOnClickListener(v->
                        // TODO add any validation code here
                        viewModel.registerForEvent(this.eventModel, form.getFormMap())
                );
        return inflatedSectionView;
    }

    private View inflateQuestionView(String sectionID) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return inflater.inflate(R.layout.form_question_layout, (ViewGroup) formSectionViews.get(sectionID), false);
    }

    private View inflateShortTextResponseView(String questionID) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View v = inflater.inflate(R.layout.form_response_short_text_layout, (ViewGroup) formSectionViews.get(questionID), false);

        TextInputEditText shortTextInput = v.findViewById(R.id.response_short_text_input);

        shortTextInput
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        form.formQuestions.get(questionID).setResponse(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        return v;
    }

    private View inflateCheckBoxResponseView(String questionID) {
        return null;
    }

    private View inflateDropDownResponseView(String questionID) {
        return null;
    }

    private View inflateDateResponseView(String questionID) {
        return null;
    }

    private enum SectionType {
        SECTION_TYPE_DEFAULT
    }

    private enum QuestionType {
        QUESTION_TYPE_DEFAULT
    }

    private enum ResponseType {

        RESPONSE_TYPE_DEFAULT,
        RESPONSE_TYPE_SHORT_TEXT,
        RESPONSE_TYPE_LONG_TEXT,
        RESPONSE_TYPE_DATE,
        RESPONSE_TYPE_TIME,
        RESPONSE_TYPE_CHECK_BOX,
        RESPONSE_TYPE_RADIO_BUTTON,
        RESPONSE_TYPE_DROPDOWN,
        RESPONSE_TYPE_FILE

    }

    // This is not a static class because it needs access to views and inflaters, etc

    private class RegistrationForm extends Form <SectionType, QuestionType, ResponseType> {

        public RegistrationForm(@NonNull Map<String, Map<String, Map<String, String>>> formData) throws ParseException {
            super(formData);
        }

        // create view for section here and store it in the main class
        @Override
        protected void onSectionCreated(Section section) {

            View inflatedSectionView = inflateSectionView();
            ((MaterialTextView)inflatedSectionView.findViewById(R.id.section_title_text)).setText(section.getTitle());
            ((MaterialTextView)inflatedSectionView.findViewById(R.id.section_description_text)).setText(section.getDescription());

            formSectionViews.put(section.getID(), inflatedSectionView);
        }

        // create view for question here and store it in the main class
        @Override
        protected void onQuestionCreated(Question question) {

            // Add main question view to section view from here
            LinearLayout inflatedQuestionView = (LinearLayout) inflateQuestionView(question.getSectionID());
            ((MaterialTextView)inflatedQuestionView.findViewById(R.id.question_title_text)).setText(question.getTitle());

            if(question.getDescription() != null && !question.getDescription().equals("")) {
                ((MaterialTextView) inflatedQuestionView.findViewById(R.id.question_description_text)).setText(question.getDescription());
            }
            else{
                ((MaterialTextView) inflatedQuestionView.findViewById(R.id.question_description_text)).setVisibility(GONE);
            }

            formQuestionViews.put(question.getID(), inflatedQuestionView);

            ((LinearLayout)(formSectionViews.get(question.getSectionID())
                    .findViewById(R.id.section_contents)))
                    .addView(inflatedQuestionView);

            // Add response views that don't need options from here
            switch (question.getResponseType()) {
                case RESPONSE_TYPE_DEFAULT:
                    break;
                case RESPONSE_TYPE_SHORT_TEXT:
                    inflatedQuestionView.addView(inflateShortTextResponseView(question.getID()));
                    break;
                case RESPONSE_TYPE_CHECK_BOX:
                    break;
                case RESPONSE_TYPE_DATE:
                    break;
                case RESPONSE_TYPE_DROPDOWN:
                    break;
            }
        }

        // create view for option here and store it in the main class
        @Override
        protected void onOptionCreated(Option option) {

            View inflatedOptionView = null;

            switch (option.getType()) {
                case RESPONSE_TYPE_DROPDOWN:
                    break;
                case RESPONSE_TYPE_CHECK_BOX:
                    break;
            }

            formOptionViews.put(option.getID(), inflatedOptionView);

        }

        @Override
        public SectionType getSectionTypeFromString(String sectionType) {
            return SectionType.SECTION_TYPE_DEFAULT;
        }

        @Override
        public QuestionType getQuestionTypeFromString(String questionType) {
            return QuestionType.QUESTION_TYPE_DEFAULT;
        }

        @Override
        public ResponseType getResponseTypeFromString(String responseType) {
            switch (responseType) {
                case "shortText":
                    return ResponseType.RESPONSE_TYPE_SHORT_TEXT;
                case "longText":
                    return ResponseType.RESPONSE_TYPE_LONG_TEXT;
                case "date":
                    return ResponseType.RESPONSE_TYPE_DATE;
                case "time":
                    return ResponseType.RESPONSE_TYPE_TIME;
                case "checkBox":
                    return ResponseType.RESPONSE_TYPE_CHECK_BOX;
                case "radioButton":
                    return ResponseType.RESPONSE_TYPE_RADIO_BUTTON;
                case "dropDown":
                    return ResponseType.RESPONSE_TYPE_DROPDOWN;
                case "file":
                    return ResponseType.RESPONSE_TYPE_FILE;
                default:
                    return ResponseType.RESPONSE_TYPE_DEFAULT;
            }
        }
    }
}
