package wp.threedcorner.service.impl;

import org.springframework.stereotype.Service;
import wp.threedcorner.model.Comment;
import wp.threedcorner.model.Project;
import wp.threedcorner.repository.CommentRepository;
import wp.threedcorner.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void deleteComment(String username, Long id) {
        Comment c=commentRepository.getById(id);
        if(c.getUser().getUsername().equals(username))
            commentRepository.delete(c);
    }
}
