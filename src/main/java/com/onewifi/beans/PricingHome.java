package com.onewifi.beans;
// Generated Nov 5, 2015 9:32:52 AM by Hibernate Tools 4.3.1.Final


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import java.util.Date;
import org.hibernate.criterion.Restrictions;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import com.onewifi.util.HibernateUtil;

/**
 * Home object for domain model class Pricing.
 * @see com.onewifi.beans.Pricing
 * @author Hibernate Tools
 */
public class PricingHome {

    private static final Log log = LogFactory.getLog(PricingHome.class);

	private static SessionFactory sessionFactory = HibernateUtil.getInstance();
	
	private static PricingHome pricingHome = getInstance();

	private static Session currentSession;

    private static Transaction currentTransaction;
	
	public static PricingHome getInstance() {
		if(pricingHome==null) {
			synchronized(PricingHome.class) { 
				if(pricingHome==null) {
					pricingHome = new PricingHome();
				}
			}
		}
		return pricingHome;
	}
	
	public Session openCurrentSession() {
        currentSession = getSessionFactory().getCurrentSession();
        return currentSession;
    }

    public Transaction beginTransaction() {
		if(currentTransaction==null || (currentTransaction!=null && !currentTransaction.isActive())) {
			openCurrentSession();
			currentTransaction = currentSession.beginTransaction();
		}
        return currentTransaction;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void commitTransaction() {
       currentTransaction.commit();
    }
	
    private static SessionFactory getSessionFactory() {
		if(sessionFactory==null) {
			synchronized(SessionFactory.class) { 
        		System.out.println("Initializing SessionFactory");
				Configuration configuration = new Configuration().configure();
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
				sessionFactory = configuration.buildSessionFactory(builder.build());		
			}
		}
        return sessionFactory;		
    }
	
    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
    
    public void persist(Pricing transientInstance) {
        log.debug("persisting Pricing instance");
        try {
			beginTransaction();
            currentSession.persist(transientInstance);
			commitTransaction();
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Pricing instance) {
        log.debug("attaching dirty Pricing instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Pricing instance) {
        log.debug("attaching clean Pricing instance");
        try {
            currentSession.buildLockRequest(new org.hibernate.LockOptions(LockMode.NONE)).lock(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Pricing persistentInstance) {
        log.debug("deleting Pricing instance");
        try {
			beginTransaction();
            currentSession.delete(persistentInstance);
			commitTransaction();
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Pricing merge(Pricing detachedInstance) {
        log.debug("merging Pricing instance");
        try {
			beginTransaction();
            Pricing result = (Pricing) currentSession
                    .merge(detachedInstance);
			commitTransaction();
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Pricing findById( com.onewifi.beans.PricingId id) {
        log.debug("getting Pricing instance with id: " + id);
        try {
			beginTransaction();
            Pricing instance = (Pricing) currentSession
                    .get("com.onewifi.beans.Pricing", id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
			commitTransaction();
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List<Pricing> findByExample(Pricing instance) {
        log.debug("finding Pricing instance by example");
        try {
			beginTransaction();
            List<Pricing> results = (List<Pricing>) currentSession
                    .createCriteria("com.onewifi.beans.Pricing")
                    .add( create(instance) )
            .list();
			commitTransaction();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
	
	 public List<Pricing> findLatestPricingDetails() {
        log.debug("finding Pricing instances by latest date");
        try {
			beginTransaction();
            Criteria criteria = currentSession.createCriteria("com.onewifi.beans.Pricing");
			criteria.add(Restrictions.sqlRestriction("ValidDate = (select max(validdate) from pricing where validdate <= curdate())"));
			List<Pricing> results = (List<Pricing>)	criteria.list();
			commitTransaction();
            System.out.println("find by latest date successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
	
	public List<Pricing> findByDate(Date validDate) {
        log.debug("finding Pricing instances by date");
        try {
			beginTransaction();
            Criteria criteria = currentSession.createCriteria("com.onewifi.beans.Pricing");
			criteria.add(Restrictions.sqlRestriction("ValidDate = DATE('"+validDate+"')"));
			List<Pricing> results = (List<Pricing>)	criteria.list();
			commitTransaction();
            System.out.println("find by date successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

