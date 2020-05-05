/**
 * 
 */
package caf.war.TestCAFTraining;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import com.webmethods.caf.faces.annotations.DTManagedBean;
import com.webmethods.caf.faces.annotations.BeanType;

/**
 * @author Administrator
 *
 */
@ManagedBean(name = "TestCAFTraining")
@ApplicationScoped
@DTManagedBean(displayName = "TestCAFTraining", beanType = BeanType.APPLICATION)
public class TestCAFTraining extends com.webmethods.caf.faces.bean.BaseApplicationBean 
{
	public TestCAFTraining()
	{
		super();
		setCategoryName( "CafApplication" );
		setSubCategoryName( "TestCAFTraining" );
	}
}