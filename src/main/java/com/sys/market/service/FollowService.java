package com.sys.market.service;

import com.sys.market.entity.Follow;
import com.sys.market.dto.criteria.FollowSearchCriteria;
import com.sys.market.mapper.FollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowMapper followMapper;

    public List<Follow> findFollowList(FollowSearchCriteria search) {
        return followMapper.selectFollowList(search);
    }

    public int findFollowCount(FollowSearchCriteria criteria) {
        return followMapper.selectFollowCount(criteria);
    }

    // follower 팔로우 한 유저, userId 팔로우 당한 유저
    public boolean followExists(String followerId, String userId) {
        FollowSearchCriteria criteria = new FollowSearchCriteria();
        criteria.setFollowerId(followerId);
        criteria.setUserId(userId);

        int count = followMapper.selectFollowCount(criteria);
        return count > 0;
    }

    public void addFollow(String followerId, String userId) {
        followMapper.insertFollow(followerId, userId);
    }

    public void removeFollow(String followerId, String userId) {
        followMapper.deleteFollow(followerId, userId);
    }
}
