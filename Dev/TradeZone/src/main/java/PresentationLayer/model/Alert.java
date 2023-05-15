package PresentationLayer.model;

public class Alert {
    private boolean success = false;
    private boolean fail = false;
    private String message = "";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        this.fail = !success;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
        this.success = !fail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
