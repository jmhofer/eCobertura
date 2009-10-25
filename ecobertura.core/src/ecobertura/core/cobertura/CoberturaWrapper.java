package ecobertura.core.cobertura;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sourceforge.cobertura.coveragedata.ProjectData;
import net.sourceforge.cobertura.instrument.Main;

public class CoberturaWrapper implements ICoberturaWrapper {

	private static final String COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS = "addInstrumentationToSingleClass"; //$NON-NLS-1$
	
	private static CoberturaWrapper instance;

	final Main coberturaMain;
	final ProjectData projectData;
	
	public static ICoberturaWrapper get() {
		if (instance == null) {
			System.err.println("CoberturaWrapper: initializing...");
			instance = new CoberturaWrapper();
		}
		return instance;
	}
	
	private CoberturaWrapper() {
		coberturaMain = new Main();
		projectData = new ProjectData();
		initializeCoberturaProjectData();
	}

	private void initializeCoberturaProjectData() {
		try {
			setPrivateProjectData();
			
		} catch (NoSuchFieldException e) {
			wrapByCoberturaException("field %s not found in Cobertura Main", e); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			wrapByCoberturaException("unable to access field %s in Cobertura Main", e); //$NON-NLS-1$
		}
	}
	
	private void setPrivateProjectData()
			throws NoSuchFieldException, IllegalAccessException {
		
		Field projectDataField = coberturaMain.getClass().getDeclaredField("projectData");
		projectDataField.setAccessible(true);
		projectDataField.set(coberturaMain, projectData);
	}
	
	@Override
	public ProjectData projectData() {
		return projectData;
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
		
		final Method addInstrumentationToSingleClass = coberturaMain.getClass().getDeclaredMethod(
				COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS, File.class);
		
		addInstrumentationToSingleClass.setAccessible(true);
		addInstrumentationToSingleClass.invoke(coberturaMain, classFileToInstrument);
	}

	private void wrapByCoberturaException(final String messageTemplate,
			final Throwable cause) {
		
		final String message = String.format(
				messageTemplate, COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS);
		
		throw new CoberturaException(message, cause);
	}
}
