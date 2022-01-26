package wmServiceMockSamples;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2006-05-31 12:05:35 EDT
// -----( ON-HOST: 5DWKV81.ncmc.ncm.pvt

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.ArrayList;
// --- <<IS-END-IMPORTS>> ---

public final class services

{
	// ---( internal utility methods )---

	final static services _instance = new services();

	static services _newInstance() { return new services(); }

	static services _cast(Object o) { return (services)o; }

	// ---( server methods )---




	public static final void filterDuplicates (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(filterDuplicates)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:1:required potentialDuplicates
		// [i] - field:0:required originationSource
		// [i] - field:0:required bizType
		// [i] - field:0:required lockExpirationDate
		// [i] - field:0:required floatLoanIndicator
		// [i] - field:0:required uwFinalDecisionCode
		// [i] - field:0:required uwDecisionExpiryDate
		// [i] - field:0:required canDate
		// [i] - field:0:required loanCloseStatusType
		// [i] - field:0:required fileReceivedAtRocDate
		// [i] - field:0:required loanReadyToFundIndicator
		// [i] - field:0:required regisDate
		// [i] - field:0:required loanSubmitToUwDate
		// [i] - field:0:required loanNumber
		// [i] - field:0:required branch
		// [i] - field:0:required underwritingDecisionCode
		// [i] - field:0:required underwritingDecisionExpirationDate
		// [i] - field:0:required lockDate
		// [i] - field:0:required lockIndicator
		// [i] - field:0:required tmoLoanStageCode
		// [i] - field:0:required tmoLoanStageDate
		// [i] - field:0:required product
		// [i] - field:0:required borrowerFirstName
		// [i] - field:0:required borrowerLastName
		// [i] - field:0:required propertyAddress
		// [i] - field:0:required propertyCity
		// [i] - field:0:required propertyState
		// [i] - field:0:required propertyZip
		// [o] record:1:required duplicates
		// [o] - field:0:required relevanceStatus
		// [o] - field:0:required loanNumber
		// [o] - field:0:required branch
		// [o] - field:0:required underwritingDecisionCode
		// [o] - field:0:required underwritingDecisionExpirationDate
		// [o] - field:0:required lockDate
		// [o] - field:0:required lockIndicator
		// [o] - field:0:required tmoLoanStageCode
		// [o] - field:0:required tmoLoanStageDate
		// [o] - field:0:required product
		// [o] - field:0:required borrowerFirstName
		// [o] - field:0:required borrowerLastName
		// [o] - field:0:required propertyAddress
		// [o] - field:0:required propertyCity
		// [o] - field:0:required propertyState
		// [o] - field:0:required propertyZip
		IDataCursor cursor = pipeline.getCursor();
		IData[] potentialDuplicates = IDataUtil.getIDataArray(cursor, "potentialDuplicates");
		ArrayList duplicateList = new ArrayList();
		if (potentialDuplicates != null && potentialDuplicates.length > 0)
		{
		  for (int i=0; i < potentialDuplicates.length; i++)
		  {
		    String loanType = checkLoanType(potentialDuplicates[i]);
		    if (loanType != null)
		    {
		      IDataCursor dupCursor = potentialDuplicates[i].getCursor();
		      dupCursor.first();
		      dupCursor.insertBefore("relevanceStatus", loanType);
		      dupCursor.destroy();
		      duplicateList.add(potentialDuplicates[i]);
		    }
		  }
		}
		
		IData[] duplicates = null;
		
		if (duplicateList.size() > 0)
		{
		  duplicates = new IData[duplicateList.size()];
		  for (int i=0; i<duplicates.length; i++)
		  {
		    duplicates[i] = (IData)duplicateList.get(i);
		  }
		}
		IDataUtil.put(cursor, "duplicates", duplicates);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static final String relevantDuplicateLoan = "Relevant";
	static final String irrelevantDuplicateLoan = "Irrelevant";
	static final String correspondentBusinessUnit = "correspondent";
	static final String wholeBusinessUnit = "wholesale";
	static final String retailBusinessUnit = "retail";
	
	public static String checkLoanType(IData idata)
	{
	  IDataCursor cursor = idata.getCursor();
	  String originationSource = IDataUtil.getString(cursor, "originationSource");
	  String bizType = IDataUtil.getString(cursor, "bizType");
	  int lockExpirationDate = IDataUtil.getInt(cursor, "lockExpirationDate", 0);
	  String floatLoanIndicator = IDataUtil.getString(cursor, "floatLoanIndicator");
	  String uwFinalDecisionCode = IDataUtil.getString(cursor, "uwFinalDecisionCode");
	  int uwDecisionExpiryDate = IDataUtil.getInt(cursor, "uwDecisionExpiryDate", 0);
	  int canDate = IDataUtil.getInt(cursor, "canDate", 0);
	  String loanCloseStatusType = IDataUtil.getString(cursor, "loanCloseStatusType");
	  int fileReceivedAtRocDate = IDataUtil.getInt(cursor, "fileReceivedAtRocDate", 0);
	  String loanReadyToFundIndicator = IDataUtil.getString(cursor, "loanReadyToFundIndicator");
	  int regisDate = IDataUtil.getInt(cursor, "regisDate", 0);
	  int loanSubmitToUwDate = IDataUtil.getInt(cursor, "loanSubmitToUwDate", 0);
	  cursor.destroy();
	  
	
	  String businessUnit = null;
	  String loanType = null;
	  GregorianCalendar today = new GregorianCalendar();
	  today.set(Calendar.HOUR, 0);
	  today.set(Calendar.MINUTE, 0);
	  today.set(Calendar.SECOND, 0);
	  today.set(Calendar.MILLISECOND, 0);
	  
	  if (originationSource.equals("F"))
	  {
	    businessUnit = correspondentBusinessUnit;
	  }
	  else
	  {
	    if (bizType.equals("WH"))
	    {
	      businessUnit = wholeBusinessUnit;
	    }
	    else if (bizType.equals("RT") || bizType.equals("AF"))
	    {
	      businessUnit = retailBusinessUnit;
	    }
	  }
	  
	  if (lockExpirationDate > 0 && correspondentBusinessUnit.equals(businessUnit))
	  {
	    loanType = relevantDuplicateLoan; 
	  }
	  else if ("Y".equals(floatLoanIndicator) && (wholeBusinessUnit.equals(businessUnit) || retailBusinessUnit.equals(businessUnit)))
	  {
	    loanType = relevantDuplicateLoan;
	  }
	  else
	  {
	    if ("2".equals(uwFinalDecisionCode) || "3".equals(uwFinalDecisionCode))
	    {
	      loanType = relevantDuplicateLoan;
	    }
	    else if ("1".equals(uwFinalDecisionCode) && uwDecisionExpiryDate > 0)
	    {
	      if (dateDiffInDays(intToDate(uwDecisionExpiryDate), today) < 120)
	      {
	        loanType = relevantDuplicateLoan;
	      }
	    }
	    else
	    {
	      if (canDate > 0)
	      {
	        loanType = irrelevantDuplicateLoan;
	      }
	      else
	      {
	        if (loanCloseStatusType != null && loanCloseStatusType.trim().length() != 0 && correspondentBusinessUnit.equals(businessUnit))
	        {
	          if (fileReceivedAtRocDate > 0)
	          {
	            if (dateDiffInDays(intToDate(fileReceivedAtRocDate), today) > 60 && loanReadyToFundIndicator.equals("Y"))
	            {
	              loanType = irrelevantDuplicateLoan;
	            }
	            else
	            {
	              loanType = relevantDuplicateLoan;
	            }
	          }
	          else if (regisDate > 0)
	          {
	            if (dateDiffInDays(intToDate(regisDate), today) > 60)
	            {
	              loanType = irrelevantDuplicateLoan;
	            }
	            else
	            {
	              loanType = relevantDuplicateLoan;
	            }
	          }
	        }
	        else
	        {
	          if (wholeBusinessUnit.equals(businessUnit) || retailBusinessUnit.equals(businessUnit))
	          {
	            if (loanSubmitToUwDate > 0 && (uwFinalDecisionCode.trim().length() == 0 || Integer.parseInt(uwFinalDecisionCode) > 0))
	            {
	              if (dateDiffInDays(intToDate(loanSubmitToUwDate), today) > 30)
	              {
	                loanType = irrelevantDuplicateLoan;
	              }
	            }
	            else if (regisDate > 0)
	            {
	              if (dateDiffInDays(intToDate(regisDate), today) > 60)
	              {
	                if ((loanSubmitToUwDate == 0 && wholeBusinessUnit.equals(businessUnit)) || retailBusinessUnit.equals(businessUnit)) 
	                {
	                  loanType = irrelevantDuplicateLoan;  
	                }
	              }
	              else
	              {
	                loanType = relevantDuplicateLoan;
	              }
	            }
	          }
	          else
	          {
	            loanType = relevantDuplicateLoan;
	          }
	        }
	      }
	    }
	  }
	  return loanType;  
	}
	
	private static GregorianCalendar intToDate(int intDate)
	{
	  String strDate = String.valueOf(intDate);
	  int year = 0;
	  int month = 0;
	  int day = 0;
	  if (strDate.length() == 8)
	  {
	    year = Integer.parseInt(strDate.substring(0,4));
	    month = Integer.parseInt(strDate.substring(4,6));
	    day = Integer.parseInt(strDate.substring(6));
	  }
	  
	  return new GregorianCalendar(year, month - 1, day);  
	}
	
	private static int dateDiffInDays(GregorianCalendar oldDate, GregorianCalendar newDate)
	{
	  return (int)((newDate.getTimeInMillis() - oldDate.getTimeInMillis())/(24*3600*1000));
	}
	// --- <<IS-END-SHARED>> ---
}

