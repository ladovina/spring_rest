package api;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

//TODO: move to repository
public interface GoogleUserRepository extends CrudRepository<GoogleUser, String> {

}