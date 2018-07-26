package factory;

import entity.GoogleUser;

public class FactoryGoogleUser {
    public static GoogleUser createGoogleUser(String googleId, String name, String gender, String pictureUrl){
        GoogleUser user = new GoogleUser();

        user.setGoogleUserId(googleId);
        user.setName(name);
        user.setGender(gender);
        user.setPictureUrl(pictureUrl);
        return user;
    }
}