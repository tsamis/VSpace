package commands;

import model.BZGrid;

public class PaintCommand implements Command {
	BZGrid grid;
	int x,y,z,r;
	double a,b,c;
	float op;
	public PaintCommand(BZGrid grid, int x, int y, int z,double a,double b, double c,int r,float op){
		this.grid 	= grid;
		this.x 		= x;
		this.y 		= y;
		this.z 		= z;
		this.a		= a;
		this.b		= b;
		this.c		= c;
		this.r		= r;
		this.op 	= op;
	}
	public void execute(){
		for(int i = x-r;i<=x+r;i++){
			for(int j = y-r;j<=y+r;j++){
				for(int k = z-r;k<=z+r;k++){
					double dsqx = Math.pow(i-x, 2);
					double dsqy = Math.pow(j-y, 2);
					double dsqz = Math.pow(k-z, 2);
					double dist = Math.sqrt(dsqx+dsqy+dsqz);
					if(dist<=r){
						grid.setA(i, j, k, a*op+(1-op)*grid.getA(i,j,k));
						grid.setB(i, j, k, b*op+(1-op)*grid.getB(i,j,k));
						grid.setC(i, j, k, c*op+(1-op)*grid.getC(i,j,k));
					}
				}
			}
		}
		
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Paint";
	}
}
