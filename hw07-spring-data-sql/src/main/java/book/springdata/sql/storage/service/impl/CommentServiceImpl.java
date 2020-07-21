package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.repository.BookRepository;
import book.springdata.sql.storage.repository.CommentRepository;
import book.springdata.sql.storage.service.BookFormatterService;
import book.springdata.sql.storage.service.CommentService;
import book.springdata.sql.storage.service.exception.CommentServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookFormatterService bookFormatterService;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public String findCommentById(long id) {
        try {
            val comment = commentRepository.findById(id);
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
            val book = bookRepository.findById(bookId);
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
            val comment = commentRepository.findById(commentId);
            comment.ifPresent(commentRepository::delete);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }

    @Override
    @Transactional
    public void updateComment(long id, String comment) {
        try {
            val comm = commentRepository.findById(id);
            comm.ifPresent(c -> c.setComment(comment));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new CommentServiceException(ex);
        }
    }
}
