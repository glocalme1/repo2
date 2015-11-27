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
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.onewifi.util.HibernateUtil;

/**
 * Home object for domain model class Retailerdevice.
 * @see com.onewifi.beans.Retailerdevice
 * @author Hibernate Tools
 */
public class RetailerdeviceHome {

    private static final Log log = LogFactory.getLog(RetailerdeviceHome.class);

	private static SessionFactory sessionFactory = HibernateUtil.getInstance();
	
	private static RetailerdeviceHome retailerdeviceHome = getInstance();	

	private static Session currentSession;

    private static Transaction currentTransaction;
	
	public static RetailerdeviceHome getInstance() {
		if(retailerdeviceHome==null) {
			synchronized(RetailerdeviceHome.class) { 
				if(retailerdeviceHome==null) {
					retailerdeviceHome = new RetailerdeviceHome();
				}
			}
		}
		return retailerdeviceHome;
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
    
    public void persist(Retailerdevice transientInstance) {
        log.debug("persisting Retailerdevice instance");
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
    
    public void attachDirty(Retailerdevice instance) {
        log.debug("attaching dirty Retailerdevice instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Retailerdevice instance) {
        log.debug("attaching clean Retailerdevice instance");
        try {
            currentSession.buildLockRequest(new org.hibernate.LockOptions(LockMode.NONE)).lock(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Retailerdevice persistentInstance) {
        log.debug("deleting Retailerdevice instance");
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
    
    public Retailerdevice merge(Retailerdevice detachedInstance) {
        log.debug("merging Retailerdevice instance");
        try {
			beginTransaction();
            Retailerdevice result = (Retailerdevice) currentSession
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
    
    public Retailerdevice findById( com.onewifi.beans.RetailerdeviceId id) {
        log.debug("getting Retailerdevice instance with id: " + id);
        try {
			beginTransaction();
            Retailerdevice instance = (Retailerdevice) currentSession
                    .get("com.onewifi.beans.Retailerdevice", id);
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
    
    public List<Retailerdevice> findByExample(Retailerdevice instance) {
        log.debug("finding Retailerdevice instance by example");
        try {
			beginTransaction();
            List<Retailerdevice> results = (List<Retailerdevice>) currentSession
                    .createCriteria("com.onewifi.beans.Retailerdevice")
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
	
    public List<Retailerdevice> findByDeviceSerialNo( String deviceSerialNo) {
        log.debug("getting Retailerdevice instance with DeviceSerialNo: " + deviceSerialNo);
        try {
			beginTransaction();
            Criteria criteria = currentSession.createCriteria("com.onewifi.beans.Retailerdevice");
			criteria.add(Restrictions.sqlRestriction("this_.DeviceSerialNo = '"+deviceSerialNo+"'"));
			List<Retailerdevice> results = (List<Retailerdevice>)	criteria.list();             
			commitTransaction();
			return results;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }	
}

