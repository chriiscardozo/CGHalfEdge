package core;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FileHalfEdges {
	public HashMap<String, Face> faces = new HashMap<String, Face>();
	public HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
	public HashMap<String, HalfEdge> halfEdges = new HashMap<String, HalfEdge>();
	
	public FileHalfEdges(String path) throws IOException{
		loadHalfEdges(path);
	}
	
	private void loadHalfEdges(String path) throws IOException{
		FileInputStream stream = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String linha = br.readLine();

		while(linha != null) {
			if(linha.length() > 0){
				String values[] = linha.split(" ");
				if(values[0].charAt(0) == 'f'){
					//f1 e1_1
					
					Face f = new Face();
					f.id = values[0];
					f.halfEdge = values[1];
					
					faces.put(f.id, f);
				}
				else if(values[0].charAt(0) == 'e'){
					//e1_1 v1 e1_2 f1 e2_1
					
					HalfEdge h = new HalfEdge();
					h.id = values[0];
					h.originVertex = values[1];
					h.nextHalfEdge = values[2];
					h.face = values[3];
					h.oppositeHalfEdge = values[4];
					
					halfEdges.put(h.id, h);
				}
				else if(values[0].charAt(0) == 'v'){
					//v1 1 1 e1_1
					
					Vertex v = new Vertex();
					v.id = values[0];
					v.x = Integer.parseInt(values[1]);
					v.y = Integer.parseInt(values[2]);
					v.halfEdge = values[3];
					
					vertices.put(v.id, v);
				}
			}
			
			linha = br.readLine();
		}
		
//		for(String key : faces.keySet()){
//			System.out.println(faces.get(key).id);
//		}
//		
//		for(String key : vertices.keySet()){
//			System.out.println(vertices.get(key).id);
//		}
//		
//		for(String key : halfEdges.keySet()){
//			System.out.println(halfEdges.get(key).id);
//		}
	}
}
