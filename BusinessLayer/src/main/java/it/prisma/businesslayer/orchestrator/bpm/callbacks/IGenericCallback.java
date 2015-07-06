package it.prisma.businesslayer.orchestrator.bpm.callbacks;

/**
 * Generic callback interface to try callback task in BP.
 * 
 * @author l.biava
 *
 */
public interface IGenericCallback {
	public void onCallback(Object data, Class<?> resultClass);
}
