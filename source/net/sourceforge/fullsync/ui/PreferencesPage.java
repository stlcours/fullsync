/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301, USA.
 *
 * For information about the authors of this project Have a look
 * at the AUTHORS file in the root of this project.
 */
package net.sourceforge.fullsync.ui;

import net.sourceforge.fullsync.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author <a href="mailto:codewright@gmx.net">Jan Kopcsek</a>
 */
public class PreferencesPage implements WizardPage {
	private WizardDialog dialog;
	private PreferencesComposite composite;
	private Preferences preferences;

	public PreferencesPage(WizardDialog dialog, Preferences preferences) {
		this.dialog = dialog;
		dialog.setPage(this);
		this.preferences = preferences;
	}

	@Override
	public String getTitle() {
		return Messages.getString("PreferencesPage.Preferences"); //$NON-NLS-1$
	}

	@Override
	public String getCaption() {
		return Messages.getString("PreferencesPage.Preferences"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return ""; //$NON-NLS-1$
	}

	@Override
	public Image getIcon() {
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void createContent(Composite content) {
		composite = new PreferencesComposite(content, SWT.NULL, preferences);
	}

	@Override
	public boolean apply() {
		composite.apply();
		return true; //FIXME: return false if failed
	}

	@Override
	public boolean cancel() {
		return true;
	}
}
