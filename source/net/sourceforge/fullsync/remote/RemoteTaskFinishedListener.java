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
/*
 * Created on Nov 29, 2004
 */
package net.sourceforge.fullsync.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import net.sourceforge.fullsync.TaskFinishedEvent;
import net.sourceforge.fullsync.TaskFinishedListener;

public class RemoteTaskFinishedListener extends UnicastRemoteObject implements RemoteTaskFinishedListenerInterface {

	private static final long serialVersionUID = 2L;

	private transient TaskFinishedListener localListener;

	public RemoteTaskFinishedListener(TaskFinishedListener localListener) throws RemoteException {
		this.localListener = localListener;
	}

	@Override
	public void taskFinished(TaskFinishedEvent event) throws RemoteException {
		localListener.taskFinished(event);
	}

}
