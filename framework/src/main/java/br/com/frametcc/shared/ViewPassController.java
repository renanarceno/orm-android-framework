package br.com.frametcc.shared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ViewPassController {

    private ViewGroup baseView;

    public ViewPassController(ViewGroup baseView) {
        this.baseView = baseView;
    }

    @SuppressWarnings("unchecked")
    public <AVIEW extends View> AVIEW findViewById(int id) {
        return (AVIEW) this.baseView.findViewById(id);
    }

    public void setText(int id, int stringId) {
        setText(id, this.baseView.getResources().getString(stringId));
    }

    public void setText(int id, String textStr) {
        View view = findViewById(id);
        ((TextView) view).setText(textStr);
        if(view instanceof EditText && textStr != null) {
            ((EditText) view).setSelection(textStr.length());
        }
    }

    public void setTextOrGoneWhenNull(int id, String textStr) {
        if(textStr == null || textStr.length() == 0) {
            this.setVisibleOrGone(true, id);
        } else {
            this.setText(id, textStr);
        }
    }

    public String getText(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

    public void setVisibleOrGone(boolean visible, int viewId) {
        this.baseView.findViewById(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}