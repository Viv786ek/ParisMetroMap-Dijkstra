
// REFERENCE
// $Id: WeightGraph.java,v 1.1 2006/11/18 01:20:12 jlang Exp $
// CSI2110 Fall 2006 Laboratory 9: Adjacency List and DFS
// ==========================================================================
// (C)opyright:
//
//   Jochen Lang
//   SITE, University of Ottawa
//   800 King Edward Ave.
//   Ottawa, On., K1N 6N5
//   Canada.
//   http://www.site.uottawa.ca
//
// Creator: jlang (Jochen Lang)
// Email:   jlang@site.uottawa.ca
// ==========================================================================
// $Log: WeightGraph.java,v $
// Revision 1.1  2006/11/18 01:20:12  jlang
// Added lab10
//
// Revision 1.1  2006/11/11 03:15:52  jlang
// Added Lab9
//
// Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca) 
// ==========================================================================
// Modified by CSI 2110 FALL 2017 ASSIGNMENT 4 GROUP 101 on DECEMBER 5th, 2017 
// Nevin Wong Syum Ganesan - 8831598 (ngane103@uottawa.ca) 
// Mabroor Kamal - 8669942 (mabroor7@gmail.com)
// ==========================================================================



import java.io.BufferedReader;
//package org.apache.commons.math3.util;
import java.io.FileReader;
import java.util.StringTokenizer;

import net.datastructures.AdjacencyMapGraph;
//import net.datastructures.Dijkstra;
import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Entry;
//import net.datastructures.MyEntry;
import net.datastructures.Map;
import net.datastructures.Vertex;
import net.datastructures.*;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
//import java.util.Map;
//import java.util.*;

public class ParisMetro{

	//==========================INSTANCE VARIABLES: ==============================
	//GRAPH STORED
	Graph<String, Integer> sGraphStored;   //Object WEIGHT = new Object(); //NOT USED

	//STORES THE LIST OF STATION ID & NAME USING ARRAYLIST
	//MY ENTRY IS A CLASS I HAVE CREATED TO STORE (STRING ID, STRING NAME)
	//https://stackoverflow.com/questions/3110547/java-how-to-create-new-entry-key-value
	private ArrayList<MyEntry<String,String>> listEntry; 

	//THIS IS TO CROSS CHECK IF I HAVE THE CORRECT NUMBER VERTICES & EDGES STORED IN THE GRAPH
	private int RecordedNumVertices; private int RecordedNumEdges;

	//============THIS IS STORING N1 ============== 
	// ESSENTIAL FOR USING OTHER METHODS, SUCH AS GETTERS, GRAPH TRAVERSALS AND SO ON
	private String vertStored;

	// IF THE EDGE IS -1, REPLACE WITH 90, 
	// NOTE THAT I HAVE REPLACED DIRECTLY WHILE BUILDING THE GRAPH
	// "In some cases, it is possible to walk in order to switch from one line to another. These cases are
	// identified by the number -1 in place of their weight. For these cases, the weight must be
	// substituted by the estimated walk time which will be a constant chosen by the program."
	private final static Integer weightTimeConstant=90;

	//3 BOOLEAN FLAG VARIABLE TO TACKLE THE readMetro(fileName) DIFFERENTLY DEPENDING ON THE SITUATION
	//QUESTION 1 REFERS TO QUESTION) 2.1 
	//QUESTION 2 REFERS TO QUESTION) 2.2 
	//QUESTION 3 REFERS TO QUESTION) 2.3
	// FOR CLARITY PURPOSE/AVOID CONFUSION HAVE USED 3 BOOLEAN VARIABLES INSTEAD OF 2
	private boolean question1; private boolean question2; private boolean question3;

	//QUESTION 2.3 HAS TWO STAGES WHEN THE LINE IS NOT FUNCTIONING GIVEN A POINT, N3
	// THIS FLAG IS USED TO CONTRUCT THE GRAPH FIRST SIMILAR TO QUESTION 2.1)
	// USING N3, I WILL ADD THE NOT FUNCTIONING LINE TO MY HashSet<String> {Station ID}
	// THEN DONE WILL BE TRUE, THEN RECONTRUCT THE GRAPH AGAIN SIMILAR TO QUESTION 2.2)
	//RECONTRUCT MEANS BUILDING THE GRAPH AGAIN AND 
	// NOW I CAN FIND THE SHORTEST PATH SIMILAR TO QUESTION 2.2)
	private boolean done;

	//DEFAULT METHOD if no arguments is entered IN THE MAIN METHOD
	public ParisMetro(String fileName) throws Exception, IOException {
		//CREATES A DIRECTED ADJACENCY MAP GRAPH WITH STRING( non leading zero IDs) 
		//AND INTEGER(Weight) (DIRECTED MEANS TRUE)
		sGraphStored = new AdjacencyMapGraph<String, Integer>(true);
		listEntry= new ArrayList<MyEntry<String,String>>();
		int RecordedNumVertices=0; int RecordedNumEdges=0; done=true;
		readMetro(fileName); 
	}

