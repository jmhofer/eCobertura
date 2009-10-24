package ecobertura.core.cobertura;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sourceforge.cobertura.coveragedata.ProjectData;
import net.sourceforge.cobertura.instrument.Main;

public class CoberturaWrapper implements ICoberturaWrapper {

	private static final String COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS = "addInstrumentationToSingleClass"; //$NON-NLS-1$

	final Main coberturaMain;
	
	public static ICoberturaWrapper get() {
		return new CoberturaWrapper();
	}
	
	private CoberturaWrapper() {
		coberturaMain = new Main();
	}

	@Override
	public void instrumentClassFile(final File classFileToInstrument) throws CoberturaException {
		try {
			invokePrivateAddInstrumentationMethodOnMain(classFileToInstrument, coberturaMain);
			
		} catch (NoSuchMethodException e) {
			wrapByCoberturaException("method %s() not found in Cobertura Main", e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			wrapByCoberturaException("unable to access method %s() in Cobertura Main", e); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			wrapByCoberturaException("exception occurred within %s() in Cobertura Main", //$NON-NLS-1$
					e.getCause());
		}
	}

	private void invokePrivateAddInstrumentationMethodOnMain(
			final File classFileToInstrument, final Main coberturaMain)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		final Method addInstrumentationToSingleClass = coberturaMain.getClass().getMethod(
				COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS, File.class);
		
		addInstrumentationToSingleClass.setAccessible(true);
		addInstrumentationToSingleClass.invoke(coberturaMain, classFileToInstrument);
	}

	@Override
	public ProjectData projectData() throws CoberturaException {
		try {
			return retrievePrivateProjectData();
			
		} catch (NoSuchFieldException e) {
			wrapByCoberturaException("field %s not found in Cobertura Main", e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			wrapByCoberturaException("unable to access field %s in Cobertura Main", e); //$NON-NLS-1$
		}
		
		// will never happen because the exception wrapper always throws a CoberturaException
		return null; 
	}

	private ProjectData retrievePrivateProjectData()
			throws NoSuchFieldException, IllegalAccessException {
		
		Field projectDataField = coberturaMain.getClass().getDeclaredField("projectData");
		projectDataField.setAccessible(true);
		return (ProjectData) projectDataField.get(coberturaMain);
	}
	
	private void wrapByCoberturaException(final String messageTemplate,
			final Throwable cause) {
		
		final String message = String.format(
				messageTemplate, COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS);
		
		throw new CoberturaException(message, cause);
	}
}
