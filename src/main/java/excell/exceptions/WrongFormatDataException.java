package excell.exceptions;

/**
 * Created by dokuchaev on 06.12.16.
 */
public class WrongFormatDataException extends Exception{
    public WrongFormatDataException(){
        super();
    }

    public WrongFormatDataException(String message){
        super(message);
    }
}