	//QUESTION 1/ (QUESTION 2.1)
	public ParisMetro(String fileName, String vertStored) throws Exception, IOException {
		//Init new Graph
		sGraphStored = new AdjacencyMapGraph<String, Integer>(true); 
		//Init new List for station ID and name 
		listEntry= new ArrayList<MyEntry<String,String>>();
		//Record
		int RecordedNumVertices=0; int RecordedNumEdges=0; 
		//Store N1
		this.vertStored=vertStored; 
		//Set flags
		question1=true; question2=false; question3=false; done=true;
		//Read file & Build Graph & List of Station ID Name
		readMetro(fileName); 
	}
	// IF N2 IS ENTERED, THIS INSTANCE VARIABLE WILL BE USED
	private String vertStoredDes;
	//QUESTION 1/ (QUESTION 2.1)
	public ParisMetro(String fileName,String vertStored,String vertStoredDes) throws Exception, IOException {
		//Init new Graph
		sGraphStored = new AdjacencyMapGraph<String, Integer>(true); 
		//Init new List for station ID and name 
		listEntry= new ArrayList<MyEntry<String,String>>();
		//Record
		int RecordedNumVertices=0; int RecordedNumEdges=0; 
		//Store N1 & N2
		this.vertStored=vertStored; this.vertStoredDes=vertStoredDes;
		//Set Flags
		question1=false; question2=true; question3=false; done=true;
		//Read file & Build Graph & List of Station ID Name
		readMetro(fileName);
	}

	
	// IF N1 N2 N3 IS ENTERED, THIS INSTANCE VARIABLE WILL BE USED
	private String vertDestroyed;
	private HashSet<String> hashSet;
	public ParisMetro(String fileName,String vertStored,String vertStoredDes,String vertDestroyed) throws Exception, IOException {
		if(vertStored.equals(vertStoredDes) && !vertStored.equals(vertDestroyed) ){
			System.out.println();
			System.out.println(" TIME TAKEN : 0 BECAUSE SAME ORIGIN AND DESTINATION IS ENTERED ");
			System.out.println();
       		System.exit(1);
		}
		if(vertStored.equals(vertStoredDes) && vertStored.equals(vertDestroyed) ){
			System.out.println();
			System.out.println(" NOT POSSIBLE ORIGIN, DESTINATION, BROKEN LINE ENTERED IS THE SAME ");
			System.out.println(" TIME TAKEN : 0 , BECAUSE ALL 3 POINTS IS THE SAME ");
			System.out.println();
       		System.exit(1);
		}
		//Init new Graph
		sGraphStored = new AdjacencyMapGraph<String, Integer>(true); 
		//Init new List for station ID and name 
		listEntry= new ArrayList<MyEntry<String,String>>();
		//Record
		int RecordedNumVertices=0; int RecordedNumEdges=0; 
		//Store N2
		this.vertStoredDes=vertStoredDes; 
		//Set Flags 
		question1=false; question2=false; question3=true;
		//init hashset to store the not functioning lines
		hashSet=new HashSet<String>();
		//add the destoy vertex into the hashset
		hashSet.add(vertDestroyed);
		
		//=========STEP ONE build color line
		done=false; 

		//swap this temporary so the line not functioning can be build
		this.vertStored=vertDestroyed;

		// read file and build the graph
		readMetro(fileName);

		// Using the same technique in Question 2.1 
		// To build the hashSET
		// But no printing here when this method is called
		// because boolean question1 is false
		printSameLine();

		// CHECK IF (N1 OR N2) IS SAME ( PATH/LINE OR VERTEX) AS N3
		// THEN STOP PROGRAM
		if(hashSet.contains(vertStored) || hashSet.contains(vertStoredDes)  ){
			//System.out.println();
			System.out.println(" NO POSSIBLE PATH ");
			if(hashSet.contains(vertStored)){ //VARIABLE FROM THE CONTRUCTOR
				System.out.println(" BECAUSE LINE NOT FUNCTIONING IS IN YOUR ORIGIN ");
			}
			if(hashSet.contains(vertStoredDes)){ //VARIABLE FROM THE CONTRUCTOR
				System.out.println(" BECAUSE LINE NOT FUNCTIONING IS IN YOUR DESTINATION");
			}
			
			System.out.println();
       		System.exit(1);
		}

		//=========STEP TWO BUILD GRAPH NORMALLY
		//set done to true after finish building the hashset
		done=true;

		//set instance variables normally
		this.vertStored=vertStored;
		this.vertStoredDes=vertStoredDes;
		this.vertDestroyed=vertDestroyed; 
		
		//System.out.println("done building set");

		//Init new Graph like Question 2.2
		sGraphStored = new AdjacencyMapGraph<String, Integer>(true);
		//Init new List Entry like Question 2.2
		listEntry= new ArrayList<MyEntry<String,String>>();
		//read normally and build the graph & listEntry
		readMetro(fileName);

		//to check if my hashSet containts the correct number of size
		//System.out.println(hashSet.size());
	}

