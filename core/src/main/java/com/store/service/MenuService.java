package com.store.service;

import com.store.domain.MenuDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangmj on 2017/6/15.
 */
@Service
public class MenuService extends BaseIndexService<MenuDO> {

    @Transactional
    public Integer getMaxIndex(){
        Integer lastIndex = (Integer)getSession().createQuery("select max(menu.index) from MenuDO menu")
                .uniqueResult();
        return null == lastIndex ? 0 : lastIndex;
    }
}
