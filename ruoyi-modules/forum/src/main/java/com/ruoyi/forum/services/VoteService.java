package com.ruoyi.forum.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.forum.entities.Vote;
import com.ruoyi.forum.mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// 投票服务实现类
@Service
public class VoteService {
    @Autowired
    VoteMapper voteMapper;
    public Integer vote(Vote vote) {
        Long uid= SecurityUtils.getUserId();
        vote.setUserId(uid);
        LambdaQueryWrapper<Vote> query = new LambdaQueryWrapper<>();
        Vote oldVote = voteMapper.selectOne(query.eq(Vote::getPostId, vote.getPostId())
                .eq(Vote::getUserId, uid));
        if (oldVote == null && vote.getUpvote()!=null) {
            voteMapper.insert(vote);
        }
        else if (oldVote != null && vote.getUpvote()==null) {
            voteMapper.deleteById(oldVote.getId());
        }
        else if (oldVote != null) {
            oldVote.setUpvote(vote.getUpvote());
            voteMapper.updateById(oldVote);
        }
        return 200;
    }


    public Integer isPostVoted(Long postId) {
        Long userId= SecurityUtils.getUserId();
        Vote vote=voteMapper.selectOne(new LambdaQueryWrapper<Vote>().eq(Vote::getPostId, postId)
                .eq(Vote::getUserId, userId));
        if(vote==null)
            return 0;
        else
            return vote.getUpvote()?1:-1;
    }


    public Integer getVoteCount(Long postId) {
        LambdaQueryWrapper<Vote> upvoteQuery = new LambdaQueryWrapper<>();
        upvoteQuery.eq(Vote::getPostId, postId).eq(Vote::getUpvote, true);
        LambdaQueryWrapper<Vote> downvoteQuery = new LambdaQueryWrapper<>();
        downvoteQuery.eq(Vote::getPostId, postId).eq(Vote::getUpvote, false);
        return Math.toIntExact(voteMapper.selectCount(upvoteQuery)
                - voteMapper.selectCount(downvoteQuery));
    }
}
