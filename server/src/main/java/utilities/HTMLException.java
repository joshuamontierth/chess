package utilities;

public class HTMLException extends Exception{
    public HTMLException(String message,int errorCode) {

        super(message);
        this.errorCode = errorCode;
    }
    int errorCode;
    public int getErrorCode() {
        return errorCode;
    }

}
