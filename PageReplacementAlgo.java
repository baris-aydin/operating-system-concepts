

public class PageReplacementAlgo
{
	//******************************************************************
	//                 Methods for supporting page replacement
	//******************************************************************

	public static void pageReplacement(int vpage, Process prc, Kernel krn)
	{
	   if(prc.pageTable[vpage].valid) return;   

	   if(!prc.areAllocatedFramesFull()) 
 	       addPageFrame(vpage, prc, krn);
	   else
	       pageReplAlgorithm(vpage, prc, krn);
	}

	public static void addPageFrame(int vpage, Process prc, Kernel krn)
	{
	     int [] fl;  
	     int freeFrame;  
	     int i;
	     freeFrame = krn.getNextFreeFrame();  
	     if(freeFrame == -1)  
	     {
		System.out.println("Could not get a free frame");
		return;
	     }
	     if(prc.allocatedFrames == null) 
	     {
		prc.allocatedFrames = new int[1];
		prc.allocatedFrames[0] = freeFrame;
	     }
	     else 
	     {
	        fl = new int[prc.allocatedFrames.length+1];
	        for(i=0 ; i<prc.allocatedFrames.length ; i++) fl[i] = prc.allocatedFrames[i];
	        fl[i] = freeFrame; 
	        prc.allocatedFrames = fl; 
	     }
	     prc.pageTable[vpage].frameNum = freeFrame;
	     prc.pageTable[vpage].valid = true;
	}

	// Calls to Replacement algorithm
	public static void pageReplAlgorithm(int vpage, Process prc, Kernel krn)
	{
	     boolean doingCount = false;
	     switch(krn.pagingAlgorithm)
	     {
		case FIFO: pageReplAlgorithmFIFO(vpage, prc); break;
		case LRU: pageReplAlgorithmLRU(vpage, prc); break;
		case CLOCK: pageReplAlgorithmCLOCK(vpage, prc); break;
		case COUNT: pageReplAlgorithmCOUNT(vpage, prc); doingCount=true; break;
	     }
	}

	public static void doneMemAccess(int vpage, Process prc, double clock)
	{
		prc.pageTable[vpage].used = true;  
		prc.pageTable[vpage].tmStamp = clock;  
		prc.pageTable[vpage].count++; 
	}

	// FIFO page Replacement algorithm
	public static void pageReplAlgorithmFIFO(int vpage, Process prc)
	{
	   int vPageReplaced;  // Page to be replaced
	   int frame;	// frame to receive new page
	   // Find page to be replaced
	   frame = prc.allocatedFrames[prc.framePtr];   // get next available frame
	   vPageReplaced = findvPage(prc.pageTable,frame);     // find current page using it (i.e written to disk)
	   prc.pageTable[vPageReplaced].valid = false;  // Old page is replaced.
	   prc.pageTable[vpage].frameNum = frame;   // load page into the frame and update table
	   prc.pageTable[vpage].valid = true;             // make the page valid
	   prc.framePtr = (prc.framePtr+1) % prc.allocatedFrames.length;  // point to next frame in list
	}

	// CLOCK page Replacement algorithm
	public static void pageReplAlgorithmCLOCK(int vpage, Process prc)
	{
		while (true) {
			int frame = prc.allocatedFrames[prc.framePtr];
			int vPageReplaced = findvPage(prc.pageTable, frame);
	
			if (!prc.pageTable[vPageReplaced].used) {  // If not used, replace
				prc.pageTable[vPageReplaced].valid = false;
				prc.pageTable[vpage].frameNum = frame;
				prc.pageTable[vpage].valid = true;
				prc.framePtr = (prc.framePtr + 1) % prc.allocatedFrames.length;  // Move pointer
				break;
			}
	
			prc.pageTable[vPageReplaced].used = false;  // Reset used bit
			prc.framePtr = (prc.framePtr + 1) % prc.allocatedFrames.length;  // Move to next frame
		}
	}

