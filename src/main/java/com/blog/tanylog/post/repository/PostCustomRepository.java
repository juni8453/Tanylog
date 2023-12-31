package com.blog.tanylog.post.repository;

import com.blog.tanylog.config.security.UserContext;
import com.blog.tanylog.post.controller.dto.request.PageSearch;
import com.blog.tanylog.post.domain.Post;
import java.util.List;

public interface PostCustomRepository {

  List<Post> readAll(PageSearch pageSearch);

  List<Post> readNoOffset(PageSearch pageSearch);

  List<Post> readMyPosts(PageSearch pageSearch, UserContext userContext);

}
