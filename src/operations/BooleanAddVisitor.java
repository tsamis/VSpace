package operations;

public class BooleanAddVisitor implements BooleanVisitor {

	@Override
	public void visit(SurfaceBooleanOperation visitable) {
		visitable.bz.applyAddSurfOperation();
	}

}
