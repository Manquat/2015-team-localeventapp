package ch.epfl.sweng.evento.RestApi;

/**
 * Created by joachimmuth on 16.10.15.
 */
public class RestException extends Exception {

    public RestException(){}

    public RestException(String message){

        super(message);
    }

    public RestException(Throwable cause){

        super(cause);
    }

    public RestException(String message, Throwable cause){

        super(message, cause);
    }

}

