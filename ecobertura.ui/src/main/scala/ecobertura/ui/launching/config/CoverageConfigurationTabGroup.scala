package ecobertura.ui.launching.config

import org.eclipse.core.runtime._
import org.eclipse.debug.core._
import org.eclipse.debug.ui._

class CoverageConfigurationTabGroup extends ILaunchConfigurationTabGroup with IExecutableExtension {
	var tabGroupToExtend: Option[ILaunchConfigurationTabGroup] = None
	
	override def setInitializationData(config: IConfigurationElement, propertyName: String,
			data: Any) = {
		val tabGroup = ConfigurationTabGroupRetriever fromConfig config
		if (tabGroup != null)
			tabGroupToExtend = Some(tabGroup)
	}
	
	override def createTabs(dialog: ILaunchConfigurationDialog, mode: String) = 
			tabGroupToExtend match {
		case Some(tabGroup) => tabGroup createTabs (dialog, ILaunchManager.RUN_MODE)
		case _ => /* nothing to do */
	}
	
	override def getTabs = tabGroupToExtend match {
		case Some(tabGroup) => tabGroup.getTabs
		case _ => Array[ILaunchConfigurationTab]()
	}
	
	override def initializeFrom(config: ILaunchConfiguration) = tabGroupToExtend match {
		case Some(tabGroup) => tabGroup initializeFrom config
		case _ => /* nothing to do */
	}
	
	override def setDefaults(configWC: ILaunchConfigurationWorkingCopy) = tabGroupToExtend match {
		case Some(tabGroup) => tabGroup setDefaults configWC
		case _ => /* nothing to do */
	}

	override def performApply(configWC: ILaunchConfigurationWorkingCopy) = tabGroupToExtend match {
		case Some(tabGroup) => tabGroup performApply configWC
		case _ => /* nothing to do */
	}
	
	override def dispose = tabGroupToExtend match {
		case Some(tabGroup) => tabGroup.dispose
		case _ => /* nothing to do */
	}
	
	override def launched(launch: ILaunch) = {
		// deprecated, ignore
	}
}
