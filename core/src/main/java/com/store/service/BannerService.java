package com.store.service;

import com.store.domain.banner.BannerDO;
import com.store.domain.banner.BannerType;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by laizy on 2017/6/14.
 */
@Service
public class BannerService extends BaseIndexService<BannerDO> {

    @Transactional
    public List<BannerDO> listByType(BannerType type) {
        return listByIndexAsc(Restrictions.eq("type", type.name()));
    }

}
