package com.tangly.scorecard;

import android.app.Activity;
import android.content.*;

import com.tangly.scorecard.datastore.*;

/**
 * Default callback which launches some Activity with an ID
 */
public class DefaultEditStorableCallback implements EditStorableCallback
{
    private Context context;
    private Class<? extends Activity> launcherActivity;

    public DefaultEditStorableCallback(Context context, Class<? extends Activity> launcherActivity)
    {
        this.context = context;
        this.launcherActivity = launcherActivity;
    }

    public void editStorable(long id)
    {
        Intent intent = new Intent(this.context, this.launcherActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Populate intent with game session details
        if (id != DatastoreDefs.INVALID_ID)
        {
            intent.putExtra(IntentDefs.STORABLE_ID, id);
        }
        this.context.startActivity(intent);
    }
}
