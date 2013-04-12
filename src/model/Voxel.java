package model;



import processing.core.*;

public class Voxel
{

	  //                4                5 
	  //                ----------------
	  //               /|             / |
	  //              / |            /  |
	  //             /  |           /   |
	  //            /   |     +    /    |
	  //          7 ---------------- 6  |
	  //           |    0 ------- |-----/ 1 
	  //           |    /         |    /          
	  //           |   /          |   / 
	  //           |  /           |  / 
	  //           | /            | /  
	  //           ---------------- 
	  //          3                2

	  float[] pos = new float[3];
	  PVector[] vert = new PVector[8];
	  double[][] concentration = new double[8][3];
	  float dx,dy,dz;
	  int id;
	  BZGrid bz;
	  public float px;
	float py;
	float pz;
	  Voxel(int idv,float[] p, BZGrid bzSpace, float dx, float dy, float dz)
	  {
		  this.bz = bzSpace;
	    this.id = idv; //Save the index in case we need it! :D
	    
	    px = p[0];py = p[1];pz= p[2];
	    this.dx = dx; this.dy = dy; this.dz = dz;
	    this.pos = p;
	    this.vert[0] = new PVector(px-dx/2,py+dy/2,pz+dz/2);
	    this.vert[1] = new PVector(px+dx/2,py+dy/2,pz+dz/2);
	    this.vert[2] = new PVector(px+dx/2,py-dy/2,pz+dz/2);
	    this.vert[3] = new PVector(px-dx/2,py-dy/2,pz+dz/2);
	    this.vert[4] = new PVector(px-dx/2,py+dy/2,pz-dz/2);
	    this.vert[5] = new PVector(px+dx/2,py+dy/2,pz-dz/2);
	    this.vert[6] = new PVector(px+dx/2,py-dy/2,pz-dz/2);
	    this.vert[7] = new PVector(px-dx/2,py-dy/2,pz-dz/2);
	    this.concentration = FindConcAtVoxelVerts(vert, bzSpace);
	    updateDrawConc();
	  }
	  public Voxel(int idv,float[] p, float dx, float dy, float dz)
	  {
	    this.id = idv; //Save the index in case we need it! :D
	    
	    px = p[0];py = p[1];pz= p[2];
	    this.dx = dx; this.dy = dy; this.dz = dz;
	    this.pos = p;
	    this.vert[0] = new PVector(px-dx/2,py+dy/2,pz+dz/2);
	    this.vert[1] = new PVector(px+dx/2,py+dy/2,pz+dz/2);
	    this.vert[2] = new PVector(px+dx/2,py-dy/2,pz+dz/2);
	    this.vert[3] = new PVector(px-dx/2,py-dy/2,pz+dz/2);
	    this.vert[4] = new PVector(px-dx/2,py+dy/2,pz-dz/2);
	    this.vert[5] = new PVector(px+dx/2,py+dy/2,pz-dz/2);
	    this.vert[6] = new PVector(px+dx/2,py-dy/2,pz-dz/2);
	    this.vert[7] = new PVector(px-dx/2,py-dy/2,pz-dz/2);
	  }


	  public float[] drawingConc=new float[3];
	  public void updateDrawConc(){
		  float returnme[] = new float[3];
	      for(int i = 0; i < 8;i++){
	        returnme[0] += concentration[i][0];
	        returnme[1] += concentration[i][1];
	        returnme[2] += concentration[i][2];
	       }
	       returnme[0] /= 8;
	       returnme[1] /= 8;
	       returnme[2] /= 8;
	       drawingConc = returnme;
	  }
	  public float[] getVariance(){
		  float average[] = new float[3];
		  float returnme[] = new float[3];
	      for(int i = 0; i < 8;i++){
	    	  
		        average[0] += concentration[i][0];
		        average[1] += concentration[i][1];
		        average[2] += concentration[i][2];
		       }
		       average[0] /= 8;
		       average[1] /= 8;
		       average[2] /= 8;
	      for(int i = 0; i < 8;i++){
	    	  
		        returnme[0] += PApplet.pow((float)(concentration[i][0]-average[0]),2);
		        returnme[1] += PApplet.pow((float)(concentration[i][1]-average[1]),2);
		        returnme[2] += PApplet.pow((float)(concentration[i][2]-average[2]),2);
		       }
		       returnme[0] /= 8;
		       returnme[1] /= 8;
		       returnme[2] /= 8;
		  
		       return returnme;
	  }
	  
