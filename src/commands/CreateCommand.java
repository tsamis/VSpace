package commands;

import main.BZEclipse;
import model.BZGrid;
import model.BZGrid.CreationMethod;

public class CreateCommand implements Command {

	CreationMethod method;
	BZEclipse bz;
	public CreateCommand(BZEclipse bz,CreationMethod method){
		this.bz = bz;
		this.method = method;
	}
	
	@Override
	public void execute() {
		bz.createSpace(this.method);
	}

	@Override
	public String getName() {
		return "Create Command";
	}

}
