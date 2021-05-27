package com.leo.spring.cloud.netflix.zuul.server;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/** This simple filter just adds a header called “Test” to the request – but of course, we can get as complex as we need
 * */

@Component
public class CustomZuulFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(CustomZuulFilter.class);
	
	 /**
     *  Zuul has four standard filter types:
     *  
     *  pre filters run before the request is routed.
     *  route filters can handle the actual routing of the request.
     *  post filters run after the request has been routed.
     *  error filters run if an error occurs in the course of handling the request.
     *  
     * We also support a "static" type for static responses see StaticResponseFilter.
     * Any filterType may be created or added and run by calling FilterProcessor.runFilters(type)
     * */
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 100;
	}
	
	@Override
    public boolean shouldFilter() {
    	// we could determine if filter applies based on RequestContext.getCurrentContext()
       return true;
    }
	
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("Test", "TestSample");
        
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        
        return null;
    }

}
