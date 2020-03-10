package vitaly.mosin.repository.exceptions;

public enum ExceptionResponseCode {

    NOT_FOUND(404),
    FAILED_SAVE_DATA(410),
    FAILED_GET_DATA(411),
    FAILED_DELETE_CONTACT_FROM_DB(412),
    FAILED_CREATE_CONTACT(413),
    FAILED_UPDATE_CONTACT(414),
    WRONG_DATA_TYPE(415),
    ENTITY_ALREADY_EXIST(416);

    private int code;

    ExceptionResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
