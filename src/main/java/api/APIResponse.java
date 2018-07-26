package api;

public class APIResponse {
    public static final int STATUS_OK = 0;
    public static final int STATUS_GENERAL_ERROR = 1;
    public static final int STATUS_GOOGLE_USER_NOT_FOOUND = 2;

    public static final String MSG_STATUS_OK = "Success";

    private int status;
    private String message;
    private Object data;

    public APIResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
