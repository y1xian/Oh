package com.yyxnb.android.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.startup.Initializer;

import com.yyxnb.android.Oh;

import java.util.Collections;
import java.util.List;

/**
 * 使用startup初始化三方库
 *
 * @author yyx
 */
public class AppInitializer implements Initializer<Void> {

    private static final String TAG = AppInitializer.class.getSimpleName();

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Log.e(TAG, "AppInitializer初始化");
        Log.i(TAG,"\n \n \n " +
                "                                                  ,----, \n" +
                "                        ,--,                    ,/   .`| \n" +
                "           .---.      ,--.'|   ,---,          ,`   .'  : \n" +
                "          /. ./|   ,--,  | :  '  .' \\       ;    ;     / \n" +
                "      .--'.  ' ;,---.'|  : ' /  ;    '.   .'___,/    ,'  \n" +
                "     /__./ \\ : ||   | : _' |:  :       \\  |    :     |   \n" +
                " .--'.  '   \\' .:   : |.'  |:  |   /\\   \\ ;    |.';  ;   \n" +
                "/___/ \\ |    ' '|   ' '  ; :|  :  ' ;.   :`----'  |  |   \n" +
                ";   \\  \\;      :'   |  .'. ||  |  ;/  \\   \\   '   :  ;   \n" +
                " \\   ;  `      ||   | :  | ''  :  | \\  \\ ,'   |   |  '   \n" +
                "  .   \\    .\\  ;'   : |  : ;|  |  '  '--'     '   :  |   \n" +
                "   \\   \\   ' \\ ||   | '  ,/ |  :  :           ;   |.'    \n" +
                "    :   '  |--\" ;   : ;--'  |  | ,'           '---'      \n" +
                "     \\   \\ ;    |   ,/      `--''                        \n" +
                "      '---\"     '---'                                    \n" +
                "                                                          \n\n");
        Oh.init(context);
        // 突破65535的限制
        MultiDex.install(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