	//REFERENCE LAB CODE
	//DEFAULT reading METHOD for metro.txt , 
	protected void readMetro(String fileName) throws Exception,IOException{
		//read file
		BufferedReader graphFile = new BufferedReader(new FileReader(fileName));
		// storing vertices
		Hashtable<String,Vertex> vertices2 = new Hashtable<String,Vertex>();
		//TO READ THE METRO.TXT FILE
		String line; int counter=0;
		while((line = graphFile.readLine())!=null){
		// reference to StringTokenizer 
		//https://stackoverflow.com/questions/10263298/check-to-see-if-next-string-tokenizer-is-empty
			StringTokenizer st= new StringTokenizer(line);
			if(counter==0){
				RecordedNumVertices= new Integer(st.nextToken());
				RecordedNumEdges= new Integer(st.nextToken());
			}
			// STORES THE ID AND STATION NAME
			if(counter>=1 && counter<=376){
				String id= st.nextToken();
				String source = st.nextToken();
				// SAME STATION NAME IS LONG
				while(st.hasMoreTokens()){
					source = source +" "+ st.nextToken();
				}
				//Test Line success //System.out.println("ID: "+ id +" Source: " + source );
				MyEntry<String,String> newEntry= new MyEntry(id,source);
				listEntry.add(counter-1, newEntry);
				//added
			}
			if(counter>=378){
				String source_id = st.nextToken();
				String destination_id= st.nextToken();
				Integer weight= new Integer(st.nextToken());	
					//added to replace
					if(weight.equals(-1)){
						if(question1==true){ //SKIP -1 IF QUESTION 1 IS TRUE TO FIND THE LINE
							break;
						}					//FOR CLARITY PURPOSE THIS IF STATEMENT IS SPILT INTO TWO PARTS BUT SAME ACTION
						if(done==false){ // QUESTION 3 ONCE DONE BUILDING THE GRAPH COLOR DONE
							break;
						}
						weight= new Integer(90);
					}
					//read file from lab code & build graph
					Vertex<String> sv = vertices2.get(source_id);
					if (sv == null) {
						// Source vertex not in graph -- insert
						sv = sGraphStored.insertVertex(source_id);
						vertices2.put(source_id, sv);
					}
					Vertex<String> dv = vertices2.get(destination_id);
					if (dv == null) {
						// Destination vertex not in graph -- insert
						dv = sGraphStored.insertVertex(destination_id);
						vertices2.put(destination_id, dv);
					}
					// check if edge is already in graph
					if (sGraphStored.getEdge(sv, dv) == null) {
						// edge not in graph -- add
						//e's element is now the distance between the vertices	
						Edge<Integer> e = sGraphStored.insertEdge(sv, dv, weight);
					}
			}
			counter++;	
		}
		System.out.println();
	}


	//========================== REFERENCE OF LAB CODE =========================================


	/**
	 * Helper routine to get a Vertex (Position) from a string naming the vertex
	 * Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca) 
	 */
	protected Vertex<String> getVertex(String vert) throws Exception {
		// Go through vertex list to find vertex -- why is this not a map
		for (Vertex<String> vs : sGraphStored.vertices()) {
			if (vs.getElement().equals(vert)) {
				return vs;
			}
		}
		throw new Exception("Vertex not in graph: " + vert);
	}

	/* FROM LAB 10
	*
	 * Printing all the vertices in the list, followed by printing all the edges
	 * Modified by A4 GROUP MEMBERS 101
	 */
	public void print() {
		System.out.println();
		System.out.println("Vertices: " + sGraphStored.numVertices() + " Edges: " + sGraphStored.numEdges());
		System.out.println();
		System.out.println("----------VERTICES----------");
		System.out.println("MY RECORDED NUMBER OF VERTICES: --------"+ RecordedNumVertices);
		System.out.println("Algorithm Vertices is: ------ " + sGraphStored.numVertices() );

		//TO DEBUG VERTICES STORED IN THE GRAPH
		for (Vertex<String> vs : sGraphStored.vertices()) {
			//System.out.println(vs.getElement());
		}

		System.out.println();
		System.out.println("----------EDGES----------");
		System.out.println("MY RECORDED NUMBER OF EDGES: --------"+ RecordedNumEdges);
		System.out.println("Algorithm Vertices is: ------ " + sGraphStored.numEdges() );

		//TO DEBUG VERTICES STORED IN THE GRAPH
		for (Edge<Integer> es : sGraphStored.edges()) {
			//System.out.println(es.getElement());
		}
		return;
	}

	/**	FROM LAB 10
	 * Helper method: Read a String representing a vertex from the console
	 */
	public static String readVertex() throws IOException {
		System.out.print("[Input] Vertex ID: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}


	//============================LAB CODE ==============================
	
	//========================================================================================================
	//Question 1 readMetro
	public void printSameLine() throws Exception{
		//making a 4 digit ID
		//creates a reference
		String vertStoredID = vertStored;
		Vertex<String> vSource = getVertex(vertStored);
		MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement()));
		String initialLocationName= (String)mineEntry.getValue();
		//making a 4 digit ID
		while(vertStoredID.length()!=4){
     			vertStoredID = "0"+ vertStoredID;
     	}
     	// ====DONE making a 4 digit ID
		if(question1==true){
			System.out.println();
			System.out.println(" Printing all same line, given station ID is " + vertStored);
			System.out.println();
			System.out.println( " ID: " + vertStoredID + " : " + initialLocationName );
			printDFS(vertStored);
			System.out.println();
		}
		// TO BUILD THE SET 
		if(question3==true){
			// TO CHECK THE SET
			//System.out.println("Printing all same line, given station ID is " + vertStored);
			//System.out.println( " ID: " + vertStoredID + " : " + initialLocationName );

			printDFS(vertStored);
		}
	}
	//modified from Lab 10 Code
	public void printDFS(String vert) throws Exception{ //int counter=0;
		Integer real= Integer.parseInt(vert); String s = real.toString(); Vertex<String> vSource = getVertex( vert );
		Map<Vertex<String>,Edge<Integer>> forest = DFSComplete(sGraphStored);
		
		for( Entry<Vertex<String>,Edge<Integer>> ent: forest.entrySet() ){ 

			//debug
			//System.out.println(counter++);
			//String endLocationID= ent.getKey().getElement(); Integer edgeDistance= ent.getValue().getElement();		
			//System.out.println( " ID: " +ent.getKey().getElement() + " Time Taken: " + ent.getValue().getElement() );

			//Make 4 digit ID
			String endLocationID= (String) ent.getKey().getElement();
			while(endLocationID.length()!=4){
     			endLocationID = "0"+endLocationID;
     		}
			MyEntry mineEndEntry = listEntry.get(Integer.parseInt(endLocationID));
			String endLocationName=(String)mineEndEntry.getValue();
			//System.out.println( " ID: " +ent.getKey().getElement() + " : " + endLocationName); 
			if(question1==true){
				System.out.println( " ID: " + endLocationID + " : " + endLocationName); 
			}
			//System.out.println( " ID: " + endLocationID + " : " + endLocationName); 
			if(question3==true && !done){
				// TO CHECK THE SET
				//System.out.println( " ID: " + endLocationID + " : " + endLocationName); 
				//System.out.println("ADDING");

				//adding to the set of same colour/line/path
				hashSet.add(ent.getKey().getElement());
			}
		}	
	}

