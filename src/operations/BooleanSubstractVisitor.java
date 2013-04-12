package operations;

public class BooleanSubstractVisitor implements BooleanVisitor {

	@Override
	public void visit(SurfaceBooleanOperation visitable) {
		visitable.bz.applySubsSurfOperation();
	}

}
