package com.store.service;

import com.store.domain.RecommendDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by laizy on 2017/6/13.
 */
@Service
public class RecommendService extends BaseIndexService<RecommendDO> {

    @Transactional
    public void deleteByProductId(Long id) {
        String sql = "delete from recommend where product_id=?";
        executeSql(sql, id);
    }
}
