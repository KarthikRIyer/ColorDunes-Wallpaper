package com.example.karthik.colorduneswallpaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.Toast;

/**
 * Created by karthik on 7/6/18.
 */

public class OverlayPreference extends ListPreference {

    private int mClickedDialogEntryIndex;
    String val[] = {"1.None","2.Dots","3.Honeycomb","4.Square","5.Triangle","6.Teapot"};
    OverlayAdapter overlayAdapter = new OverlayAdapter(getContext(),val);

    public OverlayPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public OverlayPreference(Context context){
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
                    LiveTimeWallpaperService.overlayIndex = mClickedDialogEntryIndex;
                    OverlayPreference.this.notifyChanged();
                }
                OverlayPreference.this.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
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
