package com.soarclient.utils.file.dialog;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NFDFilterItem;
import org.lwjgl.util.nfd.NativeFileDialog;

public class FileDialog {

	private DialogStatus status;
	private File output;
	private String errorMessage;

	public FileDialog() {
		status = DialogStatus.ERROR;
	}

	public void openFileDialog(String dialogTitle, String defaultPath, String... extensions) {
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			NFDFilterItem.Buffer filters = NFDFilterItem.malloc(1, stack);
			filters.get(0).name(stack.UTF8(dialogTitle)).spec(stack.UTF8(String.join(",", extensions)));

			ByteBuffer defaultPathBuffer = defaultPath != null ? MemoryUtil.memUTF8(defaultPath) : null;

			PointerBuffer pp = stack.mallocPointer(1);
			parseResult(NativeFileDialog.NFD_OpenDialog(pp, filters, defaultPathBuffer), pp);
		} catch (Exception e) {
			status = DialogStatus.ERROR;
			errorMessage = e.getMessage();
		}
	}

	public void openFolderDialog(String defaultPath) {
		
		PointerBuffer path = MemoryUtil.memAllocPointer(1);
		
		try {
			ByteBuffer defaultPathBuffer = defaultPath != null ? MemoryUtil.memUTF8(defaultPath) : null;

			parseResult(NativeFileDialog.NFD_PickFolder(path, defaultPathBuffer), path);
		} catch (Exception e) {
			status = DialogStatus.ERROR;
			errorMessage = e.getMessage();
		} finally {
			MemoryUtil.memFree(path);
		}
	}

	private void parseResult(int result, PointerBuffer path) {
		
		switch (result) {
		case NativeFileDialog.NFD_OKAY:
			File file = new File(path.getStringUTF8(0));
			if (file != null && file.exists()) {
				status = DialogStatus.SUCCESS;
				output = file;
			} else {
				status = DialogStatus.ERROR;
				errorMessage = "Selected file does not exist.";
			}
			break;
		case NativeFileDialog.NFD_CANCEL:
			status = DialogStatus.CANCEL;
			break;
		case NativeFileDialog.NFD_ERROR:
			status = DialogStatus.ERROR;
			errorMessage = NativeFileDialog.NFD_GetError();
			break;
		}
	}

	public boolean isSuccess() {
		return status.equals(DialogStatus.SUCCESS);
	}

	public boolean isCancel() {
		return status.equals(DialogStatus.CANCEL);
	}

	public boolean isError() {
		return status.equals(DialogStatus.ERROR);
	}

	public File getOutput() {
		return output;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}