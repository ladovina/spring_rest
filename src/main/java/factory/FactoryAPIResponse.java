package factory;

import api.APIResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.NoSuchElementException;

public class FactoryAPIResponse {

    public static APIResponse createSuccessResponse(){
        return FactoryAPIResponse.createSuccessResponse(null);
    }

    public static APIResponse createSuccessResponse(Object data){
        return new APIResponse(APIResponse.STATUS_OK, APIResponse.MSG_STATUS_OK, data);
    }

    public static APIResponse createFailedResponse(Exception e){
        String errorMessage = e.getMessage();
        int statusCode = APIResponse.STATUS_GENERAL_ERROR;

        if(e instanceof GoogleJsonResponseException){
            errorMessage = ((GoogleJsonResponseException) e).getStatusMessage();
            statusCode = ((GoogleJsonResponseException) e).getStatusCode();
        }

        if(e instanceof EmptyResultDataAccessException ||
                e instanceof NoSuchElementException){
            statusCode = APIResponse.STATUS_GOOGLE_USER_NOT_FOOUND;
        }

        return new APIResponse(statusCode, errorMessage, null);
    }
}