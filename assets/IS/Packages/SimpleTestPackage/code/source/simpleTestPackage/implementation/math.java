package simpleTestPackage.implementation;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2012-09-25 12:07:36 CEST
// -----( ON-HOST: sles11.softwareag.com

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class math

{
	// ---( internal utility methods )---

	final static math _instance = new math();

	static math _newInstance() { return new math(); }

	static math _cast(Object o) { return (math)o; }

	// ---( server methods )---




	public static final void calculateFibonacci (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(calculateFibonacci)>> ---
		// @sigtype java 3.5
		// [i] field:0:required n
		// [o] field:0:required fib
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		Integer n = IDataUtil.getInt( pipelineCursor, "n",0 );
		pipelineCursor.destroy();
		
		int fib = fib(n);
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "fib", "" + fib );
		pipelineCursor_1.destroy();
		
		    
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static int fib(int n ) {
		if (n <= 1) return n;
	    else return fib(n-1) + fib(n-2);
	}
	// --- <<IS-END-SHARED>> ---
}

