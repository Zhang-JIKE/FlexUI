package com.daily.flexui.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatEditText;

public class PhoneView extends AppCompatEditText {

    public PhoneView(Context context) {
        super(context);
        init();
    }

    public PhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setMaxLines(1);
        setGravity(Gravity.CENTER);
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(13) });
        setInputType(InputType.TYPE_CLASS_NUMBER);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    setText(sb.toString());
                    setSelection(index);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public String getPhoneNum() {
        return super.getText().toString().trim();
    }
}
