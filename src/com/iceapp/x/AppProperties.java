package com.iceapp.x;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * These properties are set only first time(i.e start up) and then used, the values of this 
 * properties once set can't be changed.
 * 
 */
public class AppProperties {
    
    private final static Logger logger = LoggerFactory.getLogger(AppProperties.class);
    
    
    /*
     * Singleton instance
     */
    private static AppProperties instance;
    
    private String osName; 
    
    private String osArch;
    
    private String userName;

    private String port;
    
    private String installDir;
    
    private String licenseDir;
    
    private String installerName;
    
    private String installerDesc;
    
    private String installerVersion;
    
    /*
     * Map containing resource
     * Key :  resource name
     * Value :  resource Value.
     * 
     * This map will be used to keep mislaneous resources, All important resources are kept
     *  in their separate field like contextName
     */
    private Map<String, Object> resourceMap;
    
    
    /**
     * Constructor 
     */
    private AppProperties() {
        resourceMap = new HashMap<String, Object>();
    }
    
    /**
     * Singleton instance
     * @return
     * @throws LicenseException
     */
    public synchronized static AppProperties getInstance() {

        if (instance == null) {
            instance = new AppProperties();
        }

        return instance;
    }
    
    
    public Object getLicenseProperty(String resourceName) {
        return resourceMap.get(resourceName);
    }

    public void settLicenseProperty(String resourceName, Object resourceVal) {
        if(!this.resourceMap.containsKey(resourceName)){
            this.resourceMap.put(resourceName, resourceVal);
        }
    }
    
    public void setOsName(String osName) {
        if(this.osName == null){
            logger.info("Setting os name : " + osName);
            this.osName = osName;
        }
    }
    
    public String getOsName() {
        logger.trace("Getting os name " + osName);
        return osName;
    }
    
    public String getOsArch() {
        logger.trace("Getting os arch " + osArch);
        return osArch;
    }
    
    public void setOsArch(String osArch) {
        if(this.osArch == null){
            logger.info("Setting osArch name : " + osArch);
            this.osArch = osArch;
        }
    }

    
    public String getLicensePort() {
        logger.trace("Getting property licensePort " + port);
        return port;
    }
    
    public void setLicensePort(String licensePort) {
        if(this.port == null){
            logger.info("Setting licensePort : " + licensePort);
            this.port = licensePort;
        }
    }
    
    public void setInstallDir(String installDir) {
        if(this.installDir == null){
            logger.info("Setting installDir:" + installDir);
            this.installDir = installDir;
        }
    }
    
    
    public String getInstallDir() {
        logger.trace("Getting property installDir " + installDir);
        return installDir;
    }
    
    
    public void setLicenseDir(String licenseDir) {
        if(this.licenseDir == null){
            logger.info("Setting licenseDir : " + licenseDir);
            this.licenseDir = licenseDir;
        }
    }
    
    public String getLicenseDir() {
        logger.trace("Getting property licenseDir " + licenseDir);
        return licenseDir;
    }
    
    public void setInstallerName(String installerName) {
        if(this.installerName == null){
            logger.info("Setting installerName : " + installerName);
            this.installerName = installerName;
        }
    }

    public String getInstallerName() {
        logger.trace("Getting property installerName" + installerName);
        return installerName;
    }
    
    public void setInstallerDescription(String installerDesc) {
        if(this.installerDesc == null){
            logger.trace("Setting installerDesc: " + installerDesc);
            this.installerDesc = installerDesc;
        }
    }

    public String getInstallerDescription() {
        logger.trace("Getting property installerDesc " + installerDesc);
        return installerDesc;
    }
    
    
    public void setInstallerVersion(String installerVersion) {
        if(this.installerVersion == null){
            logger.info("Setting installerVersion : " + installerVersion);
            this.installerVersion = installerVersion;
        }
    }
    
    public String getInstallerVersion() {
        logger.trace("Getting property installerVersion " + installerVersion);
        return installerVersion;
    }

    public void setUserName(String userName) {
        logger.trace("Setting userName : " + userName);
        if(this.userName == null)
            this.userName = userName;
    }
    
    public String getUserName() {
        logger.trace("Getting property userName " + userName);
        return userName;
    }
    
}
