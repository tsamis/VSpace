package main;




import java.io.File;
import java.io.PrintWriter;
import java.text.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import processing.core.*;
import processing.dxf.*;
import controlP5.*;
import controller.InterfaceController;
import peasy.*;
import util.General;

import javax.swing.JButton;

import operations.BooleanAddVisitor;
import operations.BooleanIntersectVisitor;
import operations.BooleanOperation;
import operations.BooleanSubstractVisitor;
import operations.BooleanVisitable;
import operations.BooleanVisitor;
import operations.PropertyBooleanOperation;
import operations.SpecialBooleanOperation;
import operations.SurfaceBooleanOperation;

import commands.BlendCommand;
import commands.Command;
import commands.CreateCommand;
import commands.LoadImagesCommand;
import commands.PaintCommand;
import commands.PrepareExportCommand;
import commands.UndoableCommand;

import model.BZGrid;
import model.BZGrid.CreationMethod;
import model.GeometryMode;
import model.Voxel;

import java.awt.Button;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/*
 * This class handles the creation of the BZ grid. All the parameters
 * related to the creation and visualization are also in this class.
 * 
 */
public class BZEclipse extends PApplet implements ComponentListener{
	
	public BZEclipse() {
		
		try{
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName()
		     );
			//BZFrame frame = new BZFrame();
			//frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	String pattern ="###.##";
	DecimalFormat formater = new DecimalFormat(pattern);
   
	Queue<Command> pendingCommands;
	Stack<UndoableCommand> cleanupCommands;
	ArrayList<BooleanOperation> booleanOperations;
	int newX=30,newY=30,newZ=2;
	public ControlP5 controlP5;
	public BZGrid space;
	public PeasyCam cam;
	public int wDx = 30, wDy = 30, wDz = 3;
	public int interface_w=200;
	public float speedvar = 1;
	public float speeddec = 0;
	//TODO make ambient buttons
	Boolean offsetA=false, offsetB=false,offsetC=false;
	float ambienceValue,shininessValue,specularValue,ambientlight;
	Voxel[] vox; 
	PFont fontA;
	float iso = 0.1f;
	int sign = 1;
	int debugLvl = 0;
	float fpstime1=0;
	float fpstime0=0;
	float fps = 0;
	Boolean useAvg=false;
	Boolean useBuffer 	  = true;
	public Boolean exportIt=false;
	Boolean enableOffset= false;
	Boolean enableTrim = false;
	Boolean enableTrimDisplay =false;
	Boolean buttonsReady = false;
	int booleanOperation = 0; //1 = add, 2= substract, 3 = intersect
	int abc5 = 0; //0=a, 1=b, 2 = c
	BooleanOperation boolOperation;
	GeometryMode mode = GeometryMode.normal;
	
	Boolean enablePropBoolean=false;
	Boolean enableSurfaceBoolean=true;
	
	public float booleanthreshold = 0;
	public float trimThreshold=0;
	public float offset = 0;
	public float voxthreshold=0.5f;
	public float voxthreshold2=0f;
	public float boundthreshold = 0.5f;
	public float avg_level = 0;
	
	BZGrid.CreationMethod creationMethod=BZGrid.CreationMethod.random;
	PrintWriter output;
	public int[] axis= new int[3]; //The blending axis for image loading. 0:x 1:y  2:z, made into array in case we need to make cool stuff later
	int abc1= 0; //The voxthreshold material 0:a  1:b  2:c, 
	int abc2= 0; //The marching cube material 0:a  1:b  2:c
	int abc3= 0; //The offset material  0:a 1:b  2:c
	int abc4= 0;
	
	int booleanAbc1=0;
	int booleanAbc2=0;
	public boolean specialbool;
	public boolean showsurface_a;
	public boolean showsurface_b;
	public boolean showsurface_c;
	public float isolevel_a;
	public float isolevel_b;
	public float isolevel_c;
	public float trimlevel_a;
	public float trimlevel_b;
	public float trimlevel_c;
	public boolean color_a;
	public boolean color_b;
	public boolean color_c;
	public boolean trim_a;
	public boolean trim_b;
	public boolean trim_c;
	public boolean trim_a_invert;
	public boolean trim_b_invert;
	public boolean trim_c_invert;
	public int trim_a_with;
	public int trim_b_with;
	public int trim_c_with;
	public float paramF=0.035f;
	public float paramK=0.064f;
	public float paramDu=0.082f;
	public float paramDv=0.041f;
	
	public PImage imageFrom;
	public PImage imageTo;
	
	InterfaceController inter;
	public boolean showSurface=false;
	private boolean showMat=false;
	public boolean showVox=true;
	private boolean play=false;
	private boolean step = false;
	PGraphics pickbuffer;
	
	
	float[] bufferPick;
	Boolean showvariance = false;
	public void setup() {
		boolOperation=null;
		useBuffer = false;
		bufferPick=new float[3];
		size(900,900,P3D);
		//this.frame.setResizable(true);
		//this.frame.addComponentListener(this);
		
		pendingCommands = new LinkedList<Command>();
		cleanupCommands = new Stack<UndoableCommand>();
		booleanOperations = new ArrayList<BooleanOperation>();
		pickbuffer	= createGraphics(width, height, P3D);
		
		cam = new PeasyCam(this, newX/2, newY/2, newZ/2, 153.7803426016711);
		  cam.setMinimumDistance(10);
		  cam.setMaximumDistance(5000);
		  cam.setRotations(-PI/4, PI/4, PI/4);
		  //cam.lookAt(newX/2,newY/2, newZ/2);
		colorMode(RGB,255);
		controlP5 = new ControlP5(this);
		axis[0]=1;
		
		inter = new InterfaceController(this);//Initialize components
		isolevel_a = 0.5f;
		isolevel_b = 0.5f;
		isolevel_c = 0.5f;
		trimlevel_a = 0.5f;
		trimlevel_b = 0.5f;
		trimlevel_c = 0.5f;
		background(0);
		float[] anypos = new float[3];
		anypos[0]=-10f;
		anypos[1]=-10f;
		anypos[2]=-10f;
		selectable = new Voxel(-1,anypos, 1, 1, 1);
	}
	Voxel selectable;
	void updatePickerLabel(){
		String text = "Picking Info: ";
		if(useBuffer){
			int x = floor(bufferPick[0]);
			int y = floor(bufferPick[1]);
			int z = floor(bufferPick[2]);
			if(space==null){
				text+="<Create a simulation first>";
			}else{
				if(showVox){
					if(x<=99&&y<=99&&z<=99){
						text+= "X:"+x+" Y:"+y+" Z:"+z+"  ";
						
						text+= "CONC: "+formater.format(space.a[x][y][z][space.p])+";"+formater.format(space.b[x][y][z][space.p])+";"+formater.format(space.c[x][y][z][space.p]);
					}else{
						text+= "<NO SELECTION>";
					}
				}else{
					text+="<Voxel mode is required>";
				}
			}
			
		}else{
			text += "Picker Disabled";
		}
		text+=":..Iterations:"+c;
		inter.pickerLabel.setText(text);
	}
	long c = 0;
	public void update()
	{
		String debugtext ="No debug info";
		if(space!=null){
			if(play){
				double milis = millis();
				space.setReactionSpeed(speedvar+speeddec);
				int limitn = (int) inter.slider_skip.getValue();
				for(int i = 1; i<=limitn;i++){
				if(inter.ddl_reaction.getValue()==0){
					space.update(avg_level);
				}else{
					space.GSupdate(avg_level);
				}
				c++;
				}
				if(step)
				{
					play=false;
					step=false;
				}
				
				milis = millis()-milis;
				//println("+"+milis);
			}
			space.updateVoxels();
			debugtext = "CAMERA POSITION: X:"+formater.format(cam.getPosition()[0])+"" +
					"Y:"+formater.format(cam.getPosition()[1])+"" +
					"Z:"+formater.format(cam.getPosition()[2])+"";
		}
		
		
		inter.debugLabel.setText(debugtext);

		updatePickerLabel();
		long endTime = System.currentTimeMillis();
	}
	public void button_booleanOff_pressed(){
		booleanOperation= 0;
	}
	private int getAbcFromRadio(RadioButton r) {
		int ret=-1;
		if(r.getState(0)){
			ret=0;
		}else if(r.getState(1)){
			ret=1;
		}else if(r.getState(2)){
			ret=2;
		}else{
		}
		return ret;
	}	
	
	
	public void createSpace(CreationMethod method){
		try{
			this.updateCreationValues();
		}catch(Exception e){
			System.err.println("Textbox must contain values");
		}
		pickbuffer.height = this.height;
		pickbuffer.width = this.width;
		space = new BZGrid(newX,newY,newZ,method,this,imageFrom,imageTo,axis);
		cam.lookAt(newX/2,newY/2, newZ/2);
		
	}
	
	public void keyPressed(){
		if(this.key==32){//Spacebar
			button_play_pressed();
		}
		
		if(this.key==this.CODED){
			
			if(this.keyCode==18){//alt
				this.cam.setMouseControlled(true);
				cameraenabled=true;
			}
		}
	}
	public void keyReleased(){
		if(this.key==this.CODED){
			if(this.keyCode==18){//alt
				if(useBuffer){
				cameraenabled=false;
				this.cam.setMouseControlled(false);
				}
			}
		}
	}
	public void draw() {
		
		long initTime = System.currentTimeMillis();
		inter.update();
		
		showVox=true;
		trim_a=true;
		trim_b=true;
		trim_c=true;
		int temp=getAbcFromRadio(inter.radio_show_vox);
		if(temp!=-1){
			abc1=temp;
		}else{
			showVox=false;
		}
		temp=getAbcFromRadio(inter.radio_trimA);
		if(temp!=-1){
			trim_a_with=temp;
		}else{
			trim_a=false;
		}
		
		temp=getAbcFromRadio(inter.radio_trimB);
		if(temp!=-1){
			trim_b_with=temp;
		}else{
			trim_b=false;
		}
		
		temp=getAbcFromRadio(inter.radio_trimC);
		if(temp!=-1){
			trim_c_with=temp;
		}else{
			trim_c=false;
		}

		
		showsurface_a = inter.checkbox_surface.getState(0);
		showsurface_b = inter.checkbox_surface.getState(1);
		showsurface_c = inter.checkbox_surface.getState(2);
		
		color_a = inter.checkbox_color_surface.getState(0);
		color_b = inter.checkbox_color_surface.getState(1);
		color_c = inter.checkbox_color_surface.getState(2);
		
		trim_a=inter.checkbox_trim.getState(0);
		trim_b=inter.checkbox_trim.getState(1);
		trim_c=inter.checkbox_trim.getState(2);
		
		

		
		exportIt = false;
		
		
		//True pending queue implementation:
		while(!pendingCommands.isEmpty()){
			Command c = pendingCommands.poll();
			c.execute();
			//System.out.println("Executed a Command: "+c.getName());
		}
		
		if(space!=null){
			update();
			
			lights();
			
			directionalLight(100,100, 100, -cam.getPosition()[0], -cam.getPosition()[1], -cam.getPosition()[2]);
			
			shininess(2);
			background(255,255,255);
			
		
			//emissive(0.1f);
			
			
			
		    if(pickbuffer.height!=this.height||pickbuffer.width!=this.width){

				pickbuffer.height = this.height;
				pickbuffer.width = this.width;
		    }
	    	pickbuffer.beginDraw();
	    	pickbuffer.setMatrix(g.getMatrix());
	    	pickbuffer.background(
	    	pickbuffer.color(255,255,255));
	    	
	    	
	    	space.drawGeometry(pickbuffer, voxthreshold,voxthreshold2, 0, 0, abc1, 0,GeometryMode.id);
	    	
	    	//buffer.save("foto.png");
			
			
			
			
			pickbuffer.endDraw();
				
				
			if(exportIt){
				double timestamp = System.currentTimeMillis()/1000;
				output = createWriter("BZexport_voxels"+timestamp+".txt");
				space.exportVoxels(output,voxthreshold,voxthreshold2,0,0,abc1,0,mode);
			    output.flush(); // Writes the remaining data to the file
			    output.close(); // Finishes the file
			    
				showVox=false;
				if(!(showSurface||showMat||enableTrimDisplay||enableOffset)){
					showSurface=true;
				}
				beginRaw(DXF,"BZexport_voxels"+timestamp+".dxf");
				
			}
			if(showVox){
					space.drawGeometry(this.g,voxthreshold,voxthreshold2,0,0,abc1,abc5,mode);
					
			}
			
			showSelectedSurfaces();
			
			drawBooleanOperation();
			
		    if(exportIt){
		        endRaw();
		         exportIt=false;
		      }
		    if(useBuffer&&selectable.px<space.dimX&&showVox){
		    	g.beginShape(PApplet.QUADS);
		    	selectable.EnableRegular(g);
				selectable.draw(g);
				g.endShape();
		    }
		}
		
		//If there are any cleanup commands to execute, do it now.
		while(!cleanupCommands.isEmpty()){
			UndoableCommand c = cleanupCommands.pop();
			c.undo();
			//System.out.println("Undone a Command: "+c.getName());
		}
	}
	boolean drawing = false;
	boolean cameraenabled=false;
	public void mousePressed() {
		if(!useBuffer||cameraenabled){
			return;
		}
		drawing = true;
		issuePaintCommand();
	}
	
	public void issuePaintCommand(){
		if(space==null||cameraenabled){
			return;
		}
		
		
		float paintcolorA= red(inter.cpick.getColorValue());
		float paintcolorB= green(inter.cpick.getColorValue());
		float paintcolorC= blue(inter.cpick.getColorValue());
		try{
			double previous = space.a[(int)this.bufferPick[0]][(int)this.bufferPick[1]][(int)this.bufferPick[2]][space.q];
			int x = (int)this.bufferPick[0];
			int y = (int)this.bufferPick[1];
			int z = (int)this.bufferPick[2];
			int r = (int)inter.paintsizeslider.getValue();
			float op = inter.paintopacityslider.getValue();
			Command com = new PaintCommand(this.space, x, y+1, z+1, paintcolorA, paintcolorB, paintcolorC, r-1,op);
			pendingCommands.add(com);
			
		}catch(IndexOutOfBoundsException ex){
		}
	}
	public void mouseDragged() {
		if(space==null){
			return;
		}
		bufferPick[0]=0.5f+(float)Math.floor(pickbuffer.red(pickbuffer.get(mouseX,mouseY)));
		bufferPick[1]=0.5f+(float)Math.floor(pickbuffer.green(pickbuffer.get(mouseX,mouseY)));
		bufferPick[2]=0.5f+(float)Math.floor(pickbuffer.blue(pickbuffer.get(mouseX,mouseY)));
		selectable = new Voxel(-1,bufferPick,1,1,1);
		  if(drawing){
			  issuePaintCommand();
		  }
	}
	public float getValueNoiseFromInterface(){
		return inter.slider_gs_noise.getValue();
	}
	public void mouseMoved(){
		if(space==null){
			return;
		}
		bufferPick[0]=0.5f+(float)Math.floor(pickbuffer.red(pickbuffer.get(mouseX,mouseY)));
		bufferPick[1]=0.5f+(float)Math.floor(pickbuffer.green(pickbuffer.get(mouseX,mouseY)));
		bufferPick[2]=0.5f+(float)Math.floor(pickbuffer.blue(pickbuffer.get(mouseX,mouseY)));
		selectable = new Voxel(-1,bufferPick,1,1,1);
	}
	public void mouseReleased(){
		if(drawing){
			drawing=false;
		}
	}
	
	private void showSelectedSurfaces() {
		if(showsurface_a){
			space.marchCube(isolevel_a, 0, color_a,trim_a,trim_a_with,trimlevel_a,trim_a_invert, g);
			if(offsetA){
				space.marchCube(isolevel_a+offset, 0, color_a,trim_a,trim_a_with,trimlevel_a,trim_a_invert, g);
			}
		}if(showsurface_b){
			space.marchCube(isolevel_b, 1, color_b,trim_b,trim_b_with,trimlevel_b,trim_b_invert, g);
			if(offsetB){
				space.marchCube(isolevel_b+offset, 1, color_b,trim_b,trim_b_with,trimlevel_b,trim_b_invert, g);
			}
		}if(showsurface_c){
			
			space.marchCube(isolevel_c, 2, color_c,trim_c,trim_c_with,trimlevel_c,trim_c_invert, g);
			if(offsetC){
				space.marchCube(isolevel_c+offset, 2, color_c,trim_c,trim_c_with,trimlevel_c,trim_c_invert, g);
			}
		}
		
	}

	/*
	 * This was just an experiment
	public static void main(String _args[]) {
		
		
		PApplet.main(new String[] {"main.BZEclipse" });
		
	}*/
	public int[] button_colors(){
		int[] result = new int[50];

		colorMode(RGB,1);
		int usedColor = color(255, 170, 0);
		for(int i=0;i<result.length;i++){
			result[i] = 90;
		}
		if (showVox) {
		    if (abc1==0) {
		    	result[23]=usedColor;
		    }
		    if (abc1==1) {
		    	result[24]=usedColor;
		    }
		    if (abc1==2) {
		    	result[25]=usedColor;
		    }
		  }
		  else {
		  }
		  if (showMat||showSurface) {
		    if (abc2==0) {
		    	result[26]=usedColor;
		    }
		    if (abc2==1) {
		    	result[27]=usedColor;
		    }
		    if (abc2==2) {
		    	result[28]=usedColor;
		    }
		  }
		  else {
		    
		  }
		  if (enableOffset) {
		    if (abc3==0) {
		    	result[29]=usedColor;
		    }
		    if (abc3==1) {
		    	result[30]=usedColor;
		    }
		    if (abc3==2) {
		    	result[31]=usedColor;
		    }
		  }
		  else {
		    
		  }
		  if (enableTrim) {
		    if (abc4==0) {
		    	result[32]=usedColor;
		    }
		    if (abc4==1) {
		    	result[33]=usedColor;
		    }
		    if (abc4==2) {
		    	result[34]=usedColor;
		    }
		  }
		  else {
		    
		  }

		  if (booleanOperation!=0) {
		    if (abc5==0) {
		    	result[41]=usedColor;
		    }
		    if (abc5==1) {
		    	result[42]=usedColor;
		    }
		    if (abc5==2) {
		    	result[43]=usedColor;
		    }
		  }
		  else {

		  }
		  return result;
	}
	public void button_material_pressed() {
	
		if (!showMat) {
		      showSurface = false; 
		      showMat=true;
		    }
		    else if (showVox) {
		      showMat=false;
		    }
		
	}
	public void button_surface_pressed() {
		if (!showSurface) {
		      showSurface=true; 
		      showMat=false;
		    } 
		    else if (showVox) {
		      showSurface=false;
		    }   
		
	}
	public void button_showVox_pressed() {
	    if (showVox) showVox=false; 
	    else showVox = true;
		
	}
	public void button_play_pressed() {
	    if (play) { 
		      play=false;
		    }
		    else { 
		      play = true;
		    }
		
	}
	public void button_offset_pressed() {
	    if (enableOffset) enableOffset=false; 
	    else enableOffset = true;
		
	}
	public void setCameraTop() {
		cam.setRotations(0, 0, 0);
		space.Tests();
	}
	public void setCameraBottom() {
		cam.setRotations(PI, 0, 0);
		
	}
	public void setCameraFront() {
		cam.setRotations(-PI/2, 0, 0);
		
	}
	public void setCameraBack() {
		cam.setRotations(-PI/2, PI, 0);
		
	}
	public void setCameraLeft() {
		cam.setRotations(-PI/2, -PI/2, 0);
		
	}
	public void ddl_boolean_operator(){
	}
	public void setCameraRight() {
		 cam.setRotations(-PI/2, PI/2, 0);
		
	}
	public void button_slabs_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.slabs));
	}
	public void button_export_pressed() {
		pendingCommands.add(new PrepareExportCommand(this));
	}
	public void button_creation3grid_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.grid3x3x3));
	}
	public void button_creategs_pressed() {
		pendingCommands.add(new CreateCommand(this, CreationMethod.randomgs));
	}
	public void button_createfill_pressed() {
		pendingCommands.add(new CreateCommand(this, CreationMethod.fill));
	}
	public void button_creation3gridrandom_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.randomGrid3x3x3));
	}
	public void button_creationTrigWaves_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.creationTrigWaves));
	}
	public void button_creationRandom_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.random));
	}
	public void button_composite_pressed() {
	    pendingCommands.add(new CreateCommand(this, CreationMethod.composite));
	}
	public void button_creationImages_pressed() {
	    pendingCommands.add(new LoadImagesCommand(this));
	    pendingCommands.add(new CreateCommand(this, CreationMethod.images));
	}
	public void loadImageFrom(){
		this.imageFrom = General.loadImageFile(this);
	}
	public void loadImageTo(){
		this.imageTo = General.loadImageFile(this);
		
	}
	public void button_setXAxis_pressed() {
	    axis[0]=1;
	    axis[1]=0;
	    axis[2]=0;
	}
	public void button_setYAxis_pressed() {
	    axis[0]=0;
	    axis[1]=1;
	    axis[2]=0;
	}
	public void button_setZAxis_pressed() {
	    axis[0]=0;
	    axis[1]=0;
	    axis[2]=1;
	}
	public void button_blend_pressed() {
	    pendingCommands.add(new BlendCommand(this));
	}
	public void button_ABC_pressed(int tool, int mat) {
		
		switch(tool){
		case 1:
			abc1=mat;
			break;
		case 2:
			abc2=mat;
			break;
		case 3:
			abc3=mat;
			break;
		case 4:
			abc4=mat;
			break;
		case 5:
			abc5=mat;
			break;
			
		}
		
	}
	public void button_trim_pressed() {
	    if (enableTrim) {
		      enableTrim=false;
		    } 
		    else {
		    	enableTrim = true;
		    }
		
	}
	public void button_trimsurface_pressed() {
	    if (enableTrimDisplay) {
		      enableTrimDisplay=false;
		    }else{ 
		    	enableTrimDisplay = true;
		    }		
	}
	public void setBooleanOperation(int newBooleanOperation) {
		booleanOperation=newBooleanOperation;
		
	}/*
	public void controlEvent(ControlEvent theEvent) {
		inter.controlEvent(theEvent);
	}*/
	
	public void button_applyBooleanOperation_pressed(){
		booleanOperations = new ArrayList<BooleanOperation>();
		int boolean1=(int)inter.ddl_boolean_abc1.getValue();
		int boolean2 = (int)inter.ddl_boolean_abc2.getValue();
		int operation = (int)inter.ddl_boolean_operator.getValue();
		int value= (int)inter.ddl_boolean_surforprop.getValue();
		//System.out.println(value);
		booleanOperation = operation;
		booleanAbc1 = boolean1;
		booleanAbc2 =boolean2;
		BooleanVisitor visitor;
		if(operation==3){//Intersect
			visitor = new BooleanIntersectVisitor();
		}else 
		if(operation==2){//Substract
			visitor = new BooleanSubstractVisitor();
		}else if(operation==1){ //1 = Add 
			visitor = new BooleanAddVisitor();
		}else{
			visitor = null;
			boolOperation = null;
			return;
		}
		if (value==0){
			booleanOperations.add(new SurfaceBooleanOperation(this,visitor));
		}
		if(value==1){
			booleanOperations.add(new PropertyBooleanOperation(this));
			
		}
		if(value==2){//Both
			booleanOperations.add(new PropertyBooleanOperation(this));
			booleanOperations.add(new SurfaceBooleanOperation(this,visitor));
		}
		if(value==3){
			specialbool=true;
			//booleanOperations.add(new SpecialBooleanOperation());
		}
		
	}
	void drawBooleanOperation(){
		for(BooleanOperation bop : booleanOperations){
			bop.apply();
		}
		
		if(booleanOperation!=0){
		
			if(specialbool){
				this.applySpecialOperation();
			}
		
		}
	}
	boolean getIsColoredForMaterial(int material){
		if(material==0){
			return color_a;
		}if(material==1){
			return color_b;
		}if(material==2){
			return color_c;
		}
		return false;
	}
	float getIsoLevelForMaterial(int material){
		if(material==0){
			return isolevel_a;
		}if(material==1){
			return isolevel_b;
		}if(material==2){
			return isolevel_c;
		}
		return 0;
	}
	void SpeedDecimals(float value) {
		  speeddec  =value;
	}

	public void GrayScottARate(float value){
		if(space!=null){
			space.paramDu = value;
		}	
	}
	public void GrayScottBRate(float value){
		if(space!=null){
		space.paramDv = value;
		}
	}
	public void GrayScottF(float value){
		if(space!=null){
	
		space.paramF = value;
		}
	}
	public void GrayScottK(float value){
		if(space!=null){
			space.paramK = value;
		}
	}
	
	public void booleanSlider(float value) {
	  booleanthreshold  =value;
	}
	void trimSlider(float value) {
	  trimThreshold  =value;
	}
	void Speed(float value) {
	  speedvar = value;
	}
	void wDxSlider(float value) {
	  newX=(int)(value);
	  inter.tf_dimx.setText(""+(int)newX);
	}
	public void updateCreationValues(){
		try{
			newX=Integer.parseInt(inter.tf_dimx.getText());
			newY=Integer.parseInt(inter.tf_dimy.getText());
			newZ=Integer.parseInt(inter.tf_dimz.getText());
		}catch(Exception ex){
			throw ex;
		}
	}
	void wDySlider(float value) {
		  newY=(int)(value);
	  inter.tf_dimy.setText(""+(int)newY);
	}
	void wDzSlider(float value) {
		  newZ=(int)(value);
	  inter.tf_dimz.setText(""+(int)newZ);
	}
	void voxThresholdSlider(float value) {
	  voxthreshold=value;
	}
	void voxThreshold2Slider(float value) {
		  voxthreshold2=value;
		}
	void boundThresholdSlider(float value) {
	  boundthreshold=value;
	}
	public void slider_avg_change(float value){
		avg_level = value;
	}
	public void trimA_slider_change(float value){
		trimlevel_a = value;
	}
	public void trimB_slider_change(float value){
		trimlevel_b = value;
	}
	public void trimC_slider_change(float value){
		trimlevel_c = value;
	}

	public void button_diffusion_pressed() {
		if(showvariance){
			showvariance= false;
			mode = GeometryMode.normal;
		}else{
			showvariance = true;
			mode= GeometryMode.diffusion;
		}
		//println("crazy");
		
		
		
	}
	public void button_step_pressed(){
		play=true;
		step=true;
	}
	public void surfaceA_slider_change(float value){
		isolevel_a = value;
	}
	public void surfaceB_slider_change(float value){
		isolevel_b = value;
	}
	public void surfaceC_slider_change(float value){
		isolevel_c = value;
	}
	public void controlEvent(ControlEvent theEvent){

	}
	
	public void checkbox_pick_press(int value){
		if(inter.checkbox_pick.getState(0)){
			useBuffer =true;
			cam.setMouseControlled(false);
		}else{
			cam.setMouseControlled(true);
			useBuffer = false;
		}
	}
	public void cb_mode(int value){
		if(inter.cb_mode.getState(0)){
			useAvg = true;
		}else{
			useAvg=false;
		}
	}
	public void offset_slider_change(float value){
		offset = value;
	}
	public void checkbox_offset(int value){
		if(inter.checkbox_offset.getState(0)){
			offsetA=true;
		}else{
			offsetA = false;
		}
		if(inter.checkbox_offset.getState(1)){
			offsetB=true;
		}else{
			offsetB = false;
		}
		if(inter.checkbox_offset.getState(2)){
			offsetC=true;
		}else{
			offsetC = false;
		}
	}
	
	public void checkbox_difanalysis_press(int value){
		if(inter.checkbox_difanalysis.getState(0)){
			showvariance = true;
			mode = GeometryMode.diffusion;
		}else{
			showvariance=false;
			mode = GeometryMode.normal;
		}
	}
	public void addCleanupCommand(UndoableCommand undoCommand) {
		this.cleanupCommands.add(undoCommand);
	}
	public void stopSim() {
		this.play = false;
		
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentResized(ComponentEvent e) {
		if(cam!=null){
			cam.lookAt(newX/2,newY/2, newZ/2);
		}
	}
	@Override
	public void componentShown(ComponentEvent e) {
		
	}
	public void applyPropBoolOperation() {
		space.drawGeometry(g, getIsoLevelForMaterial(booleanAbc1),0, getIsoLevelForMaterial(booleanAbc2), booleanOperation, booleanAbc1, booleanAbc2,mode);
		
		
	}
	public void applyAddSurfOperation(){
		space.marchCube(getIsoLevelForMaterial(booleanAbc1), booleanAbc1, getIsColoredForMaterial(booleanAbc1),true,booleanAbc2,getIsoLevelForMaterial(booleanAbc2),true, g);
   	 	space.marchCube(getIsoLevelForMaterial(booleanAbc2), booleanAbc2, getIsColoredForMaterial(booleanAbc2),true,booleanAbc1,getIsoLevelForMaterial(booleanAbc1),true, g);
		 
		
    
	}
	public void applySubsSurfOperation(){
		 space.marchCube(getIsoLevelForMaterial(booleanAbc1), booleanAbc1, getIsColoredForMaterial(booleanAbc1),true,booleanAbc2,getIsoLevelForMaterial(booleanAbc2),false, g);
		 space.marchCube(getIsoLevelForMaterial(booleanAbc2), booleanAbc2, getIsColoredForMaterial(booleanAbc2),true,booleanAbc1,getIsoLevelForMaterial(booleanAbc1),true, g);
		  
	}
	public void applyInterSurfOperation(){
		space.marchCube(getIsoLevelForMaterial(booleanAbc1), booleanAbc1, getIsColoredForMaterial(booleanAbc1),true,booleanAbc2,getIsoLevelForMaterial(booleanAbc2),false, g);
		space.marchCube(getIsoLevelForMaterial(booleanAbc2), booleanAbc2,  getIsColoredForMaterial(booleanAbc2),true,booleanAbc1,getIsoLevelForMaterial(booleanAbc1),false, g);
	    
	}
	public void applySpecialOperation(){
		
		BZGrid grid= new BZGrid(space);
		grid.a = new double[grid.dimX][grid.dimY][grid.dimZ][2];
		grid.b = new double[grid.dimX][grid.dimY][grid.dimZ][2];
		grid.c = new double[grid.dimX][grid.dimY][grid.dimZ][2];
		double[][][][] p0;
		double[][][][] p1;
		double[][][][] p2;
		float newIsoValue=0.5f;
		switch(booleanAbc1){
		case 0:
			p0=grid.a;
			p1=space.a;
			break;
		case 1:
			p0=grid.b;
			p1=space.b;
			break;
		default:
			p0=grid.c;
			p1=space.c;
			break;
		}
		switch(booleanAbc2){
		case 0:
			p2=space.a;
			break;
		case 1:
			p2=space.b;
			break;
		default:
			p2=space.c;
			break;
		}
		
		for(int i = 0; i<grid.dimX;i++){
			for(int j = 0; j<grid.dimY;j++){
				for(int k = 0; k<grid.dimZ;k++){
					if(booleanOperation==1){
						p0[i][j][k][grid.p]=p1[i][j][k][grid.p]+p2[i][j][k][grid.p]-p1[i][j][k][grid.p]*p2[i][j][k][grid.p];
						newIsoValue = getIsoLevelForMaterial(booleanAbc1)+getIsoLevelForMaterial(booleanAbc2)-getIsoLevelForMaterial(booleanAbc1)*getIsoLevelForMaterial(booleanAbc2);
					}
					if(booleanOperation==2){
						p0[i][j][k][grid.p]=p2[i][j][k][grid.p]-p1[i][j][k][grid.p];
						newIsoValue = getIsoLevelForMaterial(booleanAbc2)-getIsoLevelForMaterial(booleanAbc1);
					}
					if(booleanOperation==3){
						p0[i][j][k][grid.p]=p1[i][j][k][grid.p]*p2[i][j][k][grid.p];
						newIsoValue = getIsoLevelForMaterial(booleanAbc1)*getIsoLevelForMaterial(booleanAbc2);
					}
				}	
			}
		}
		
		
		grid.updateVoxels();
		grid.marchCube(newIsoValue, booleanAbc1, getIsColoredForMaterial(booleanAbc1), false, 0, 0, false, g);
		
	}
	public boolean isWithinSpecialTrim(Voxel voxel) {
		float xlow = inter.xlow.getValue()*space.dimX;
		float ylow = inter.ylow.getValue()*space.dimY;
		float zlow = inter.zlow.getValue()*space.dimZ;
		float xhi = inter.xhi.getValue()*space.dimX;
		float yhi = inter.yhi.getValue()*space.dimY;
		float zhi = inter.zhi.getValue()*space.dimZ;
		boolean ret = true;
		if(xlow>voxel.getX()||voxel.getX()>xhi){
			return false;
		}
		if(ylow>voxel.getY()||voxel.getY()>yhi){
			return false;
		}
		if(zlow>voxel.getZ()||voxel.getZ()>zhi){
			return false;
		}
		return true;
		
	}
	public int getPickColor() {
		return inter.cpick.getColorValue();
		
	}
	public void dimx_changed(String value){
		System.out.println("Changed to "+value);
	}
	
}
