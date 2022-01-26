package com.wm.ps.serviceMock.samples;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.wm.ps.test.SetupException;
import com.wm.ps.test.WmTestSuite;

import junit.framework.Test;

public class WmSampleTestSuite extends WmTestSuite {

	public static Test suite() throws Exception
	{
		WmSampleTestSuite suite = new WmSampleTestSuite();
		suite.setName("WmServiceMockSamples");
		return suite;
	}

	public WmSampleTestSuite() throws SetupException {
		super();
	}

	@Override
	protected String getSetupFilename() {
		String path = getClass().getResource(".").toString();
		int indexOf = path.indexOf("/resources/test");
		String substring = path.substring(0,indexOf);
		try {
			File file = new File(new URI(substring));
			String path1 = file.toString();
			System.setProperty("user.dir",path1);
			return path1+"/resources/test/setup/wmTestSuite.xml";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
	}
  /*
	@Override
	protected String getServerName(WmTestCase testcase) {
		return "localhost";
	}
	
	@Override
	protected int getPortNumber(WmTestCase testcase) {
		return 5555;
	}
	
	@Override
	protected String getUsername(WmTestCase testcase) {
		return "Administrator";
	}
	
	@Override
	protected String getPassword(WmTestCase testcase) {
		return "manage";
	}
	
	*/
}
