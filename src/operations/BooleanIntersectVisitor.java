package operations;

public class BooleanIntersectVisitor implements BooleanVisitor {

	@Override
	public void visit(SurfaceBooleanOperation visitable) {
		visitable.bz.applyInterSurfOperation();

	}

}
