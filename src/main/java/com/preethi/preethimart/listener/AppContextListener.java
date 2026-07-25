package com.preethi.preethimart.listener;

import com.preethi.preethimart.util.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("PreethiMart Application starting up. Initializing database and connection pool...");
        try {
            DBConnectionManager.getInstance();
            logger.info("PreethiMart successfully initialized and ready to serve requests.");
        } catch (Exception e) {
            logger.error("Failed to initialize database during context startup: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("PreethiMart Application shutting down. Releasing HikariCP resources...");
        DBConnectionManager.getInstance().shutdown();
    }
}
