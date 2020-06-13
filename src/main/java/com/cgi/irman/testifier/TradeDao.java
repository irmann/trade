package com.cgi.irman.testifier;

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
public class TradeDao {

    private HibernateTemplate hibernateTemplate;

    @Transactional(readOnly = false)
    public void save(TradeModel tradeModel) {
        hibernateTemplate.save(tradeModel);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<Long> findMaxVersion(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.add(Restrictions.eq("tradeId", tradeId))
                .setProjection(Projections.projectionList()
                        .add(groupProperty("tradeId"), "tradeId")
                        .add(Projections.max("tradeVersion"), "tradeVersion"));
        criteria.setResultTransformer(Transformers.aliasToBean(TradeModel.class));
        List<?> list = hibernateTemplate.findByCriteria(criteria);
        return list.stream().map(trade ->
                ((TradeModel)trade).getTradeVersion())
                .findFirst();
    }

    @Transactional(readOnly = false)
    public void delete(List<?> tradeList) {
        if (!tradeList.isEmpty()) {
            hibernateTemplate.deleteAll(tradeList);
        }
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

}
