	void printAllShortestDistancesLab(String vert) throws Exception {
		Vertex<String> vSource = getVertex(vert);
		
		GraphAlgorithms dj = new GraphAlgorithms();
		Map<Vertex<String>, Integer> result = dj.shortestPathLengths(sGraphStored, vSource);
		
		// Print shortest path to named cities
		for (Vertex<String> vGoal : sGraphStored.vertices()) {
			if (vGoal.getElement().length() > 2) {
				//System.out.println(vSource.getElement() + " to " + vGoal.getElement() + " = " + result.get(vGoal));

				MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement()));
     		
     			String initialLocationID= (String)mineEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     			String initialLocationName= (String)mineEntry.getValue(); //System.out.println(mineEntry.getValue()); //Abbessess

     			MyEntry mineEndEntry = listEntry.get(Integer.parseInt(vGoal.getElement()));

     			String endLocationID= (String)mineEndEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     			String endLocationName= (String)mineEndEntry.getValue();

     			Integer edgeDistance= result.get(vGoal);

        		System.out.println("StartID: " + initialLocationID + " InitialName: " +initialLocationName 
        			+" To EndID: " + endLocationID +" Destination: " +endLocationName + " = " + edgeDistance +" seconds" );
			}
		}
		return;
	}
	void printAllShortestDistancesLab(String vert, String vert2) throws Exception {
		Vertex<String> vSource = getVertex(vert);
		Vertex<String> vSource2= getVertex(vert2);
		MyEntry wantedEntry = listEntry.get(Integer.parseInt(vSource2.getElement()));
     	String wantedLocationID=(String)wantedEntry.getKey();
		GraphAlgorithms dj = new GraphAlgorithms();
		Map<Vertex<String>, Integer> result = dj.shortestPathLengths(sGraphStored, vSource);
		// Print shortest path to named cities
		for (Vertex<String> vGoal : sGraphStored.vertices()) {
			if (vGoal.getElement().length() > 2) {
				//System.out.println(vSource.getElement() + " to " + vGoal.getElement() + " = " + result.get(vGoal));

				MyEntry mineEntry = listEntry.get(Integer.parseInt(vSource.getElement()));
     		
     			String initialLocationID= (String)mineEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     			String initialLocationName= (String)mineEntry.getValue(); //System.out.println(mineEntry.getValue()); //Abbessess

     			MyEntry mineEndEntry = listEntry.get(Integer.parseInt(vGoal.getElement()));

     			String endLocationID= (String)mineEndEntry.getKey(); //System.out.println(mineEntry.getKey()); //0000
     			String endLocationName= (String)mineEndEntry.getValue();

     			Integer edgeDistance= result.get(vGoal);
     			System.out.println(endLocationID +"   VS  " + wantedLocationID);
     			if(endLocationID.equals(wantedLocationID)){
     				System.out.println("StartID: " + initialLocationID + " InitialName: " +initialLocationName 
        			+" To EndID: " + endLocationID +" Destination: " +endLocationName + " = " + edgeDistance +" seconds" );
     			}
			}
		}
		return;
	}