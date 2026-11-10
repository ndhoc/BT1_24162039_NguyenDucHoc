package lt_web.dao;

public class DataAccessException extends RuntimeException {
    public DataAccessException(Throwable cause) { super("Không thể truy cập cơ sở dữ liệu.", cause); }
}
