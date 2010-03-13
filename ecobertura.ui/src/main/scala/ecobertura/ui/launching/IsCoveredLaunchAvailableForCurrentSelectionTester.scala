package ecobertura.ui.launching

import org.eclipse.core.runtime.IConfigurationElement
import org.eclipse.core.runtime.Platform
import org.eclipse.core.expressions._
import org.eclipse.debug.core.ILaunchManager

class IsCoveredLaunchAvailableForCurrentSelectionTester extends PropertyTester {
	private val launchShortCutExtensionPoint = "org.eclipse.debug.ui.launchShortcuts" // $NON-NLS-1$
		
	override def test(receiver: Any, property: String, args: Array[Object], expectedValue: Any) = {
		val idOfLaunchShortcutToDelegateTo = args(0).asInstanceOf[String]
		val selectionContext = selectionContextFor(receiver)
		enablementConfigurationOfLaunchShortcutToDelegateTo(idOfLaunchShortcutToDelegateTo) match {
			case Some(enablement) => evaluateEnablementConfigElement(enablement, selectionContext)
			case None => false
		}
	}

	private def selectionContextFor(selection: Any) = {
		val selectionContext = new EvaluationContext(null, selection)
		selectionContext.addVariable("selection", selection) // $NON-NLS-1$
		selectionContext
	}
	
	private def enablementConfigurationOfLaunchShortcutToDelegateTo(id: String) = {
		val fittingShortcuts = shortcutsWithRunModeAndId(id)
		enablementNodesForConfigurations(fittingShortcuts).find(_.size == 1) match {
			case Some(enablementNodes) => Some(enablementNodes.head)
			case None => None
		}
	}
	
	private def shortcutsWithRunModeAndId(id: String) = {
		val launchShortcutConfigurations = Platform.getExtensionRegistry.getConfigurationElementsFor(
				launchShortCutExtensionPoint)
		launchShortcutConfigurations.filter(_.getAttribute("id") == id).filter(supportsRunMode(_)) // $NON-NLS-1$
	}
	
	private def enablementNodesForConfigurations(configurations: Array[IConfigurationElement]) = {
		for {
			configElement <- configurations
			contextualLaunchNodes = configElement.getChildren("contextualLaunch") // $NON-NLS-1$
			if contextualLaunchNodes.size == 1
		} yield contextualLaunchNodes.head.getChildren(ExpressionTagNames.ENABLEMENT)
	}

	private def supportsRunMode(configurationElement: IConfigurationElement) = {
		val launchModes = configurationElement.getAttribute("modes") // $NON-NLS-1$
		if (launchModes == null) false 
		else launchModes.split("\\W").find(_ == ILaunchManager.RUN_MODE) != None 
	}
	
	private def evaluateEnablementConfigElement(enablement: IConfigurationElement, 
			selectionContext: EvaluationContext) = {
		
		val enablementExpression = ExpressionConverter.getDefault.perform(enablement)
		enablementExpression.evaluate(selectionContext) != EvaluationResult.FALSE
	}
}
