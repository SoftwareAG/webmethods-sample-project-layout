/**
 * 
 */
package caf.war.TestCAFTraining.sampleportlet;

/**
 * @author Administrator
 *
 */

import javax.portlet.PortletPreferences;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.webmethods.caf.faces.annotations.ExpireWithPageFlow;
import com.webmethods.caf.faces.annotations.DTManagedBean;
import com.webmethods.caf.faces.annotations.BeanType;

@ManagedBean(name = "SamplePortlet")
@SessionScoped
@ExpireWithPageFlow
@DTManagedBean(displayName = "SamplePortlet", beanType = BeanType.PORTLET)
public class SamplePortlet  extends   com.webmethods.caf.faces.bean.BaseFacesPreferencesBean {

	private transient caf.war.TestCAFTraining.TestCAFTraining testCAFTraining = null;
	/**
	 * List of portlet preference names
	 */
	public static final String[] PREFERENCES_NAMES = new String[] {
		"OrderId"
	};
	
	/**
	 * Create new preferences bean with list of preference names
	 */
	public SamplePortlet() {
		super(PREFERENCES_NAMES);
	}
	
	/**
	 * Call this method in order to persist
	 * Portlet preferences
	 */
	public void storePreferences() throws Exception {
		updatePreferences();
		PortletPreferences preferences = getPreferences();
		preferences.store();
	}

	public caf.war.TestCAFTraining.TestCAFTraining getTestCAFTraining()  {
		if (testCAFTraining == null) {
		    testCAFTraining = (caf.war.TestCAFTraining.TestCAFTraining)resolveExpression("#{TestCAFTraining}");
		}
		return testCAFTraining;
	}

	/**
	 * The algorithm for this 'smart' preference getter is:
	 * 1) Check the Request Map (skip this step if it isn't a 'smart' preference)
	 * 2) Check the Member variable
	 * 3) Fall back to the PortletPreferences
	 */
	public String getOrderId() throws Exception {
		return (String) getPreferenceValue("OrderId", String.class);
	}

	/**
	 * Invoke {@link #storePreferences} to persist these changes
	 */
	public void setOrderId(String orderId) throws Exception {
		setPreferenceValue("OrderId", orderId);
	}
}