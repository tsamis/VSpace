package model;





import java.io.PrintWriter;

import processing.core.*;
import util.General;

import javax.xml.bind.JAXBElement.GlobalScope;

import main.BZEclipse;


public class BZGrid {
	public double[][][][] a;
	public double[][][][] b;
	public double[][][][] c;
	public int p = 0;
	public int q = 1;
	public int dimX;
	public int dimY;
	public int dimZ;
	float reactionSpeed;
	BZEclipse _parent;
	Voxel[] vox;
	int[] axis;
	PImage imageFrom;
	PImage imageTo;
	public double paramF;
	public double paramK;
	public double paramDu;
	public double paramDv;
	public enum CreationMethod {
		random,randomgs, grid3x3x3,slabs, randomGrid3x3x3, creationTrigWaves, images, composite,spheres,fill
	}
	CreationMethod _cm;
	
	public void setReactionSpeed(float reactionSpeed) {
		this.reactionSpeed = reactionSpeed;
	}
	public BZGrid(BZGrid clonefrom) {
		this.dimX=clonefrom.dimX;
		this.dimY=clonefrom.dimY;
		this.dimZ = clonefrom.dimZ;
		this.a=clonefrom.a.clone();
		this.b=clonefrom.b.clone();
		this.c=clonefrom.c.clone();
		this.p=clonefrom.p;
		this.q=clonefrom.q;
		this._parent = clonefrom._parent;
		vox= new Voxel[(dimX-1)*(dimY-1)*(dimZ-1)];
		createVoxels();
		
		
	}
	public BZGrid(int dimX, int dimY, int dimZ, CreationMethod cm, BZEclipse parent) {
		baseThings(dimX,dimY,dimZ,cm,parent);
		initializeGrid();
		createVoxels();
	}
	public BZGrid(int dimX, int dimY, int dimZ, CreationMethod cm, BZEclipse parent,PImage imageFrom,PImage imageTo,int[] axis) {
		baseThings(dimX,dimY,dimZ,cm,parent);
		this.imageFrom =imageFrom;
		this.imageTo = imageTo;
		this.axis = axis;
		initializeGrid();
		createVoxels();
	}
	void baseThings(int dimX, int dimY, int dimZ, CreationMethod cm, BZEclipse parent){
		this.dimX = dimX;
		this.dimY = dimY;
		this.dimZ = dimZ;
		_cm = cm;
		_parent = parent;
		reactionSpeed = 1;
		this.a = new double[dimX][dimY][dimZ][2];
		this.b = new double[dimX][dimY][dimZ][2];
		this.c = new double[dimX][dimY][dimZ][2];
		this.paramDu = parent.paramDu;
		this.paramDv = parent.paramDv;
		this.paramK = parent.paramK;
		this.paramF = parent.paramF;
		vox= new Voxel[(dimX-1)*(dimY-1)*(dimZ-1)];
	}
	public void prepareImageLoad(int[] axis,PImage from, PImage to){
		this.axis = axis;
		this.imageFrom = from;
		this.imageTo = to;
	}
	public void setA(int i,int j,int k,double value){
		
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		a[i][j][k][p]=value;
	}
	
	public void setB(int i,int j,int k,double value){
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		b[i][j][k][p]=value;
		
	}
	public void setC(int i,int j,int k,double value){
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		c[i][j][k][p]=value;
		
	}
	void initializeGrid(){
		switch(_cm){
		case random:
			createRandom(a, b, c);
			break;
		case randomgs:
			createSmallRandom();
			break;
		case creationTrigWaves:
			createTrigWaves();
			break;
		case fill:
			createFill();
			break;
		case grid3x3x3:
			createColorGrid();
			break;
		case images:
			createImages();
			break;
		case randomGrid3x3x3:
			createRandomGrid();
			break;
		case composite:
			createComposite();
			break;
		case slabs:
			createSlabs();
			break;
		case spheres:
			createSpheres();
			
			break;
		}
	}
	
