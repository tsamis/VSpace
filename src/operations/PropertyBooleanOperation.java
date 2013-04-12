package operations;

import main.BZEclipse;
import model.BZGrid;

public class PropertyBooleanOperation implements BooleanOperation {
	
	BZEclipse bz;
	public PropertyBooleanOperation(BZEclipse bz){
		this.bz = bz;
	}
	
	@Override
	public void apply() {
		
		this.bz.applyPropBoolOperation();
	}

}
