package com.store.service;

import com.core.dao.RecordDO;
import com.google.common.collect.Maps;
import com.store.domain.RecommendDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public void batchSave(Collection<RecommendDO> recommends) {
        Integer maxIndex = getMaxIndex();
        maxIndex = maxIndex == null ? 1 : maxIndex;
        for (RecommendDO recommendDO : recommends) {
            recommendDO.setIndex(maxIndex++);
        }
        super.batchSave(recommends);
    }

    @Transactional
    public Collection<Long> truncateExists(Collection<Long> productIds) {
        List<Long> pIds = getSession().createQuery("select product.id as pid from RecommendDO where id in (:ids)").setParameterList("ids", productIds).list();
        productIds.removeAll(pIds);
        return productIds;
    }
}
