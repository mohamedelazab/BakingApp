package mohamed.com.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private SharedPreferences sharedPreferences;
    private Context context;

    private static final String Thumbnail ="Thumbnail";
    private static final String WidgetId ="WidgetId";

    public Preferences(Context context){
        this.context =context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setThumbnail(String strImg){
        sharedPreferences.edit().putString(Thumbnail, strImg).apply();
    }

    public String getThumbnail(){
        return sharedPreferences.getString(Thumbnail,"");
    }

    public void setWidgetId(int widgetId){
        sharedPreferences.edit().putInt(WidgetId,widgetId).apply();
    }

    public int getWidgetId(){
        return sharedPreferences.getInt(WidgetId,0);
    }

}
