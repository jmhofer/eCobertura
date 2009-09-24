package ecobertura.ui.annotation;


import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import ecobertura.core.log.Logger;


// TODO cleanup

public class EditorTracker {

	private final IWorkbench workbench;

	private final IWindowListener windowListener = new IWindowListener() {
		public void windowOpened(IWorkbenchWindow window) {
			window.getPartService().addPartListener(partListener);
		}

		public void windowClosed(IWorkbenchWindow window) {
			window.getPartService().removePartListener(partListener);
		}

		public void windowActivated(IWorkbenchWindow window) {}
		public void windowDeactivated(IWorkbenchWindow window) {}
	};

	private final IPartListener2 partListener = new IPartListener2() {
		public void partOpened(IWorkbenchPartReference partref) {
			annotateEditor(partref);
		}

		public void partActivated(IWorkbenchPartReference partref) {}
		public void partBroughtToTop(IWorkbenchPartReference partref) {}
		public void partVisible(IWorkbenchPartReference partref) {}
		public void partInputChanged(IWorkbenchPartReference partref) {}
		public void partClosed(IWorkbenchPartReference partref) {}
		public void partDeactivated(IWorkbenchPartReference partref) {}
		public void partHidden(IWorkbenchPartReference partref) {}
	};

	public static EditorTracker trackEditorsOf(final IWorkbench workbench) {
		return new EditorTracker(workbench);
	}
	
	private EditorTracker(final IWorkbench workbench) {
		this.workbench = workbench;
		addListenersToEditorWindows();
		annotateAllEditors();
		Logger.info("EditorTracker registered.");
	}

	private void addListenersToEditorWindows() {
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			window.getPartService().addPartListener(partListener);
		}
		workbench.addWindowListener(windowListener);
	}

	private void annotateAllEditors() {
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				IEditorReference[] editors = page.getEditorReferences();
				for (IEditorReference editor : editors) {
					annotateEditor(editor);
				}
			}
		}
	}
	
	private void annotateEditor(IWorkbenchPartReference partref) {
		IWorkbenchPart part = partref.getPart(false);
		if (part instanceof ITextEditor) {
			CoverageAnnotationModel.attachTo((ITextEditor) part);
		}
	}

	public void dispose() {
		workbench.removeWindowListener(windowListener);
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			window.getPartService().removePartListener(partListener);
		}
	}
}
