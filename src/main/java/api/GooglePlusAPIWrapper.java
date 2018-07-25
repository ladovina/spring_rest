package api;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Person;
import org.springframework.stereotype.Component;
import params.PostUsers;

import java.util.ArrayList;
import java.util.List;


public class GooglePlusAPIWrapper {
    public static final String APPLICATION_NAME = "TestROI";

    public static final long ACTIVITIES_PER_REQ = 100L;
    public static final String ACTIVITY_TYPE = "public";

    /*
     * Default HTTP transport to use to make HTTP requests.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /*
     * Default JSON factory to use to deserialize JSON.
     */
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();


    private PostUsers postUsers;
    private GoogleCredential credential;
    private Plus plus;

    public GooglePlusAPIWrapper(PostUsers postUsers){
        this.postUsers = postUsers;
        this.credential = new GoogleCredential().setAccessToken(postUsers.getAccessToken());
        this.plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    //TODO: check access token valid
    public GoogleUser getGoogleUser() throws Exception {

            Person profile = this.plus.people().get(this.postUsers.getGoogleUserId()).execute();

            return FactoryGoogleUser.createGoogleUser(
                    profile.getId(),
                    profile.getDisplayName(),
                    profile.getGender(),
                    profile.getImage().getUrl());

    }

    private Plus.Activities.List getPlusActivitiesList() throws Exception {
        Plus.Activities.List listActivities = plus.activities().list(
                postUsers.getGoogleUserId(),
                ACTIVITY_TYPE);

        listActivities.setMaxResults(ACTIVITIES_PER_REQ);
        return listActivities;
    }

    public List<GoogleUserActivity> getActivities(GoogleUser googleUser) throws Exception {

        List<GoogleUserActivity> allActivityList = new ArrayList<>();

        Plus.Activities.List listActivities = getPlusActivitiesList();

        // Execute the request for the first page
        ActivityFeed activityFeed = listActivities.execute();

        // Unwrap the request and extract the pieces we want
        List<Activity> activities = activityFeed.getItems();

        // Loop through until we arrive at an empty page
        while (activities.size() > 0) {
            for (Activity activity : activities) {
                GoogleUserActivity googleUserActivity = FactoryGoogleUserActivity
                        .createGoogleUserActivity(
                                activity.getEtag(),
                                activity.getTitle(),
                                activity.getPublished().toString(),
                                activity.getUpdated().toString(),
                                activity.getId(),
                                activity.getUrl(),
                                googleUser
                        );

                allActivityList.add(googleUserActivity);
            }

            // We will know we are on the last page when the next page token is null.
            // If this is the case, break.
            if (activityFeed.getNextPageToken() == null) {
                break;
            }

            // Prepare to request the next page of activities
            listActivities.setPageToken(activityFeed.getNextPageToken());

            // Execute and process the next page request
            activityFeed = listActivities.execute();
            activities = activityFeed.getItems();
        }

        return allActivityList;
    }

}

