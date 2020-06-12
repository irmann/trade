package com.cgi.irman.testifier;

import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class GreetingDao {
    private HibernateTemplate hibernateTemplate;
    @Transactional(readOnly=true)
    public List<?> findContent(String name) {
        List<?> greetingList = hibernateTemplate.find("from Greeting where content like '%?%'", name);
        return greetingList;
    }

    @Transactional(readOnly=false)
    public void deleteGreetings(List<?> greetingList) {
        if (!greetingList.isEmpty()) {
            hibernateTemplate.deleteAll(greetingList);
        }
    }

    @Transactional(readOnly=false)
    public void createGreeting(final String content){
        Greeting greeting = (Greeting)hibernateTemplate.execute(new HibernateCallback() {
            public Greeting doInHibernate(Session session) throws HibernateException {
                return new Greeting(content);
            }
        });
    }

    @Transactional(readOnly=false)
    public void saveGreeting(Greeting greeting){
        hibernateTemplate.save(greeting);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
