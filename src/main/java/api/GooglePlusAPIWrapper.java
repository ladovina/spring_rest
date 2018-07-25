package api;


import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequestInitializer;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import params.PostUsers;

import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


@Component
public class GooglePlusAPIWrapper {
    public static final String APPLICATION_NAME = "TestROI";
    public static final String API_KEY = "";

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

    /*
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    private static final Gson GSON = new Gson();

    /*
     * Creates a client secrets object from the client_secrets.json file.
     */
    private static GoogleClientSecrets clientSecrets;

    static {
        try {
            Reader reader = new FileReader("client_secrets.json");
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);
        } catch (IOException e) {
            throw new Error("No client_secrets.json found", e);
        }
    }

    /*
     * This is the Client ID that you generated in the API Console.
     */
    private static final String CLIENT_ID = clientSecrets.getWeb().getClientId();

    /*
     * This is the Client Secret that you generated in the API Console.
     */
    private static final String CLIENT_SECRET = clientSecrets.getWeb().getClientSecret();


    //TODO: singleton
    //TODO: refactor, move the same code to constructor
    public GoogleUser getGoogleUser(PostUsers postUsers) throws Exception {

        if (postUsers.getAccessToken() == null) {
            //TODO:
            return null;
        }
//        try {
            // Build credential from stored token data.
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(JSON_FACTORY)
                    .setTransport(TRANSPORT)
                    .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                    .setFromTokenResponse(JSON_FACTORY.fromString(
                            postUsers.getAccessToken(), GoogleTokenResponse.class));
            // Create a new authorized API client.
            Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Person profile = plus.people().get(postUsers.getGoogleUserId()).execute();

            return FactoryGoogleUser.createGoogleUser(
                    profile.getId(),
                    profile.getDisplayName(),
                    profile.getGender(),
                    profile.getImage().getUrl());

            // Get a list of people that this user has shared with this app.
//            PeopleFeed people = service.people().list("me", "visible").execute();
//            response.setStatus(HttpServletResponse.SC_OK);
//            response.getWriter().print(GSON.toJson(people));
//        } catch (IOException e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().print(GSON.toJson("Failed to read data from Google. " +
//                    e.getMessage()));
//        }















//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = new JacksonFactory();
//
//        Plus plus = new Plus.Builder(httpTransport, jsonFactory, null)
//                .setApplicationName(APPLICATION_NAME)
//                .setGoogleClientRequestInitializer(new PlusRequestInitializer(API_KEY)).build();



    }

    public List<GoogleUserActivity> getGoogleUserActivities(GoogleUser googleUser) throws Exception {

        List<GoogleUserActivity> allActivityList = new ArrayList<GoogleUserActivity>();

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        Plus plus = new Plus.Builder(httpTransport, jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setGoogleClientRequestInitializer(new PlusRequestInitializer(API_KEY)).build();

        Plus.Activities.List listActivities = plus.activities().list(
                googleUser.getGoogleId(),
                ACTIVITY_TYPE);

        listActivities.setMaxResults(ACTIVITIES_PER_REQ);

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

