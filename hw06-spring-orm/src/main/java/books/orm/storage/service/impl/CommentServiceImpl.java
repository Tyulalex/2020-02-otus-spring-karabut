package books.orm.storage.service.impl;

import books.orm.storage.db.repository.BookRepositoryJpa;
import books.orm.storage.db.repository.CommentRepositoryJpa;
import books.orm.storage.service.BookFormatterService;
import books.orm.storage.service.CommentService;
import books.orm.storage.service.exception.CommentServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepositoryJpa commentRepositoryJpa;
    private final BookFormatterService bookFormatterService;
    private final BookRepositoryJpa bookRepositoryJpa;

    @Override
    @Transactional(readOnly = true)
    public String findCommentById(long id) {
        try {
            val comment = commentRepositoryJpa.findById(id);
            return comment.isPresent() ? comment.get().toString() : bookFormatterService.noItemString("comment", id);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findCommentsByBookId(long bookId) {
        try {
            val book = bookRepositoryJpa.findById(bookId);
            if (book.isPresent()) {
                val comments = book.get().getComments();
                return bookFormatterService.commentsToString(comments);
            }
            return bookFormatterService.noItemString("book", bookId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        try {
            val comment = commentRepositoryJpa.findById(commentId);
            comment.ifPresent(commentRepositoryJpa::delete);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void updateComment(long id, String comment) {
        try {
            val comm = commentRepositoryJpa.findById(id);
            comm.ifPresent(c -> c.setComment(comment));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }
}


