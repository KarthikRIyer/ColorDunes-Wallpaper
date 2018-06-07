package com.example.karthik.colorduneswallpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by karthik on 7/6/18.
 */

public class DividerPreference extends ListPreference {

    private int mClickedDialogEntryIndex;
    private String val[] = {"1.None","2.Dot","3.Colon","4.Space","5.Pipe","6.Slash"};
    OverlayAdapter overlayAdapter = new OverlayAdapter(getContext(),val);

    public DividerPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public DividerPreference(Context context){
        super(context);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
        mClickedDialogEntryIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(overlayAdapter, mClickedDialogEntryIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mClickedDialogEntryIndex != which){
                    mClickedDialogEntryIndex = which;
                    LiveTimeWallpaperService.dividerIndex = mClickedDialogEntryIndex;
                    DividerPreference.this.notifyChanged();
                }
                DividerPreference.this.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
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
