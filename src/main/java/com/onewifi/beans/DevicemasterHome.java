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
import org.hibernate.criterion.Restrictions;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import com.onewifi.util.HibernateUtil;

/**
 * Home object for domain model class Devicemaster.
 * @see com.onewifi.beans.Devicemaster
 * @author Hibernate Tools
 */
public class DevicemasterHome {

    private static final Log log = LogFactory.getLog(DevicemasterHome.class);

	private static SessionFactory sessionFactory = HibernateUtil.getInstance();
	
	private static DevicemasterHome devicemasterHome = getInstance();
	
	private static Session currentSession;

    private static Transaction currentTransaction;
	
	public static DevicemasterHome getInstance() {
		if(devicemasterHome==null) {
			synchronized(DevicemasterHome.class) { 
				if(devicemasterHome==null) {
					devicemasterHome = new DevicemasterHome();
				}
			}
		}
		return devicemasterHome;
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
    
    public void persist(Devicemaster transientInstance) {
        log.debug("persisting Devicemaster instance");
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
    
    public void attachDirty(Devicemaster instance) {
        log.debug("attaching dirty Devicemaster instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Devicemaster instance) {
        log.debug("attaching clean Devicemaster instance");
        try {
            currentSession.buildLockRequest(new org.hibernate.LockOptions(LockMode.NONE)).lock(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Devicemaster persistentInstance) {
        log.debug("deleting Devicemaster instance");
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
    
    public Devicemaster merge(Devicemaster detachedInstance) {
        log.debug("merging Devicemaster instance");
        try {
			beginTransaction();
            Devicemaster result = (Devicemaster) currentSession
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
    
    public Devicemaster findById( java.lang.String id) {
        log.debug("getting Devicemaster instance with id: " + id);
        try {
			beginTransaction();
            Devicemaster instance = (Devicemaster) currentSession
                    .get("com.onewifi.beans.Devicemaster", id);
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
    
    public List<Devicemaster> findByExample(Devicemaster instance) {
        log.debug("finding Devicemaster instance by example");
        try {
			beginTransaction();
            List<Devicemaster> results = (List<Devicemaster>) currentSession
                    .createCriteria("com.onewifi.beans.Devicemaster")
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
	
    public List<Devicemaster> findByStatus(Statuscodemaster statuscodemaster) {
        log.debug("finding Devicemaster instance by example");
        try { 
			beginTransaction();
			Criteria criteria = currentSession.createCriteria("com.onewifi.beans.Devicemaster");
			criteria.add(Restrictions.sqlRestriction("this_.StatusCode = '"+statuscodemaster.getStatusCode()+"'"));
			List<Devicemaster> results = (List<Devicemaster>)	criteria.list();             
			commitTransaction();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 	
}

