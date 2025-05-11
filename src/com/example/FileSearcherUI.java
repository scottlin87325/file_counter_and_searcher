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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.insyde.base.Ui;
import com.insyde.base.UtilFile;
import com.insyde.base.UtilSwt;

public class FileSearcherUI extends Composite {

	// naming conflict (org.eclipse.swt.widgets.List)
	private java.util.List<String> results;

	// constructor
	public FileSearcherUI(Composite parent, int style) {
		super(parent, style);
		createPartControl();
	}

	// UI
	private void createPartControl() {

		// layout setting
		setLayout(new GridLayout(3, false));

		// font setting
		Font font = new Font(getDisplay(), new FontData("", 14, SWT.NORMAL));

		// Label " Select Directory : "
		Label directoryLabel = new Label(this, SWT.NONE);
		directoryLabel.setFont(font);
		directoryLabel.setText("  Select Directory : ");
		directoryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

		// directory text
		Text directoryText = new Text(this, SWT.BORDER);
		directoryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// Button "Browse..."
		Button browseButton = new Button(this, SWT.NONE);
		GridData browseButtonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		browseButtonGridData.heightHint = 28;
		browseButton.setText("Browse...");
		browseButton.setLayoutData(browseButtonGridData);

		// "Browse..." Listener
		browseButton.addListener(SWT.Selection, event -> {
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			// open directoryDialog
			String selectedDir = directoryDialog.open();
			if (selectedDir != null) {
				directoryText.setText(selectedDir);
			}
		});

		// Label " Enter Keywords : "
		Label keywordLabel = new Label(this, SWT.NONE);
		keywordLabel.setFont(font);
		keywordLabel.setText("  Enter Keywords : ");
		keywordLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

		// keyword text
		Text keywordText = new Text(this, SWT.BORDER);
		keywordText.setMessage("Type something...");
		keywordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// Button "Search"
		Button searchButton = new Button(this, SWT.PUSH);
		GridData searchButtonGridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		searchButtonGridData.heightHint = 28;
		searchButton.setText("Search");
		searchButton.setLayoutData(searchButtonGridData);

		// result list
		List resultList = new List(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		resultList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		// "Search" Listener
		searchButton.addListener(SWT.Selection, event -> {
			String directory = directoryText.getText();
			String keyword = keywordText.getText();

			// prompt message
			if (directory.isEmpty() || keyword.isEmpty()) {
				Ui.showErrorMessage("Directory and keyword cannot be empty", "Error");
				return;
			} else if (!UtilFile.dirExist(directory)) {
				Ui.showErrorMessage("Invalid directory", "Error");
				return;
			}

			// progress bar
			UtilSwt.runAsyncProgressive((monitor) -> {
				monitor.beginTask("Searching files in " + directory + " with keyword : " + keyword,
						IProgressMonitor.UNKNOWN);

				// search file with keywords
				FileSearcher fileSearcher = new FileSearcher();
				fileSearcher.searchInDirectory(directory, keyword);
				results = fileSearcher.getResult();

				// end of search
				monitor.done();
			});

			// clear list
			resultList.removeAll();
			
			// result message
			if (results.isEmpty()) {
				Ui.showMessage("No files found with keyword : " + keyword, "Search Result");
			} else {
				Ui.showMessage("Found " + results.size() + " files", "Search Result");
				// add result to list
				for (String result : results) {
					resultList.add(result);
				}
			}
		});
	}
}
