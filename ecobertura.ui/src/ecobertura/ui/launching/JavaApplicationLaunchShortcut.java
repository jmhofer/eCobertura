package ecobertura.ui.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

public class JavaApplicationLaunchShortcut implements ILaunchShortcut, IExecutableExtension {

	private ILaunchShortcut shortcutToExtend;
	private boolean launchDelegationPossible;
	
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, 
			Object data) throws CoreException {
		
		shortcutToExtend = LaunchShortcutRetriever.fromId((String) data);
		launchDelegationPossible = (shortcutToExtend != null); 
	}

	@Override
	public void launch(ISelection selection, String mode) {
		if (launchDelegationPossible) {
			shortcutToExtend.launch(selection, mode);
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		if (launchDelegationPossible) {
			shortcutToExtend.launch(editor, mode);
		}
	}
}
