package simpleTestPackage.implementation;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-01-18 12:25:11 CET
// -----( ON-HOST: sles11.softwareag.com

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
// --- <<IS-END-IMPORTS>> ---

public final class filepolling

{
	// ---( internal utility methods )---

	final static filepolling _instance = new filepolling();

	static filepolling _newInstance() { return new filepolling(); }

	static filepolling _cast(Object o) { return (filepolling)o; }

	// ---( server methods )---




	public static final void checkErrorDirectory (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(checkErrorDirectory)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inboundFile
		// [o] field:0:required errorDirectory
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	inboundFile = IDataUtil.getString( pipelineCursor, "inboundFile");
		String	fileName = IDataUtil.getString( pipelineCursor, "fileName" );
		
		File inbound = new File(inboundFile);
		File filepollingdir = inbound.getParentFile().getParentFile();
		File errorDir = new File(filepollingdir, "error");
		
		// pipeline
		IDataUtil.put( pipelineCursor, "errorDirectory", errorDir.getAbsolutePath() );
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void writeInvalidRecordsToFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(writeInvalidRecordsToFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fileName
		// [i] field:0:required dir
		// [i] recref:1:required invalidRecordList simpleTestPackage.documents.flatfile:invalidRecord
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	fileName = IDataUtil.getString( pipelineCursor, "fileName" );
		String	dir = IDataUtil.getString( pipelineCursor, "dir" );
		
		File f = new File(dir, fileName);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(f));
			IData[]	invalidRecord = IDataUtil.getIDataArray( pipelineCursor, "invalidRecordList" );
			if ( invalidRecord != null)
			{
				for ( int i = 0; i < invalidRecord.length; i++ )
				{
					IDataCursor invalidRecordCursor = invalidRecord[i].getCursor();
					String	position = IDataUtil.getString( invalidRecordCursor, "position" );
					String	msg = IDataUtil.getString( invalidRecordCursor, "msg" );
					invalidRecordCursor.destroy();
		
					out.write(position + "," + msg);
					out.newLine();
				}
			}
			
		} catch( IOException ioe ) {
			throw new ServiceException("could not write file to path " + fileName + ": " + ioe);
		} finally {
			if( out != null ) {
				try {
					out.close();					
				} catch( IOException ioe ) {
					// ignore
				}
			}
		}
		// --- <<IS-END>> ---

                
	}
}

