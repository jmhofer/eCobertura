package ecobertura.ui.editors;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

final class WindowListener implements IWindowListener {
	private EditorTracker editorTracker;
	
	WindowListener(EditorTracker editorTracker) {
		this.editorTracker = editorTracker;
	}
	
	private final IPartListener2 partListener = new IPartListener2() {
		public void partOpened(IWorkbenchPartReference partref) {
			editorTracker.annotateEditor(partref);
		}

		public void partActivated(IWorkbenchPartReference partref) {}
		public void partBroughtToTop(IWorkbenchPartReference partref) {}
		public void partVisible(IWorkbenchPartReference partref) {}
		public void partInputChanged(IWorkbenchPartReference partref) {}
		public void partClosed(IWorkbenchPartReference partref) {}
		public void partDeactivated(IWorkbenchPartReference partref) {}
		public void partHidden(IWorkbenchPartReference partref) {}
	};
	
	public void windowOpened(IWorkbenchWindow window) {
		window.getPartService().addPartListener(partListener);
	}

	public void windowClosed(IWorkbenchWindow window) {
		window.getPartService().removePartListener(partListener);
	}

	public void windowActivated(IWorkbenchWindow window) { /* NOP */ }
	public void windowDeactivated(IWorkbenchWindow window) { /* NOP */ }

	IPartListener2 getPartListener() {
		return partListener;
	}
}

