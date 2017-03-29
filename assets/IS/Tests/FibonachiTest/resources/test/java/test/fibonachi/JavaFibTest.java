package test.fibonachi;

import junit.framework.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.ps.test.WmTestCase;


@SuppressWarnings("unused")
public class JavaFibTest extends WmTestCase {
	
	
	public static final String IS_TEST_HOST = "localhost";
	
	public static final int IS_TEST_PORT = 5555;
	
	public static final String IS_USER = "Administrator";

	public static final String IS_PASSWORD = "manage";
	
	public JavaFibTest(){
		super(IS_TEST_HOST, IS_TEST_PORT, IS_USER, IS_PASSWORD);
	}
	
	
	
	
	public void testFibonachi() throws Exception{
		
		/*//Mock output
		IData outputMock = IDataFactory.create();
	    IDataCursor mockCursor = outputMock.getCursor();
	    IDataUtil.put(mockCursor, "value", "5"); 
	    mockCursor.destroy();
	
		
		
		mockService("pub.math", "subtractInts", outputMock, "test");*/
		
		
		//Input of the Service
		IData input = IDataFactory.create();
	    IDataCursor inputCursor = input.getCursor();
	    IDataUtil.put(inputCursor, "n", "5"); 
	    inputCursor.destroy();
	    
		IData output = invokeService("Fibonachi.services", "getFibunachiNumber", input);
		
		//Output of the service
		IDataCursor outputCursor = output.getCursor();
	    String result = IDataUtil.getString(outputCursor, "fibonachiNumber"); 
	    inputCursor.destroy();
	    
	    assertEquals("5", result);	
	}
	
	
	public void testFibonachiException(){
		
		//Input of the Service
		IData input = IDataFactory.create();
	    IDataCursor inputCursor = input.getCursor();
	    IDataUtil.put(inputCursor, "n", "-1"); 
	    inputCursor.destroy();
	    
		IData output;
		try {
			output = invokeService("Fibonachi.services", "getFibunachiNumber", input);
			fail("Exception should occur.");
		} catch (com.wm.lang.flow.FlowException e) {

		} catch (Exception e1){
			
		}
	}
	
	
	
	
	
	

}
