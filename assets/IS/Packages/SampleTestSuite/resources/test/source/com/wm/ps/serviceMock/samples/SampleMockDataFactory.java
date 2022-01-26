package com.wm.ps.serviceMock.samples;

import com.wm.app.b2b.server.BaseService;
import com.wm.app.b2b.server.invoke.ServiceStatus;
import com.wm.data.IData;
import com.wm.data.IDataFactory;
import com.wm.ps.serviceMock.MockDataFactory;

public class SampleMockDataFactory implements MockDataFactory
{
  private static final long serialVersionUID = 2L;
  
  public IData createData(BaseService baseService, IData pipeline, ServiceStatus serviceStatus)
  {
    IData[] results = new IData[]{IDataFactory.create(new Object[][]{
        {"originationSource","W"},
          {"bizType","RT"},
          {"lockExpirationDate","20050427"},
          {"floatLoanIndicator","Y"},
          {"uwFinalDecisionCode","0"},
          {"uwDecisionExpiryDate","20050427"},
          {"canDate","20050427"},
          {"loanCloseStatusType","T"},
          {"fileReceivedAtRocDate","20050221"},
          {"loanReadyToFundIndicator","P"},
          {"regisDate","20051221"},
          {"loanSubmitToUwDate","20050427"},
          {"loanNumber","0000000001"},
          {"branch","TOTAL ADVANTEDGE LLC          "},
          {"underwritingDecisionCode","0"},
          {"underwritingDecisionExpirationDate","20050427"},
          {"lockDate","20051220"},
          {"lockIndicator","Y"},
          {"tmoLoanStageCode","3"},
          {"tmoLoanStageDate","20050427"},
          {"product","C30       "},
          {"borrowerFirstName",".          "},
          {"borrowerLastName","XX             "},
          {"propertyAddress","937 S MEYER                   "},
          {"propertyCity","TUCSON                    "},
          {"propertyState","AZ"},
          {"propertyZip","85701"}
      })};
    
    IData output = IDataFactory.create(new Object[][]{{"results", results}});
    return IDataFactory.create(new Object[][]{{"getPotentialDuplicatesOutput", output}});
  }
}