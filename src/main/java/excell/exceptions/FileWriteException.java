package excell.exceptions;

/**
 * Created by dokuchaev on 06.12.16.
 */
public class FileWriteException extends Exception{
    public FileWriteException(){
        super();
    }

    public FileWriteException(String message){
        super(message);
    }

    public FileWriteException(String message, Throwable cause){
        super(message, cause);
    }
}