	// LRU page Replacement algorithm
	public static void pageReplAlgorithmLRU(int vpage, Process prc)
	{
		int lruPage = -1;
		double oldestTime = Double.MAX_VALUE;
	
		for (int frame : prc.allocatedFrames) {
			int vPageReplaced = findvPage(prc.pageTable, frame);
			if (prc.pageTable[vPageReplaced].tmStamp < oldestTime) {
				oldestTime = prc.pageTable[vPageReplaced].tmStamp;
				lruPage = vPageReplaced;
			}
		}
	
		if (lruPage != -1) {
			prc.pageTable[lruPage].valid = false;
			prc.pageTable[vpage].frameNum = prc.pageTable[lruPage].frameNum;
			prc.pageTable[vpage].valid = true;
			prc.pageTable[vpage].tmStamp = System.nanoTime();  // Update timestamp
		}
	}

	// COUNT page Replacement algorithm
	public static void pageReplAlgorithmCOUNT(int vpage, Process prc)
	{
		int minCountPage = -1;
		long minCount = Long.MAX_VALUE;
	
		for (int frame : prc.allocatedFrames) {
			int vPageReplaced = findvPage(prc.pageTable, frame);
			if (prc.pageTable[vPageReplaced].count < minCount) {
				minCount = prc.pageTable[vPageReplaced].count;
				minCountPage = vPageReplaced;
			}
		}
	
		if (minCountPage != -1) {
			prc.pageTable[minCountPage].valid = false;
			prc.pageTable[vpage].frameNum = prc.pageTable[minCountPage].frameNum;
			prc.pageTable[vpage].valid = true;
			prc.pageTable[vpage].count = 1;  // Reset count for new page
		}
	}

	// finds the virtual page loaded in the specified frame fr
	public static int findvPage(PgTblEntry [] ptbl, int fr)
	{
	   int i;
	   for(i=0 ; i<ptbl.length ; i++)
	   {
	       if(ptbl[i].valid)
	       {
	          if(ptbl[i].frameNum == fr)
	          {
		      return(i);
	          }
	       }
	   }
	   System.out.println("Could not find frame number in Page Table "+fr);
	   return(-1);
	}

	public static void logProcessState(Process prc)
	{
	   int i;

	   System.out.println("--------------Process "+prc.pid+"----------------");
	   System.out.println("Virtual pages: Total: "+prc.numPages+
			      " Code pages: "+prc.numCodePages+
			      " Data pages: "+prc.numDataPages+
			      " Stack pages: "+prc.numStackPages+
			      " Heap pages: "+prc.numHeapPages);
	   System.out.println("Simulation data: numAccesses left in cycle: "+prc.numMemAccess+
			      " Num to next change in working set: "+prc.numMA2ChangeWS);
           System.out.println("Working set is :");
           for(i=0 ; i<prc.workingSet.length; i++)
           {
              System.out.print(" "+prc.workingSet[i]);
           }
           System.out.println();
	   // page Table
	   System.out.println("Page Table");
	   if(prc.pageTable != null)
	   {
	      for(i=0 ; i<prc.pageTable.length ; i++)
	      {
		 if(prc.pageTable[i].valid) 
		 {
	           System.out.println("   Page "+i+"(valid): "+
				      " Frame "+prc.pageTable[i].frameNum+
				      " Used "+prc.pageTable[i].used+
				      " count "+prc.pageTable[i].count+
				      " Time Stamp "+prc.pageTable[i].tmStamp);
		 }
		 else System.out.println("   Page "+i+" is invalid (i.e not loaded)");
	      }
	   }
	   // allocated frames
	   System.out.println("Allocated frames (max is "+prc.numAllocatedFrames+")"+
			      " (frame pointer is "+prc.framePtr+")");
	   if(prc.allocatedFrames != null)
	   {
	      for(i=0 ; i<prc.allocatedFrames.length ; i++)
 	           System.out.print(" "+prc.allocatedFrames[i]);
	   }
	   System.out.println();
           System.out.println("---------------------------------------------");
	}
}


