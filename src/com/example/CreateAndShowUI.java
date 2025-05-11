package com.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class CreateAndShowUI {
	public static void createUI() {

		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		// shell setting
		shell.setText("File APP");
		shell.setMinimumSize(600, 300);
		shell.setSize(800, 600);
		shell.setLayout(new FillLayout());

		// careat a tabfolder in shell (folder)(TabFolder is sub of Composite)
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		// File Counter Tab
		// new TabItem (item)
		TabItem fileCounterTab = new TabItem(tabFolder, SWT.NONE);
		// set TabItem text
		fileCounterTab.setText("File Counter");
		// Create a fileCounterUI control and set its parent container to tabFolder
		FileCounterUI fileCounterUI = new FileCounterUI(tabFolder, SWT.NONE);
		// Set fileCounterUI as the control for the tabitem
		fileCounterTab.setControl(fileCounterUI);

		// File Searcher Tab
		// new TabItem (item)
		TabItem fileSearcherTab = new TabItem(tabFolder, SWT.NONE);
		// set TabItem text
		fileSearcherTab.setText("File Searcher");
		// Create a fileSearcherUI control and set its parent container to tabFolder
		FileSearcherUI fileSearcherUI = new FileSearcherUI(tabFolder, SWT.NONE);
		// Set fileSearcherUI as the control for the tabitem
		fileSearcherTab.setControl(fileSearcherUI);

		shell.open();

		// event loop
		// if shell open
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// if no event occure, sleep
				display.sleep();
			}
		}

		// shell close, release source
		display.dispose();
	}
}