	  public void EnableRegular(PGraphics g){
	      g.colorMode(PApplet.RGB, 1); 
	      int vert = 0;
	      g.fill( 
	         PApplet.constrain((float)(concentration[vert][0]),0,1),PApplet.constrain((float)(concentration[vert][1]),0,1),
	         PApplet.constrain((float)(concentration[vert][2]),0,1));
	      g.noStroke();
	  }
	  public void EnableGS(PGraphics g){
	      g.colorMode(PApplet.RGB, 1); 
	      int vert = 0;
	      g.fill( 
	         0,0,
	         1-PApplet.constrain((float)(concentration[vert][0]),0,1));
	      g.noStroke();
	  }
	  public void EnableVariance(PGraphics g,int abc1){
		    boolean showVariance=true;
		    if(showVariance){
		    	 g.colorMode(PApplet.RGB, 1); 
		    	 float[] val = getVariance();
			      float val0=3*PApplet.sqrt(val[0]);
			      float val1=3*PApplet.sqrt(val[1]);
			      float val2=3*PApplet.sqrt(val[2]);
			      if(abc1==0){
			    	  g.fill( 
			    			  PApplet.constrain(1,0,1),
						         PApplet.constrain(1-val1,0,1),
						         PApplet.constrain(1-val2,0,1));
						         
			      }if(abc1==1){
			    	  g.fill( 
			    			  PApplet.constrain(1-val0,0,1),
						         PApplet.constrain(1,0,1),
						         PApplet.constrain(1-val2,0,1));
			      }if(abc1==2){
			    	  g.fill( 
						         
						         PApplet.constrain(1-val0,0,1),
						         PApplet.constrain(1-val1,0,1),
						         PApplet.constrain(1,0,1));  
			      }
			      
			      g.noStroke();
		    }
	  }
	  public void EnableID(PGraphics g){
	      g.colorMode(PApplet.RGB,100); 
	      int vert = 0;
	      g.fill(px,py,pz);
	      g.noStroke();
	      //PApplet.println("X"+px+"Y"+py+"Z"+pz);
	  }
	  public void EnableEmpty(PGraphics g){
		  g.colorMode(PApplet.RGB,1);
		  g.noFill();
		  g.stroke(0);
	  }
	  public void draw(PGraphics g)
	  {
	    boolean isFilled = true;
	    

	   
	    if(isFilled){
	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]+dz/2);
	  
	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]+dz/2);
	  

	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]-dz/2); 
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]-dz/2);
	  
	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]+dz/2); 
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]-dz/2);

	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]+dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]+dy/2,pos[2]+dz/2);
	  
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]-dz/2);
	      g.vertex(pos[0]+dx/2,pos[1]-dy/2,pos[2]+dz/2);
	      g.vertex(pos[0]-dx/2,pos[1]-dy/2,pos[2]+dz/2);
	    }
	  }
	  
	  void update(BZGrid bzS)
	  {
	    //update concentration
	    
	   
	    this.concentration = FindConcAtVoxelVerts(vert, bz);
	     updateDrawConc();
	  }
	  
	  double[][] FindConcAtVoxelVerts(PVector[] v, BZGrid bz){
		  double [][] conc = new double[8][3];
	    conc[0] = bz.calcConcAt(v[0]);
	    conc[1] = bz.calcConcAt(v[1]);
	    conc[2] = bz.calcConcAt(v[2]);
	    conc[3] = bz.calcConcAt(v[3]);
	    conc[4] = bz.calcConcAt(v[4]);
	    conc[5] = bz.calcConcAt(v[5]);
	    conc[6] = bz.calcConcAt(v[6]);
	    conc[7] = bz.calcConcAt(v[7]);
	    return conc;
	  }
	  public float getX(){
		  return this.pos[0];
	  }
	  public float getY(){
		  return this.pos[1];
	  }
	  public float getZ(){
		  return this.pos[2];
	  }
	}

