package com.example.karthik.colorduneswallpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by karthik on 7/6/18.
 */

public class FontPreference extends ListPreference {

    private int mClickedDialogEntryIndex;
    private String val[] = {"1.Color Dunes!","2.Color Dunes!","3.Color Dunes!","4.Color Dunes!","5.Color Dunes!"};
    FontAdapter fontAdapter = new FontAdapter(getContext(),val);

    public FontPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public FontPreference(Context context){
        super(context);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
        mClickedDialogEntryIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(fontAdapter, mClickedDialogEntryIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mClickedDialogEntryIndex != which){
                    mClickedDialogEntryIndex = which;
                    LiveTimeWallpaperService.fontIndex = mClickedDialogEntryIndex;
                    FontPreference.this.notifyChanged();
                }
                FontPreference.this.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(null,null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){

        if(mClickedDialogEntryIndex >=0){
            String value = Integer.toString(mClickedDialogEntryIndex);
            if(callChangeListener(value)){
                setValue(value);
            }
        }
    }

}
