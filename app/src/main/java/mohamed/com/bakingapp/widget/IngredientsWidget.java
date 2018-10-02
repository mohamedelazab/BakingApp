package mohamed.com.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import mohamed.com.bakingapp.R;
import mohamed.com.bakingapp.model.BakedResponse;
import mohamed.com.bakingapp.model.IngredientResponse;
import mohamed.com.bakingapp.utils.Preferences;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        new Preferences(context).setWidgetId(appWidgetId);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setRemoteAdapter(R.id.widget_lv, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateFromActivity(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int itemClicked, ArrayList<BakedResponse> bakes) {


        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.updated_widget_layout);

            List<IngredientResponse> ingredients = bakes.get(itemClicked).getIngredientResponses();
            int i = 0;
            String x = "";
            for (IngredientResponse ingredient : ingredients) {
                x = x + (++i) + ". " + ingredient.getIngredient().substring(0, 1).toUpperCase() + ingredient.getIngredient().substring(1) + " \n";
            }

            views.setTextViewText(R.id.widget_title, bakes.get(itemClicked).getName() + " Ingredients");
            views.setTextViewText(R.id.widget_content, x);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
