package com.wm.ps.serviceMock.samples;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.ps.test.WmTestCase;
import com.wm.ps.test.WmTestSuiteUtils;

public class DuplicateCheckTest extends WmTestCase
{
  public void testDupCheckCatchBlock() throws Exception
  {    
    IData input = IDataFactory.create(new Object[][]{
        {"lienType", "1"},
        {"borrowerSSN", "111-11-1111"},
        {"propertyAddress", "937 S Meyer"},
        {"propertyZip", "85701"}
        }); 
    
    String exceptionText = "Bad argument";
    mockService("wmServiceMockSamples.data.services", "getPotentialDuplicates", new IllegalArgumentException(exceptionText));
    try
    {
      invokeService("wmServiceMockSamples.services", "getDuplicateLoans", input);
      assertFalse(true); //Control getting here means failure   
    }
    catch (Exception e)
    {
      assertTrue(e.getMessage().endsWith(exceptionText));
    }
  }
  
  public void testDupCheckSucessNoResults() throws Exception
  {
    IData input = IDataFactory.create(new Object[][]{
        {"lienType", "1"},
        {"borrowerSSN", "111-11-1111"},
        {"propertyAddress", "937 S Meyer"},
        {"propertyZip", "85701"}
        });
    
    IData mockOutput = IDataFactory.create(new Object[][]{
        {"getPotentialDuplicatesOutput" , IDataFactory.create(new Object[][]{{"results"}, new IData[0]})}
    });
   
    mockService("wmServiceMockSamples.data.services", "getPotentialDuplicates", mockOutput);
    IData output = invokeService("wmServiceMockSamples.services", "getDuplicateLoans", input);
    IDataCursor outCursor = output.getCursor();
    IData response = IDataUtil.getIData(outCursor, "response");
    IDataCursor responseCursor = response.getCursor();
    String creationTime = IDataUtil.getString(responseCursor, "@creationTime");
    assertNotNull(creationTime);
    assertEquals(28, creationTime.length());
    assertNull(IDataUtil.getIDataArray(responseCursor, "duplicateLoans"));
  }
  
  public void testDupCheckSucessWithResults() throws Exception
  {
    IData input = IDataFactory.create(new Object[][]{
    {"lienType", "1"},
    {"borrowerSSN", "111-11-1111"},
    {"propertyAddress", "937 S Meyer"},
    {"propertyZip", "85701"}
    });
    
    IData mockOutput = WmTestSuiteUtils.getIDataFromFile("resources/test/data/mockDupCheckOutputResults.xml");
    mockService("wmServiceMockSamples.data.services", "getPotentialDuplicates", mockOutput);
    IData output = invokeService("wmServiceMockSamples.services", "getDuplicateLoans", input);
    
    IDataCursor outCursor = output.getCursor();
    IData response = IDataUtil.getIData(outCursor, "response");
    IDataCursor responseCursor = response.getCursor();
    String creationTime = IDataUtil.getString(responseCursor, "@creationTime");
    assertNotNull(creationTime);
    assertEquals(28, creationTime.length());
    IData[] duplicateLoans = IDataUtil.getIDataArray(responseCursor, "duplicateLoans");
    assertEquals(duplicateLoans.length, 1);
  }
  
  /**
   * Example that shows how to mock a service when executing a service without using the webMethods 
   * client API. This one uses the httpunit classes to post XML.
   * @throws Exception
   */
  /*public void testDupCheckSucessWithXMLResults() throws Exception
  {
    IData mockOutput = WmTestSuiteUtils.getIDataFromFile("resources/test/data/mockDupCheckOutputResults.xml");
    mockService("wmServiceMockSamples.data.services", "getPotentialDuplicates", mockOutput);
    WebResponse response = httpPost("wmServiceMockSamples.services", "getDuplicateLoansXML", new FileInputStream("resources/test/data/dupCheckSuccessWithXMLInput.xml"), "text/xml");
    Document doc = response.getDOM();
    Element root = doc.getDocumentElement();
    assertEquals("duplicateCheckResponse", root.getNodeName());
    NodeList list = root.getElementsByTagName("duplicateLoans");
    assertEquals(list.getLength(), 1);
  }*/
}