package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import core.Face;
import core.FileHalfEdges;
import core.HalfEdge;
import core.Vertex;

@SuppressWarnings("serial")
public class HalfEdgeWindow extends JPanel {

	private static final int MAX_COORDINATE = 15;
	private static final int PREF_W = 800;
	private static final int PREF_H = 600;
	private static final int BORDER_GAP = 30;
	private static final Color GRAPH_COLOR = Color.green;
	private static final Color GRAPH_POINT_COLOR = Color.black;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
	private static final int GRAPH_POINT_WIDTH = 12;
	private static final int Y_HATCH_CNT = MAX_COORDINATE;
	HashMap<String, Face> faces;
	HashMap<String, Vertex> vertices;
	HashMap<String, HalfEdge> halfEdges;

	public HalfEdgeWindow(HashMap<String, Face> faces, HashMap<String, Vertex> vertices, HashMap<String, HalfEdge> halfEdges) {
		this.faces = faces;
		this.vertices = vertices;
		this.halfEdges = halfEdges;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (MAX_COORDINATE - 1);
		double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_COORDINATE - 1);

		// create x and y axes 
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

		// create hatch marks for y axis. 
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (Y_HATCH_CNT) + BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);
		}
		
		for(String key : faces.keySet()){
			Face f = faces.get(key);
			String initialHalfEdge = f.halfEdge;
			HalfEdge h = halfEdges.get(initialHalfEdge);
			
			do{
				// Print actual origin vertex
				int x1 = (int) (vertices.get(h.originVertex).x * xScale + BORDER_GAP);
				int y1 = (int) ((MAX_COORDINATE - 1 - vertices.get(h.originVertex).y) * yScale + BORDER_GAP);
				printVertexOnScreen(x1, y1, g2);
				
				HalfEdge prox = halfEdges.get(h.nextHalfEdge);
				int x2 = (int) (vertices.get(prox.originVertex).x * xScale + BORDER_GAP);
				int y2 = (int) ((MAX_COORDINATE - 1 - vertices.get(prox.originVertex).y) * yScale + BORDER_GAP);
				
				Stroke oldStroke = g2.getStroke();
				printEdgeOnScreen(x1, y1, x2, y2, g2);
				g2.setStroke(oldStroke);
				
				h = prox;
			} while(!initialHalfEdge.equals(h.id));
		}
	}

	private void printVertexOnScreen(int x1, int y1, Graphics2D g2) {
		g2.setColor(GRAPH_POINT_COLOR);
		g2.fillOval(x1 - GRAPH_POINT_WIDTH / 2, y1 - GRAPH_POINT_WIDTH / 2, GRAPH_POINT_WIDTH, GRAPH_POINT_WIDTH);
	}
	
	private void printEdgeOnScreen(int x1, int y1, int x2, int y2, Graphics2D g2){
		g2.setColor(GRAPH_COLOR);
		g2.setStroke(GRAPH_STROKE);
		g2.drawLine(x1, y1, x2, y2);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	private static void createAndShowGui() throws IOException {
		FileHalfEdges fhe = new FileHalfEdges("input.txt");

		HalfEdgeWindow mainPanel = new HalfEdgeWindow(fhe.faces, fhe.vertices, fhe.halfEdges);

		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGui();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}