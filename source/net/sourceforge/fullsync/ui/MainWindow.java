package net.sourceforge.fullsync.ui;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.fullsync.ExceptionHandler;
import net.sourceforge.fullsync.Profile;
import net.sourceforge.fullsync.ProfileManager;
import net.sourceforge.fullsync.ProfileSchedulerListener;
import net.sourceforge.fullsync.Synchronizer;
import net.sourceforge.fullsync.Task;
import net.sourceforge.fullsync.TaskGenerationListener;
import net.sourceforge.fullsync.TaskTree;
import net.sourceforge.fullsync.fs.File;
import net.sourceforge.fullsync.schedule.SchedulerChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class MainWindow extends org.eclipse.swt.widgets.Composite 
	implements ProfileSchedulerListener, ProfileListControlHandler, TaskGenerationListener, SchedulerChangeListener
{
    private ToolItem toolItemNew;
    private Menu menuBarMainWindow;
    private StatusLine statusLine;
    private CoolBar coolBar;
    private CoolItem coolItem1;
    private ToolBar toolBar1;
    private ToolItem toolItemRun;
    private ToolItem toolItemDelete;
    private ToolItem toolItemEdit;
    private CoolItem coolItem2;
    private ToolBar toolBar2;
    private ToolItem toolItemScheduleIcon;
    private ToolItem toolItemScheduleStart;
    private ToolItem toolItemScheduleStop;
    
    private Composite profileListContainer;
    private Menu profilePopupMenu; 
    private ProfileListComposite profileList;
    private GuiController guiController;
    
    private String statusDelayString;
    private Timer statusDelayTimer;
    
	public MainWindow(Composite parent, int style, GuiController initGuiController) 
	{
		super(parent, style);
		this.guiController = initGuiController;
		initGUI();

		getShell().addShellListener(new ShellAdapter() {
		    public void shellClosed(ShellEvent event) {
				event.doit = false;
		    	if (guiController.getPreferences().closeMinimizesToSystemTray()) 
		    	{
					minimizeToTray();
		    	} else {
		    	    guiController.closeGui();
		    	}
		    }

		    public void shellIconified(ShellEvent event) {
		        if (guiController.getPreferences().minimizeMinimizesToSystemTray())
		        {
		            event.doit = false;
		            minimizeToTray();
		        } else {
		            event.doit = true;
		        }
			}

		} );
		
	    ProfileManager pm = guiController.getProfileManager();
        pm.addSchedulerListener( this );
        pm.addSchedulerChangeListener( this );
        guiController.getSynchronizer().getTaskGenerator().addTaskGenerationListener(this);

        // REVISIT [Michele] Autogenerated event? I don't like it!
        schedulerStatusChanged(pm.isSchedulerEnabled());
	}

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	protected void initGUI(){
		try {
			this.setSize(600, 300);

			GridLayout thisLayout = new GridLayout();
			thisLayout.horizontalSpacing = 0;
			thisLayout.marginHeight = 0;
			thisLayout.marginWidth = 0;
			thisLayout.verticalSpacing = 0;
			this.setLayout(thisLayout);
            {
                coolBar = new CoolBar(this, SWT.NONE);
                coolBar.setLocked(false);
                {
                    coolItem1 = new CoolItem(coolBar, SWT.NONE);
                    {
                        toolBar1 = new ToolBar(coolBar, SWT.FLAT);
                        {
                            toolItemNew = new ToolItem(toolBar1, SWT.PUSH);
                    		toolItemNew.setImage( guiController.getImage( "Button_New.png" ) ); //$NON-NLS-1$
                    		toolItemNew.setToolTipText(Messages.getString("MainWindow.New_Profile")); //$NON-NLS-1$
                            toolItemNew
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        createNewProfile();
                                    }
                                });
                        }
                        {
                            toolItemEdit = new ToolItem(toolBar1, SWT.PUSH);
                    		toolItemEdit.setImage( guiController.getImage( "Button_Edit.png" ) ); //$NON-NLS-1$
                    		toolItemEdit.setToolTipText(Messages.getString("MainWindow.Edit_Profile")); //$NON-NLS-1$
                            toolItemEdit
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        editProfile( profileList.getSelectedProfile() );
                                    }
                                });
                        }
                        {
                            toolItemDelete = new ToolItem(toolBar1, SWT.PUSH);
                    		toolItemDelete.setImage( guiController.getImage( "Button_Delete.png" ) ); //$NON-NLS-1$
                    		toolItemDelete.setToolTipText(Messages.getString("MainWindow.Delete_Profile")); //$NON-NLS-1$
                            toolItemDelete
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        deleteProfile( profileList.getSelectedProfile() );
                                    }
                                });
                        }
                        {
                            toolItemRun = new ToolItem(toolBar1, SWT.PUSH);
                    		toolItemRun.setImage( guiController.getImage( "Button_Run.png" ) ); //$NON-NLS-1$
                    		toolItemRun.setToolTipText(Messages.getString("MainWindow.Run_Profile")); //$NON-NLS-1$
                            toolItemRun
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        runProfile( profileList.getSelectedProfile() );
                                    }
                                });
                        }
                        toolBar1.pack();
                    }
                    coolItem1.setControl(toolBar1);
                    //coolItem1.setMinimumSize(new org.eclipse.swt.graphics.Point(128, 22));
                    coolItem1.setPreferredSize(new org.eclipse.swt.graphics.Point(128, 22));
                }
                {
                    coolItem2 = new CoolItem(coolBar, SWT.NONE);
                    coolItem2.setSize(494, 22);
                    coolItem2.setMinimumSize(new org.eclipse.swt.graphics.Point(24,22));
                    coolItem2.setPreferredSize(new org.eclipse.swt.graphics.Point(24,22));
                    {
                        toolBar2 = new ToolBar(coolBar, SWT.FLAT);
                        coolItem2.setControl(toolBar2);
                        {
                            toolItemScheduleIcon = new ToolItem(toolBar2, SWT.NULL);
                            toolItemScheduleIcon.setImage( guiController.getImage( "Scheduler_Icon.png" ) ); //$NON-NLS-1$
            		        toolItemScheduleIcon.setDisabledImage( guiController.getImage( "Scheduler_Icon.png" ) ); //$NON-NLS-1$
            		        toolItemScheduleIcon.setEnabled( false );
                        }
                        {
                            toolItemScheduleStart = new ToolItem(toolBar2, SWT.NULL);
                            toolItemScheduleStart.setToolTipText( Messages.getString("MainWindow.Start_Scheduler") ); //$NON-NLS-1$
            		        toolItemScheduleStart.setImage( guiController.getImage( "Scheduler_Start.png" ) ); //$NON-NLS-1$
                            toolItemScheduleStart
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        guiController.getProfileManager().startScheduler();
                                    }
                                });
                        }
                        {
                            toolItemScheduleStop = new ToolItem(toolBar2, SWT.PUSH);
                            toolItemScheduleStop.setToolTipText( Messages.getString("MainWindow.Stop_Scheduler") ); //$NON-NLS-1$
            		        toolItemScheduleStop.setImage( guiController.getImage( "Scheduler_Stop.png" ) ); //$NON-NLS-1$
                            toolItemScheduleStop
                                .addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        guiController.getProfileManager().stopScheduler();
                                    }
                                });
                        }
                    }
                }
                GridData coolBarLData = new GridData();
                coolBarLData.grabExcessHorizontalSpace = true;
                coolBarLData.horizontalAlignment = GridData.FILL;
                coolBarLData.verticalAlignment = GridData.FILL;
                coolBar.setLayoutData(coolBarLData);
                coolBar.setLocked(true);
            }
			{
				menuBarMainWindow = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menuBarMainWindow);
			}
			{
			    profileListContainer = new Composite( this, SWT.NULL );
			    GridData profileListLData = new GridData();
			    profileListLData.grabExcessHorizontalSpace = true;
		        profileListLData.grabExcessVerticalSpace = true;
		        profileListLData.horizontalAlignment = GridData.FILL;
		        profileListLData.verticalAlignment = GridData.FILL;
		        profileListContainer.setLayoutData( profileListLData );
		        profileListContainer.setLayout( new FillLayout() );
			}
            {
                statusLine = new StatusLine(this, SWT.NONE);
                GridData statusLineLData = new GridData();
                statusLineLData.grabExcessHorizontalSpace = true;
                statusLineLData.horizontalAlignment = GridData.FILL;
                statusLine.setLayoutData(statusLineLData);
            }
			createMenu();
			createPopupMenu();
			createProfileList();
			this.layout();
	
		} catch (Exception e) {
			ExceptionHandler.reportException( e );
		}
	}
	protected void createMenu()
	{
		//toolBar1.layout();
		
		// Menu Bar
		MenuItem menuItemFile = new MenuItem(menuBarMainWindow, SWT.CASCADE);
		menuItemFile.setText(Messages.getString("MainWindow.File_Menu")); //$NON-NLS-1$
		
		Menu menuFile = new Menu(menuItemFile);
		menuItemFile.setMenu(menuFile);
		
		MenuItem menuItemNewProfile = new MenuItem(menuFile, SWT.PUSH);
		menuItemNewProfile.setText(Messages.getString("MainWindow.New_Profile_Menu")); //$NON-NLS-1$
		menuItemNewProfile.setImage( guiController.getImage( "Button_New.png" ) ); //$NON-NLS-1$
		menuItemNewProfile.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					createNewProfile();
				}
			}
		);

		MenuItem separatorItem3 = new MenuItem(menuFile, SWT.SEPARATOR);

		MenuItem menuItemEditProfile = new MenuItem(menuFile, SWT.PUSH);
		menuItemEditProfile.setText(Messages.getString("MainWindow.Edit_Profile_Menu")); //$NON-NLS-1$
		menuItemEditProfile.setImage( guiController.getImage( "Button_Edit.png" ) ); //$NON-NLS-1$
		menuItemEditProfile.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					editProfile( profileList.getSelectedProfile() );
				}
			}
		);

		MenuItem menuItemRunProfile = new MenuItem(menuFile, SWT.PUSH);
		menuItemRunProfile.setText(Messages.getString("MainWindow.Run_Profile_Menu")); //$NON-NLS-1$
		menuItemRunProfile.setImage( guiController.getImage( "Button_Run.png" ) ); //$NON-NLS-1$
		menuItemRunProfile.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					runProfile( profileList.getSelectedProfile() );
				}
			}
		);
		
		MenuItem separatorItem4 = new MenuItem(menuFile, SWT.SEPARATOR);

		MenuItem menuItemDeleteProfile = new MenuItem(menuFile, SWT.PUSH);
		menuItemDeleteProfile.setText(Messages.getString("MainWindow.Delete_Profile_Menu")); //$NON-NLS-1$
		menuItemDeleteProfile.setImage( guiController.getImage( "Button_Delete.png" ) ); //$NON-NLS-1$
		menuItemDeleteProfile.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					deleteProfile( profileList.getSelectedProfile() );
				}
			}
		);

		MenuItem separatorItem5 = new MenuItem(menuFile, SWT.SEPARATOR);

		MenuItem menuItemExitProfile = new MenuItem(menuFile, SWT.PUSH);
		menuItemExitProfile.setText(Messages.getString("MainWindow.Exit")); //$NON-NLS-1$
		menuItemExitProfile.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
		    	    guiController.closeGui();
				}
			}
		);
		
		
		MenuItem menuItemEdit = new MenuItem(menuBarMainWindow, SWT.CASCADE);
		menuItemEdit.setText(Messages.getString("MainWindow.Edit_Menu")); //$NON-NLS-1$
		
		Menu menuEdit = new Menu(menuItemEdit);
		menuItemEdit.setMenu(menuEdit);

		MenuItem logItem = new MenuItem(menuEdit, SWT.PUSH);
		logItem .setText(Messages.getString("MainWindow.Show_Log_Menu")); //$NON-NLS-1$
		logItem .setAccelerator(SWT.CTRL|SWT.SHIFT+'L');
		logItem .addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					// open main log file
					Program.launch( new java.io.File( "logs/fullsync.log" ).getAbsolutePath() ); //$NON-NLS-1$
				}
			}
		);

		MenuItem preferencesItem = new MenuItem(menuEdit, SWT.PUSH);
		preferencesItem.setText(Messages.getString("MainWindow.Preferences_Menu")); //$NON-NLS-1$
		preferencesItem.setAccelerator(SWT.CTRL|SWT.SHIFT+'P');
		preferencesItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					// show the Preferences Dialog.
					WizardDialog dialog = new WizardDialog( getShell(), SWT.APPLICATION_MODAL );
					WizardPage page = new PreferencesPage( dialog, guiController.getPreferences() );
					dialog.show();
				}
			}
		);
		
		MenuItem menuItemRemoteConnection = new MenuItem(menuBarMainWindow, SWT.CASCADE);
		menuItemRemoteConnection.setText(Messages.getString("MainWindow.Remote_Connection_Menu")); //$NON-NLS-1$
		
		Menu menuRemoteConnection = new Menu(menuItemRemoteConnection);
		menuItemRemoteConnection.setMenu(menuRemoteConnection);
		
		final MenuItem connectItem = new MenuItem(menuRemoteConnection, SWT.PUSH);
		connectItem.setText(Messages.getString("MainWindow.Connect_Menu")); //$NON-NLS-1$
		connectItem.setAccelerator(SWT.CTRL|SWT.SHIFT+'C');
		connectItem.setEnabled(true);

		final MenuItem disconnectItem = new MenuItem(menuRemoteConnection, SWT.PUSH);
		disconnectItem.setText(Messages.getString("MainWindow.Disconnect_Menu")); //$NON-NLS-1$
		disconnectItem.setAccelerator(SWT.CTRL|SWT.SHIFT+'D');
		disconnectItem.setEnabled(false);

		connectItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					WizardDialog dialog = new WizardDialog( getShell(), SWT.APPLICATION_MODAL );
					ConnectionPage page = new ConnectionPage(dialog);
					dialog.show();
					if (GuiController.getInstance().getProfileManager().isConnected()) {
						connectItem.setEnabled(false);
						disconnectItem.setEnabled(true);
						GuiController.getInstance().getMainShell().setImage(
						        GuiController.getInstance().getImage( "Remote_Connect.png" )); //$NON-NLS-1$
					}
				}
			}
		);
		
		disconnectItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					MessageBox mb = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
					mb.setText(Messages.getString("MainWindow.Confirmation")); //$NON-NLS-1$
					mb.setMessage(Messages.getString("MainWindow.Do_You_Want_To_Disconnect")+" \n"); //$NON-NLS-1$ //$NON-NLS-2$

					if (mb.open() == SWT.YES) {
		            	GuiController.getInstance().getProfileManager().disconnectRemote();		            	
		            	GuiController.getInstance().getSynchronizer().disconnectRemote();
		            	
						connectItem.setEnabled(true);
						disconnectItem.setEnabled(false);
						GuiController.getInstance().getMainShell().setImage(
						        GuiController.getInstance().getImage( "FullSync.png" ));					 //$NON-NLS-1$
					}
				}
			}
		);

		MenuItem menuItemHelp = new MenuItem(menuBarMainWindow, SWT.CASCADE);
		menuItemHelp.setText(Messages.getString("MainWindow.Help_Menu")); //$NON-NLS-1$
		
		Menu menuHelp = new Menu(menuItemHelp);
		menuItemHelp.setMenu(menuHelp);

		MenuItem menuItemHelpContent = new MenuItem(menuHelp, SWT.PUSH);
		menuItemHelpContent.setText(Messages.getString("MainWindow.Help_Menu_Item")); //$NON-NLS-1$
		menuItemHelpContent.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					//TODO help contents
				    java.io.File helpIndex = new java.io.File("docs/manual/index.html"); //$NON-NLS-1$
				    if( helpIndex.exists() )
				    {
				        try {
				            Program.launch(helpIndex.toURL().toString());
				        } catch( MalformedURLException ex ) {}
				    } else {
				        Program.launch("http://fullsync.sourceforge.net/docs/manual/index.html"); //$NON-NLS-1$
				    }
				}
			}
		);
        
        MenuItem separatorItem6 = new MenuItem(menuHelp, SWT.SEPARATOR);

        MenuItem menuItemSystem = new MenuItem(menuHelp, SWT.PUSH);
        menuItemSystem.setText("System Information");
        menuItemSystem.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event e) {
                    WizardDialog dialog = new WizardDialog( getShell(), SWT.NULL );
                    new SystemStatusPage( dialog );
                    dialog.show();
                }
            }
        );

		MenuItem separatorItem7 = new MenuItem(menuHelp, SWT.SEPARATOR);

		MenuItem menuItemAbout = new MenuItem(menuHelp, SWT.PUSH);
		menuItemAbout.setAccelerator(SWT.CTRL+'A');
		menuItemAbout.setText(Messages.getString("MainWindow.About_Menu")); //$NON-NLS-1$
		menuItemAbout.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					AboutDialog aboutDialog = new AboutDialog(getShell(), SWT.NULL);
					aboutDialog.open();
				}
			}
		);
	}
	protected void createPopupMenu()
    {
		// PopUp Menu for the Profile list.
		profilePopupMenu = new Menu(getShell(), SWT.POP_UP);
		
		MenuItem runItem = new MenuItem(profilePopupMenu, SWT.PUSH);
		runItem.setText(Messages.getString("MainWindow.Run_Profile")); //$NON-NLS-1$
		runItem.setImage( guiController.getImage( "Button_Run.png" ) ); //$NON-NLS-1$
		runItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					runProfile( profileList.getSelectedProfile() );
				}
			}
		);

		MenuItem editItem = new MenuItem(profilePopupMenu, SWT.PUSH);
		editItem.setText(Messages.getString("MainWindow.Edit_Profile")); //$NON-NLS-1$
		editItem.setImage( guiController.getImage( "Button_Edit.png" ) ); //$NON-NLS-1$
		editItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					editProfile( profileList.getSelectedProfile() );
				}
			}
		);

		MenuItem deleteItem = new MenuItem(profilePopupMenu, SWT.PUSH);
		deleteItem.setText(Messages.getString("MainWindow.Delete_Profile")); //$NON-NLS-1$
		deleteItem.setImage( guiController.getImage( "Button_Delete.png" ) ); //$NON-NLS-1$
		deleteItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					deleteProfile( profileList.getSelectedProfile() );
				}
			}
		);
		
		MenuItem separatorItem1 = new MenuItem(profilePopupMenu, SWT.SEPARATOR);

		MenuItem addItem = new MenuItem(profilePopupMenu, SWT.PUSH);
		addItem.setText(Messages.getString("MainWindow.New_Profile")); //$NON-NLS-1$
		addItem.setImage( guiController.getImage( "Button_New.png" ) ); //$NON-NLS-1$
		addItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					createNewProfile();
				}
			}
		);
    }
    public void createProfileList() 
    {
        if( profileList != null )
        {
            // take away our menu so it's not disposed
            profileList.setMenu( null );
            profileList.dispose();
        }

        if( guiController.getPreferences().getProfileListStyle().equals( "NiceListView" ) ) //$NON-NLS-1$
	         profileList = new NiceListViewProfileListComposite( profileListContainer, SWT.NULL );
	    else profileList = new ListViewProfileListComposite( profileListContainer, SWT.NULL );
        profileList.setMenu( profilePopupMenu );
        profileList.setHandler( this );
    	profileList.setProfileManager( guiController.getProfileManager() );
    	
    	profileListContainer.layout();
    }
	public StatusLine getStatusLine()
	{
	    return statusLine;
	}
	
	public GuiController getGuiController()
    {
        return guiController;
    }

	protected void minimizeToTray() 
	{
	    // on OSX use this: 
	    //    mainWindow.setMinimized(true);
	    getShell().setMinimized(true);
	    getShell().setVisible(false);
	    // TODO make sure Tray is visible here
	}
	
	// TODO [Michele] Implement this listener also on the remote interface
	public void schedulerStatusChanged( final boolean enabled ) 
	{
    	getDisplay().syncExec(new Runnable() {
    		public void run() {
		        toolItemScheduleStart.setEnabled( !enabled );
		        toolItemScheduleStop.setEnabled( enabled );
    		}
    	});
	}
	
    public void taskTreeStarted( TaskTree tree )
    {
    }
    public void taskGenerationStarted( final File source, final File destination )
    {
        //statusLine.setMessage( "checking "+source.getPath() );
        statusDelayString = Messages.getString("MainWindow.Checking_File", source.getPath() ); //$NON-NLS-1$
    }
    public void taskGenerationFinished( Task task )
    {
        
    }
    public void taskTreeFinished( TaskTree tree )
    {
        statusLine.setMessage( Messages.getString("MainWindow.Sync_Finished")); //$NON-NLS-1$
    }
    public void profileExecutionScheduled( Profile profile )
    {
        Synchronizer sync = guiController.getSynchronizer();
        TaskTree tree = sync.executeProfile( profile );
        if( tree == null )
        {
            profile.setLastError( 1, Messages.getString("MainWindow.Error_Comparing_Filesystems") ); //$NON-NLS-1$
        } else {
            int errorLevel = sync.performActions( tree );
            if( errorLevel > 0 ) {
                profile.setLastError( errorLevel, Messages.getString("MainWindow.Error_Copying_Files") ); //$NON-NLS-1$
            } else { 
                profile.beginUpdate();
                profile.setLastError( 0, null );
                profile.setLastUpdate( new Date() );
                profile.endUpdate();
            }
        }
    }

	
	public void createNewProfile()
	{
		ProfileDetails.showProfile( getShell(), guiController.getProfileManager(), null );
	}

	public void runProfile( final Profile p )
	{
		if( p == null )
			return;
		
		Thread worker = new Thread( new Runnable() {
			public void run()
			{
				_doRunProfile(p);
			}
		});
		worker.start();
	}
	private synchronized void _doRunProfile( Profile p )
	{
	    TaskTree t = null;
        try {
            guiController.showBusyCursor( true );
			try {
			    // REVISIT wow, a timer here is pretty much overhead / specific for
			    //         this generell problem
			    statusDelayTimer = new Timer( true );
			    statusDelayTimer.schedule( new TimerTask() {
			        public void run()
			        {
			            statusLine.setMessage( statusDelayString );
			        }
			    }, 10, 100 );
			    statusDelayString = Messages.getString("MainWindow.Starting_Profile")+p.getName()+"..."; //$NON-NLS-1$ //$NON-NLS-2$
				statusLine.setMessage( statusDelayString );
				t = guiController.getSynchronizer().executeProfile( p );
				if( t == null )
		        {
		            p.setLastError( 1, Messages.getString("MainWindow.Error_Comparing_Filesystems") ); //$NON-NLS-1$
		            statusLine.setMessage( Messages.getString("MainWindow.Error_Processing_Profile", p.getName() ) ); //$NON-NLS-1$
		        } else {
		            statusLine.setMessage( Messages.getString("MainWindow.Finished_Profile", p.getName() ) ); //$NON-NLS-1$
		        }
			} catch (Error e) {
				ExceptionHandler.reportException( e );
			} finally {
			    statusDelayTimer.cancel();
            	guiController.showBusyCursor( false );
			}
			if( t != null )
			    TaskDecisionList.show( guiController, p, t );
            
        } catch( Exception e ) {
            ExceptionHandler.reportException( e );
        }
	}

	public void editProfile( final Profile p )
	{
		if( p == null )
		    return;

		ProfileDetails.showProfile( getShell(), guiController.getProfileManager(), p.getName() );
	}

	public void deleteProfile( final Profile p )
	{
		if( p == null )
		    return;

		ProfileManager profileManager = guiController.getProfileManager();

		MessageBox mb = new MessageBox( getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO );
		mb.setText( Messages.getString("MainWindow.Confirmation") ); //$NON-NLS-1$
	    mb.setMessage( Messages.getString("MainWindow.Do_You_Want_To_Delete_Profile")+" "+p.getName()+" ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    if( mb.open() == SWT.YES )
	    {
	        profileManager.removeProfile( p );
	        profileManager.save();
	    }
	}
    protected void toolItemScheduleWidgedSelected(SelectionEvent evt)
    {
	    ProfileManager profileManager = guiController.getProfileManager();
	    if( profileManager.isSchedulerEnabled() )
        {
            profileManager.stopScheduler();
        } else {
            profileManager.startScheduler();
        }
//        updateTimerEnabled();
    }
}
