package it.prisma.businesslayer.orchestrator.bpm;

import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.IEJBCommand;
import it.prisma.businesslayer.orchestrator.bpm.ejbcommands.cloudify.CfyAPIUploadRecipeCommand;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * This class contains all EJB Commands and it's used to let jBPM executor's
 * commands rejoin CDI enviroment.
 * 
 * @author l.biava
 * 
 */
@Singleton
@Startup
public class OrchestratorContext {

	@Inject
	private Instance<IEJBCommand> ejbCommands;
	
	@Inject
	private CfyAPIUploadRecipeCommand cfyAPIUploadRecipeCommand;


	public CfyAPIUploadRecipeCommand getCfyAPIUploadRecipeCommand() {
		return cfyAPIUploadRecipeCommand;
	}

	public IEJBCommand getCommand(String className) throws ClassNotFoundException {
		for(IEJBCommand ejbCommand : ejbCommands) {
			if(ejbCommand.getClass().getName().equals(className))
				return ejbCommand;
		}
		
		throw new ClassNotFoundException(className + " is not a loaded EJBCommand");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("OrchestratorContext Alive !");
		System.out.flush();
		
		
	}
}
