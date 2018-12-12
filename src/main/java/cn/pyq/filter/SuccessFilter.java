package cn.pyq.filter;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @desc:
 * @author: pyq
 * @date: 2018-12-10 13:31
 */
public class SuccessFilter extends FormAuthenticationFilter {
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		System.out.println(getSuccessUrl());
		WebUtils.issueRedirect(request,response,getSuccessUrl(),null,true);
	}
}

