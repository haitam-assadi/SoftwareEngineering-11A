package ServiceLayer;

/**
 * this class represent the result of a call to a void function.
 * if an exception was thrown, ErrorOccurred will be activated and will return an error message,
 * else ErrorOccurred will not be activated and a null message will be returned.
 **/

public class Response {

    public String errorMessage;
    public boolean ErrorOccurred;

    public Response()
    {
    }
    public Response(String msg){
        errorMessage = msg;
        ErrorOccurred = errorMessage!=null;
    }

}