	// Modified from Lab 10 CODE
	public <String,Integer> Map<Vertex<String>,Edge<Integer>> DFSComplete(Graph<String,Integer> g) throws Exception {
   		Set<Vertex<String>> known = new HashSet<>();
   		Vertex vSource = getVertex(vertStored);
    	Map<Vertex<String>,Edge<Integer>> forest = new ProbeHashMap<>();
    	for (Vertex<String> u : g.vertices())
      		if (!known.contains(u))
        		DFSHelper(g, vSource, known, forest);
    	return forest;
	}

   // Modified from Lab 10 CODE
   /**
   * Performs depth-first search of the unknown portion of Graph g starting at Vertex u.
   *
   * @param g Graph instance
   * @param u Vertex of graph g that will be the source of the search
   * @param known is a set of previously discovered vertices
   * @param forest is a map from nonroot vertex to its discovery edge in DFS forest
   *
   * As an outcome, this method adds newly discovered vertices (including u) to the known set,
   * and adds discovery graph edges to the forest.
   */
  	private <V,E> void DFSHelper(Graph<V,E> g, Vertex<V> u, Set<Vertex<V>> known, Map<Vertex<V>,Edge<E>> forest) {
    	known.add(u);                              // u has been discovered
    	for (Edge<E> e : g.outgoingEdges(u)) {     // for every outgoing edge from u
	      Vertex<V> v = g.opposite(u, e);
	      	if (!known.contains(v)) {
	        	forest.put(v, e);                      // e is the tree edge that discovered v
	        	DFSHelper(g, v, known, forest);              // recursively explore from v
	      	}
	    }
	}

	//================================ END OF Question 1 ===============================================


	//========================================================================================================
	/**
	 * Print the shortest distances
	 * Modified by A4 GROUP 101, FROM LAB 10
	 * @throws Exception 
	 */
	// PURPOSE: 
	// 1) FOR DEFAULT METHOD WHEN NO ARGUMENT IS ENTERED
	// 2) USEFUL FOR DEBUGGING/CHECKING BEFORE DOING THE ASSIGNMENt
	// DEFAULT METHOD FOR PRINTING ALL SHORTEST DISTANCE GIVEN A STATION ID IN STRING
	void printAllShortestDistances(String vert) throws Exception {

		//Removes the Leading Zero From ID, for example if the User Enters 0375 -> 375
		Integer real= Integer.parseInt(vert); 
		String s = real.toString(); 
		Vertex<String> vSource = getVertex(s);

       	//Calls the Graph Algorithms class given in lab
        GraphAlgorithms graphA = new GraphAlgorithms();

        // Finds the shortest path and stores in the cloud, 
        // Takes 2 arguments : i) Using the the graph stored and ii) vSource is our origin point
        Map<Vertex<String>,Integer> cloud=graphA.shortestPathLengths(sGraphStored,vSource);

        //Prints the title
		System.out.println("PRINTING ALL SHORTEST DISTANCE  -----------------");

		//int counter=0; //for debugging the number of points
		SortedPriorityQueue sPQ= new SortedPriorityQueue();

		//Loops inside the cloud, FOR every Entry IN my Cloud EntrySet 
		for (Entry<Vertex<String>,Integer> ent: cloud.entrySet() ){  //System.out.println(counter++);

     		//Find Name of Station by ID (Origin)
     		MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement())); //System.out.println(mineEntry);

     		//Gets the 4 Digit ID, for example 0000 (Origin)
     		String initialLocationID= (String)mineEntry.getKey(); 
     		//System.out.println(mineEntry.getKey()); //debugging purpose

     		//Gets the Station name associated with the ID, for example Abbessess
     		String initialLocationName= (String)mineEntry.getValue(); 
     		//System.out.println(mineEntry.getValue()); //debugging purpose

     		//Gets the Digit ID, for example 1 (Destination)
     		String endLocationID= (String) ent.getKey().getElement();

     		//Because the ID stored in the cloud is not 4 digits, 
     		// we want to make it print nicer, we add the leading zeroes
     		while(endLocationID.length()!=4){
     			endLocationID = "0"+endLocationID;
     		}

     		//Gets the edgeDistance (in Time Seconds ) From ORIGIN TO DESTINATION
     		Integer edgeDistance= (Integer) ent.getValue();

     		//CALLS My Entry to determine End ID stored in 4 digits
     		MyEntry mineEndEntry = listEntry.get(Integer.parseInt(endLocationID));

     		//Gets the name in String
     		String endLocationName=(String)mineEndEntry.getValue();

     		//Insert the Sorted PQ to Sort 
     		sPQ.insert(edgeDistance,endLocationID);

     		//Debugging 
        	//System.out.println( vSource.getElement() +" to " +ent.getKey().getElement() + " = " + ent.getValue() );

