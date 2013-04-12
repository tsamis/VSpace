package commands;

import java.util.List;


public class MacroCommand implements Command{
	List<Command> commands;

	@Override
	public void execute() {
		for(Command c : commands){
			c.execute();
		}
		
	}

	@Override
	public String getName() {
		return "Macro";
	}
	
}
