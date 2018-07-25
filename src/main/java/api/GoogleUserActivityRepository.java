package api;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

//TODO: move to repository
public interface GoogleUserActivityRepository extends CrudRepository<GoogleUserActivity, Long> {
    List<GoogleUserActivity> findByGoogleUserGoogleUserId(String googleUserId);

}