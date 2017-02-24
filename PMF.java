package db;

/*
 * This class creates a static instance of PersistanceManagerFactory that is to be shared 
 * among all classes that access it.
 */

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}
    /** 
     * Gets an instance of PersistenceManagerFactory
     * @returns A static PersistenceManagerFactory instance
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance; // static instance so that every other class accessing can get same copy
    }
}