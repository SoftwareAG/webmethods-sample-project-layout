package wmServiceMockSamples;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2006-06-02 13:18:19 EDT
// -----( ON-HOST: 5DWKV81.ncmc.ncm.pvt

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void createException (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(createException)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required message
		// [i] field:0:required exceptionClassName
		// [o] object:0:required exceptionObject
		IDataCursor cursor = pipeline.getCursor();
		String message = IDataUtil.getString(cursor, "message");
		String exceptionClassName = IDataUtil.getString(cursor, "exceptionClassName");
		try
		{
		  IDataUtil.put(cursor, "exceptionObject", Class.forName(exceptionClassName).getConstructor(new Class[]{String.class}).newInstance(new Object[]{message}));
		}
		catch (Exception e)
		{
		  throw new ServiceException(e);
		}
		finally
		{
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void getSessionId (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getSessionId)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required sessionId
		IDataCursor cursor = pipeline.getCursor();
		IDataUtil.put(cursor, "sessionId", Service.getSession().getSessionID());
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}

