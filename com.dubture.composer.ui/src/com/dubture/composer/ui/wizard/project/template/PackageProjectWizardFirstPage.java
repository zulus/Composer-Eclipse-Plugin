package com.dubture.composer.ui.wizard.project.template;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.php.internal.ui.wizards.CompositeData;
import org.eclipse.php.internal.ui.wizards.NameGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;

import com.dubture.composer.ui.ComposerUIPlugin;
import com.dubture.composer.ui.converter.String2KeywordsConverter;
import com.dubture.composer.ui.wizard.LocationGroup;
import com.dubture.composer.ui.wizard.project.BasicSettingsGroup;
import com.dubture.composer.ui.wizard.project.ComposerProjectWizardFirstPage;
import com.dubture.composer.ui.wizard.project.VersionGroup;
import com.dubture.getcomposer.core.ComposerPackage;

/**
 * @author Robert Gruendler <r.gruendler@gmail.com>
 */
@SuppressWarnings("restriction")
public class PackageProjectWizardFirstPage extends ComposerProjectWizardFirstPage implements IShellProvider {

	private Validator projectTemplateValidator;
	
	private Button overrideComposer;
	private boolean doesOverride = false;
	
	public PackageProjectWizardFirstPage() {
		super();
		setPageComplete(false);
		setTitle("Basic Composer Configuration");
		setDescription("Create a new project from existing package");
	}
	
	@Override
	public void createControl(Composite parent) {

		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), false));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		initialName = "";
		// create UI elements
		nameGroup = new NameGroup(composite, initialName, getShell());
		nameGroup.addObserver(this);
		PHPLocationGroup = new LocationGroup(composite, nameGroup, getShell());
		
		overrideComposer = new Button(composite, SWT.CHECK);
		overrideComposer.setText("Override composer.json from target package");
		overrideComposer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doesOverride = overrideComposer.getSelection();
				settingsGroup.setEnabled(overrideComposer.getSelection());
			}
		});
		
		final Group group = new Group(composite, SWT.None);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(3, false));
		group.setText("");
		
		settingsGroup = new BasicSettingsGroup(group, getShell());
		settingsGroup.setEnabled(false);
		settingsGroup.addObserver(this);

		CompositeData data = new CompositeData();
		data.setParetnt(composite);
		data.setSettings(getDialogSettings());
		data.setObserver(PHPLocationGroup);

		versionGroup = new VersionGroup(this, composite);
		nameGroup.addObserver(PHPLocationGroup);

		// initialize all elements
		nameGroup.notifyObservers();
		// create and connect validator
		projectTemplateValidator = new Validator(this);
		nameGroup.addObserver(projectTemplateValidator);
		PHPLocationGroup.addObserver(projectTemplateValidator);

		Dialog.applyDialogFont(composite);
		
		setControl(composite);
		composerPackage = new ComposerPackage();
		keywordConverter = new String2KeywordsConverter(composerPackage);
		setHelpContext(composite);
	}
	
	@Override
	public void performFinish(final IProgressMonitor monitor) {
		

	}
	
	@Override
	protected void setHelpContext(Control container) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ComposerUIPlugin.PLUGIN_ID + "." + "help_context_wizard_template_firstpage");
	}
	
	public boolean doesOverrideComposer() {
		return doesOverride;
	}
}
