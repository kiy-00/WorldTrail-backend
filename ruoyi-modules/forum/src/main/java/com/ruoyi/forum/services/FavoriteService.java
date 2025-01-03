package com.ruoyi.forum.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.forum.entities.Favorite;
import com.ruoyi.forum.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// 收藏服务实现类
@Service
public class FavoriteService  {
    @Autowired
    FavoriteMapper favoriteMapper;

    public Integer postFavorite(Long postId) {
        Favorite favorite = new Favorite();
        Long uid= SecurityUtils.getUserId();
        favorite.setUserId(uid);
        favorite.setPostId(postId);
        favoriteMapper.insert(favorite);
        return 200;
    }


    public Integer deleteFavorite(Long id) {
        Favorite favorite = favoriteMapper.selectById(id);
        Long uid= SecurityUtils.getUserId();
        if(favorite.getUserId().equals(uid)){
            favoriteMapper.deleteById(id);
            return 200;
        }
        return 0;
    }


    public List<Favorite> listFavoriteByUser() {
        Long userId= SecurityUtils.getUserId();
        return favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId,userId));
    }
}
