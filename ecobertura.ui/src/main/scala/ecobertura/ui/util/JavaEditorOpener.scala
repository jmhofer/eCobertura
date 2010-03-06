package ecobertura.ui.util

import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.ui.JavaUI

object JavaEditorOpener {
	def openAndReveal(javaElement: IJavaElement) = {
		val editorPart = JavaUI.openInEditor(javaElement)
		JavaUI.revealInEditor(editorPart, javaElement)
	}
}
