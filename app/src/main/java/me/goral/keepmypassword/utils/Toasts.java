package me.goral.keepmypassword.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.vdx.designertoast.DesignerToast;

public class Toasts {
    public static void makeSuccessToast(String input, Context context){
        DesignerToast.Success(context, "Success", input, Gravity.BOTTOM, Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
    }

    public static void makeWarningToast(String input, Context context){
        DesignerToast.Warning(context, "Warning", input, Gravity.BOTTOM, Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
    }

    public static void makeErrorToast(String input, Context context){
        DesignerToast.Error(context, "Error", input, Gravity.BOTTOM, Toast.LENGTH_SHORT, DesignerToast.STYLE_DARK);
    }

    public static void makeInfoToast(String input, Context context){
        DesignerToast.Info(context, input, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    }
}