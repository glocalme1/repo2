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
import com.onewifi.util.HibernateUtil;

/**
 * Home object for domain model class Statuscodemaster.
 * @see com.onewifi.beans.Statuscodemaster
 * @author Hibernate Tools
 */
public class StatuscodemasterHome {

    private static final Log log = LogFactory.getLog(StatuscodemasterHome.class);

	private static SessionFactory sessionFactory = HibernateUtil.getInstance();
	
	private static StatuscodemasterHome statuscodemasterHome = getInstance();

	private static Session currentSession;

    private static Transaction currentTransaction;
	
	public static StatuscodemasterHome getInstance() {
		if(statuscodemasterHome==null) {
			synchronized(StatuscodemasterHome.class) {
				if(statuscodemasterHome==null) {
					statuscodemasterHome = new StatuscodemasterHome();
				}
			}
		}
		return statuscodemasterHome;
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
    
    public void persist(Statuscodemaster transientInstance) {
        log.debug("persisting Statuscodemaster instance");
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
    
    public void attachDirty(Statuscodemaster instance) {
        log.debug("attaching dirty Statuscodemaster instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Statuscodemaster instance) {
        log.debug("attaching clean Statuscodemaster instance");
        try {
            currentSession.buildLockRequest(new org.hibernate.LockOptions(LockMode.NONE)).lock(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Statuscodemaster persistentInstance) {
        log.debug("deleting Statuscodemaster instance");
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
    
    public Statuscodemaster merge(Statuscodemaster detachedInstance) {
        log.debug("merging Statuscodemaster instance");
        try {
			beginTransaction();
            Statuscodemaster result = (Statuscodemaster) currentSession
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
    
    public Statuscodemaster findById( java.lang.String id) {
        log.debug("getting Statuscodemaster instance with id: " + id);
        try {
			beginTransaction();
            Statuscodemaster instance = (Statuscodemaster) currentSession
                    .get("com.onewifi.beans.Statuscodemaster", id);
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
    
    public List<Statuscodemaster> findByExample(Statuscodemaster instance) {
        log.debug("finding Statuscodemaster instance by example");
        try {
			beginTransaction();
            List<Statuscodemaster> results = (List<Statuscodemaster>) currentSession
                    .createCriteria("com.onewifi.beans.Statuscodemaster")
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
}

