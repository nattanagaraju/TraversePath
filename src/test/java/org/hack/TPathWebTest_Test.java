package org.hack;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tlog.TPath;

import com.sun.jersey.api.view.Viewable;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpServletRequest.class, TPath.class})
public class TPathWebTest_Test {

	TPathWeb svc = null;
	
	@Before
	public void setUp() throws Exception {
		svc = new TPathWeb();
		PowerMockito.mockStatic(TPath.class);
		svc.request = PowerMockito.mock(HttpServletRequest.class);
	}

	@After
	public void tearDown() throws Exception {
		svc = null;
	}

	@Test
	public void testGetPage() {
		Viewable view = svc.getPage();
		Assert.assertEquals("/view.jsp", view.getTemplateName());
	}

	@Test
	public void testGetUserDetails() {
		PowerMockito.when(TPath.getThreadVal()).thenReturn(new HashMap<String, Object>());
		Viewable view = svc.getUserDetails("raju@yahoo.com");
		Assert.assertEquals("/view.jsp", view.getTemplateName());
	}

}
