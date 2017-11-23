package cn.gao.exception;

/**
 * Created by tarena on 2016/10/8.
 */
public class CustomException extends Exception {
    public CustomException(String message){
        super(message);
        this.message =message;
    }
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
