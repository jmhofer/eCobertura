package ecobertura.core.launching;

import java.util.Arrays;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathProvider;
import org.eclipse.jdt.launching.JavaRuntime;

import ecobertura.core.cobertura.CoberturaWrapper;

public class CoverageClasspathProvider implements IRuntimeClasspathProvider {
	private static final Logger logger = Logger.getLogger("ecobertura.core.launching");
	
	public static final String ID = "ecobertura.core.launching.coverageClasspathProvider"; //$NON-NLS-1$
	
	private static ThreadLocal<IRuntimeClasspathProvider> wrappedProvider = 
		new ThreadLocal<IRuntimeClasspathProvider>();

	public static void wrap(IRuntimeClasspathProvider wrappedProvider) {
		if (wrappedProvider != CoverageClasspathProvider.wrappedProvider) {
			logger.fine("wrapping provider...");
			CoverageClasspathProvider.wrappedProvider.set(wrappedProvider);
		}
	}
	
	@Override
	public IRuntimeClasspathEntry[] computeUnresolvedClasspath(
			final ILaunchConfiguration configuration) throws CoreException {
		return wrappedProvider.get().computeUnresolvedClasspath(configuration);
	}

	@Override
	public IRuntimeClasspathEntry[] resolveClasspath(
			final IRuntimeClasspathEntry[] entries, final ILaunchConfiguration configuration)
			throws CoreException {
		
		logger.fine("resolving classpath...");
		final IRuntimeClasspathEntry[] resolvedEntries = wrappedProvider.get().resolveClasspath(
				entries, configuration);
		final IRuntimeClasspathEntry[] resolvedEntriesWithCobertura = Arrays.copyOf(
				resolvedEntries, resolvedEntries.length + 1);
		resolvedEntriesWithCobertura[resolvedEntries.length] = coberturaEntry();
		logger.fine("resolved entries: " + Arrays.toString(resolvedEntriesWithCobertura));
		
		return resolvedEntriesWithCobertura;
	}
	
	private IRuntimeClasspathEntry coberturaEntry() throws CoreException {
		final IPath pathToCoberturaJar = CoberturaWrapper.get().pathToJar();
		return JavaRuntime.newArchiveRuntimeClasspathEntry(pathToCoberturaJar);
	}
}
