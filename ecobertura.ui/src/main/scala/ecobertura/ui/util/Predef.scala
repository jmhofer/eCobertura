package ecobertura.ui.util

import org.eclipse.jface.viewers._
import org.eclipse.jface.action._

object Predef {
	implicit def actionFunction2Action(func: => Unit) = new Action {
		override def run = func
	}
	
	implicit def menuListener(func: IMenuManager => Unit) = new IMenuListener {
		override def menuAboutToShow(menuManager: IMenuManager) = func(menuManager)
	}
	
	implicit def doubleClickListener(func: DoubleClickEvent => Unit) = new IDoubleClickListener {
		override def doubleClick(event: DoubleClickEvent) = func(event)
	}
	
	implicit def run(taskToRun: => Unit) = new Runnable() {
			override def run = taskToRun
	}
}