	private void createSpheres() {
		// TODO Auto-generated method stub
		
	}
	private void createFill(){
		int color = _parent.getPickColor();
		float re = _parent.red(color);
		float gr= _parent.green(color);
		float bl = _parent.blue(color);
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				for (int z = 0; z < dimZ; z++) {
					a[x][y][z][p] = re;
					b[x][y][z][p] = gr;
					c[x][y][z][p] = bl;
				}
			}
		}
		
	}
	private void createSlabs(){

	      
	      int slabs=9;
	      float steper=((float)dimX/((float)slabs));
	      int sel=0;
	      float x2=0;
	      
	      for (int x = 0; x < dimX; x++) {
	        x2+=1;   
	        if(x2>steper){
	                sel++;
	                x2=0;
	              }
	              
	          for (int y = 0; y < dimY; y++) {
	            for (int z = 0; z < dimZ; z++) {
	        
	              a[x][y][z][p] = 0;
	              b[x][y][z][p] = 0;
	              c[x][y][z][p] = 0;
	               
	              if(sel%3==0){
	                a[x][y][z][p] = 1;
	              }else if(sel%3==1){
	                b[x][y][z][p] = 1;
	              }else{
	                c[x][y][z][p] = 1;
	              }
	            }
	          }
	        }
	        
	        
	}

	private void createComposite(){
		float[][][][] colores = new float[3][3][3][3];
        int select=0;
        int x3=0;
        int y3=0;
        int z3=0;
		float rand = _parent.random(0f,1f);
		
	      for(int i = 0; i <3;i++){
	        for(int j = 0; j <3;j++){
	          
	           for(int k = 0; k <3;k++){
	        	   rand = _parent.random(0f,1f);
	        	   if(rand>0.3){
		              colores[i][j][k][0]=_parent.random(0.0f,1.0f);
		              colores[i][j][k][1]=_parent.random(0.0f,1.0f);
		              colores[i][j][k][2]=_parent.random(0.0f,1.0f);
	        	   }else{
	        		   colores[i][j][k][0]=-1;
	        	   }
	        	   
	           }
	        } 
	      }   
	      
	       for (int x = 0; x < dimX; x++) {
	          if(x<dimX/3){x3=0;}
	          else if(x>=dimX/3&&x<=2*dimX/3){x3=1;}
	          else{x3=2;}
	          for (int y = 0; y < dimY; y++) {
	            if(y<dimY/3){y3=0;}
	            else if(y>=dimY/3&&y<=2*dimY/3){y3=1;}
	            else{y3=2;}
	            for (int z = 0; z < dimZ; z++) {
	              if(z<dimZ/3){z3=0;}
	              else if(z>=dimZ/3&&z<=2*dimZ/3){z3=1;}
	              else{z3=2;}
	             
	              
	              
	              if(colores[x3][y3][z3][0]==-1){
						a[x][y][z][p] = _parent.random(0f, 1f);
						b[x][y][z][p] = _parent.random(0f, 1f);
						c[x][y][z][p] = _parent.random(0f, 1f);
	              }else{
		              a[x][y][z][p] = colores[x3][y3][z3][0];
		              b[x][y][z][p] = colores[x3][y3][z3][1];
		              c[x][y][z][p] = colores[x3][y3][z3][2];
	              }
	            }
	          }
	        }
	}
	private void createSmallRandom(){
		float[][][][] colores = new float[3][3][3][3];
        int select=0;
        int x3=0;
        int y3=0;
        int z3=0;

	      for(int i = 0; i <3;i++){
	        for(int j = 0; j <3;j++){
	          
	           for(int k = 0; k <3;k++){
		              colores[i][j][k][0]=1;
		              colores[i][j][k][1]=0;
		              colores[i][j][k][2]=1;
	           }
	        } 
	      }
	      colores[1][1][1][0]=-1;
	      
	       for (int x = 0; x < dimX; x++) {
	          if(x<dimX/3){x3=0;}
	          else if(x>=dimX/3&&x<=2*dimX/3){x3=1;}
	          else{x3=2;}
	          for (int y = 0; y < dimY; y++) {
	            if(y<dimY/3){y3=0;}
	            else if(y>=dimY/3&&y<=2*dimY/3){y3=1;}
	            else{y3=2;}
	            for (int z = 0; z < dimZ; z++) {
	              if(z<dimZ/3){z3=0;}
	              else if(z>=dimZ/3&&z<=2*dimZ/3){z3=1;}
	              else{z3=2;}
	             
	              
	              
	              if(colores[x3][y3][z3][0]==-1){
						a[x][y][z][p] = 0.5f*(1+_parent.random(-1f, 1f));
						b[x][y][z][p] = 0.25f*(1+_parent.random(-1f, 1f));
						c[x][y][z][p] = _parent.random(0f, 1f);;
	              }else{
		              a[x][y][z][p] = colores[x3][y3][z3][0];
		              b[x][y][z][p] = colores[x3][y3][z3][1];
		              c[x][y][z][p] = colores[x3][y3][z3][2];
	              }
	            }
	          }
	        }
	}
	private void createRandomGrid(){
		float[][][][] colores = new float[3][3][3][3];
        int select=0;
        int x3=0;
        int y3=0;
        int z3=0;
		 
	      for(int i = 0; i <3;i++){
	        for(int j = 0; j <3;j++){
	          
	           for(int k = 0; k <3;k++){
	              colores[i][j][k][0]=_parent.random(0.0f,1.0f);
	              colores[i][j][k][1]=_parent.random(0.0f,1.0f);
	              colores[i][j][k][2]=_parent.random(0.0f,1.0f);
	              
	           }
	        } 
	      }   
	      
	       for (int x = 0; x < dimX; x++) {
	          if(x<dimX/3){x3=0;}
	          else if(x>=dimX/3&&x<=2*dimX/3){x3=1;}
	          else{x3=2;}
	          for (int y = 0; y < dimY; y++) {
	            if(y<dimY/3){y3=0;}
	            else if(y>=dimY/3&&y<=2*dimY/3){y3=1;}
	            else{y3=2;}
	            for (int z = 0; z < dimZ; z++) {
	              if(z<dimZ/3){z3=0;}
	              else if(z>=dimZ/3&&z<=2*dimZ/3){z3=1;}
	              else{z3=2;}
	             
	              
	              
	              
	              a[x][y][z][p] = colores[x3][y3][z3][0];
	              b[x][y][z][p] = colores[x3][y3][z3][1];
	              c[x][y][z][p] = colores[x3][y3][z3][2];
	             
	            }
	          }
	        }
	}
	private void createColorGrid(){
	    float[][][][] colores = new float[3][3][3][3];
        int select=0;
        int x3=0;
        int y3=0;
        int z3=0;
	      for(int i = 0; i <3;i++){
	        for(int j = 0; j <3;j++){
	          
	           for(int k = 0; k <3;k++){
	              colores[i][j][k][0]=0;
	              colores[i][j][k][1]=0;
	              colores[i][j][k][2]=0;
	              if(select%3==0){
	                colores[i][j][k][0]=1;
	              }else if(select%3==1){
	                colores[i][j][k][1]=1;
	              }else{
	                 colores[i][j][k][2]=1;
	              }
	              select++;
	           }
	           select++;
	        } 
	        select++;
	      }   
	       for (int x = 0; x < dimX; x++) {
	          if(x<dimX/3){x3=0;}
	          else if(x>=dimX/3&&x<=2*dimX/3){x3=1;}
	          else{x3=2;}
	          for (int y = 0; y < dimY; y++) {
	            if(y<dimY/3){y3=0;}
	            else if(y>=dimY/3&&y<=2*dimY/3){y3=1;}
	            else{y3=2;}
	            for (int z = 0; z < dimZ; z++) {
	              if(z<dimZ/3){z3=0;}
	              else if(z>=dimZ/3&&z<=2*dimZ/3){z3=1;}
	              else{z3=2;}
	             
	              
	              
	              
	              a[x][y][z][p] = colores[x3][y3][z3][0];
	              b[x][y][z][p] = colores[x3][y3][z3][1];
	              c[x][y][z][p] = colores[x3][y3][z3][2];
	             
	            }
	          }
	        }
	}
	private void createTrigWaves() {
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
              for (int z = 0; z < dimZ; z++) {
                a[x][y][z][p] = (PApplet.sin(PApplet.PI*x/(dimX/4))+1)/2;
                b[x][y][z][p] = (PApplet.cos(PApplet.PI*y/(dimY/4))+1)/2;
                c[x][y][z][p] = (PApplet.cos(PApplet.PI*z/(dimZ/4))+1)/2;
              }
            }
          }
		
	}
	public float distance(float[] a, float[] b){
		float result = PApplet.sqrt(PApplet.pow(a[0]-b[0], 2)+
				PApplet.pow(a[1]-b[1], 2)+
				PApplet.pow(a[2]-b[2], 2)
				);
		
		return 2;
	}
	private void createspheres() {
		float[] aCenter=new float[3];
		float[] bCenter=new float[3];
		float[] cCenter=new float[3];
		
		
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
              for (int z = 0; z < dimZ; z++) {
                a[x][y][z][p] = (PApplet.sin(PApplet.PI*x/(dimX/4))+1)/2;
                b[x][y][z][p] = (PApplet.cos(PApplet.PI*y/(dimY/4))+1)/2;
                c[x][y][z][p] = (PApplet.cos(PApplet.PI*z/(dimZ/4))+1)/2;
              }
            }
          }
		
	}
	private void createRandom(double[][][][] a,double[][][][] b, double[][][][] c){
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				for (int z = 0; z < dimZ; z++) {
					a[x][y][z][p] = _parent.random(0f, 1f);
					b[x][y][z][p] = _parent.random(0f, 1f);
					c[x][y][z][p] = _parent.random(0f, 1f);
				}
			}
		}
	}
	private void createVoxels(){
		  int voxIndex=0;
		  for(int xx = 0; xx < (dimX-1); xx++){
		    for(int yy = 0; yy <(dimY-1); yy++){
		      for(int zz = 0; zz < (dimZ-1); zz++){
		        float[] center = {xx+0.5f,yy+0.5f,zz+0.5f};
		        vox[voxIndex] = new Voxel(voxIndex,center, this, 1, 1, 1);
		        voxIndex++;
		      }
		    }
		  }
		  
	}
	private void createImages(){
		if(imageFrom==null||imageTo==null){
			System.out.println("BAD IMAGES");
			  
			return;
		}
		int fromwidth = imageFrom.width;
        int fromheight = imageFrom.height;
        int towidth = imageTo.width;
        int toheight = imageTo.height;
       
        float fader = 0;    
        imageFrom.loadPixels();
        int nullthem = 1;
        if(axis[0]==1){
        
        for (int x = 0; x < dimX; x++) {
          for (int z = 0; z < dimZ; z++) {
            for (int y = 0; y < dimY; y++) {
              if(x<=1){
                a[x][y][z][p] = (_parent.red(imageFrom.get((int)(y*fromwidth/dimY),(int)(z*fromheight/dimZ)))/255);
                b[x][y][z][p] = (_parent.green(imageFrom.get((int)(y*fromwidth/dimY),(int)(z*fromheight/dimZ)))/255);
                c[x][y][z][p] =(_parent.blue(imageFrom.get((int)(y*fromwidth/dimY),(int)(z*fromheight/dimZ)))/255);
                
              }
              else if(x>=dimX-3){
                a[x][y][z][p] = (_parent.red(imageTo.get((int)(y*towidth/dimY),(int)(z*toheight/dimZ)))/255);
                b[x][y][z][p] = (_parent.green(imageTo.get((int)(y*towidth/dimY),(int)(z*toheight/dimZ)))/255);
                c[x][y][z][p] = (_parent.blue(imageTo.get((int)(y*towidth/dimY),(int)(z*toheight/dimZ)))/255);
                
              }
              else{
                a[x][y][z][p] = 0;
                b[x][y][z][p] = 0;
                c[x][y][z][p] = 0;
              }
            }
          }
        }
        }else if(axis[1]==1){
          
        for (int y = 0; y < dimY; y++) {
          for (int z = 0; z < dimZ; z++) {
            for (int x = 0; x < dimX; x++) {
              if(y<=1){
                a[x][y][z][p] = (_parent.red(imageFrom.get((int)(x*fromwidth/dimX),(int)(z*fromwidth/dimZ)))/255);
                b[x][y][z][p] = (_parent.green(imageFrom.get((int)(x*fromwidth/dimX),(int)(z*fromwidth/dimZ)))/255);
                c[x][y][z][p] =(_parent.blue(imageFrom.get((int)(x*fromwidth/dimX),(int)(z*fromwidth/dimZ)))/255);
                
              }
              else if(y>=dimY-2){
                a[x][y][z][p] = (_parent.red(imageTo.get((int)(x*towidth/dimX),(int)(z*towidth/dimZ)))/255);
                b[x][y][z][p] = (_parent.green(imageTo.get((int)(x*towidth/dimX),(int)(z*towidth/dimZ)))/255);
                c[x][y][z][p] = (_parent.blue(imageTo.get((int)(x*towidth/dimX),(int)(z*towidth/dimZ)))/255);
                
              }
              else{
                a[x][y][z][p] = 0;
                b[x][y][z][p] = 0;
                c[x][y][z][p] = 0;
              }
            }
          }
        }
        }else{
        for (int z = 0; z < dimZ; z++) {
          for (int y = 0; y < dimY; y++) {
            for (int x = 0; x < dimX; x++) {
              if(z<=1){
                a[x][y][z][p] = (_parent.red(imageFrom.get((int)(x*fromwidth/dimX),(int)(y*fromheight/dimY)))/255);
                b[x][y][z][p] = (_parent.green(imageFrom.get((int)(x*fromwidth/dimX),(int)(y*fromheight/dimY)))/255);
                c[x][y][z][p] =(_parent.blue(imageFrom.get((int)(x*fromwidth/dimX),(int)(y*fromheight/dimY)))/255);
                
              }
              else if(z>=dimZ-2){
                a[x][y][z][p] = (_parent.red(imageTo.get((int)(x*towidth/dimX),(int)(y*toheight/dimY)))/255);
                b[x][y][z][p] = (_parent.green(imageTo.get((int)(x*towidth/dimX),(int)(y*toheight/dimY)))/255);
                c[x][y][z][p] = (_parent.blue(imageTo.get((int)(x*towidth/dimX),(int)(y*toheight/dimY)))/255);
                
              }
              else{
                a[x][y][z][p] = 0;
                b[x][y][z][p] = 0;
                c[x][y][z][p] = 0;
              }
            }
          }
        }}
	}
	public void GSupdate(float avg_level) {
		
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				for (int z = 0; z < dimZ; z++) {
					
						int xnext = (x+1+dimX)%dimX;
						int xprev = (x-1+dimX)%dimX;
						int ynext = (y+1+dimY)%dimY;
						int yprev = (y-1+dimY)%dimY;
						int znext = (z+1+dimZ)%dimZ;
						int zprev = (z-1+dimZ)%dimZ;
						double sa= a[x][y][z][p];
						double sb= b[x][y][z][p];
						double sc= c[x][y][z][p];

						double ddu=(a[xnext][y][z][p]-2*a[x][y][z][p]+a[xprev][y][z][p]
								+a[x][ynext][z][p]-2*a[x][y][z][p]+a[x][yprev][z][p]
								+a[x][y][znext][p]-2*a[x][y][z][p]+a[x][y][zprev][p])/6f;
					
						
						
						double ddv=(b[xnext][y][z][p]-2*b[x][y][z][p]+b[xprev][y][z][p]
								+b[x][ynext][z][p]-2*b[x][y][z][p]+b[x][yprev][z][p]
								+b[x][y][znext][p]-2*b[x][y][z][p]+b[x][y][zprev][p])/6f;
						
						double ddc=4*(c[xnext][y][z][p]-2*c[x][y][z][p]+c[xprev][y][z][p]
								+c[x][ynext][z][p]-2*c[x][y][z][p]+c[x][yprev][z][p]
								+c[x][y][znext][p]-2*c[x][y][z][p]+c[x][y][zprev][p])/6f;
						
						ddu*=4.0f;
						ddv*=4.0f;
						
						/*
						float ddu=(a[xnext][y][z][p]-2*a[x][y][z][p]+a[xprev][y][z][p]
								+a[x][ynext][z][p]-2*a[x][y][z][p]+a[x][yprev][z][p])/4f;
					
						
						
						float ddv=(b[xnext][y][z][p]-2*b[x][y][z][p]+b[xprev][y][z][p]
								+b[x][ynext][z][p]-2*b[x][y][z][p]+b[x][yprev][z][p])/4f;
						
						*/
						float noise = _parent.getValueNoiseFromInterface();
						double du = paramDu*(ddu)-sa*sb*sb+paramF*(1-a[x][y][z][p])+noise*c[x][y][z][p] ;
						//This line is for the original GS reaction
						double dv = paramDv*(ddv)+sa*sb*sb-(paramF+paramK)*b[x][y][z][p]+noise*c[x][y][z][p];
						
						
					a[x][y][z][q] =a[x][y][z][p]+du;
					b[x][y][z][q] =b[x][y][z][p]+dv;
					//c[x][y][z][q] =this.randomizeIfOut(sc,0.7*c[x][y][z][p]+ 0.3*(c[x][y][z][p]+dc), 0, 1);
					
					c[x][y][z][q] = (c[x][y][z][p]+ddc)%1;
					
				}
			}
		}

		if (p == 0) {
			p = 1;
			q = 0;
		} else {
			p = 0;
			q = 1;
		}
	}
	
	private double randomizeIfOut(double previous, double d, int i, int j) {
		if(d<0||d>1){
			return previous;
		}else{
			return d;
		}
	}
	public void update(float avg_level) {
		
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				for (int z = 0; z < dimZ; z++) {
					float c_a = 0f;
					float c_b = 0f;
					float c_c = 0f;
					for (int i = x - 1; i <= x + 1; i++) {
						for (int j = y - 1; j <= y + 1; j++) {
							for (int k = z - 1; k <= z + 1; k++) {
								c_a += a[(i + dimX) % dimX][(j + dimY)
										% dimY][(k + dimZ) % dimZ][p];
								c_b += b[(i + dimX) % dimX][(j + dimY)
										% dimY][(k + dimZ) % dimZ][p];
								c_c += c[(i + dimX) % dimX][(j + dimY)
										% dimY][(k + dimZ) % dimZ][p];
							}
						}
					}

					c_a /= 27.0;
					c_b /= 27.0;
					c_c /= 27.0;
					float resolution = 0;
					float alf = reactionSpeed;
					float bet = reactionSpeed;
					float gam = reactionSpeed;
					float na= PApplet.constrain((float)(resolution
							* a[x][y][z][p] + (1 - resolution)
							* (a[x][y][z][p]  + c_a * (alf * c_b - gam * c_c))), 0, 1);;
					float nb= PApplet.constrain((float)(resolution
							* b[x][y][z][p] + (1 - resolution)
							* (b[x][y][z][p]  + c_b * (bet * c_c - alf * c_a))), 0, 1);
					float nc= PApplet.constrain((float)(resolution
							* c[x][y][z][p] + (1 - resolution)
							* (c[x][y][z][p]  + c_c * (gam * c_a - bet * c_b))), 0, 1);
					float aa = PApplet.constrain((float)(resolution
							* a[x][y][z][p] + (1 - resolution)
							* (c_a + c_a * (alf * c_b - gam * c_c))), 0, 1);
					float ab= PApplet.constrain((float)(resolution
							* b[x][y][z][p] + (1 - resolution)
							* (c_b + c_b * (bet * c_c - alf * c_a))), 0, 1);
					float ac= PApplet.constrain((float)(resolution
							* c[x][y][z][p] + (1 - resolution)
							* (c_c + c_c * (gam * c_a - bet * c_b))), 0, 1);
					
					a[x][y][z][q] = avg_level*na+(1-avg_level)*aa;
					b[x][y][z][q] = avg_level*nb+(1-avg_level)*ab;
					c[x][y][z][q] = avg_level*nc+(1-avg_level)*ac;
					
				}
			}
		}

		if (p == 0) {
			p = 1;
			q = 0;
		} else {
			p = 0;
			q = 1;
		}

	}
	
	public void updateVoxels(){
		for(Voxel v : vox){
			v.update(this);
		}
	}
	void marchCubeTrim(float isolevel, int substance, boolean col,boolean trim,int abc4,float trimthreshold,boolean invertrim,PGraphics g){

	    for(int ii = 0; ii < vox.length; ii++){

	      /*
	      Determine the index into the edge table which
	       tells us which vertices are inside of the surface
	       */
	       //println(ii);
	      int cubeindex = 0;
	      if (vox[ii].concentration[0][substance] < isolevel) cubeindex |= 1;
	      if (vox[ii].concentration[1][substance] < isolevel) cubeindex |= 2; 
	      if (vox[ii].concentration[2][substance] < isolevel) cubeindex |= 4; 
	      if (vox[ii].concentration[3][substance] < isolevel) cubeindex |= 8; 
	      if (vox[ii].concentration[4][substance] < isolevel) cubeindex |= 16; 
	      if (vox[ii].concentration[5][substance] < isolevel) cubeindex |= 32; 
	      if (vox[ii].concentration[6][substance] < isolevel) cubeindex |= 64; 
	      if (vox[ii].concentration[7][substance] < isolevel) cubeindex |= 128;
	      
	      if(trim){
	    	  int incount = 0;
	    	  int outcount = 0;
	    	  if(vox[ii].concentration[0][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[1][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[2][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[3][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[4][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[5][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[6][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[7][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  outcount = 8-incount;
	    	  
	          if(abc4!=substance&&incount<=0){
	            continue;
	          }
	          
	       }
	      
	      int trimindex = 0;
	      if (vox[ii].concentration[0][abc4] < trimthreshold) trimindex |= 1;
	      if (vox[ii].concentration[1][abc4] < trimthreshold) trimindex |= 2; 
	      if (vox[ii].concentration[2][abc4] < trimthreshold) trimindex |= 4; 
	      if (vox[ii].concentration[3][abc4] < trimthreshold) trimindex |= 8; 
	      if (vox[ii].concentration[4][abc4] < trimthreshold) trimindex |= 16; 
	      if (vox[ii].concentration[5][abc4] < trimthreshold) trimindex |= 32; 
	      if (vox[ii].concentration[6][abc4] < trimthreshold) trimindex |= 64; 
	      if (vox[ii].concentration[7][abc4] < trimthreshold) trimindex |= 128;
	     
	     
	      
	      //int bothindex = trimindex&cubeindex;
	      
	      
	      if (BZGlobals.edgeTable[cubeindex] != 0x0) 
	      { 
	    	  
	        _parent.pushStyle();
	        PVector[] vertlist = new PVector[12];
	        Voxel v = vox[ii];
	        /* Find the vertices where the surface intersects the cube */
	        if ((BZGlobals.edgeTable[cubeindex] & 1) == 1) vertlist[0] = VertexInterp(isolevel,v.vert[0],v.vert[1],v.concentration[0][substance],v.concentration[1][substance]);
	        if ((BZGlobals.edgeTable[cubeindex] & 2) == 2) vertlist[1] = VertexInterp(isolevel,v.vert[1],v.vert[2],v.concentration[1][substance],v.concentration[2][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 4) == 4) vertlist[2] = VertexInterp(isolevel,v.vert[2],v.vert[3],v.concentration[2][substance],v.concentration[3][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 8) == 8) vertlist[3] = VertexInterp(isolevel,v.vert[3],v.vert[0],v.concentration[3][substance],v.concentration[0][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 16) == 16) vertlist[4] = VertexInterp(isolevel,v.vert[4],v.vert[5],v.concentration[4][substance],v.concentration[5][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 32) == 32) vertlist[5] = VertexInterp(isolevel,v.vert[5],v.vert[6],v.concentration[5][substance],v.concentration[6][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 64) == 64) vertlist[6] = VertexInterp(isolevel,v.vert[6],v.vert[7],v.concentration[6][substance],v.concentration[7][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 128) == 128) vertlist[7] = VertexInterp(isolevel,v.vert[7],v.vert[4],v.concentration[7][substance],v.concentration[4][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 256) == 256) vertlist[8] = VertexInterp(isolevel,v.vert[0],v.vert[4],v.concentration[0][substance],v.concentration[4][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 512) == 512) vertlist[9] = VertexInterp(isolevel,v.vert[1],v.vert[5],v.concentration[1][substance],v.concentration[5][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 1024) == 1024) vertlist[10] = VertexInterp(isolevel,v.vert[2],v.vert[6],v.concentration[2][substance],v.concentration[6][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 2048) == 2048) vertlist[11] = VertexInterp(isolevel,v.vert[3],v.vert[7],v.concentration[3][substance],v.concentration[7][substance]); 
	        
	       /*DeleteVERTEXES
	        * 
	        */
	        
	        
	        if (BZGlobals.edgeTable[trimindex] != 0x0) 
		      { 
		    	  	
			        PVector[] trimlist = new PVector[12];
			       
			        /* Find the vertices where the surface intersects the cube */
			        
			        if ((BZGlobals.edgeTable[trimindex] & 1) == 1) trimlist[0] = VertexInterp(trimthreshold,v.vert[0],v.vert[1],v.concentration[0][abc4],v.concentration[1][abc4]);
			        if ((BZGlobals.edgeTable[trimindex] & 2) == 2) trimlist[1] = VertexInterp(trimthreshold,v.vert[1],v.vert[2],v.concentration[1][abc4],v.concentration[2][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 4) == 4) trimlist[2] = VertexInterp(trimthreshold,v.vert[2],v.vert[3],v.concentration[2][abc4],v.concentration[3][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 8) == 8) trimlist[3] = VertexInterp(trimthreshold,v.vert[3],v.vert[0],v.concentration[3][abc4],v.concentration[0][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 16) == 16) trimlist[4] = VertexInterp(trimthreshold,v.vert[4],v.vert[5],v.concentration[4][abc4],v.concentration[5][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 32) == 32) trimlist[5] = VertexInterp(trimthreshold,v.vert[5],v.vert[6],v.concentration[5][abc4],v.concentration[6][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 64) == 64) trimlist[6] = VertexInterp(trimthreshold,v.vert[6],v.vert[7],v.concentration[6][abc4],v.concentration[7][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 128) == 128) trimlist[7] = VertexInterp(trimthreshold,v.vert[7],v.vert[4],v.concentration[7][abc4],v.concentration[4][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 256) == 256) trimlist[8] = VertexInterp(trimthreshold,v.vert[0],v.vert[4],v.concentration[0][abc4],v.concentration[4][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 512) == 512) trimlist[9] = VertexInterp(trimthreshold,v.vert[1],v.vert[5],v.concentration[1][abc4],v.concentration[5][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 1024) == 1024) trimlist[10] = VertexInterp(trimthreshold,v.vert[2],v.vert[6],v.concentration[2][abc4],v.concentration[6][abc4]); 
			        if ((BZGlobals.edgeTable[trimindex] & 2048) == 2048) trimlist[11] = VertexInterp(trimthreshold,v.vert[3],v.vert[7],v.concentration[3][abc4],v.concentration[7][abc4]); 
			        
			        
			        
			        
			        
			        /* Create the triangle interped */
			        /* Create the triangle interp this with the other one. this rules. */
			        for (int jj=0; BZGlobals.triTable[cubeindex][jj]!=-1; jj+=3) {
			        	
			        	PVector[] pt = new PVector[3];
			        	pt[0] = vertlist[BZGlobals.triTable[cubeindex][jj  ]];
			        	pt[1] = vertlist[BZGlobals.triTable[cubeindex][jj+1]];
			        	pt[2] = vertlist[BZGlobals.triTable[cubeindex][jj+2]];
			        	boolean b1 =false;
			        	boolean b2 =false;
			        	boolean b3 =false;
			        	
			        	for (int kk=0; BZGlobals.triTable[trimindex][kk]!=-1; kk+=3) {
				        	
				        	
				        	PVector[] ptrim = new PVector[3];
				        	ptrim[0] = trimlist[BZGlobals.triTable[trimindex][kk  ]];
				        	ptrim[1] = trimlist[BZGlobals.triTable[trimindex][kk+1]];
				        	ptrim[2] = trimlist[BZGlobals.triTable[trimindex][kk+2]];
				        	
				        	
				        	
				        	if(calcConcAt(pt[0])[abc4]>trimthreshold&&b1==false&&pt[0].dist(ptrim[0])<=2){
				        		pt[0]=pointToTri(pt[0], ptrim[0], ptrim[1], ptrim[2]);
				        		
				        	}
				        	if(calcConcAt(pt[1])[abc4]>trimthreshold&&b2==false&&pt[1].dist(ptrim[0])<=2){
								pt[1]=pointToTri(pt[1], ptrim[0], ptrim[1], ptrim[2]);
								
				        	}
				        	if(calcConcAt(pt[2])[abc4]>trimthreshold&&b3==false&&pt[2].dist(ptrim[0])<=2){
								pt[2]=pointToTri(pt[2], ptrim[0], ptrim[1], ptrim[2]);
								
				        	}
				        	
				        	
				      }
			        	drawTriFace(pt, vox[ii], false,g);
			      }
			        for (int kk=0; BZGlobals.triTable[trimindex][kk]!=-1; kk+=3) {
			        	
			        	
			        	PVector[] ptrim = new PVector[3];
			        	ptrim[0] = trimlist[BZGlobals.triTable[trimindex][kk  ]];
			        	ptrim[1] = trimlist[BZGlobals.triTable[trimindex][kk+1]];
			        	ptrim[2] = trimlist[BZGlobals.triTable[trimindex][kk+2]];
			        	
			        	drawTriFace(ptrim, vox[ii], true,g);

			        	
			        	
			      }
		        	
		        	
			        _parent.popStyle();
			        continue;
			      }
	        
	        /* Create the triangle */
	        for (int jj=0; BZGlobals.triTable[cubeindex][jj]!=-1; jj+=3) {
	        	
	        	PVector[] pt = new PVector[3];
	        	pt[0] = vertlist[BZGlobals.triTable[cubeindex][jj  ]];
	        	pt[1] = vertlist[BZGlobals.triTable[cubeindex][jj+1]];
	        	pt[2] = vertlist[BZGlobals.triTable[cubeindex][jj+2]];
	        	drawTriFace(pt, vox[ii], col,g);
	        
	      }
	        
	        
	       _parent.popStyle();
	      }
	    }
	  }
	
	PVector pointToTri(PVector point, PVector tri1,PVector tri2, PVector tri3){
		PVector result = new PVector();
		PVector v1=new PVector(tri1.x,tri1.y,tri1.z);
		PVector v2 = new PVector(tri2.x,tri2.y,tri2.z);
		PVector v3 = new PVector(tri3.x,tri3.y,tri3.z);
		/*
		v1.sub(tri1);
		v2.sub(tri1);
		v3.sub(tri1);
		
		point.sub(tri1);*/
		PVector n = v2.cross(v3);
		n.normalize();
		/*
		
		
		result = n.cross(point).cross(n);
		result.add(tri1);
*/		result = PVector.sub(point, PVector.mult(n, PVector.dot(point, n)));
		return result;
	}
	public void Tests(){
		PVector point = new PVector(1,1,1);
		PVector t1 = new PVector(0,0,0);
		PVector t2 = new PVector(1,0,0);
		PVector t3 = new PVector(0,1,0);
		
		PVector actual = pointToTri(point,t1,t2,t3);
		PApplet.println(actual.x + " "+actual.y+" "+actual.z);
	}
	public void marchCube(float isolevel, int substance, boolean col,boolean trim,int abc4,float trimthreshold,boolean invertrim,PGraphics g){

		    for(int ii = 0; ii < vox.length; ii++){

		      /*
		      Determine the index into the edge table which
		       tells us which vertices are inside of the surface
		       */
		       //println(ii);
		      int cubeindex = 0;
		      if (vox[ii].concentration[0][substance] < isolevel) cubeindex |= 1;
		      if (vox[ii].concentration[1][substance] < isolevel) cubeindex |= 2; 
		      if (vox[ii].concentration[2][substance] < isolevel) cubeindex |= 4; 
		      if (vox[ii].concentration[3][substance] < isolevel) cubeindex |= 8; 
		      if (vox[ii].concentration[4][substance] < isolevel) cubeindex |= 16; 
		      if (vox[ii].concentration[5][substance] < isolevel) cubeindex |= 32; 
		      if (vox[ii].concentration[6][substance] < isolevel) cubeindex |= 64; 
		      if (vox[ii].concentration[7][substance] < isolevel) cubeindex |= 128;

		      if(trim){
		    	  int incount = 0;
		    	  int outcount = 0;
		    	  if(vox[ii].concentration[0][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[1][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[2][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[3][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[4][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[5][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[6][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  if(vox[ii].concentration[7][abc4]<=trimthreshold){
		    		  incount++;
		    	  }
		    	  outcount = 8-incount;
		    	  if(!invertrim){
		          if(abc4!=substance&&incount>3){
		            continue;
		          }
		    	  }else{
		    		  if(abc4!=substance&&outcount>3){
				            continue;
				          }
		    	  }
		          
		       }
		      if (BZGlobals.edgeTable[cubeindex] != 0x0) 
		      { 
		    	  
		        _parent.pushStyle();
		        PVector[] vertlist = new PVector[12];
		        Voxel v = vox[ii];
		        /* Find the vertices where the surface intersects the cube */
		        if ((BZGlobals.edgeTable[cubeindex] & 1) == 1) vertlist[0] = VertexInterp(isolevel,v.vert[0],v.vert[1],v.concentration[0][substance],v.concentration[1][substance]);
		        if ((BZGlobals.edgeTable[cubeindex] & 2) == 2) vertlist[1] = VertexInterp(isolevel,v.vert[1],v.vert[2],v.concentration[1][substance],v.concentration[2][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 4) == 4) vertlist[2] = VertexInterp(isolevel,v.vert[2],v.vert[3],v.concentration[2][substance],v.concentration[3][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 8) == 8) vertlist[3] = VertexInterp(isolevel,v.vert[3],v.vert[0],v.concentration[3][substance],v.concentration[0][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 16) == 16) vertlist[4] = VertexInterp(isolevel,v.vert[4],v.vert[5],v.concentration[4][substance],v.concentration[5][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 32) == 32) vertlist[5] = VertexInterp(isolevel,v.vert[5],v.vert[6],v.concentration[5][substance],v.concentration[6][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 64) == 64) vertlist[6] = VertexInterp(isolevel,v.vert[6],v.vert[7],v.concentration[6][substance],v.concentration[7][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 128) == 128) vertlist[7] = VertexInterp(isolevel,v.vert[7],v.vert[4],v.concentration[7][substance],v.concentration[4][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 256) == 256) vertlist[8] = VertexInterp(isolevel,v.vert[0],v.vert[4],v.concentration[0][substance],v.concentration[4][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 512) == 512) vertlist[9] = VertexInterp(isolevel,v.vert[1],v.vert[5],v.concentration[1][substance],v.concentration[5][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 1024) == 1024) vertlist[10] = VertexInterp(isolevel,v.vert[2],v.vert[6],v.concentration[2][substance],v.concentration[6][substance]); 
		        if ((BZGlobals.edgeTable[cubeindex] & 2048) == 2048) vertlist[11] = VertexInterp(isolevel,v.vert[3],v.vert[7],v.concentration[3][substance],v.concentration[7][substance]); 
		        
		        /* Create the triangle */
		        for (int jj=0; BZGlobals.triTable[cubeindex][jj]!=-1; jj+=3) {
		          PVector[] pt = new PVector[3];
		          pt[0] = vertlist[BZGlobals.triTable[cubeindex][jj  ]];
		          pt[1] = vertlist[BZGlobals.triTable[cubeindex][jj+1]];
		          pt[2] = vertlist[BZGlobals.triTable[cubeindex][jj+2]];
		          drawTriFace(pt,  _parent.space.vox[ii], col,g);
		        
		      }
		        
		        
		       _parent.popStyle();
		      }
		    }
		  }
	//TODO: finish this function. It should add the substances before making the surface.
	void marchCube_boolean(float isolevel, int substance,int substance2, boolean col,boolean trim,int abc4,float trimthreshold,boolean invertrim,PGraphics g){

	    for(int ii = 0; ii < vox.length; ii++){

	      /*
	      Determine the index into the edge table which
	       tells us which vertices are inside of the surface
	       */
	       //println(ii);
	      int cubeindex = 0;
	      if (vox[ii].concentration[0][substance] < isolevel) cubeindex |= 1;
	      if (vox[ii].concentration[1][substance] < isolevel) cubeindex |= 2; 
	      if (vox[ii].concentration[2][substance] < isolevel) cubeindex |= 4; 
	      if (vox[ii].concentration[3][substance] < isolevel) cubeindex |= 8; 
	      if (vox[ii].concentration[4][substance] < isolevel) cubeindex |= 16; 
	      if (vox[ii].concentration[5][substance] < isolevel) cubeindex |= 32; 
	      if (vox[ii].concentration[6][substance] < isolevel) cubeindex |= 64; 
	      if (vox[ii].concentration[7][substance] < isolevel) cubeindex |= 128;

	      if(trim){
	    	  int incount = 0;
	    	  int outcount = 0;
	    	  if(vox[ii].concentration[0][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[1][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[2][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[3][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[4][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[5][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[6][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  if(vox[ii].concentration[7][abc4]<=trimthreshold){
	    		  incount++;
	    	  }
	    	  outcount = 8-incount;
	    	  if(!invertrim){
	          if(abc4!=substance&&incount>3){
	            continue;
	          }
	    	  }else{
	    		  if(abc4!=substance&&outcount>3){
			            continue;
			          }
	    	  }
	          
	       }
	      if (BZGlobals.edgeTable[cubeindex] != 0x0) 
	      { 
	    	  
	        _parent.pushStyle();
	        PVector[] vertlist = new PVector[12];
	        Voxel v = vox[ii];
	        /* Find the vertices where the surface intersects the cube */
	        if ((BZGlobals.edgeTable[cubeindex] & 1) == 1) vertlist[0] = VertexInterp(isolevel,v.vert[0],v.vert[1],v.concentration[0][substance],v.concentration[1][substance]);
	        if ((BZGlobals.edgeTable[cubeindex] & 2) == 2) vertlist[1] = VertexInterp(isolevel,v.vert[1],v.vert[2],v.concentration[1][substance],v.concentration[2][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 4) == 4) vertlist[2] = VertexInterp(isolevel,v.vert[2],v.vert[3],v.concentration[2][substance],v.concentration[3][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 8) == 8) vertlist[3] = VertexInterp(isolevel,v.vert[3],v.vert[0],v.concentration[3][substance],v.concentration[0][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 16) == 16) vertlist[4] = VertexInterp(isolevel,v.vert[4],v.vert[5],v.concentration[4][substance],v.concentration[5][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 32) == 32) vertlist[5] = VertexInterp(isolevel,v.vert[5],v.vert[6],v.concentration[5][substance],v.concentration[6][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 64) == 64) vertlist[6] = VertexInterp(isolevel,v.vert[6],v.vert[7],v.concentration[6][substance],v.concentration[7][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 128) == 128) vertlist[7] = VertexInterp(isolevel,v.vert[7],v.vert[4],v.concentration[7][substance],v.concentration[4][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 256) == 256) vertlist[8] = VertexInterp(isolevel,v.vert[0],v.vert[4],v.concentration[0][substance],v.concentration[4][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 512) == 512) vertlist[9] = VertexInterp(isolevel,v.vert[1],v.vert[5],v.concentration[1][substance],v.concentration[5][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 1024) == 1024) vertlist[10] = VertexInterp(isolevel,v.vert[2],v.vert[6],v.concentration[2][substance],v.concentration[6][substance]); 
	        if ((BZGlobals.edgeTable[cubeindex] & 2048) == 2048) vertlist[11] = VertexInterp(isolevel,v.vert[3],v.vert[7],v.concentration[3][substance],v.concentration[7][substance]); 
	        
	        /* Create the triangle */
	        for (int jj=0; BZGlobals.triTable[cubeindex][jj]!=-1; jj+=3) {
	          PVector[] pt = new PVector[3];
	          pt[0] = vertlist[BZGlobals.triTable[cubeindex][jj  ]];
	          pt[1] = vertlist[BZGlobals.triTable[cubeindex][jj+1]];
	          pt[2] = vertlist[BZGlobals.triTable[cubeindex][jj+2]];
	          drawTriFace(pt, _parent.space.vox[ii], col,g);
	        
	      }
	        
	        
	       _parent.popStyle();
	      }
	    }
	  }
	
	public void exportVoxels(PrintWriter output,float voxthreshold,float voxthreshold2,float booleanthreshold,int booleanOperation,int abc1,int abc5,GeometryMode mode) {
		Boolean usevar =false;
		Boolean drawable;
		  for(int i = 0; i < vox.length; i++) 
		  { 
		    drawable=isDrawable(voxthreshold, voxthreshold2,
					booleanthreshold, booleanOperation, abc1, abc5, i);
		    
		  if(drawable){
		        output.println(vox[i].pos[0]+","+
	                       vox[i].pos[1]+","+
	                       vox[i].pos[2]
	                  );
		        float[] color= {(float) vox[i].concentration[0][0],(float) vox[i].concentration[0][1],(float) vox[i].concentration[0][2]};
	                  if(usevar){
	                	  color=vox[i].getVariance();
	                  }
	                  output.println(color[0]+","+
	                		  color[1]+","+
	                		  color[2]
	                  );
		  }
		  	
		  }
	}
	
	public void drawGeometry(PGraphics g,float voxthreshold,float voxthreshold2,float booleanthreshold,int booleanOperation,int abc1,int abc5,GeometryMode geomode) {
		  Boolean drawable;
		  for(int i = 0; i < vox.length; i++) 
		  { 
		    drawable = isDrawable(voxthreshold, voxthreshold2,
					booleanthreshold, booleanOperation, abc1, abc5, i);
		    g.beginShape(PApplet.QUADS);
		  if(drawable) {
			  if(geomode==GeometryMode.diffusion){
				  vox[i].EnableVariance(g, abc1);
			  }
			  
			  if(geomode==GeometryMode.normal){
				  vox[i].EnableRegular(g);
				  //vox[i].EnableGS(g);
				  
			  }if(geomode==GeometryMode.id){
				  vox[i].EnableID(g);
			  }
			  vox[i].draw(g);
		  }
		  g.endShape();
		  }
	}
	private Boolean isDrawable(float voxthreshold, float voxthreshold2,
			float booleanthreshold, int booleanOperation, int abc1, int abc5,
			int i) {
		Boolean drawable;
		drawable=false;  
		  if(vox[i].concentration[0][abc1]<=voxthreshold&&vox[i].concentration[0][abc1]>=voxthreshold2){
		    drawable=true;
		  }
		 if(booleanOperation==1){
		   if(vox[i].concentration[0][abc5]<=booleanthreshold){ //if the voxel is inside the intersecting space draw it
		     drawable=true;
		   }
		 }
		 if(booleanOperation==2){
		   if(vox[i].concentration[0][abc5]<=booleanthreshold){ //If the voxel is inside the intersecting space, dont draw it
		     drawable=false;
		   }
		 }
		 if(booleanOperation==3){
		   if(vox[i].concentration[0][abc5]>booleanthreshold){//if the voxel is outside the intersecting space, dont draw it...
		     drawable=false;
		   }
		 }  
		 if(!_parent.isWithinSpecialTrim(vox[i])){
			 drawable=false;
		 }
		return drawable;
	}
	
	PVector VertexInterpTrim(float isolevel,PVector p1,PVector p2,float valp1,float valp2,float valt1, float valt2,float trim)
	  {
	    float mu;
	    PVector p = new PVector(0,0,0);
	    
	    if (PApplet.abs(isolevel-valp1) < 0.00001)
	      return(p1);
	    if (PApplet.abs(isolevel-valp2) < 0.00001)
	      return(p2);
	    
	    		
		if (PApplet.abs(valp1-valp2) < 0.00001)
			  return(p1);
		    
	    mu = (isolevel - valp1) / (valp2 - valp1);
	    p.x = p1.x + mu * (p2.x - p1.x);
	    p.y = p1.y + mu * (p2.y - p1.y);
	    p.z = p1.z + mu * (p2.z - p1.z);
	    if(PApplet.abs(p.x)>100){
	    	int stopme =10;
	    	stopme +=2;
	    }
	    return(p);
	  }		
	  PVector VertexInterp(float isolevel,PVector p1,PVector p2,double valp1,double valp2)
	  {
	    double mu;
	    PVector p = new PVector(0,0,0);

	    if (PApplet.abs((float) (isolevel-valp1)) < 0.00001)
	      return(p1);
	    if (PApplet.abs((float) (isolevel-valp2)) < 0.00001)
	      return(p2);
	    if (PApplet.abs((float) (valp1-valp2)) < 0.00001)
	      return(p1);

	    mu = (isolevel - valp1) / (valp2 - valp1);
	    p.x = (float) (p1.x + mu * (p2.x - p1.x));
	    p.y = (float) (p1.y + mu * (p2.y - p1.y));
	    p.z = (float) (p1.z + mu * (p2.z - p1.z));

	    return(p);
	  }
	  
	  void drawTriFace(PVector[] f, Voxel v, boolean col,PGraphics g)
	  {
	    g.pushStyle();

	    g.noStroke();
	    g.colorMode(PApplet.RGB, 1);
	    g.beginShape(PApplet.TRIANGLES);
	    
	    if(col)g.fill(PApplet.constrain(v.drawingConc[0],0,1),PApplet.constrain(v.drawingConc[1],0,1),PApplet.constrain(v.drawingConc[2],0,1));
	                 
	                 
	    //if(!col) g.fill(f[0].x/dimX*0.6f+0.2f); 
	    if(!col) g.fill(0.4f); 
	    
	    g.vertex(f[0].x,f[0].y,f[0].z);
	   
	    g.vertex(f[1].x,f[1].y,f[1].z);
	    
	    g.vertex(f[2].x,f[2].y,f[2].z);
	    
	    g.endShape();
	    g.popStyle();
	  }
	  double[] calcConcAt(PVector pp) {
		    
		  	double[] conc = new double[3];
		    int i = (int) PApplet.constrain(pp.x, 0, dimX-1);
		    int j = (int) PApplet.constrain(pp.y, 0, dimY-1);
		    int k =(int) PApplet.constrain(pp.z, 0, dimZ-1);
		    
		    conc[0] = a[i][j][k][p];
		    conc[1] = b[i][j][k][p];
		    conc[2] = c[i][j][k][p];
		    
		    return conc;
	  }
	  public void fadeYourself(int[] axis){
		    float fader = 0;
		    if(axis[0]==1){//Freezing YZ
		      for (int x = 0; x < dimX; x++) {
		          for (int z = 0; z < dimZ; z++) {
		            for (int y = 0; y < dimY; y++) {
		              //The fading axis[] should be cero!
		              
		              a[x][y][z][p] = ((a[0][y][z][p])*(1-fader) + (a[dimX-1][y][z][p])*fader);
		              b[x][y][z][p] = ((b[0][y][z][p])*(1-fader) + (b[dimX-1][y][z][p])*fader);
		              c[x][y][z][p] = ((c[0][y][z][p])*(1-fader) + (c[dimX-1][y][z][p])*fader);
		              
		            }
		          }
		          fader+=1f/dimX;
		      }  
		    }else if(axis[1]==1){//Freezing the XZ
		      
		      for (int y = 0; y < dimY; y++) {
		          for (int x = 0; x < dimX; x++) {
		            for (int z = 0; z < dimZ; z++) {
		              //The fading axis[] should be cero!
		              a[x][y][z][p] = ((a[x][0][z][p])*(1-fader)+(a[x][dimY-1][z][p])*fader);
		              b[x][y][z][p] = ((b[x][0][z][p])*(1-fader) +(b[x][dimY-1][z][p])*fader);
		              c[x][y][z][p] = ((c[x][0][z][p])*(1-fader) +(c[x][dimY-1][z][p])*fader);
		           }
		        }
		          fader+=1f/dimY;
		      }  
		   }else if(axis[2]==1){//Freezing the XY
		     for (int z = 0; z < dimZ; z++) {
		          for (int y = 0; y < dimY; y++) {
		            for (int x = 0; x < dimX; x++) {
		              //The fading axis[] should be cero!
		              a[x][y][z][p] = ((a[x][y][0][p])*(1-fader)+(a[x][y][dimZ-1][p])*fader);
		              b[x][y][z][p] = ((b[x][y][0][p])*(1-fader) +(b[x][y][dimZ-1][p])*fader);
		              c[x][y][z][p] = ((c[x][y][0][p])*(1-fader) +(c[x][y][dimZ-1][p])*fader);
		            }
		          }
		          fader+=1f/dimZ;
		      }  
		    }   
		    
		    
		    
		    
		  }
	public void makeBooleanOperationInPoint(int i, int j, int k,
			int booleanAbc1, int booleanAbc2,float tre1,float tre2, int booleanOperation,BZGrid withspace) {
		double[][][][][] properties = {a,b,c};
		double[][][][][] properties2 = {withspace.a,withspace.b,withspace.c};
		if(properties[booleanAbc1][i][j][k][p]>tre1){//
			double value = properties[booleanAbc2][i][j][k][p];
			if(value<tre2){
				properties[booleanAbc1][i][j][k][p]=tre2-value+tre1;
			}
		}
			
	}
	public double getA(int i, int j, int k) {
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		return a[i][j][k][p];
	}
	public double getB(int i, int j, int k) {
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		return b[i][j][k][p];
	}
	public double getC(int i, int j, int k) {
		i=General.wrapIndex(i,dimX);
		j=General.wrapIndex(j,dimY);
		k=General.wrapIndex(k,dimZ);
		return c[i][j][k][p];
	}

}
