package ecobertura.ui.editors;

import java.util.logging.Logger;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

import ecobertura.ui.annotation.CoverageAnnotationModel;

public class EditorTracker {

	private static final Logger logger = Logger.getLogger("ecobertura.ui.editors"); //$NON-NLS-1$
	
	private final IWorkbench workbench;

	private final WindowListener windowListener = new WindowListener(this);

	public static EditorTracker trackEditorsOf(final IWorkbench workbench) {
		return new EditorTracker(workbench);
	}
	
	private EditorTracker(final IWorkbench workbench) {
		this.workbench = workbench;
		addListenersToEditorWindows();
		annotateAllEditors();
		logger.fine("EditorTracker registered."); //$NON-NLS-1$
	}

	private void addListenersToEditorWindows() {
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		addPartListenersTo(windows);
		workbench.addWindowListener(windowListener);
	}

	private void addPartListenersTo(IWorkbenchWindow[] windows) {
		for (IWorkbenchWindow window : windows) {
			window.getPartService().addPartListener(windowListener.getPartListener());
		}
	}

	private void annotateAllEditors() {
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			annotateEditorsOfWindow(window);
		}
	}

	private void annotateEditorsOfWindow(IWorkbenchWindow window) {
		IWorkbenchPage[] pages = window.getPages();
		for (IWorkbenchPage page : pages) {
			annotateEditorsOfPage(page);
		}
	}

	private void annotateEditorsOfPage(IWorkbenchPage page) {
		IEditorReference[] editors = page.getEditorReferences();
		for (IEditorReference editor : editors) {
			annotateEditor(editor);
		}
	}
	
	void annotateEditor(IWorkbenchPartReference partref) {
		IWorkbenchPart part = partref.getPart(false);
		if (part instanceof ITextEditor) {
			CoverageAnnotationModel.attachTo((ITextEditor) part);
		}
	}

	public void dispose() {
		workbench.removeWindowListener(windowListener);
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			window.getPartService().removePartListener(windowListener.getPartListener());
		}
	}
}
