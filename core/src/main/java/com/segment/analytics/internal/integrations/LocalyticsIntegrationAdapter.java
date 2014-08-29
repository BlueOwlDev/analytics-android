package com.segment.analytics.internal.integrations;

import android.app.Activity;
import android.content.Context;
import com.localytics.android.LocalyticsSession;
import com.segment.analytics.Traits;
import com.segment.analytics.internal.payload.IdentifyPayload;
import com.segment.analytics.internal.payload.ScreenPayload;
import com.segment.analytics.internal.payload.TrackPayload;
import com.segment.analytics.json.JsonMap;
import java.util.Map;

import static com.segment.analytics.internal.Utils.isNullOrEmpty;

/**
 * Localytics is a general-purpose mobile analytics tool that measures customer acquisition, ad
 * attribution, retargeting campaigns and user actions in your mobile apps.
 *
 * @see <a href="http://www.localytics.com/">Localytics</a>
 * @see <a href="https://segment.io/docs/integrations/localytics/">Localytics Integration</a>
 * @see <a href="http://www.localytics.com/docs/android-integration/">Localytics Android SDK</a>
 */
public class LocalyticsIntegrationAdapter extends AbstractIntegrationAdapter<LocalyticsSession> {
  private LocalyticsSession localyticsSession;

  @Override public void initialize(Context context, JsonMap settings)
      throws InvalidConfigurationException {
    // todo: docs mentions wake_lock, but not if it is required
    localyticsSession = new LocalyticsSession(context, settings.getString("appKey"));
  }

  @Override public LocalyticsSession getUnderlyingInstance() {
    return localyticsSession;
  }

  @Override public String className() {
    return "com.localytics.android.LocalyticsSession";
  }

  @Override public String key() {
    return "Localytics";
  }

  @Override public void onActivityResumed(Activity activity) {
    super.onActivityResumed(activity);
    localyticsSession.open();
  }

  @Override public void onActivityPaused(Activity activity) {
    super.onActivityPaused(activity);
    localyticsSession.close();
  }

  @Override public void flush() {
    super.flush();
    localyticsSession.upload();
  }

  @Override public void optOut(boolean optOut) {
    super.optOut(optOut);
    localyticsSession.setOptOut(optOut);
  }

  @Override public void identify(IdentifyPayload identify) {
    super.identify(identify);
    localyticsSession.setCustomerId(identify.userId());
    Traits traits = identify.traits();
    String email = traits.email();
    if (!isNullOrEmpty(email)) localyticsSession.setCustomerEmail(email);
    String name = traits.name();
    if (!isNullOrEmpty(name)) localyticsSession.setCustomerName(name);
    for (Map.Entry<String, Object> entry : traits.entrySet()) {
      localyticsSession.setCustomerData(entry.getKey(), String.valueOf(entry.getValue()));
    }
  }

  @Override public void screen(ScreenPayload screen) {
    super.screen(screen);
    localyticsSession.tagScreen(screen.event());
  }

  @Override public void track(TrackPayload track) {
    super.track(track);
    localyticsSession.tagEvent(track.event(), track.properties().toStringMap());
  }
}