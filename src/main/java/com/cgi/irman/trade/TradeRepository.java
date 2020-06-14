package com.cgi.irman.trade;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hibernate.criterion.Projections.groupProperty;

@Service
public class TradeRepository {

    private HibernateTemplate hibernateTemplate;

    @Transactional(readOnly = false)
    public void save(TradeModel tradeModel) {
        hibernateTemplate.save(tradeModel);
    }

    @Transactional(readOnly = true)
    public Optional<Long> findMaxVersion(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.add(Restrictions.eq("tradeId", tradeId))
                .setProjection(Projections.projectionList()
                        .add(groupProperty("tradeId"), "tradeId")
                        .add(Projections.max("tradeVersion"), "tradeVersion"));
        criteria.setResultTransformer(Transformers.aliasToBean(TradeModel.class));
        List<?> list = hibernateTemplate.findByCriteria(criteria);
        return list.stream().map(trade ->
                ((TradeModel) trade).getTradeVersion())
                .findFirst();
    }

    @Transactional(readOnly = false)
    public void delete(List<?> tradeList) {
        if (!tradeList.isEmpty()) {
            hibernateTemplate.deleteAll(tradeList);
        }
    }

    @Transactional(readOnly = true)
    public Optional<?> findById(String id) {
        return hibernateTemplate.find("from Trade where Id = ?", id).stream().findFirst();
    }

    public List<TradeModel> findByTradeId(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.add(Restrictions.eq("tradeId", tradeId));
        return (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
    }

    public List<TradeModel> findAll() {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        return (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
