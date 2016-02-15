package com.iceapp.x;

import java.io.File;

public class Util {

	public static File getInstallationDirectory(){
        String root = AppProperties.getInstance().getInstallDir();
        if(root == null)
            root = System.getProperty(Constants.install_dir);
        
        File rootDir = new File(root);
        return rootDir;
    }

}
