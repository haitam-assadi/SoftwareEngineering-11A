package ServiceLayer;

/**
 * this class represent the result of a call to a non-void function.
 * if an exception was thrown, ErrorOccurred will be activated, an error message will be returned and value will be null,
 * else ErrorOccurred will not be activated, error message will be null and a value of type T will be returned.
 **/

public class ResponseT<T> extends Response {

    public T value;
    public ResponseT(T _value){
        super();
        value = _value;
    }

    public ResponseT(T _value, boolean flag){
        super();
        value = _value;
    }
    public ResponseT(String msg){
        super(msg);
    }

    public T getValue(){
        return value;
    }
}
