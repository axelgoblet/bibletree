package bibletree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphTest {
	
	public static void main(String[] args) throws IOException  {
		
		File input = new File("../formattedRelations.csv");
		String text = "";
		Scanner scanner = new Scanner(input);
		text = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		List<Relation> relationList = new ArrayList<Relation>();
		
		String[] lineList = text.split("\n");
		
		// fill relationList
		for ( int i = 0; i < lineList.length; i++ ) {
			String[] relation = lineList[i].split(",");
			String part1 = relation[0].replace("\n", "").replace("\r", "");
			String part2 = relation[1].replace("\n", "").replace("\r", "");
			String part3 = relation[2].replace("\n", "").replace("\r", "");
			
			if (part2.contains("daughter")){
				relationList.add(new Relation(part3, part1, "unknown", "female"));
			} else if ( part2.contains("son") ){
				relationList.add(new Relation(part3, part1, "unknown", "male"));
			} else if ( part2.contains("child") ){
				relationList.add(new Relation(part3, part1, "unknown", "unknown"));
			} else if ( part2.contains("mother") ){
				relationList.add(new Relation(part1, part3, "female", "unknown"));
			} else if ( part2.contains("father") ){
				relationList.add(new Relation(part1, part3, "male", "unknown"));
			} else if ( part2.contains("parent") ){
				relationList.add(new Relation(part1, part3, "unknown", "unknown"));
			} else{
				System.out.println("incorrect relation");
			}
		}
		
		
		Graph graph = new MultiGraph("Test1");
		
		for(Relation relation : relationList){
			Node node1;
			Node node2;
			if (graph.getNode(relation.getParent()) == null){
				node1 = graph.addNode(relation.getParent());
				node1.setAttribute("ui.label", relation.getParent());
				node1.addAttribute("ui.style", "text-size: 20; fill-color: #000000;");
				if ( relation.getParentGender().equals("female") ){
					node1.addAttribute("ui.style", "text-size: 20; fill-color: #ffb6c1;");
				}
				else if ( relation.getParentGender().equals("male") ){
					node1.addAttribute("ui.style", "text-size: 20; fill-color: #89cff0;");
				}
			}
			else {
				node1 = graph.getNode(relation.getParent());
			}
			
			if (graph.getNode(relation.getChild()) == null){
				node2 = graph.addNode(relation.getChild());
				node2.setAttribute("ui.label", relation.getChild());
				node2.addAttribute("ui.style", "text-size: 20; fill-color: #000000;");
				if ( relation.getChildGender().equals("female") ){
					node2.addAttribute("ui.style", "text-size: 20; fill-color: #ffb6c1;");
				}
				else if ( relation.getChildGender().equals("male") ){
					node2.addAttribute("ui.style", "text-size: 20; fill-color: #89cff0;");
				}
			}
			else {
				node2 = graph.getNode(relation.getChild());
			}
			
			
			if (graph.getEdge(relation.getParent()+relation.getChild()) == null){
				Edge edge = graph.addEdge(relation.getParent()+relation.getChild(), node1, node2, true);
				edge.addAttribute("ui.style", "size: 1px;");
			}
		}
		
//		SpriteManager sman = new SpriteManager(graph);
//		Sprite s = sman.addSprite("S1");
//		s.getLabel("YO TEST");
//		s.setPosition(0.1, 1, 0);
//		s.attachToNode("A");	
		
		
		Viewer viewer = graph.display();
		View view = viewer.getDefaultView();
		view.addMouseListener(new GraphMouseListener(view));
		view.addKeyListener(new GraphKeyListener(view));
		
	}

	//-------------------------------------------------------------------------------//
	// CONSTANTS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// MEMBERS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// CONSTRUCTORS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// PUBLIC METHODS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// PRIVATE METHODS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// IMPLEMENTED METHODS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// MEMBERS
	//-------------------------------------------------------------------------------//

	//-------------------------------------------------------------------------------//
	// INNER CLASSES
	//-------------------------------------------------------------------------------//
	
	public static class GraphMouseWheelListener implements MouseWheelListener{

		
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static class GraphKeyListener implements KeyListener{

		View view;
		
		public GraphKeyListener(View view) {
			this.view = view;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
		    int keyCode = e.getKeyCode();
		    double x = view.getCamera().getViewCenter().x;
		    double y = view.getCamera().getViewCenter().y;
		    double z = view.getCamera().getViewCenter().z;
		    switch( keyCode ) { 
		        case KeyEvent.VK_UP:
		            view.getCamera().setViewCenter(x, y+(0.5*view.getCamera().getViewPercent()), z); 
		            break;
		        case KeyEvent.VK_DOWN:
		            view.getCamera().setViewCenter(x, y-0.5*view.getCamera().getViewPercent(), z); 
		            break;
		        case KeyEvent.VK_LEFT:
		            view.getCamera().setViewCenter(x-0.5*view.getCamera().getViewPercent(), y, z); 
		            break;
		        case KeyEvent.VK_RIGHT :
		            view.getCamera().setViewCenter(x+0.5*view.getCamera().getViewPercent(), y, z); 
		            break;
		     }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class GraphMouseListener implements MouseListener{

		View view;
		
		public GraphMouseListener(View view) {
			super();
			this.view = view;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(e.getX() + "    " + e.getY());
			if (e.getButton() == e.BUTTON1){
				view.getCamera().setViewPercent(Math.max(view.getCamera().getViewPercent() - 0.05, 0.01));
				System.out.println(view.getCamera().getViewPercent());
			}
			else if ( e.getButton() == e.BUTTON3 ) {
				view.getCamera().setViewPercent(Math.max(view.getCamera().getViewPercent() + 0.05, 0.01));
				System.out.println(view.getCamera().getViewPercent());
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static class Relation{
		public String parent;
		public String getParent() { return this.parent; }
		public String parentGender;
		public String getParentGender() { return this.parentGender; }
		public String child;
		public String getChild() { return this.child; }
		public String childGender;
		public String getChildGender() { return this.childGender; }
		
		public Relation(String parent, String child, String parentGender, String childGender){
			this.parent = parent;
			this.child = child;
			this.parentGender = parentGender;
			this.childGender = childGender;
		}
	}
	
}

