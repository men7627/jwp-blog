package techcourse.myblog.exception;

public class DuplicatedUserException extends IllegalArgumentException {
    public DuplicatedUserException() {
        super("중복된 e-mail입니다.");
    }
}
