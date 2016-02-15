package com.iceapp.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountStorageManager {

	private final static Logger logger = LoggerFactory.getLogger(AccountStorageManager.class);

	private static String DEFAULT_ADMIN_LOGIN = "administrator";
	private static String DEFAULT_PASSWD = "admin@4050#";
	private static AccountStorageManager instance;

	private Map<String, String> usernamePasswordCache;
	private File userCredentialFile;
	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	private AccountStorageManager(){
		usernamePasswordCache = new HashMap<String, String>();
		usernamePasswordCache.put(DEFAULT_ADMIN_LOGIN, getHash(DEFAULT_PASSWD));

		File installationDirectory = Util.getInstallationDirectory();
		File accessDir = new File(installationDirectory, Constants.ACCESS_DIR_NAME);
		userCredentialFile = new File(accessDir, ServerConstants.LOGIN_ACCOUNT_FILENAME);
		loadCredentials();
	}

	public static AccountStorageManager getInstance() {
		if (instance == null) {
			synchronized (AccountStorageManager.class) {
				if (instance == null) {
					instance = new AccountStorageManager();
				}
			}
		}

		return instance;
	}

	protected void loadCredentials(){
		if (userCredentialFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(userCredentialFile));
				String line = reader.readLine();

				if (line != null) {
					line = line.trim();
					String account[] = line.split(":");
					String userName = account[0];
					String passwd = account[1];
					usernamePasswordCache.put(userName, passwd);
				}
			} catch (Exception e) {
				logger.error("Error reading Credentials file.", e);
			} finally {
				if(reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("some error in closing file reader");
					}
			}
		}
	}

	protected boolean storeCredentials() throws IOException {
		File tempFile = new File(userCredentialFile.getAbsolutePath() + ".temp");

		if (userCredentialFile.exists()) {
			boolean successful = userCredentialFile.renameTo(tempFile);
			if (!successful)
				successful = userCredentialFile.renameTo(tempFile);
			if (!successful)
				throw new IOException("Unable to write Credentials file");
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(userCredentialFile));
			for(String user : usernamePasswordCache.keySet()){
				writer.write(user + ":" + usernamePasswordCache.get(user));
			}

			tempFile.delete();
			logger.info("Credentials stored successfully.");
			return true;
		} catch (IOException e) {
			tempFile.renameTo(userCredentialFile);
			throw new IOException("Unable to write Credentials file");
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				logger.error("Error closing writer.");
			}
		}
	}

	public String getPassword(String username){
		rwl.readLock().lock();
		String password = null;
		try {
			password = usernamePasswordCache.get(username);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwl.readLock().unlock();
		}
		return password;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws LicenseException
	 */
	public boolean storePassword(String username, String password){

		rwl.writeLock().lock();
		String oldPasswordHash = usernamePasswordCache.get(username);
		usernamePasswordCache.put(username, getHash(password));

		boolean success = false;
		try {
			storeCredentials();
			success = true;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			// restore old password
			usernamePasswordCache.put(username, oldPasswordHash);
		} finally {
			rwl.writeLock().unlock();
		}

		return success;
	}

	public String getHash(String text){
		Md5Hash md5 = new Md5Hash(text);
		return md5.toHex();
	}
}
