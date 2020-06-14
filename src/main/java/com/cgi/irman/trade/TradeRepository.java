package com.cgi.irman.trade;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
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

    //TODO use findLastVersionByTradId to get last version of a trade
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
    public Optional<?> findById(BigInteger id) {
        return hibernateTemplate.find("from TradeModel where Id=?0", id).stream().findFirst();
    }

    public List<TradeModel> findByTradeId(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.add(Restrictions.eq("tradeId", tradeId));
        return (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
    }

    public List<TradeModel> findAll() {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.addOrder(Order.desc("tradeId"))
                .addOrder( Order.desc("tradeVersion") )
               ;
        return (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<TradeModel> findLastVersionByTradId(String tradeId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);
        criteria.add(Restrictions.eq("tradeId", tradeId))
                .setProjection(Projections.projectionList()
                        .add(groupProperty("tradeId"), "tradeId")
                        .add(Projections.max("tradeVersion"), "tradeVersion"));
        criteria.setResultTransformer(Transformers.aliasToBean(TradeModel.class));
        List<TradeModel> list = (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
        return list.stream().findFirst();
    }


    public List<TradeModel> findAllForMaxMaturityDate(Date date) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TradeModel.class);

        criteria.add(Restrictions.lt("maturityDate", date))
                .setProjection(Projections.projectionList()
                        .add(groupProperty("tradeId"), "tradeId")
                        .add(Projections.max("maturityDate"), "maturityDate"));
        criteria.setResultTransformer(Transformers.aliasToBean(TradeModel.class));
        return (List<TradeModel>) hibernateTemplate.findByCriteria(criteria);
    }
}
