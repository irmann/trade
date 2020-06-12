package com.cgi.irman.testifier;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.criterion.Projections.groupProperty;

public class TradeDao {

    private HibernateTemplate hibernateTemplate;

    @Transactional(readOnly=false)
    public void save(TradeModel tradeModel){
        hibernateTemplate.save(tradeModel);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public TradeModel findMaxVersion(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.setProjection(Projections.projectionList()
                .add(groupProperty("tradeId"))
                .add(Projections.max("tradeVersion")));
        List<?> list = hibernateTemplate.findByCriteria(criteria);
        if (list.size() > 0 )
            return (TradeModel) list.get(0);
        return null;
    }
}
