package operations;

import main.BZEclipse;

public class SurfaceBooleanOperation implements BooleanOperation , BooleanVisitable{

	BZEclipse bz;
	BooleanVisitor visitor;
	public SurfaceBooleanOperation(BZEclipse bz,BooleanVisitor visitor){
		this.bz=bz;
		this.visitor = visitor;
	}
	@Override
	public void apply() {
		this.accept(visitor);
	}

	@Override
	public void accept(BooleanVisitor visitor) {
		visitor.visit(this);
	}

}
