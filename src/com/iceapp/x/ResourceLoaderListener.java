package com.iceapp.x;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoaderListener implements ServletContextListener {

    private final static Logger logger = LoggerFactory.getLogger(ResourceLoaderListener.class);
	private String contextPath;
	private String catalinaHome;

    @Override
    public void contextInitialized (ServletContextEvent sce) {
        logger.info("Initializin Floating Server resources.");

        ServletContext servletContext = sce.getServletContext();
        
        contextPath = servletContext.getContextPath();
        catalinaHome = System.getProperty(ServerConstants.CATALINA_HOME);
        
        /*
         * Load system properties
         */
        Enumeration<String> attributeNames = servletContext.getInitParameterNames();
        while (attributeNames.hasMoreElements()) {
            String attrbName = attributeNames.nextElement();
            Object attrbVal = servletContext.getInitParameter(attrbName);

            if (attrbVal instanceof String) {
                logger.info("Setting property " + attrbName + " : " + attrbVal);
                System.setProperty(attrbName, (String) attrbVal);
            }
        }

        // Set install.dir property
        String installationDirCanonicalPath = getInstallationDirCanonicalPath();
        logger.info("Setting property : installDir : " + installationDirCanonicalPath);
        
        /**
         * set license properties
         */
        AppProperties nlicenseProperties = AppProperties.getInstance();
        nlicenseProperties.setInstallerName("FloatingServer");
        nlicenseProperties.setInstallDir(installationDirCanonicalPath);
        nlicenseProperties.setLicenseDir("license");
        nlicenseProperties.setOsName(System.getProperty(Constants.OS_NAME_PROPERTY));
        nlicenseProperties.setOsArch(System.getProperty(Constants.OS_ARCH_PROPERTY));
        //nlicenseProperties.setLicensePort(arg0);
        nlicenseProperties.setInstallerDescription(System.getProperty(Constants.PRODUCT_DESCRIPTION));;
        nlicenseProperties.setInstallerVersion(System.getProperty(Constants.PRODUCT_VERSION));
        nlicenseProperties.setUserName(System.getProperty(Constants.USER_NAME_PROPERTY));
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Closing Floating Server resources.");
    }

    private String getInstallationDirCanonicalPath(){
        File webapp = new File(catalinaHome, "webapps");
        File installDir = new File(webapp, contextPath);
        
        String canonicalPath = null;
        try {
            canonicalPath = installDir.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = installDir.getAbsolutePath();
            logger.error("Error in getting canonical path. So, using absolutepath : " + canonicalPath , e);
        }
        
        return canonicalPath;
    }
}
