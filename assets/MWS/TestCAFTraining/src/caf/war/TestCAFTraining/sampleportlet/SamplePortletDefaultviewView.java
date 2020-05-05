/**
 * 
 */
package caf.war.TestCAFTraining.sampleportlet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.webmethods.caf.faces.annotations.ExpireWithPageFlow;
import com.webmethods.caf.faces.annotations.DTManagedBean;
import com.webmethods.caf.faces.annotations.BeanType;

/**
 * @author Administrator
 *
 */

@ManagedBean(name = "SamplePortletDefaultviewView")
@SessionScoped
@ExpireWithPageFlow
@DTManagedBean(displayName = "SamplePortlet/default", beanType = BeanType.PAGE)
public class SamplePortletDefaultviewView  extends   com.webmethods.caf.faces.bean.BasePageBean {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 *
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions. See Sun docs
	 * for <a href=http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/version.html> 
	 * details. </a>
	 */
	private static final long serialVersionUID = 1L;
	private static final String[][] INITIALIZE_PROPERTY_BINDINGS = new String[][] {
	};
	private transient caf.war.TestCAFTraining.sampleportlet.SamplePortlet samplePortlet = null;
	private java.lang.String firstName;
	private java.lang.String dateOfBirth;
	private java.lang.String activeEmployee;
	private java.lang.String[] preferredColours = null;
	private static final String[][] PREFERREDCOLOURS_PROPERTY_BINDINGS = new String[][] {
	};

	/**
	 * Initialize page
	 */
	public String initialize() {
		try {
		    resolveDataBinding(INITIALIZE_PROPERTY_BINDINGS, null, "initialize", true, false);
		} catch (Exception e) {
			error(e);
			log(e);
		}
		return null;	
	}

	public caf.war.TestCAFTraining.sampleportlet.SamplePortlet getSamplePortlet()  {
		if (samplePortlet == null) {
		    samplePortlet = (caf.war.TestCAFTraining.sampleportlet.SamplePortlet)resolveExpression("#{SamplePortlet}");
		}
		return samplePortlet;
	}

	public java.lang.String getFirstName()  {
		
		return firstName;
	}

	public void setFirstName(java.lang.String firstName)  {
		this.firstName = firstName;
	}

	public String submitFormData() {
	    // TODO: implement java method
	    //business logic
		return null;
	}

	public java.lang.String getDateOfBirth()  {
		
		return dateOfBirth;
	}

	public void setDateOfBirth(java.lang.String dateOfBirth)  {
		this.dateOfBirth = dateOfBirth;
	}

	public java.lang.String getActiveEmployee()  {
		
		return activeEmployee;
	}

	public void setActiveEmployee(java.lang.String activeEmployee)  {
		this.activeEmployee = activeEmployee;
	}

	public java.lang.String[] getPreferredColours()  {
		if (preferredColours == null) {
			//TODO: create/set default value here
		}
	
	    resolveDataBinding(PREFERREDCOLOURS_PROPERTY_BINDINGS, preferredColours, "preferredColours", false, false);
		return preferredColours;
	}

	public void setPreferredColours(java.lang.String[] preferredColours)  {
		this.preferredColours = preferredColours;
	}
	
}