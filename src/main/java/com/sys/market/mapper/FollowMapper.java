package com.sys.market.mapper;

import com.sys.market.entity.Follow;
import com.sys.market.dto.criteria.FollowSearchCriteria;

import java.util.List;

public interface FollowMapper {
    List<Follow> selectFollowList(FollowSearchCriteria criteria);

    int selectFollowCount(FollowSearchCriteria criteria);

    void insertFollow(String followerId, String userId);

    void deleteFollow(String followerId, String userId);
}
