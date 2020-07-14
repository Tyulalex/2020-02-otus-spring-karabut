package books.orm.storage.service;

public interface CommentService {

    String findCommentById(long id);

    String findCommentsByBookId(long bookId);

    void deleteComment(long commentId);

    void updateComment(long id, String comment);
}
