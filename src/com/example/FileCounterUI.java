package com.example;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.insyde.base.Ui;
import com.insyde.base.UtilFile;
import com.insyde.base.UtilSwt;

public class FileCounterUI extends Composite {

	private int fileCount;

	// constructor
	public FileCounterUI(Composite parent, int style) {
		super(parent, style);
		createPartControl();
	}

	// UI
	public void createPartControl() {
		// layout setting
		setLayout(new GridLayout(4, false));

		// font setting
		Font font = new Font(this.getDisplay(), new FontData("", 14, SWT.NORMAL));

		// Label " Select Directory : "
		Label selectLabel = new Label(this, SWT.NONE);
		selectLabel.setFont(font);
		selectLabel.setText("  Select Directory : ");
		selectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		// directory text
		Text directoryPathText = new Text(this, SWT.BORDER);
		directoryPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// Button "browser..."
		Button browseButton = new Button(this, SWT.NONE);
		GridData browseButtonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		browseButtonGridData.heightHint = 28;
		browseButton.setText("Browser...");
		browseButton.setLayoutData(browseButtonGridData);

		// "browser..." listener
		browseButton.addListener(SWT.Selection, event -> {
			DirectoryDialog directoryDialog = new DirectoryDialog(this.getShell());
			String selectedDir = directoryDialog.open();
			if (selectedDir != null) {
				directoryPathText.setText(selectedDir);
			}
		});

		// button "Start Counting"
		Button startButton = new Button(this, SWT.PUSH);
		GridData startButtonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		startButtonGridData.heightHint = 28;
		startButton.setText("Start Counting");
		startButton.setLayoutData(startButtonGridData);

		// Label " Total Files : "
		Label resultLabel = new Label(this, SWT.NONE);
		resultLabel.setFont(font);
		resultLabel.setText("  Total Files : ");
		resultLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		// "Start Counting" listener
		startButton.addListener(SWT.Selection, event -> {
			String dirPath = directoryPathText.getText();

			// prompt message
			if (!UtilFile.dirExist(dirPath)) {
				Ui.showErrorMessage("Invalid directory", "Error");
				return;
			}

			// progress bar
			UtilSwt.runAsyncProgressive((monitor) -> {
				monitor.beginTask("Counting files in " + dirPath, IProgressMonitor.UNKNOWN);

				// counting files
				FileCounter task = new FileCounter();
				task.countFiles(dirPath);
				fileCount = task.getResult();

				// end of counting
				monitor.done();
			});

			// update result on window
			resultLabel.setText("  Total Files : " + fileCount);

			// messageBox for result
			Ui.showMessage("Total Files : " + fileCount, "File Count Result");
		});
	}
}