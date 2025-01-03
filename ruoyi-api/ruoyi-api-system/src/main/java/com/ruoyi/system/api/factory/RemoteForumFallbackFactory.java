package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteForumService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.api.model.StatusDTO;
import com.ruoyi.system.api.model.SysUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteForumFallbackFactory implements FallbackFactory<RemoteForumService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteForumFallbackFactory.class);

    @Override
    public RemoteForumService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteForumService()
        {

            @Override
            public R<Object> getPost(Long id, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }

            @Override
            public R<Object> getComment(Long commentId, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> updateComment(StatusDTO statusDTO, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> updatePost(StatusDTO statusDTO, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }

            @Override
            public R<String> getPostContent(Long id, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }

            @Override
            public R<String> getCommentContent(Long id, String source) {
                return R.fail("调用失败:" + throwable.getMessage());
            }
        };
    }
}
