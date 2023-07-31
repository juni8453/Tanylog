package com.blog.tanylog.post.service;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.global.exception.domain.OtherUserDeleteException;
import com.blog.tanylog.global.exception.domain.PostNotFound;
import com.blog.tanylog.global.exception.domain.UserNotFound;
import com.blog.tanylog.post.controller.dto.request.PostSaveRequest;
import com.blog.tanylog.post.domain.Post;
import com.blog.tanylog.post.repository.PostRepository;
import com.blog.tanylog.user.domain.User;
import com.blog.tanylog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public void save(UserContext userContext, PostSaveRequest request) {
    Long userId = userContext.getSessionUser().getUserId();
    User findUser = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);

    String title = request.getTitle();
    String content = request.getContent();
    boolean isDeleted = request.isDeleted();

    Post post = request.toEntity(title, content, isDeleted);
    post.addUser(findUser);

    postRepository.save(post);
  }

  @Transactional
  public void delete(Long postId, UserContext userContext) {
    Long userId = userContext.getSessionUser().getUserId();
    User loginUser = userRepository.findById(userId)
        .orElseThrow(UserNotFound::new);

    Post findPost = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    // 다른 유저라면,
    if (!findPost.checkUser(loginUser)) {
      throw new OtherUserDeleteException();
    }

    postRepository.delete(findPost);
  }
}