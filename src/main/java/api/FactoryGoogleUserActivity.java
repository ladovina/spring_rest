package api;

import org.springframework.stereotype.Component;

@Component
public class FactoryGoogleUserActivity {
    public static GoogleUserActivity createGoogleUserActivity(String etag,
                                                              String title,
                                                              String published,
                                                              String updated,
                                                              String activityId,
                                                              String url,
                                                              GoogleUser googleUser){

        GoogleUserActivity userActivity = new GoogleUserActivity();
        userActivity.setEtag(etag);
        userActivity.setTitle(title);
        userActivity.setPublished(published);
        userActivity.setUpdated(updated);
        userActivity.setActivityId(activityId);
        userActivity.setUrl(url);
        userActivity.setGoogleUser(googleUser);

        return userActivity;
    }
}