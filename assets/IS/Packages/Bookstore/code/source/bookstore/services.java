package bookstore;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-03-05 14:28:04 GMT
// -----( ON-HOST: 10.151.56.67

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class services

{
	// ---( internal utility methods )---

	final static services _instance = new services();

	static services _newInstance() { return new services(); }

	static services _cast(Object o) { return (services)o; }

	// ---( server methods )---




	public static final void addBookRating (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addBookRating)>> ---
		// @sigtype java 3.5
		// [i] field:0:required isbn
		// [i] record:0:required rating
		// [i] - recref:0:required rating bookstore.docs:ratingT
		// [o] field:0:required result
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	isbn = IDataUtil.getString( pipelineCursor, "isbn" );
		
		// rating
		String result = "ok";
		IData	rating = IDataUtil.getIData( pipelineCursor, "rating" );
		if ( rating != null)
		{
			IDataCursor ratingCursor = rating.getCursor();
		
				// i.rating
				IData	rating_1 = IDataUtil.getIData( ratingCursor, "rating" );
				if ( rating_1 != null)
				{
					IDataCursor rating_1Cursor = rating_1.getCursor();
					String	score = IDataUtil.getString( rating_1Cursor, "score" );
					String	comment = IDataUtil.getString( rating_1Cursor, "comment" );
					String	user = IDataUtil.getString( rating_1Cursor, "user" );
					rating_1Cursor.destroy();
					
					int scoreI = 0;
					try {
						scoreI = new Integer(score).intValue();
						java.util.List<BookRatingEntry> ratings = getRatings(isbn);
						ratings.add(new BookRatingEntry(isbn, scoreI, comment, new java.util.Date(), user));
					} catch (NumberFormatException e) {
						result = e.getMessage();
					}
					
				}
			ratingCursor.destroy();
		}
		pipelineCursor.destroy();
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "result", result );
		pipelineCursor_1.destroy();			
		// --- <<IS-END>> ---

                
	}



	public static final void getBookDetails (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getBookDetails)>> ---
		// @sigtype java 3.5
		// [i] field:0:required isbn
		// [o] record:0:required book
		// [o] - recref:0:required book bookstore.docs:bookT
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	isbn = IDataUtil.getString( pipelineCursor, "isbn" );
		pipelineCursor.destroy();
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		
		for (int i = 0; i < bookList.length; i++) {
			BookListEntry b = bookList[i]; 
			if (b.getIsbn().equals(isbn)) {
				// book
				IData	book = IDataFactory.create();
				IDataCursor bookCursor = book.getCursor();
				IData	book_1 = IDataFactory.create();
				IDataCursor book_1Cursor = book_1.getCursor();
		
				java.util.Map<String, String> requestURL = getRequestURL();
				String host = requestURL.get("host");
				String url = requestURL.get("url");
				
				IDataUtil.put( book_1Cursor, "@isbn", b.getIsbn() );
				IDataUtil.put( book_1Cursor, "author", b.getAuthor() );
				IDataUtil.put( book_1Cursor, "title", b.getTitle() );
				IDataUtil.put( book_1Cursor, "rating", getAverageRating(b.getIsbn()) );
				IDataUtil.put( book_1Cursor, "votes", getNumberOfVotes(b.getIsbn()) );
				IDataUtil.put( book_1Cursor, "price", b.getPrice() );
				IDataUtil.put( book_1Cursor, "description", b.getDescription() );
				IDataUtil.put( book_1Cursor, "thumbnailurl", "http://"+host+"/Bookstore/images/thumbnails/"+b.getIsbn()+".png" );
				IDataUtil.put( book_1Cursor, "coverurl", "http://"+host+"/Bookstore/images/covers/"+b.getIsbn()+".png" );
				IDataUtil.put( book_1Cursor, "ratingsurl", "http://"+host+url+"/ratings" );
				book_1Cursor.destroy();
				IDataUtil.put( bookCursor, "book", book_1 );
				bookCursor.destroy();
				IDataUtil.put( pipelineCursor_1, "book", book );
				pipelineCursor_1.destroy();
			}
		}
			
		// --- <<IS-END>> ---

                
	}



	public static final void getBookRatings (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getBookRatings)>> ---
		// @sigtype java 3.5
		// [i] field:0:required isbn
		// [o] recref:0:required ratinglist bookstore.docs:ratinglistT
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	isbn = IDataUtil.getString( pipelineCursor, "isbn" );
		pipelineCursor.destroy();
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		
		// ratinglist
		IData	ratinglist = IDataFactory.create();
		IDataCursor ratinglistCursor = ratinglist.getCursor();
		
		// ratinglist.ratinglist
		IData	ratinglist_1 = IDataFactory.create();
		IDataCursor ratinglist_1Cursor = ratinglist_1.getCursor();
		IDataUtil.put( ratinglist_1Cursor, "@isbn", isbn );
		
		java.util.List<BookRatingEntry> ratings = getRatings(isbn);	
		IData[]	rating = new IData[ratings.size()];
		
		// ratinglist.ratinglist.rating
		for (int i = 0; i < ratings.size(); i++) {
			BookRatingEntry e = (BookRatingEntry)ratings.get(i);
			rating[i] = IDataFactory.create();
			IDataCursor ratingCursor = rating[i].getCursor();
			IDataUtil.put( ratingCursor, "score", e.getScore() );
			IDataUtil.put( ratingCursor, "comment", e.getComment() );
			IDataUtil.put( ratingCursor, "datetime", e.getDatetime() );
			IDataUtil.put( ratingCursor, "user", e.getUser() );
			ratingCursor.destroy();
		}	
		
		IDataUtil.put( ratinglist_1Cursor, "rating", rating );
		ratinglist_1Cursor.destroy();
		IDataUtil.put( ratinglistCursor, "ratinglist", ratinglist_1 );
		ratinglistCursor.destroy();
		IDataUtil.put( pipelineCursor_1, "ratinglist", ratinglist );
		pipelineCursor_1.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void getBooks (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getBooks)>> ---
		// @sigtype java 3.5
		// [o] recref:0:required booklist bookstore.docs:booklistT
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		
		// booklist
		IData	booklist = IDataFactory.create();
		IDataCursor booklistCursor = booklist.getCursor();
		
		// booklist.booklist
		IData	booklist_1 = IDataFactory.create();
		IDataCursor booklist_1Cursor = booklist_1.getCursor();
		
		// booklist.booklist.book
		IData[]	book = new IData[bookList.length];
		java.util.Map<String, String> requestURL = getRequestURL();
		String host = requestURL.get("host");
		String url = requestURL.get("url");
		
		for (int i = 0; i < bookList.length; i++) {
			BookListEntry b = bookList[i];
			book[i] = IDataFactory.create();
			IDataCursor bookCursor = book[i].getCursor();
			IDataUtil.put( bookCursor, "@isbn", b.getIsbn() );
			IDataUtil.put( bookCursor, "author", b.getAuthor() );
			IDataUtil.put( bookCursor, "title", b.getTitle() );
			IDataUtil.put( bookCursor, "rating", getAverageRating(b.getIsbn()) );
			IDataUtil.put( bookCursor, "votes", getNumberOfVotes(b.getIsbn()) );
			IDataUtil.put( bookCursor, "detailsurl", "http://"+host+url+"/"+b.getIsbn() );
			IDataUtil.put( bookCursor, "thumbnailurl", "http://"+host+"/Bookstore/images/thumbnails/"+b.getIsbn()+".png");
			bookCursor.destroy();
		}
		
		IDataUtil.put( booklist_1Cursor, "book", book );
		booklist_1Cursor.destroy();
		IDataUtil.put( booklistCursor, "booklist", booklist_1 );
		booklistCursor.destroy();
		IDataUtil.put( pipelineCursor, "booklist", booklist );
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static final class BookListEntry {
	
	    private final String isbn;
	    private final String title;
	    private final String author;
	    private final String price;
	    private final String description;
	
	    public BookListEntry(String isbn, String title, String author, String price, String description) {
	        this.isbn = isbn;
	        this.title = title;
	        this.author = author;
	        this.price = price;
	        this.description = description;
	    }
	
	    public String getAuthor() {
	        return author;
	    }
	
	    public String getIsbn() {
	        return isbn;
	    }
	
	    public String getPrice() {
	        return price;
	    }
	
	    public String getDescription() {
	        return description;
	    }
	
	    public String getTitle() {
	        return title;
	    }
	}
	
	private static final BookListEntry[] bookList = {
	
	        new BookListEntry(
	            "9781584883470",
	            "CRC Concise Encyclopedia of Mathematics",
	            "Eric Weisstein",
	            "54.99",
	            "Upon publication, the first edition of the CRC Concise Encyclopedia of Mathematics received overwhelming accolades for its unparalleled scope, readability, and utility. It soon took its place among the top selling books in the history of Chapman & Hall/CRC, and its popularity continues unabated. Yet also unabated has been the dedication of author Eric Weisstein to collecting, cataloging, and referencing mathematical facts, formulas, and definitions. He has now updated most of the original entries and expanded the Encyclopedia to include 1000 additional pages of illustrated entries.The accessibility of the Encyclopedia along with its broad coverage and economical price make it attractive to the widest possible range of readers and certainly a must for libraries, from the secondary to the professional and research levels. For mathematical definitions, formulas, figures, tabulations, and references, this is simply the most impressive compendium available."
	        ),
			
	        new BookListEntry(
	            "9780691138718",
	            "Fearless symmetry",
	            "Avner Ash & Robert Gross",
	            "12.89",
	            "Mathematicians solve equations, or try to. But sometimes the solutions are not as interesting as the beautiful symmetric patterns that lead to them. Written in a friendly style for a general audience, Fearless Symmetry is the first popular math book to discuss these elegant and mysterious patterns and the ingenious techniques mathematicians use to uncover them. Hidden symmetries were first discovered nearly two hundred years ago by French mathematician Evariste Galois. They have been used extensively in the oldest and largest branch of mathematics--number theory--for such diverse applications as acoustics, radar, and codes and ciphers. They have also been employed in the study of Fibonacci numbers and to attack well-known problems such as Fermat's Last Theorem, Pythagorean Triples, and the ever-elusive Riemann Hypothesis. Mathematicians are still devising techniques for teasing out these mysterious patterns, and their uses are limited only by the imagination. The first popular book to address representation theory and reciprocity laws, Fearless Symmetry focuses on how mathematicians solve equations and prove theorems. It discusses rules of math and why they are just as important as those in any games one might play. The book starts with basic properties of integers and permutations and reaches current research in number theory. Along the way, it takes delightful historical and philosophical digressions. Required reading for all math buffs, the book will appeal to anyone curious about popular mathematics and its myriad contributions to everyday life."
	        ),
	
	        new BookListEntry(
	            "9780812979183",
	            "The Black Swan",
	            "Nassim Nicholas Taleb",
	            "17.28",
	            "In business and government, major money is spent on prediction. Uselessly, according to Taleb, who administers a severe thrashing to MBA- and Nobel Prize-credentialed experts who make their living from economic forecasting. A financial trader and current rebel with a cause, Taleb is mathematically oriented and alludes to statistical concepts that underlie models of prediction, while his expressive energy is expended on roller-coaster passages, bordering on gleeful diatribes, on why experts are wrong. They neglect Taleb's metaphor of 'the black swan', whose discovery invalidated the theory that all swans are white. Taleb rides this manifestation of the unpredicted event into a range of phenomena, such as why a book becomes a best-seller or how an entrepreneur becomes a billionaire, taking pit stops with philosophers who have addressed the meaning of the unexpected and confounding. Taleb projects a strong presence here that will tempt outside-the-box thinkers into giving him a look."
	        ),
	
	        new BookListEntry(
	            "9780525950622",
	            "The Science of Fear",
	            "Daniel Gardener",
	            "16.47",
	            "Gardner, a columnist and senior writer for the Ottawa Citizen, is both matter-of-fact and entertaining in this look at fear and how it shapes our lives. Although we are capable of reason, says Gardner, we often rely instead on intuitive snap judgments. We also assume instinctively, but incorrectly, that [i]f examples of something can be recalled easily, that thing must be common. And what is more memorable than headlines and news programs blaring horrible crimes and diseases, plane crashes and terrorist attacks? In fact, such events are rare, but their media omnipresence activates a gut-level fear response that is out of proportion to the likelihood of our going through such an event. It doesn't help that scientific data and statistics are often misunderstood and misused and that our risk assessment is influenced less by the facts than by how others respond. Gardner's vivid, direct style, backed up by clear examples and solid data from science and psychology, brings a breath of fresh air and common sense to an emotional topic."
	        ),
	
	        new BookListEntry(
	            "9783492051101",
	            "Die Riemannsche Vermutung",
	            "Atle Naess",
	            "9.90",
	            "Auf die eine oder andere Weise w\u00FCrde die Sache fehlschlagen. Das hatte Terje Huuse, von Haus aus Mathematiker, von Anfang an geahnt. Nur wen das Ungl\u00FCck am h\u00E4rtesten treffen w\u00FCrde, schien damals noch offen. Terje hatte es sich in den Kopf gesetzt, die Biografie seines gro\u00DFen Vorbilds Bernhard Riemann zu verfassen und endlich das Abenteuer von der Suche nach einer Regelm\u00E4\u00DFigkeit bei den Primzahlen zu beschreiben. Aber die Arbeit war ins Stocken geraten, so einfach war das alles nicht. Nur mit seiner Bekannten Ingvild konnte sich der verheiratete Terje da\u00ADr\u00FCber unterhalten. Die Sache mit ihr glitt ihm schnell aus der Hand, unrettbar geriet er in den Sog dieser Beziehung. Bis Terje pl\u00F6tzlich verschwindet - und seine Tochter eine merkw\u00FCrdige Datei auf seinem Computer entdeckt"
	        ),
	
	        new BookListEntry(
	            "9781584885085",
	            "Cryptography",
	            "Douglas R. Stinson",
	            "51.69",
	            "Douglas R. Stinson's Cryptography: Theory and Practice is a mathematically intensive examination of cryptography, including ciphers, the Data Encryption Standard (DES), public key cryptography, one-way hash functions, and digital signatures. Stinson's explication of 'zero-sum proofs' -- a process by which one person lets another person know that he or she has a password without actually revealing any information--is especially good. If you are new to the math behind cryptography but want to tackle it, the author covers all of the required background to understand the real mathematics here. Cryptography includes extensive exercises with each chapter and makes an ideal introduction for any math-literate person willing to get acquainted with this material."
	        )
	};
	
	private static final class BookRatingEntry {
		
		private final String isbn;
		private final int score;
		private final String comment;
		private final String datetime;
		private final String user;
		
		public BookRatingEntry(String isbn, int score, String comment, String datetime, String user) {
			this.isbn = isbn;
			this.score = score;
			this.comment = comment;
			this.datetime = datetime;
			this.user = user;
		}
		
		public BookRatingEntry(String isbn, int score, String comment, java.util.Date datetimeD, String user) {
			this.isbn = isbn;
			this.score = score;
			this.comment = comment;
			this.datetime = dateFormat.format(datetimeD);
			this.user = user;
		}
		
		public String getIsbn() {
			return isbn;
		}
	
		public int getScore() {
			return score;
		}
		
		public String getComment() {
			return comment;
		}
		
		public String getDatetime() {
			return datetime;
		}
		public String getUser() {
			return user;
		}
	}
	
	private static final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
	
	private static final java.util.Map<String, java.util.List> bookRatingList = new java.util.HashMap<String, java.util.List>();
	
	private static final java.util.List<BookRatingEntry> getRatings(String isbn) {
		java.util.List<BookRatingEntry> ratings = (java.util.List<BookRatingEntry>)bookRatingList.get(isbn);
		if (ratings == null) {
			ratings = new java.util.ArrayList<BookRatingEntry>();
			bookRatingList.put(isbn, ratings);
		}
		return ratings;
	}
	
	static {
		try {
			String isbn;
			java.util.List<BookRatingEntry> ratings;
			
			isbn = "9781584883470";
			ratings = getRatings(isbn);
			ratings.add(new BookRatingEntry(isbn, 4, "Very good for beginners.", new java.util.Date(), "Ann"));	
			ratings.add(new BookRatingEntry(isbn, 3, "Not bad but overprized.", new java.util.Date(), "Bill"));
			
			isbn = "9780691138718";
			ratings = getRatings(isbn);
			ratings.add(new BookRatingEntry(isbn, 3, "Well ... gives a good overview.", new java.util.Date(), "Carl"));	
			ratings.add(new BookRatingEntry(isbn, 1, "Not worth the money.", new java.util.Date(), "Dustin"));
			ratings.add(new BookRatingEntry(isbn, 2, "I agree with Carl ... but too expensive.", new java.util.Date(), "Eve"));
			
			isbn = "9780812979183";
			ratings = getRatings(isbn);
			ratings.add(new BookRatingEntry(isbn, 5, "Excellent!", new java.util.Date(), "Fred"));	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
		
	private static final java.util.Map<String, String> getRequestURL() {
		IData input = IDataFactory.create();
		IData 	output = IDataFactory.create();
		java.util.Map result = new java.util.HashMap<String, String>();
		try {
			output = Service.doInvoke( "pub.flow", "getTransportInfo", input );
		}
		catch( Exception e){}
		
		IDataCursor outputCursor = output.getCursor();
	
		// transport
		IData	transport = IDataUtil.getIData( outputCursor, "transport" );
		if ( transport != null) {
			IDataCursor transportCursor = transport.getCursor();
	
			IData	http = IDataUtil.getIData( transportCursor, "http" );
			if ( http != null) {
				IDataCursor httpCursor = http.getCursor();
				String requestUrl = IDataUtil.getString( httpCursor, "requestUrl" );
				
				IData	requestHdrs = IDataUtil.getIData( httpCursor, "requestHdrs" );
				if ( requestHdrs != null) {
					IDataCursor requestHdrsCursor = requestHdrs.getCursor();
					String	host = IDataUtil.getString( requestHdrsCursor, "Host" );
					if (host != null && host.length() > 0) {
						result.put("host", host);
					}
					requestHdrsCursor.destroy();
				}
				
				result.put("url", trimUrl(requestUrl));
				httpCursor.destroy();
			}	
			transportCursor.destroy();
		}
		outputCursor.destroy();
		return result;
	}
	
	private static final String getAverageRating(String isbn) {
		// get ratings
		java.util.List<BookRatingEntry> rl = getRatings(isbn);
		float scoreF = 0;
		int nr = rl.size();
		for (int i = 0; i < nr; i++) {
			BookRatingEntry e = (BookRatingEntry)rl.get(i);
			scoreF = scoreF + (float)e.getScore();
		}
		if (nr > 0) {
			scoreF = scoreF/(float)nr;
		}
		
		return String.valueOf(scoreF);
	}
	
	private static final String getNumberOfVotes(String isbn) {
		// get ratings
		java.util.List<BookRatingEntry> rl = getRatings(isbn);
		int nr = rl.size();
		
		return String.valueOf(nr);
	}
	
	private static final String trimUrl (String url) {
		String result = url;
		while (result != null && result.length() > 0 && result.endsWith("/")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	// --- <<IS-END-SHARED>> ---
}

