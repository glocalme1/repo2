package com.onewifi.util;
 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
 
public class HibernateUtil {
 
    private static SessionFactory sessionFactory = getInstance();
 
    public static SessionFactory getInstance() {
        try {
           if(sessionFactory==null) {
				synchronized(SessionFactory.class) { 
					if(sessionFactory==null) {
						System.out.println("Initializing HibernateUtil SessionFactory");
						Configuration configuration = new Configuration().configure();
						StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
						sessionFactory = configuration.buildSessionFactory(builder.build());
						//sessionFactory.openSession();						
					}
				}			
		   }
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
		return sessionFactory;		
    }
 
    public static void shutdown() {
    	// Close caches and connection pools
    	sessionFactory.close();
    }
 
}