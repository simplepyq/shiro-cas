package cn.pyq.realms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: pyq
 * @date: 2018-12-10 9:03
 */
public class MyCasRealm extends CasRealm {

	private Logger log = LoggerFactory.getLogger(MyCasRealm.class);

	private TicketValidator ticketValidator;

	protected TicketValidator ensureTicketValidator() {
		if (ticketValidator == null)
			ticketValidator = createTicketValidator();
		return ticketValidator;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("开始认证");
		CasToken casToken = (CasToken) token;
		if (token == null)
			return null;
		String ticket = (String) casToken.getCredentials(); //获取ticket
		System.out.println(ticket);
		TicketValidator ticketValidator = ensureTicketValidator(); //生成ticket验证器
		try {
			Assertion casAssertion = ticketValidator.validate(ticket, getCasService());//获取客户端的地址
			AttributePrincipal casPrincipal = casAssertion.getPrincipal();//获取login的主体
			System.out.println(casPrincipal);
			String userId = casPrincipal.getName();
			System.out.println(userId);
			log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}", ticket, getCasServerUrlPrefix(), userId);
			Map<String, Object> attributes = casPrincipal.getAttributes();
			casToken.setUserId(userId);
			String rememberMeAttributeName = getRememberMeAttributeName();
			String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
			boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
			if (isRemembered)
				casToken.setRememberMe(true);
			/**  此处是封装用户信息
			 sUsr su = new sUsr();
			 su.setUsrCde(userId);
			 sUsr susr = isUsrService.findByCode(su);
			 AccessTokenInfo atInfo = new AccessTokenInfo();
			 atInfo.setUsrCde(userId);
			 //获取apikey
			 AccessTokenInfo ati = accessTokenInfoService.selectOneByObject(atInfo);
			 //构建ShiroUserAccount
			 ShiroUserAccount sua = new ShiroUserAccount(susr,ati);
			 */
			List<Object> principals = CollectionUtils.asList(userId, attributes);
			PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
			return new SimpleAuthenticationInfo(principalCollection, ticket);
		} catch (TicketValidationException e) {//ticket过期
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return super.doGetAuthorizationInfo(principals);
	}
}

