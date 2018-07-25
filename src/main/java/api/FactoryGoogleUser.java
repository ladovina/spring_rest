package api;

public class FactoryGoogleUser {
    public static GoogleUser createGoogleUser(String googleId, String name, String gender, String pictureUrl){
        GoogleUser user = new GoogleUser();

        user.setGoogleId(googleId);
        user.setName(name);
        user.setGender(gender);
        user.setPictureUrl(pictureUrl);
        return user;
    }
}