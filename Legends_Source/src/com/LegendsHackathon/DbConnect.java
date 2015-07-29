package src.com.LegendsHackathon;


import java.sql.*; 
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class DbConnect {


		   protected Connection connection;
		   protected ResultSet resultSet;
		   protected ResultSetMetaData metaData;
		   protected Statement stmt;
		   protected Statement updateStmt;

		   protected int rowCount;
		   protected int columnCount;

		   protected String queryString;
		   protected String queryMessage;
		   protected String updateString;
		   int execUpdate=0;
		   

		   SimpleDateFormat dform = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		
		   @SuppressWarnings("unused")
		public DbConnect( String driver, String host, String username, String password )
		      throws Exception
		   {
		      try
		      { 
		    	
		         Class.forName( driver );
		         // conn = DriverManager.getConnection ("jdbc:oracle:oci:@","","");
		         //connection = DriverManager.getConnection( host, username, password );
		         connection = DriverManager.getConnection( host, username, password );
		         
		         ////////
		      // Create Oracle DatabaseMetaData object 
		 		DatabaseMetaData meta = connection.getMetaData (); 
		 	
		 		// gets driver info: 
		 	/*
		 		System.out.println("\n=============\nDatabase Product Name is ... " + 
		 		meta.getDatabaseProductName()); 
		 		*/
		 
		      }
		      catch ( Exception exc )
		      {
		    	  System.out.println("Exception In DbConnect:"+exc);
		         throw new Exception( "Unable to create DB connection. " +
		            "driver: " + driver + "; " +
		            "host: " + host + "; " +
		            "username: " + username + "; " +
		         // "password: " + password + "; " +
		            "message: " + exc.getMessage() );
		      }
		   }

		   /**
		    * runQuery: opens a cursor and puts all data retrieved in queryResults (an ArrayList)
		    * returns: ArrayList containing query results
		    */
		   public ArrayList<String[]> runQuery ( ) throws Exception
		   {
			 
		      ArrayList<String[]> queryResults = new ArrayList<String[]>();
		      System.out.println("queryString:"+queryString);
		      openCursor ( );
		      setColumnCount();
		      String resultRow[];
		      rowCount = 0;
		      try
		      {  
		    	  	 //long usedMem = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory());
		    	
		         while ( resultSet.next() )
		         {
		            
		        	 rowCount++;
		            resultRow = new String [ columnCount ];            
		           
		            for ( int columnNo = 0; columnNo < columnCount; columnNo++ )
		            {
		               resultRow[columnNo] = getValue( columnNo );
		            }
		            queryResults.add ( resultRow );
		         }
		         setQueryMessage ( "runQuery processed " + columnCount + " columns in " + rowCount + " rows."  );
		      }
		      catch ( Exception exc )
		      {  
		    	 setQueryMessage ( exc.getMessage() );
		         throw new Exception ("Exception in Excecuting the query:"+exc);		         
		      }finally {
		    	  closeCursor();
		      }
		        //long usedMemFinal = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory());
		        return queryResults;
			  
		   }
		  /** execUpdateQuery: execte the DB Update
		    * returns: int updateCount
		    */
		   public int execUpdateQuery ( ) throws Exception
		   {
			   //System.out.println("UpdateString:"+updateString);
		      try
		      {  
		    	  dbUpdate ( );
		      }
		      catch ( Exception exc )
		      {  
		    	 setQueryMessage ( exc.getMessage() );
		         throw new Exception ("Exception in Excecuting the query:"+exc);		         
		      }finally {
		    	  closeUpdateConnection();
		      }
		        //long usedMemFinal = (Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory());
		        return execUpdate;
			  
		   }

		   public void setQueryString( String queryString )
		   {
		      this.queryString = queryString;
		   }
		   public String getQueryString( )
		   {
		      return queryString;
		   }
		   
		   public void setUpdateString( String updateString )
		   {
		      this.updateString = updateString;
		   }
		   public String getUpdateString( )
		   {
		      return updateString;
		   }
		   
		   protected void setQueryMessage( String queryMessage )
		   {
		      this.queryMessage = queryMessage;
		   }
		   public String getQueryMessage( )
		   {
		      return queryMessage;
		   }

		   public void setColumnCount() throws Exception
		   {
		      columnCount = metaData.getColumnCount();
		   }
		   public int getColumnCount()
		   {
		      return columnCount;
		   }

		   public int getRowCount()
		   {
		      return rowCount;
		   }

		   protected void openCursor( ) throws Exception
		   {
		      stmt = connection.createStatement();
		      resultSet = stmt.executeQuery( queryString );
		      metaData  = resultSet.getMetaData();
		   }
		   protected void dbUpdate( ) throws Exception
		   {
			   updateStmt = connection.createStatement();
			   connection.setAutoCommit(false);
		       execUpdate = updateStmt.executeUpdate( updateString );
		       connection.commit();  
		   }

		   public String getValue( int col ) throws Exception
		   {
		      String colType = getType( col );
		      String s = "";

		      if ( colType.equalsIgnoreCase("LongChar") )
		      {
		         InputStream is = resultSet.getAsciiStream(col+1);
		         int ch = ' ';
		         while ( (ch = is.read()) != -1 )
		         {
		           s += (char) ch;
		         }
		         is.close();
		      }
		      else if ( colType.equalsIgnoreCase("Date") || colType.equalsIgnoreCase("DateTime") )
		      {
		         s =  resultSet.getDate( col+1 ).toString() ;
		      }
		      else if ( colType.equalsIgnoreCase("Number") )
		      {
		         try
		         {
		            if ( getClassName( col ).equals( "java.math.BigDecimal" ) )
		            {
		               s = resultSet.getString( col+1 );
		            }
		            else
		            {
		               s = getClassName( col );
		            // s = Double.parseDouble( resultSet.getString( col+1 ))+"";
		            }
		         }
		         catch ( Exception e )
		         {
		            s = "0";
		            System.exit(5);
		         }
		      }
		      else
		      {
		         s = resultSet.getString( col+1 );
		       }
		       return s;
		   }

		   public String getType( int col ) throws Exception
		   {
		      return metaData.getColumnTypeName( col+1 );
		   }

		   public String getClassName( int col ) throws Exception
		   {
		      return metaData.getColumnClassName( col+1 );
		   }

		   public String getColumnName( int col ) throws Exception
		   {
		      return metaData.getColumnName( col+1 );
		   }
		   protected void closeCursor( ) 
		   {
		      try{
		    	  resultSet.close();
		    	  stmt.close();		    				
		      }catch (Exception e){
		    	  System.out.println(" Execption in closing the DB:"+e.getMessage());
		      }
		      
		   }
		   protected void closeUpdateConnection( ) 
		   {
		      try{
		    	  updateStmt.close();		    				
		      }catch (Exception e){
		    	  System.out.println(" Execption in closing the closeUpdateConnection:"+e.getMessage());
		      }
		      
		   }
		   
		   public void closeConnection( ) 
		   {
		      try{
		    	connection.close();			
		      }catch (Exception e){
		    	  System.out.println(" Execption in closing the DB:"+e.getMessage());
		      }
		      
		   }
		   /*
		   public void setSchema(String schema){
			   
		   }
		   */
	}


