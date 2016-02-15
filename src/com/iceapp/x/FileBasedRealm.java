package com.iceapp.x;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileBasedRealm extends AuthorizingRealm {
	private final static Logger logger = LoggerFactory.getLogger(FileBasedRealm.class);
	
	private final static String FILE_REALM = "FileBasedRealm";
	
	public static String ADMIN_ROLE = "administrator";

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		logger.debug("primaryPrincipal : "+ principal.getPrimaryPrincipal());
		Set<String> roles = new HashSet<String>(){
			{
				add(ADMIN_ROLE);
			}
		};
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		info.setRoles(roles); //fill in roles 
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if (token == null)
			return null;
		
		UsernamePasswordToken uToken = (UsernamePasswordToken) token;
		String userName = uToken.getUsername();
		
		AccountStorageManager asm = AccountStorageManager.getInstance();
		String storedPasswdHash = asm.getPassword(userName);
		AuthenticationInfo aInfo = new SimpleAuthenticationInfo(userName, storedPasswdHash, null, getName());
		return aInfo;
	}

	@Override
	public String getName() {
		return FILE_REALM;
	}

}