        	//Prints out everything unsorted from start to end point with time taken
        	System.out.println("StartID: " + initialLocationID + " InitialName: " +initialLocationName +" To EndID: " + endLocationID +" Destination: " +endLocationName + " = " + ent.getValue() +" seconds" );
        }
        
        // SAME AS ABOVE BUT SORTED
        // Print shortest path to named cities in Order
        System.out.println();
        System.out.println("After sorting");
        System.out.println();
        while(!sPQ.isEmpty()){
        	MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement()));
     		String initialLocationID= (String)mineEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     		String initialLocationName= (String)mineEntry.getValue(); //System.out.println(mineEntry.getValue()); //Abbessess

        	Entry<Integer,String> tempMin = sPQ.removeMin(); 
        	MyEntry temp1 = listEntry.get(Integer.parseInt(tempMin.getValue()));
        	String tempName=(String)temp1.getValue();
        	System.out.println("StartID: "+initialLocationID+ " InitialName: " + initialLocationName +" To EndID: "+ tempMin.getValue() +" Destination: " + tempName+ " = "+ tempMin.getKey()+ " seconds");
        }
        return;
	}
	//======================================================================================================================================================================
	

	//========== QUESTION 2.2 ========================
	//Question 2.2 Modified to find the total time taken from Point A to B
	public void printAllShortestDistances(String vert, String vert2) throws Exception {
		//Remove Leading Zero From ID
		Integer real= Integer.parseInt(vert);  		Integer real2= Integer.parseInt(vert2);
		String s = real.toString();			   		String s2 = real2.toString();
        Vertex<String> vSource = getVertex( s ); 	Vertex<String> vSource2 = getVertex( s2 );

        //store the shortest path
        //ArrayList<MyEntry<String,Integer>> storedEntryList = new ArrayList<MyEntry<String,Integer>>(); 

        GraphAlgorithms graphA = new GraphAlgorithms();

        // Find shortest path
        Map<Vertex<String>,Integer> cloud=graphA.shortestPathLengths(sGraphStored,vSource);

        System.out.println();
		System.out.println("------------- PRINTING THE SPECIFIED SHORTEST DISTANCE QUESTION 2.2  -----------------");
		System.out.println(); 
		//int index=0; c
        for (Entry<Vertex<String>,Integer> ent: cloud.entrySet() ){ //counter++;
     		//System.out.println(counter++);
     		//find name of station by ID
     		MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement())); 
     		MyEntry wantedEntry = listEntry.get(Integer.parseInt(vSource2.getElement()));
     		//System.out.println(mineEntry);
     		String initialLocationID= (String)mineEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     		String initialLocationName= (String)mineEntry.getValue(); //System.out.println(mineEntry.getValue()); //Abbessess

     		String wantedLocationID= (String)wantedEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     		String wantedLocationName= (String)wantedEntry.getValue(); //System.out.println(mineEntry.getValue()); //Abbessess

     		String endLocationID= (String) ent.getKey().getElement();
     		while(endLocationID.length()!=4){
     			endLocationID = "0"+endLocationID;
     		}
     		Integer edgeDistance= (Integer) ent.getValue();
     		MyEntry mineEndEntry = listEntry.get(Integer.parseInt(endLocationID));
     		String endLocationName=(String)mineEndEntry.getValue();

        	//System.out.println( vSource.getElement() +" to " +ent.getKey().getElement() + " = " + ent.getValue() );
        	if(wantedLocationID.equals(endLocationID)){
        		
        		//storing the required edges and ID name
        		MyEntry<String,Integer> toBeStored = new MyEntry(endLocationID,edgeDistance);
        		//storedEntryList.add(index,toBeStored); index++;
        		System.out.println(" StartID: " + initialLocationID + " InitialName: " +initialLocationName +" TO EndID: " + endLocationID +" Destination: " +endLocationName);
        		System.out.println(" TOTAL TIME TAKEN : " + ent.getValue() +" SECONDS " );
        	}
        	if(wantedLocationID.equals(endLocationID) && initialLocationID.equals(endLocationID)){
        		System.out.println("Your have entered the same start location and  the same end location, time taken expected to be 0");
        	}
        	//for -1 cases, u can walk directly
        	//if(ent.getValue() == 90){ //13 and 151 bug 90 seconds
        		//System.out.println(counter);
        		//System.out.println("U can walk directly with constant time of 90 seconds");
        	//}
        }
        return;
	}

  	  // QUESTION 2.2  ================================================================
  	// ================================================
	public void shortestPathLengthsStations(Graph<String,Integer> g, Vertex<String> src, Vertex<String> des) throws Exception {
		//LinkedList<String> listNot = new LinkedList(); LinkedList<String> listPath = new LinkedList();
		//LinkedList<String> allPath= new LinkedList();
		//LinkedList<MyEntry<MyEntry<String,String>,Integer>> connection = new LinkedList();
		//LinkedList<MyEntry<String,String>> connectionVertex = new LinkedList();
		java.util.Map<String,String> connectionMap= new java.util.HashMap<String,String>();
		java.util.Map<String,Integer> connectionEdges= new java.util.HashMap<String,Integer>();
		Hashtable<String,Vertex> vertices = new Hashtable<String,Vertex>();
		//LinkedList<Integer> connectionEdges = new LinkedList();
		LinkedList<String> testPath= new LinkedList();


		Map<Vertex<String>, Integer> d = new ProbeHashMap<>(); // d.get(v) is upper bound on distance from src to v
		Map<Vertex<String>, Integer> cloud = new ProbeHashMap<>(); // map reachable v to its d value
		AdaptablePriorityQueue<Integer, Vertex<String>> pq; pq = new HeapAdaptablePriorityQueue<>(); // pq will have vertices as elements, with d.get(v) as key
		Map<Vertex<String>, Entry<Integer,Vertex<String>>> pqTokens; pqTokens = new ProbeHashMap<>(); // maps from vertex to its pq locator
		// for each vertex v of the graph, add an entry to the priority queue, with
		// the source having distance 0 and all others having infinite distance
		for (Vertex<String> v : g.vertices()) {
			if (v == src)
				d.put(v,0);
			else
				d.put(v, Integer.MAX_VALUE);
				pqTokens.put(v, pq.insert(d.get(v), v));       // save entry for future updates
			}
		// now begin adding reachable vertices to the cloud
		while (!pq.isEmpty()) {
			Entry<Integer, Vertex<String>> entry = pq.removeMin();
			int key = entry.getKey(); Vertex<String> u = entry.getValue();
			if(u==des){
				//return cloud;
				break;
			}   
			cloud.put(u, key);   // this is actual distance to u
			pqTokens.remove(u);  // u is no longer in pq
				for (Edge<Integer> e : g.outgoingEdges(u)) {
				    Vertex<String> v = g.opposite(u,e);
				    	if (cloud.get(v) == null) {
				        // perform relaxation step on edge (u,v)
				        	int wgt = e.getElement();
				        	if (d.get(u) + wgt < d.get(v)) {              // better path to v?
		//======ADDED
		//POINT A POINT B THEN WEIGHT (SECONDS)
		            		//MyEntry vertexAB= new MyEntry(u.getElement() , v.getElement());
		            		//Integer edgeAB= (d.get(u) + wgt);
		            		//MyEntry<MyEntry<String,String>,Integer> vertexEdgeAB = new MyEntry(vertexAB, edgeAB);
		            		connectionMap.put(v.getElement(),u.getElement());
		            		connectionEdges.put(v.getElement(),d.get(u) + wgt);
		            		//connectionVertex.add(vertexAB);
		            		//connectionEdges.add(edgeAB);
		  //System.out.println(" Vertex u: "+ u.getElement()+ " Vertex v: " + v.getElement() + " Edge: " + (d.get(u) + wgt) );
		  //========END
				            	d.put(v, d.get(u) + wgt);                   // update the distance
				            	pq.replaceKey(pqTokens.get(v), d.get(v));   // update the pq entry
				          	}
				        }
				      }
				    }
	
		LinkedList<String> record = new LinkedList();

		String ans = connectionMap.get(des.getElement());
		record.addFirst(des.getElement());
		for(int i=0; i< connectionMap.size() ; i++){
			record.addFirst(ans);
			ans= connectionMap.get(ans);
			if(src.getElement().equals(ans)){
				record.addFirst(src.getElement());
				break;
			}

		}
		//Vertex<String> trace = getVertex(record.get(record.size()-1)); 	
		//Integer sum3 = connectionEdges.get(trace.getElement());	
		//shows the array of paths
		System.out.println(record.toString());
		System.out.println();
		System.out.println("PRINTING ALL THE PATH ID'S & NAMES IN BETWEEN");
		System.out.println();
		for(int i=0; i< record.size() ; i++){
			String oriID = record.get(i);
			String id = record.get(i);
			while(id.length()!=4){
     			id = "0"+ id ;
     		}
     		//CALLS My Entry to determine End ID stored in 4 digits
     		MyEntry mineEndEntry = listEntry.get(Integer.parseInt(id));

     		//Gets the name in String
     		String locationName=(String)mineEndEntry.getValue();
     		//String name = listEntry.get();
     		Vertex<String> trace = getVertex(record.get(i));
     		Integer sum2 = connectionEdges.get(trace.getElement()); 
     		if(sum2==null){
     			System.out.println( " Time Taken : " + "  0" + " seconds" + " ID: " + id + " : " + locationName ); 
     		}
     		else{
     			//Make printing nicer to read
     			int length = String.valueOf(sum2).length();
     			if(length==2){
     				System.out.println( " Time Taken :  " + sum2 + " seconds" + " ID: " + id + " : " + locationName ); 
     			}
     			else{
     				System.out.println( " Time Taken : " + sum2 + " seconds" + " ID: " + id + " : " + locationName ); 
     			}
     			
     		}
			
		}
		System.out.println();
		//check if exist a time
		if(record!=null && record.size()>0 && record.toString()!=null){
			Vertex<String> trace = getVertex(record.get(record.size()-1)); 	
			Integer sum3 = connectionEdges.get(trace.getElement());	
			System.out.println(" TOTAL TIME TAKEN : " + sum3 + " seconds ");
			System.out.println();
		}
		
  	}
  	//SHARED METHOD QUESTION 2.2 AND 2.3
  	public void printAllStations(String vertSource,String vertDes) throws Exception{
  		//If Question 2 is true
		if(question3!=true){
  			shortestPathLengthsStations(sGraphStored,getVertex(vertSource),getVertex(vertDes));
  		}
  		//If Question 3 is true
  		if(question3==true){
  			System.out.println("--------PRINTING THE SHORTEST PATH FOR QUESTION 2.3-------");
  			System.out.println();
  			shortestPathLengthsStations3(sGraphStored,getVertex(vertSource),getVertex(vertDes));
  		}
  	}
  	//================= QUESTION 2.3 =========================================
	//

  	//Modified for Question 2.3
  	public void shortestPathLengthsStations3(Graph<String,Integer> g, Vertex<String> src, Vertex<String> des) throws Exception {
		
		java.util.Map<String,String> connectionMap= new java.util.HashMap<String,String>();
		Hashtable<String,Vertex> vertices = new Hashtable<String,Vertex>();
		java.util.Map<String,Integer> connectionEdges= new java.util.HashMap<String,Integer>();
		
		Map<Vertex<String>, Integer> d = new ProbeHashMap<>(); // d.get(v) is upper bound on distance from src to v
		Map<Vertex<String>, Integer> cloud = new ProbeHashMap<>(); // map reachable v to its d value
		java.util.Map<Vertex<String>, Integer> jCloud = new java.util.HashMap<Vertex<String>, Integer>(); 
		AdaptablePriorityQueue<Integer, Vertex<String>> pq; pq = new HeapAdaptablePriorityQueue<>(); // pq will have vertices as elements, with d.get(v) as key
		Map<Vertex<String>, Entry<Integer,Vertex<String>>> pqTokens; pqTokens = new ProbeHashMap<>(); // maps from vertex to its pq locator
		// for each vertex v of the graph, add an entry to the priority queue, with
		// the source having distance 0 and all others having infinite distance
		for (Vertex<String> v : g.vertices()) {
			if (v == src)
				d.put(v,0);
			else
				d.put(v, Integer.MAX_VALUE);
				pqTokens.put(v, pq.insert(d.get(v), v));       // save entry for future updates
			}
		// now begin adding reachable vertices to the cloud
		while (!pq.isEmpty()) {
			Entry<Integer, Vertex<String>> entry = pq.removeMin();
			int key = entry.getKey(); Vertex<String> u = entry.getValue();

			// ADDED
			//BREAKS OUT FROM THE LOOP ONCE VERTEX U REACHED DESTINATION
			if(u==des){
				//return cloud;
				break;
			}
			// END ADDED
			cloud.put(u, key);   //jCloud.put(u,key);// this is actual distance to u 
			pqTokens.remove(u);  // u is no longer in pq
				for (Edge<Integer> e : g.outgoingEdges(u)) {
				    Vertex<String> v = g.opposite(u,e);
				    	if(hashSet.contains(v.getElement())){
				    		continue;
				    	}
				    	if (cloud.get(v) == null) {
				        // perform relaxation step on edge (u,v)
				        	int wgt = e.getElement();
				        	if (d.get(u) + wgt < d.get(v)) {              // better path to v?
		//======ADDED
		            		connectionMap.put(v.getElement(),u.getElement());
		            		MyEntry vertexAB = new MyEntry(v.getElement(),u.getElement()) ;
		            		connectionEdges.put(v.getElement(),d.get(u) + wgt);
		            		//connectionEdgesLL.addLast(d.get(u) + wgt);
		  //System.out.println(" Vertex u: "+ u.getElement()+ " Vertex v: " + v.getElement() + " Edge: " + (d.get(u) + wgt) );
		  //========END
				            	d.put(v, d.get(u) + wgt);                   // update the distance
				            	pq.replaceKey(pqTokens.get(v), d.get(v));   // update the pq entry	
				          	}
				        }
				 }
				    
			}
		//=========================================================================================
		LinkedList<String> record = new LinkedList();
		//LinkedList<Integer> recordEdges = new LinkedList();
		
		String ans = connectionMap.get(des.getElement());
		//=========================================================================================
		record.addFirst(des.getElement());
		for(int i=0; i< connectionMap.size() ; i++){
			record.addFirst(ans);
			ans= connectionMap.get(ans);
			if(src.getElement().equals(ans)){
				record.addFirst(src.getElement());
				break;
			}
		}
		//this is to print the list of path in between but there is 3 exceptional cases
		//System.out.println(record.toString());

		System.out.println(); //MyEntry toEnter = new MyEntry(trace2,trace);
		//System.out.println(); //Vertex<String> trace2 = getVertex(record.get(record.size()-2)); //itr = connectionEdges.values();


		Vertex<String> trace = getVertex(record.get(record.size()-1)); 	
		Integer sum3 = connectionEdges.get(trace.getElement());	
		//System.out.println();
		//System.out.println();

		if(sum3==null){
			//System.out.println(record.toString()); //debugging purpose only
			System.out.println();
			System.out.println(" No possible Path ");
			System.out.println(" Time taken : 0 " );
			System.out.println(" You have entered the same point" );
			System.out.println();
		}
		else if(sum3<0){ //if time is -ve means there is no possible path
			System.out.println(" No Possible Path " );
			System.out.println();
			System.out.println(" Time taken : Not Possible Because " + sum3);
			System.out.println();
		}
		else{ //normal case
			System.out.println(record.toString());
			System.out.println();
			System.out.println(" Time taken : "+ sum3);
			System.out.println();
			System.out.println("PRINTING ALL THE PATH ID'S & NAMES IN BETWEEN");
			System.out.println();
				for(int i=0; i< record.size() ; i++){
					String oriID = record.get(i);
					String id = record.get(i);
					while(id.length()!=4){
		     			id = "0"+ id ;
		     		}
		     		//CALLS My Entry to determine End ID stored in 4 digits
		     		MyEntry mineEndEntry = listEntry.get(Integer.parseInt(id));

		     		//Gets the name in String
		     		String locationName=(String)mineEndEntry.getValue();
		     		//String name = listEntry.get();
		     		Vertex<String> trace2 = getVertex(record.get(i));
		     		Integer sum2 = connectionEdges.get(trace2.getElement()); 
		     		if(sum2==null){
		     			System.out.println( " Time Taken : " + "  0" + " seconds" + " ID: " + id + " : " + locationName ); 
		     		}
		     		else{
		     			int length = String.valueOf(sum2).length();
		     			if(length==2){
		     				System.out.println( " Time Taken :  " + sum2 + " seconds" + " ID: " + id + " : " + locationName ); 
		     			}
		     			else{
		     				System.out.println( " Time Taken : " + sum2 + " seconds" + " ID: " + id + " : " + locationName ); 
		     			}
		     		}
					
				}
				System.out.println();
				//check if exist a time
				if(record!=null && record.size()>0 && record.toString()!=null){
					Vertex<String> trace3 = getVertex(record.get(record.size()-1)); 	
					Integer sum4 = connectionEdges.get(trace3.getElement());	
					System.out.println(" TOTAL TIME TAKEN : " + sum4 + " seconds ");
					System.out.println();
				}
		}
	}

  //================= END OF QUESTION 2.3 =========================================


	/**
	 * Generate a Graph from File and prints the vertices visited by a
	 * DepthFirstSearch
	 */
	public static void main(String[] argv) {
		if (argv.length < 1) {
			//PURPOSE:
			//IF USER DOESNT KNOW HOW TO USE THE PROGRAM
			//SHOWS INTRUCTIONS
			System.err.println("Usage: java ParisMetro fileName or N1 or N1 N2");
			System.out.println("You have not enter an Input, please enter a number between 0000 and 0375, you may ignore the leading zero of the id");
			System.out.println("For example java ParisMetro N1, where N1 is a string containing 375 or 0375");
			System.out.println("For example java ParisMetro 0000 can also be entered as java ParisMetro 0");
			System.out.println();
			System.exit(-1);
		}
		//QUESTION 2.1
		if(argv.length==1){
			try { 
				//Default readline prompts for one vertex 
				//USEFUL FOR DEBUG BEFORE STARTING THE ASSIGNMENT
				//IF USER INPUTS java ParisMetro metro.txt
				if(argv[0].equals("metro.txt")){
					//Test code for default
					ParisMetro sGraphDefault = new ParisMetro(argv[0]);
					sGraphDefault.print();
					// Ask for vertex to start
					System.out.println("Source Vertex for Shortest Path:");
					sGraphDefault.printAllShortestDistances(readVertex());
				}

				else{ 
					//equal to N1 (ID of specified station)
					if( (Integer.parseInt(argv[0])>=0 && Integer.parseInt(argv[0]) <=375 )){
						// to avoid errors if user enters 4 digits
						Integer safety1= Integer.parseInt(argv[0]);  
						String n1 = safety1.toString();
						ParisMetro sGraphN1 = new ParisMetro("metro.txt",n1);
						//sGraphN1.printAllShortestDistances(n1);
						sGraphN1.printSameLine();
					}
					else{
						System.out.println("You have entered an invalidInput, please enter a number between 0000 and 0375, you may ignore the leading zero of the id");
					}	
				}
		} 		
			catch (Exception except) {
				System.out.println(" INVALID INPUT PLEASE TRY AGAIN ");
				System.out.println();
				System.exit(-1);
				System.err.println(except);
				except.printStackTrace();
			}

		}
		//QUESTION 2.2
		if(argv.length==2){
			try {
				// to avoid errors if user enters 4 digits
				Integer safety1= Integer.parseInt(argv[0]);  
				String n1 = safety1.toString();
				Integer safety2= Integer.parseInt(argv[1]);  
				String n2 = safety2.toString();

				ParisMetro sGraph2 = new ParisMetro("metro.txt",n1,n2);

				//sGraph2.print();
				// Ask for vertex to start
				//System.out.println("Source Vertex for Shortest Path:");
				sGraph2.printAllShortestDistances(n1,n2);
				System.out.println();
				sGraph2.printAllStations(n1,n2);		     
		} 
			catch (Exception except) {
				System.err.println(except);
				except.printStackTrace();
			}
		}
		//QUESTION 2.3
		if(argv.length==3){
			try {
				// to avoid errors if user enters 4 digits
				Integer safety1= Integer.parseInt(argv[0]);  String n1 = safety1.toString();
				Integer safety2= Integer.parseInt(argv[1]);  String n2 = safety2.toString();
				Integer safety3= Integer.parseInt(argv[2]);  String n3 = safety3.toString();

				ParisMetro sGraph3 = new ParisMetro("metro.txt",n1,n2,n3);
				//sGraph2.print();
				// Ask for vertex to start
				//System.out.println("Source Vertex for Shortest Path:");
				//sGraph3.printAllShortestDistances(argv[0],argv[1]);
				System.out.println();
				sGraph3.printAllStations(n1,n2);		     
		} 
			catch (Exception except) {
				System.err.println(except);
				except.printStackTrace();
			}
		}
		else if(argv.length>3){ //ELSE INVALID INPUT AND TEACHES THE USER TO HANDLE THE PROGRAM
			System.out.println();
			System.err.println(" ----------------INVALID INPUT ----------------");
			System.err.println("Usage: java ParisMetro fileName or N1 or N1 N2");
			System.out.println("You have not enter an Input, please enter a number between 0000 and 0375, you may ignore the leading zero of the id");
			System.out.println("For example java ParisMetro N1, where N1 is a string containing 375 or 0375");
			System.out.println("For example java ParisMetro 0000 can also be entered as java ParisMetro 0");
			System.out.println();
			System.exit(-1);
		}
	}
}
